__author__ = 'gmytil'
__date__ = '2018-11-13'

import sys
import numpy as np
from sklearn import svm
from sklearn.model_selection import cross_val_score

from column_model import ColumnModel


class SVMColumnModel(ColumnModel):
    '''
    Implementation of the SVM classification algorithm.
    '''

    def compression_classifier(self, X, Y, params={}):
        classifier = svm.SVC(gamma='scale', kernel='rbf', decision_function_shape='ovo')
        classifier.fit(X, Y)
        return classifier
   
    def predict(self, classifier, f):
        return self.reverse_encoding_mapping[int(classifier.predict(np.array(f).reshape(1, -1))[0])]

    def evaluate(self, classifier, X, Y):
        print '======================================================='
        print 'Accuracy: {}'.format(cross_val_score(classifier, X, Y, cv=5, scoring='recall_macro')[0])


if __name__ == '__main__':

    model_builder = SVMColumnModel()
    samples, labels = model_builder.input_to_numpy_array(sys.argv[1])
    (X_train, y_train), (X_test, y_test) = model_builder.split_train_test(samples, labels)

    clf = model_builder.compression_classifier(X_train, y_train)
    model_builder.evaluate(clf, X_test, y_test)

    # Example of prediction

    print model_builder.predict(clf, [4, 2, 100000000])





