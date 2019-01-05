import ast
import sys
from os import listdir
from os.path import isfile, join

encoding = {'rle':'1', 'roaring':'2', 'delta':'3'}


def traverse(path):
    for f in listdir(path):
        new_path = join(path, f)
        if isfile(new_path):
            transform(new_path)
        else:
            traverse(new_path)


def transform(infile):
    with open(infile, 'r') as f:
        lines = f.readlines()
        for row in lines:
            row = row.replace('rle','"rle"')
            row = row.replace('roaring','"roaring"')
            row = row.replace('delta','"delta"')
            key = str(ast.literal_eval(row)[0])[1:-1].replace(' ','')
            value = encoding[ast.literal_eval(row)[1]]
            print '{} {}'.format(key, value)
        f.close()

if __name__ == '__main__':
    infile = sys.argv[1]
    traverse(infile)


