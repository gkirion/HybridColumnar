package org.george.chunk;

import java.io.Serializable;
import java.util.Arrays;

public class ContainerArray implements Container, Serializable {

	private final int INITIAL_CAPACITY = 4;
	private final int MAX_CAPACITY = 65536;
	private short[] array;
	private int cardinality;
	
	public ContainerArray() {
		array = new short[INITIAL_CAPACITY];
		cardinality = 0;
	}
	
	public ContainerArray(short[] array, int cardinality) {
		this.array = array;
		this.cardinality = cardinality;
	}

	@Override
	public void add(short item) {
		if (array.length <= cardinality) {
			int newCapacity = array.length < 2048 ? array.length * 2 : (int) (array.length < 16384 ? array.length * 1.5 : Integer.min((int) (array.length * 1.2), MAX_CAPACITY));
			array = Arrays.copyOf(array, newCapacity);
		}
		array[cardinality] = item;
		cardinality++;
	}
	
	public ContainerBitmap convertToBitmap() {
		ContainerBitmap newContainer = new ContainerBitmap();
		for (int i = 0; i < cardinality; i++) {
			newContainer.add(array[i]);
		}
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
		}
		else if (value > key) {
			return find(left, mid - 1, key);
		}
		else {
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
		}
		else if (value > key) {
			if (mid - 1 < 0 || ((array[mid - 1] & 0xFFFF) < key)) {
				return mid;
			}
			return findGreaterThanOrEqual(left, mid - 1, key);
		}
		else {
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
		}
		else if (value > key) {
			return findLessThanOrEqual(left, mid - 1, key);
		}
		else {
			return mid;
		}
	}

	@Override
	public boolean get(short i) {
		int index = find(0, cardinality - 1, i & 0xFFFF);
		if (index >= 0) {
			return true;
		}
		return false;
	}
	
	@Override
	public Container get(int start, int end) {
		int fromIndex = findGreaterThanOrEqual(0, cardinality - 1, start);
		int toIndex = findLessThanOrEqual(0, cardinality - 1, end);
		if (fromIndex < 0 || toIndex < 0) {
			System.out.println(start + " " + end);
			System.out.println(fromIndex + " " + toIndex);
			System.out.println("start: " + (array[0] & 0xFFFF));
			System.out.println("end: " + (array[cardinality - 1] & 0xFFFF));
			return new ContainerArray();
		}
		if ((array[toIndex] & 0xFFFF) < end) {
			toIndex++;
		}
		short[] newArray = Arrays.copyOfRange(array, fromIndex, toIndex);
		int newCardinality = toIndex - fromIndex;
		return new ContainerArray(newArray, newCardinality);
	}

	@Override
	public int getCardinality() {
		return cardinality;
	}

	@Override
	public int getSize() {
		return cardinality;
	}

}
