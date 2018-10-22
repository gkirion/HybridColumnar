package org.george.hybridcolumnar.roaring;

import java.io.Serializable;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Iterator;

public class ContainerArray implements Container, Serializable {

	private short[] array;
	private int cardinality;
	private final int INITIAL_CAPACITY = 4;
	private final int MAX_CAPACITY = 65536;
	private int key;

	public ContainerArray() {
		this(0);
	}

	public ContainerArray(int key) {
		this.key = key;
		array = new short[INITIAL_CAPACITY];
		cardinality = 0;
	}

	public ContainerArray(short[] array, int cardinality) {
		this.array = array;
		this.cardinality = cardinality;
	}

	@Override
	public void setKey(int key) {
		this.key = key;
	}

	@Override
	public int getKey() {
		return key;
	}

	public short[] getArray() {
		return array;
	}

	@Override
	public void set(int item) {
		if (array.length <= cardinality) { // if size is not enough
			int newCapacity = array.length < 512 ? array.length * 2
					: (int) (array.length < 4096 ? array.length * 1.5
							: Integer.min((int) (array.length * 1.2), MAX_CAPACITY));
			array = Arrays.copyOf(array, newCapacity);
		}
		array[cardinality] = (short) item;
		cardinality++;
	}

	@Override
	public void set(int start, int end) {
		for (int i = start; i < end; i++) {
			set(i);
		}
	}

	@Override
	public boolean get(int item) {
		int index = find(0, cardinality - 1, item & 0xFFFF);
		if (index >= 0) { // item found
			return true;
		}
		return false;
	}

	@Override
	public Container get(int start, int end) {
		int fromIndex = findGreaterThanOrEqual(0, cardinality - 1, start);
		int toIndex = findLessThanOrEqual(0, cardinality - 1, end);
		if (fromIndex < 0 || toIndex < 0) { // no element in that range
			return new ContainerArray();
		}
		if ((array[toIndex] & 0xFFFF) < end) { // if last element is smaller than end include that too
			toIndex++;
		}
		short[] newArray = Arrays.copyOfRange(array, fromIndex, toIndex);
		int newCardinality = toIndex - fromIndex;
		Container container = new ContainerArray(newArray, newCardinality);
		container.setKey(key);
		return container;
	}

	public ContainerBitmap convertToBitmap() {
		ContainerBitmap newContainer = new ContainerBitmap();
		for (int i = 0; i < cardinality; i++) {
			newContainer.set(array[i] & 0xFFFF);
		}
		newContainer.setKey(key);
		return newContainer;
	}

	protected int find(int left, int right, int key) {
		int mid = (left + right) / 2;
		if (left > right) {
			return -1;
		}
		int value = array[mid] & 0xFFFF;
		if (value < key) {
			return find(mid + 1, right, key);
		} else if (value > key) {
			return find(left, mid - 1, key);
		} else {
			return mid;
		}
	}

	protected int findGreaterThanOrEqual(int left, int right, int key) {
		int mid = (left + right) / 2;
		if (left > right) {
			return -1;
		}
		int value = array[mid] & 0xFFFF;
		if (value < key) {
			return findGreaterThanOrEqual(mid + 1, right, key);
		} else if (value > key) {
			if (mid - 1 < 0 || ((array[mid - 1] & 0xFFFF) < key)) {
				return mid;
			}
			return findGreaterThanOrEqual(left, mid - 1, key);
		} else {
			return mid;
		}
	}

	protected int findLessThanOrEqual(int left, int right, int key) {
		int mid = (left + right) / 2;
		if (left > right) {
			return -1;
		}
		int value = array[mid] & 0xFFFF;
		if (value < key) {
			if (mid + 1 > right || ((array[mid + 1] & 0xFFFF) > key)) {
				return mid;
			}
			return findLessThanOrEqual(mid + 1, right, key);
		} else if (value > key) {
			return findLessThanOrEqual(left, mid - 1, key);
		} else {
			return mid;
		}
	}

	@Override
	public Container or(Container container) {
		/*
		 * if (container instanceof ContainerArray) { return
		 * or((ContainerArray)container); } else { return
		 * or((ContainerBitmap)container); }
		 */
		Container container3 = new ContainerBitmap(key);
		for (Integer val : this) {
			container3.set(val);
		}
		for (Integer val : container) {
			container3.set(val);
		}
		return container3;
	}

	@Override
	public Container and(Container container) {
		/*
		 * if (container instanceof ContainerArray) { return
		 * and((ContainerArray)container); } else { return
		 * and((ContainerBitmap)container); }
		 */
		Container container3 = new ContainerArray(key);
		for (Integer element : this) {
			if (container.get(element)) {
				container3.set(element);
			}
		}
		return container3;
	}

	@Override
	public Container or(ContainerBitmap containerBitmap) {
		BitSet bitSet = (BitSet) containerBitmap.getBitSet().clone();
		for (int i = 0; i < cardinality; i++) {
			bitSet.set(array[i] & 0xFFFF);
		}
		return new ContainerBitmap(bitSet);
	}

	@Override
	public Container or(ContainerArray containerArray) {
		int n1 = getSize();
		int n2 = containerArray.getSize();
		short[] array2 = containerArray.getArray();
		short[] newArray = new short[n1 + n2];
		int i = 0, j = 0, z = 0;
		while (i < n1 && j < n2) {
			if ((array[i] & 0xFFFF) < (array2[j] & 0xFFFF)) {
				newArray[z] = array[i];
				i++;
			} else if ((array[i] & 0xFFFF) == (array2[j] & 0xFFFF)) {
				newArray[z] = array[i];
				i++;
				j++;
			} else {
				newArray[z] = array2[j];
				j++;
			}
			z++;
		}
		if (i >= n1) {
			for (int k = j; k < n2; k++) {
				newArray[z] = array2[k];
				z++;
			}
		} else {
			for (int k = i; k < n1; k++) {
				newArray[z] = array[k];
				z++;
			}
		}
		array = newArray;
		cardinality = z;
		return this;
	}

	@Override
	public Container or(BitSet bitSet) {
		BitSet bSet = (BitSet) bitSet.clone();
		for (int i = 0; i < cardinality; i++) {
			bSet.set(array[i] & 0xFFFF);
		}
		ContainerBitmap containerBitmap = new ContainerBitmap(bSet);
		containerBitmap.setKey(key);
		return containerBitmap;
	}

	@Override
	public Container and(ContainerBitmap containerBitmap) {
		BitSet bitSet = containerBitmap.getBitSet();
		int n = getSize();
		short[] array2 = new short[n];
		int k = 0;
		for (int i = 0; i < n; i++) {
			if (bitSet.get(array[i] & 0xFFFF)) {
				array2[k] = array[i];
				k++;
			}
		}
		array = array2;
		cardinality = k;
		return this;
	}

	@Override
	public Container and(ContainerArray containerArray) {
		int n1 = getSize();
		int n2 = containerArray.getSize();
		short[] array2 = containerArray.getArray();
		short[] newArray = new short[n1];
		int i = 0, j = 0, z = 0;
		while (i < n1 && j < n2) {
			if ((array[i] & 0xFFFF) < (array2[j] & 0xFFFF)) {
				i++;
			} else if ((array[i] & 0xFFFF) == (array2[j] & 0xFFFF)) {
				newArray[z] = array[i];
				i++;
				j++;
				z++;
			} else {
				j++;
			}
		}
		array = newArray;
		cardinality = z;
		return this;
	}

	@Override
	public Container and(BitSet bitSet) {
		BitSet newBitSet = new BitSet();
		for (int i = 0; i < cardinality; i++) {
			if (bitSet.get(array[i] & 0xFFFF)) {
				newBitSet.set(array[i] & 0xFFFF);
			}
		}
		return new ContainerBitmap(newBitSet);
	}

	@Override
	public int getCardinality() {
		return cardinality;
	}

	@Override
	public int getSize() {
		return cardinality;
	}

	@Override
	public void clear() {
		array = new short[INITIAL_CAPACITY];
		cardinality = 0;
	}

	@Override
	public int getLength() {
		return (array[cardinality - 1] & 0xFFFF) + 1;
	}

	@Override
	public Iterator<Integer> iterator() {
		return new ContainerArrayIterator();
	}

	private class ContainerArrayIterator implements Iterator<Integer> {

		private int index;

		public ContainerArrayIterator() {
			index = 0;
		}

		@Override
		public boolean hasNext() {
			return index < cardinality;
		}

		@Override
		public Integer next() {
			int value = array[index] & 0xFFFF;
			index++;
			return value;
		}

	}

}
