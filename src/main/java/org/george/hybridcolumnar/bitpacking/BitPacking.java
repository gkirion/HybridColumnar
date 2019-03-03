package org.george.hybridcolumnar.bitpacking;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;

@SuppressWarnings("serial")
public class BitPacking implements Iterable<Integer>, Serializable {

	private int[] array;
	private int index;
	private int length;
	private int dataSize;
	private final int CELL_SIZE = Integer.SIZE;
	private final int MASK;
	private final int NUMBER_CELLS;

	public BitPacking() {
		length = 4;
		array = new int[length];
		index = 0;
		dataSize = 1;
		MASK = (int) Math.pow(2, dataSize) - 1;
		NUMBER_CELLS = CELL_SIZE / dataSize;
	}

	public BitPacking(int dataSize) {
		length = 4;
		array = new int[length];
		index = 0;
		// to shift exei provlima pou douleuei se dinameis tou 2

		for (int i = dataSize; i <= CELL_SIZE; i++) {
			if (CELL_SIZE % i == 0) {
				this.dataSize = i;
				break;
			}
		}

		MASK = (int) Math.pow(2, this.dataSize) - 1;
		NUMBER_CELLS = CELL_SIZE / this.dataSize;
	}

	public void add(int value) {
		int arrayIndex = index >> (CELL_SIZE - Integer.numberOfLeadingZeros(NUMBER_CELLS) - 1); // i / NUMBER_CELLS
		int cellIndex = index & (NUMBER_CELLS - 1); // i % NUMBER_CELLS
		if (arrayIndex >= length) {
			length = length * 2;
			array = Arrays.copyOf(array, length);
		}
		// mask = mask >>> cellIndex * dataSize;
		// int cell = value << ((CELL_SIZE / dataSize - 1) - cellIndex) * dataSize;
		value = value << cellIndex * dataSize;
		array[arrayIndex] = array[arrayIndex] | value;
		index++;
	}

	public Integer get(int i) {
		int arrayIndex = i >> (CELL_SIZE - Integer.numberOfLeadingZeros(NUMBER_CELLS) - 1); // i / NUMBER_CELLS
		int cellIndex = i & (NUMBER_CELLS - 1); // i % NUMBER_CELLS
		int mask = MASK;
		mask = mask << cellIndex * dataSize;
		int value = array[arrayIndex] & mask;
		value = value >>> cellIndex * dataSize;
		return value;
	}

	public int size() {
		return index;
	}

	public long sizeEstimation() {
		return array.length;
	}

	public boolean isEmpty() {
		return index == 0;
	}

	@Override
	public Iterator<Integer> iterator() {
		return new BitPackingIterator();
	}

	private class BitPackingIterator implements Iterator<Integer> {

		private int i;

		public BitPackingIterator() {
			i = 0;
		}

		@Override
		public boolean hasNext() {
			return i < index;
		}

		@Override
		public Integer next() {
			Integer value = get(i);
			i++;
			return value;
		}

	}

}
