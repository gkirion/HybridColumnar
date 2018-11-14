__author__ = 'gmytil'
__date__ = '2018-11-13'

import numpy as np
import random
from datetime import datetime


class ColumnModel:
    '''
    Abstract implementation of a model that can predict which is the most beneficial compression
    algorithm for a single column consisted of numerical data.
    '''

    def __init__(self):
        # not a good idea to have the map hard-coded here
        self.reverse_encoding_mapping = {1: 'rle', 2: 'roaring', 3: 'delta'}

    def input_to_numpy_array(self, infile):
        '''
        Reads training set from file and instantiates the required numpy arrays.
        :param infile: The path of the training set in the filesystem
        :return: A tuple (X, Y), where X is the numpy array containing the input features and
        Y is the numpy array that contains the corresponding labels
        '''
        with open(infile, 'r') as f:
            lines = f.readlines()
            samples = []
            labels = []
            for l in lines:
                kv = l.split()
                samples.append(map(lambda x: float(x), kv[0].split(',')))
                labels.append(kv[1])
            f.close()
        return np.array(samples), np.array(labels)

    def split_train_test(self, samples, labels, percentage=0.8):
        '''
        Splits input arrays to training set and test set.
        :param samples: Array containing input features
        :param labels: Array containing input labels
        :param percentage: Indicates which percentage of the input dataset should be used for the training set
        :return: Two tuples (X_train, y_train), (X_test, y_test) that correspond to annotated training and test
        sets respectively.
        '''
        training_set = []
        training_labels = []
        test_set = []
        test_labels = []
        training_size = percentage * len(samples)
        random.seed(datetime.now())
        indices = random.sample(range(len(samples)), int(training_size))
        for i in range(len(samples)):
            if i in indices:
                training_set.append(samples[i])
                training_labels.append(labels[i])
            else:
                test_set.append(samples[i])
                test_labels.append(labels[i])
        return (np.array(training_set), np.array(training_labels)), (np.array(test_set), np.array(test_labels))

    def compression_classifier(self, X, Y, params={}):
        '''
        Builds a model able to predict the most suitable compression method for a given dataset
        :param X: Array with the features of the training set
        :param Y: Array with the labels for the training set
        :param params: A dictionary carries any additional parameter an algorithm may need
        :return: A model able to predict which compression format is the most appropriate for a given dataset
        '''
        pass

    def evaluate(self, classifier, X, Y):
        '''
        Uses the test set in order to evaluate the accuracy of the built model
        :param classifier: The classification model under evaluation
        :param X: Array with the features of the test set
        :param Y: Array with the labels of the test set
        '''
        pass

    def predict(self, classifier, f):
        '''
        Given a classification model and a block of data, this method can predict which is the most advantageous
        compression method for this data block
        :param classifier: The classification model
        :param f: An array containing the block of data
        :return: The most suitable compression method for this block
        '''
        pass


