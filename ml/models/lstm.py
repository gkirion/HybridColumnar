__author__ = 'gmytil'
__date__ = '2018-11-13'

import numpy as np
import sys

from keras.models import Sequential
from keras.layers import Dense
from keras.layers import LSTM
from keras.layers import Dropout
from keras.layers.convolutional import Conv1D
from keras.layers.convolutional import MaxPooling1D
from keras.utils import np_utils
from sklearn.preprocessing import LabelEncoder

from column_model import ColumnModel

# fix random seed for reproducibility
np.random.seed(7)

class LSTMColumnModel(ColumnModel):
    '''
    Implementation of a deep neural network based on LSTM cells.
    '''

    #The input to every LSTM layer must be three-dimensional.
    #The three dimensions of this input are:
    #    Samples: One sequence is one sample. A batch is comprised of one or more samples.
    #    Time Steps: One time step is one point of observation in the sample.
    #    Features: One feature is one observation at a time step.

    def compression_classifier(self, X_train, y_train, params={}):
        # parse params
        try:
            X_test = params['X_test']
            y_test = params['y_test']
            lstm_units = params['lstm_units']
            epochs = params['epochs']
            batch_size = params['batch_size']
            block_size = params['block_size']
        except KeyError:
            print 'Failure in parameter parsing'
            return

        # create the model
        model = Sequential()
        #model.add(Conv1D(filters=1, kernel_size=3, padding='same', activation='relu'))
        #model.add(MaxPooling1D(pool_size=2))
        model.add(LSTM(lstm_units, input_shape=(block_size, 1)))# block_size time steps and 1 feature
        model.add(Dropout(0.2))
        model.add(Dense(y_train.shape[1], activation='softmax'))
        model.compile(loss='categorical_crossentropy', optimizer='adam', metrics=['accuracy'])
        print(model.summary())
        model.fit(X_train, y_train, validation_data=(X_test, y_test), epochs=epochs, batch_size=batch_size)
        return model

    def evaluate(self, model, X_test, y_test):
        scores = model.evaluate(X_test, y_test, verbose=0)
        print("Accuracy: %.2f%%" % (scores[1]*100))

    def predict(self, classifier, f):
        predicted_class = classifier.predict_classes(f)
        return self.reverse_encoding_mapping[predicted_class[0]]

    def one_hot_encoding(self, Y):
        '''
        Apply one hot encoding to each label. The categorical_crossentropy function requires each label
        to be in the form of a vector and not an integer
        :param Y: An array with labels in the form of integers
        :return: An array with labels, where each label is an one hot encoded vector
        '''
        encoder = LabelEncoder()
        encoder.fit(Y)
        encoded_Y = encoder.transform(Y)
        dummy_y = np_utils.to_categorical(encoded_Y)
        return dummy_y

    def format_for_prediction(self, block):
        '''
        :param block: A Python list containing the raw values of the input block
        :return: A numpy array of the input block, appropriately reshaped in order to feed the neural network
        '''
        block = np.array(block)
        block = block.reshape(1, len(block), 1)
        return block


if __name__=='__main__':
    model_builder = LSTMColumnModel()
    samples, labels = model_builder.input_to_numpy_array(sys.argv[1])
    block_size = 100
    (X_train, y_train), (X_test, y_test) = model_builder.split_train_test(samples, labels)
    X_train = X_train.reshape(len(X_train), block_size, 1)
    X_test = X_test.reshape(len(X_test), block_size, 1)
    y_train = model_builder.one_hot_encoding(y_train)
    y_test = model_builder.one_hot_encoding(y_test)
    params = {'X_test': X_test, 'y_test': y_test, 'lstm_units': 32,
              'epochs': 3, 'batch_size': 64, 'block_size': block_size}
    model = model_builder.compression_classifier(X_train, y_train, params)
    model_builder.evaluate(model, X_test, y_test)

    # Example of prediction

    new_block = [58,23,38,29,57,28,11,62,79,63,24,44,94,4,29,49,88,29,49,50,32,4,49,94,48,82,69,20,21,23,36,44,97,81,
                 19,82,89,86,63,59,1,82,19,44,25,74,94,5,53,66,43,65,2,35,48,82,9,16,52,89,19,22,61,13,91,41,30,46,62
        ,44,91,64,8,68,47,13,91,92,76,26,5,74,91,15,39,68,67,79,61,70,53,80,45,10,65,89,8,100,46,47]

    new_block = model_builder.format_for_prediction(new_block)

    print model_builder.predict(model, new_block)