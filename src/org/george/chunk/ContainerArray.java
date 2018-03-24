package org.george.chunk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class ContainerArray implements Container {

	private final int INITIAL_CAPACITY = 4;
	private final int MAX_CAPACITY = 65536;
	private short[] array;
	private int cardinality;
	private ArrayList<Integer> arrayList;
	
	public ContainerArray() {
		arrayList = new ArrayList<>();
		array = new short[INITIAL_CAPACITY];
		cardinality = 0;
	}

	@Override
	public void add(short item) {
		arrayList.add((int)item);
		if (array.length <= cardinality) {
			int newCapacity = array.length < 2048 ? array.length * 2 : (int) (array.length < 16384 ? array.length * 1.5 : Integer.min((int) (array.length * 1.2), MAX_CAPACITY));
			array = Arrays.copyOf(array, newCapacity);
		}
		array[cardinality] = item;
		cardinality++;
	}
	
	protected boolean find(int left, int right, int key) {
		int mid = (left + right) / 2;
		if (left > right) {
			return false;
		}
		if (arrayList.get(mid) < key) {
			return find(mid + 1, right, key);
		}
		else if (arrayList.get(mid) > key) {
			return find(left, mid - 1, key);
		}
		else {
			return true;
		}
	}

	@Override
	public boolean get(short i) {
		//return find(0, arrayList.size() - 1, i);
		int index = Arrays.binarySearch(array,0, cardinality, i);
		if (index >= 0)
			return true;
		return false;
	}

	@Override
	public int getCardinality() {
		//return arrayList.size();
		return cardinality;
	}

	@Override
	public int getSize() {
		//return arrayList.size();
		return cardinality;
	}

	@Override
	public Iterator<Integer> iterator() {
		return arrayList.iterator();
	}
}
