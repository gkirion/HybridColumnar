__author__ = 'gmytil'
__date__ = '2018-11-13'

import numpy as np
import sys
import glob
import random
from datetime import datetime
import ast
import time
from keras.datasets import imdb 
from keras.models import Sequential
from keras.layers import Dense
from keras.layers import LSTM
from keras.layers import Dropout
from keras.layers.embeddings import Embedding
from keras.preprocessing import sequence

# fix random seed for reproducibility
np.random.seed(7)



encoding = {'RLE': [1,0,0], 'ROARING': [0,1,0], 'DELTA': [0,0,1]}

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
				l = l.replace(', ', ',')
				kv = l.split()
				#print(kv[0])
				#print(kv[1])
				new_sample = list(map(lambda x: int(x), kv[0].strip('[').strip(']').split(',')))
				
				if len(new_sample) == 1000:
					samples.append(new_sample)
					labels.append(kv[1])
				# assertion to check that data comply to the given shape
				# In normal operation, delete the above "If" clause and uncomment the following three lines
				#assert len(samples[-1])==1000
				#samples.append(new_sample)
				#labels.append(kv[1])
			f.close()
		#print(len(samples))
		#return np.array(samples), np.array(labels)
		return samples, labels

	def split_train_test(self, samples, labels, percentage=0.8, persist=True):
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

		if persist:
			f = open('training_samples', 'w')
			f.write(str(training_set))
			f.close()
			f = open('training_labels', 'w')
			f.write(str(training_labels))
			f.close()
			f = open('test_samples', 'w')
			f.write(str(test_set))
			f.close()
			f = open('test_labels', 'w')
			f.write(str(test_labels))
			f.close()

		return (np.array(training_set), np.array(training_labels)), (np.array(test_set), np.array(test_labels))

	def load_as_numpy(self, filename):
		start = time.time()
		with open(filename, 'r') as f:
			line = f.readlines()[0]
			array = np.array(ast.literal_eval(line))
			f.close()
			end = time.time()
			print('Load Time: {}. {} samples found'.format(end-start, len(array)))
		return array

	# efficient string parsing
	def load_samples_as_numpy(self, filename):
		start = time.time()
		array = []
		num = []
		with open(filename, 'r') as f:
			line = f.readlines()[0]
			char_index = 1
			for c in line:
				if char_index == len(line):
					break
				if c == '[' or c == ']' or c == ' ' or c == ',':
					if c == '[':
						temp = []
					if c == ']':
						array.append(temp)
					if num:
						temp.append(float(''.join(num)))
						num = []
				else:
					num.append(c)
				char_index += 1
			f.close()
			end = time.time()
			print('Load Time: {}. {} samples found'.format(end-start, len(array)))
		return np.array(array)


	def load_datasets(self):
		X_train = self.load_samples_as_numpy('training_samples')
		print('Training samples have been loaded.')
		Y_train = self.load_as_numpy('training_labels')
		print('Training labels have been loaded.')
		X_test = self.load_samples_as_numpy('test_samples')
		print('Test samples have been loaded.')
		Y_test = self.load_as_numpy('test_labels')
		print('Test labels have been loaded.')
		return (X_train, Y_train), (X_test, Y_test)


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

	def convert_labels_to_one_hot(self, labels):
		one_hot_labels = list(map(lambda x: encoding[x], labels))
		return np.array(one_hot_labels)

	def create_model(self):
		# create the model
		max_pack_size = 1000
		cardinality = 5000
		embedding_vector_length = 32
		model = Sequential()
		model.add(Embedding(cardinality, embedding_vector_length, input_length=max_pack_size))
		model.add(Dropout(0.2))
		model.add(LSTM(100))
		model.add(Dropout(0.2))
		model.add(Dense(3, activation='softmax'))
		model.compile(loss='categorical_crossentropy', optimizer='adam', metrics=['accuracy'])
		print(model.summary())
		return model

	def train(self, training_set, training_labels, test_set, test_labels, model):
		

		max_pack_size = 1000

		training_set = sequence.pad_sequences(training_set, maxlen=max_pack_size)
		test_set = sequence.pad_sequences(test_set, maxlen=max_pack_size)

		training_labels = self.convert_labels_to_one_hot(training_labels)
		test_labels = self.convert_labels_to_one_hot(test_labels)
		print(training_set[0])
		print(training_labels[0])


		model.fit(training_set, training_labels, validation_data=(test_set, test_labels), epochs=3, batch_size=64)

		# Final evaluation of the model
		scores = model.evaluate(test_set, test_labels, verbose=0)
		print("Accuracy: %.2f%%" % (scores[1]*100))



if __name__ == '__main__':
	x = ColumnModel()
	model = x.create_model()
	print (glob.glob(sys.argv[1]))
	samples_full = []
	labels_full = []
	for input_file in glob.glob(sys.argv[1]):
		(samples, labels) = x.input_to_numpy_array(input_file)
		print(np.array(samples).shape)
		print(np.array(labels).shape)
		#print(samples)
		for sample in samples:
			samples_full.append(sample)
		for label in labels:
			labels_full.append(label)
		#print (input_file)

	samples_full = np.array(samples_full)
	labels_full = np.array(labels_full)
	print(samples_full.shape)
	print(labels_full.shape)

	(training_set, training_labels), (test_set, test_labels) = x.split_train_test(samples=samples_full, labels=labels_full, percentage=0.8, persist=False)
	x.train(training_set, training_labels, test_set, test_labels, model)
	model.save("column-model-full7.hdf5")
