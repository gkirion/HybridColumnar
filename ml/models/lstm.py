__author__ = 'gmytil'
__date__ = '2018-11-13'

import numpy as np
import sys
import os

from keras.models import Sequential
from keras.layers import Dense
from keras.layers import LSTM
from keras.layers import Dropout
from keras.layers.convolutional import Conv1D
from keras.layers.convolutional import MaxPooling1D
from keras.utils import np_utils
from keras.models import model_from_json
from keras.callbacks import ModelCheckpoint
from sklearn.preprocessing import LabelEncoder

from column_model import ColumnModel

import pickle

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
            model_descr = params['model_descr']
        except KeyError:
            print ('Failure in parameter parsing')
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
        self.save_model(model, model_descr)
        # checkpoint
        filepath="weights-{epoch:02d}-{val_acc:.2f}.hdf5"
        checkpoint = ModelCheckpoint(filepath, monitor='val_acc', verbose=1, save_best_only=True, mode='max')
        callbacks_list = [checkpoint]
        model.fit(X_train, y_train, validation_data=(X_test, y_test), epochs=epochs, batch_size=batch_size, callbacks=callbacks_list)
        return model

    def evaluate(self, model, X_test, y_test):
        scores = model.evaluate(X_test, y_test, verbose=0)
        print("Accuracy: %.2f%%" % (scores[1]*100))

    def predict(self, classifier, encoder, f):
        predicted_class = classifier.predict_classes(f)
        #print('Predicted class: {}'.format(predicted_class))
        decoded_class = encoder.inverse_transform(predicted_class)
        print('Decoded class: {}'.format(decoded_class))
        return self.reverse_encoding_mapping[int(decoded_class[0])]

    def encode_training_labels(self, Y, encoder_path):
        '''
        To be completed
        '''
        encoder = LabelEncoder()
        encoder.fit(Y)
        filehandler = open(encoder_path, 'wb')
        pickle.dump(encoder, filehandler)
        filehandler.close()
        print('encoder classes: {}'.format(encoder.classes_)) 
        return encoder

    def one_hot_encoding(self, Y, encoder):
        '''
        Apply one hot encoding to each label. The categorical_crossentropy function requires each label
        to be in the form of a vector and not an integer
        :param Y: An array with labels in the form of integers
        :return: An array with labels, where each label is an one hot encoded vector
        '''
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

    def save_model(self, model, model_description_file):
        # serialize model to JSON
        model_json = model.to_json()
        with open(model_description_file, "w") as json_file:
            json_file.write(model_json)
        print("Saved model to disk")

    def load_model(self, model_description_file, weights_file):
        json_file = open(model_description_file, 'r')
        loaded_model_json = json_file.read()
        json_file.close()
        loaded_model = model_from_json(loaded_model_json)
        # load weights into new model
        loaded_model.load_weights(weights_file)
        print("Loaded model from disk")
        return loaded_model
        

if __name__=='__main__':
    # option can be either split or run
    option = sys.argv[1]
    model_builder = LSTMColumnModel()
    if option == 'split':
        samples, labels = model_builder.input_to_numpy_array(sys.argv[2])
        (X_train, y_train), (X_test, y_test) = model_builder.split_train_test(samples, labels)
    elif option == 'train':
        ######## uncomment to force training on CPU ############
        #os.environ["CUDA_DEVICE_ORDER"] = "PCI_BUS_ID" 
        #os.environ["CUDA_VISIBLE_DEVICES"] = ""
        ########################################

        (X_train, y_train), (X_test, y_test) = model_builder.load_datasets()
        print('Datasets are loaded')
        # block_size: the length of each individual input sample
        block_size = int(sys.argv[2])
        model_description = sys.argv[3]
        encoder_path = sys.argv[4]

        encoder = model_builder.encode_training_labels(y_train, encoder_path)

        X_train = X_train.reshape(len(X_train), block_size, 1)
        X_test = X_test.reshape(len(X_test), block_size, 1)
        y_train = model_builder.one_hot_encoding(y_train, encoder)
        y_test = model_builder.one_hot_encoding(y_test, encoder)
        params = {'X_test': X_test, 'y_test': y_test, 'lstm_units': 32,
                'epochs': 10, 'batch_size': 64, 'block_size': block_size, 'model_descr' : model_description}
        model = model_builder.compression_classifier(X_train, y_train, params)
        model_builder.evaluate(model, X_test, y_test)

        # model_builder.save_model(model)
    elif option == 'predict':
        # This option is only for testing purposes as well as to demonstrate the way that
        # a prediction for a new sample happens.

        # Prediction should happen on the CPU
        os.environ["CUDA_DEVICE_ORDER"] = "PCI_BUS_ID"   
        os.environ["CUDA_VISIBLE_DEVICES"] = ""

        model_description = sys.argv[2]
        weights_file = sys.argv[3]
        encoder_path = sys.argv[4]
        block_size = int(sys.argv[5])

        encfile = open(encoder_path,'rb')
        encoder = pickle.load(encfile)
        encfile.close()

        print('classes: {}'.format(encoder.classes_))

        model = model_builder.load_model(model_description,weights_file)
        ##############################################
        #### Same number #############################
        new_block = []
        for i in range(block_size):
            new_block.append(1)
        new_block = model_builder.format_for_prediction(new_block)
        print(model_builder.predict(model, encoder, new_block))
        ##############################################
        #### Sequential numbers ######################
        new_block = []
        for i in range(block_size):
            new_block.append(i)
        new_block = model_builder.format_for_prediction(new_block)
        print(model_builder.predict(model, encoder, new_block))
        ##############################################
        #### Random numbers ##########################
        new_block = []
        for i in range(block_size):
            new_block.append(np.random.randint(1,100))
        new_block = model_builder.format_for_prediction(new_block)
        print(model_builder.predict(model, encoder, new_block))
    else:
        print('Wrong option')

