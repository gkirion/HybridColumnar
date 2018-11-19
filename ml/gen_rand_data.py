from random import *

'''
Script that generates random data in the form we needed for the training of deep nets
This script can be used only for testing purposes.

Each generated row is in the form:
v1,v2,v3,...,vk enc

The vi values comprise blocks of raw input data of size k, and enc is the most appropriate
encoding format for compressing this specific block.
'''

bs = 100# block size
n = 200# number of block sizes
encodings = 4# number of available compression encodings
dom = 100# dataset values domain

i = 0
while i < n:
    s = ''
    j = 0
    while j < bs:
        s += str(randint(1, dom)) + ','
        j += 1
    s = s[:-1] + ' ' + str(randint(1, encodings))
    print s
    i += 1
