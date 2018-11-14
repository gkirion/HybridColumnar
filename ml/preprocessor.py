__author__ = 'giagulei'
__author__ = 'gmytil'
__date__ = '2018-11-13'

import sys
from collections import defaultdict


class Preprocessor:

    def assign_minimum(self, x, y):
        '''
        :param x: x is a tuple in the form [label, metric], where label is the selected encoding and
                  metric is the corresponding achieved memory/time
        :param y: similarly to x, y is a [label, metric] tuple
        :return: the minimum of x, y based on the metric comparison
        '''
        if x[1] < y[1]:
            return x
        else:
            return y

    def annotate_with_labels(self, infile):
        '''
        This method reads the training set from a file in the form:
        [f1,f2,..,fn,encoding metric] and deduces the label for each input record.
        It returns a map with key = [f1,f2,..,fn] and value = enc*, where enc* is the encoding that
        achieves the minimum metric value for a given set o features.
        :param infile:  String containing the path of the input file
        '''
        annotated_dataset = dict()
        with open(infile, 'r') as f:
            lines = f.readlines()
            for l in lines:
                row = l.split()
                attributes = row[0].split(',')
                features = ','.join(attributes[:-1])
                label = int(attributes[-1])
                metric = float(row[-1])
                try:
                    annotated_dataset[features] = self.assign_minimum(annotated_dataset[features], [label, metric])
                except KeyError as e:
                    annotated_dataset[features] = [label, metric]
            f.close()
        for key in annotated_dataset:
            print '{} {}'.format(key, annotated_dataset[key][0])

    def dump_labels_histo(self, infile):
        '''
        Gets the result of profiling, generates the training set and computes a histogram for the assigned
        lables. This way, we can check if we have a balanced dataset or not.
        '''
        _ , Y = self.annotate_with_labels(infile)
        histo = defaultdict(int)
        for lbl in Y:
            histo[lbl] += 1
        print histo

    def filter_samples(self, lbl):
        '''
        Custom logic to control which samples should be included in the training set. A sample may be included or not
        according to its label
        :return : A boolean indicating if the sample should be included or not
        '''
        return lbl != 3

if __name__ == '__main__':
    infile = sys.argv[1]
    p = Preprocessor()
    p.annotate_with_labels(infile)

