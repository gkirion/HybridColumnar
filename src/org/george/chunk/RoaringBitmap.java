package org.george.chunk;

import java.io.Serializable;
import java.util.Arrays;

public class RoaringBitmap implements Serializable {
	
	Container[] containers;
	short[] keys;
	int size;
	private final int INITIAL_CAPACITY = 4;
	private final int MAX_CAPACITY = 65536;
	
	public RoaringBitmap() {
		containers = new Container[INITIAL_CAPACITY];
		keys = new short[INITIAL_CAPACITY];
		size = 0;
	}
	
	public boolean get(int i) {
		short key = (short)(i / MAX_CAPACITY); // in which container i belongs
		Container container = getContainer(key);
		if (container == null) {
			return false;
		}
		return container.get((short)(i % MAX_CAPACITY));
	}
	
	public RoaringBitmap get(int start, int end) {
		RoaringBitmap roaringBitmap = new RoaringBitmap();
		for (int i = 0; i < size; i++) {
			int key = keys[i] & 0xFFFF;
			if ((key + 1) * MAX_CAPACITY > start && key * MAX_CAPACITY < end) {
				int from = Integer.max(key * MAX_CAPACITY, start) % MAX_CAPACITY;
				int to = Integer.min((key + 1) * MAX_CAPACITY, end) % MAX_CAPACITY;
				if (to <= from) {
					to = MAX_CAPACITY;
				}
				Container container = containers[i].get(from, to);
				if (container.getCardinality() == 0) System.out.println(container.getCardinality());
				roaringBitmap.addContainer((short)key, container);
			}
		}
		return roaringBitmap;
	}
	
	public void set(int i) {
		short key = (short)(i / MAX_CAPACITY); // in which container i belongs
		Container container = getContainer(key);
		if (container == null) { // if container doesn't exist, create it
			container = new ContainerArray();
			addContainer(key, container);
		}
		container.add((short) (i % MAX_CAPACITY));
		if (container instanceof ContainerArray && container.getCardinality() > 4096) { // container is dense, so convert it to bitmap
			Container newContainer = ((ContainerArray)container).convertToBitmap();
			replaceContainer(key, newContainer);
		}
	}
	
	public Container getContainer(short key) {
		int index = find(0, size - 1, key & 0xFFFF);
		if (index >= 0) {
			return containers[index];
		}
		return null;
	}
	
	public void addContainer(short key, Container container) {
		if (size >= keys.length) { // if size is not enough
			int newCapacity = keys.length < 2048 ? keys.length * 2 : (int) (keys.length < 16384 ? keys.length * 1.5 : Integer.min((int) (keys.length * 1.2), MAX_CAPACITY));
			containers = Arrays.copyOf(containers, newCapacity);
			keys = Arrays.copyOf(keys, newCapacity);
		}
		keys[size] = key;
		containers[size] = container;
		size++;
	}
	
	public void replaceContainer(short key, Container container) {
		int index = find(0, size - 1, key & 0xFFFF);
		containers[index] = container;
	}
	
	public int cardinality() {
		int cardinality = 0;
		for (int i = 0; i < size; i++) {
			cardinality += containers[i].getCardinality();
		}
		return cardinality;
	}
	
	protected int find(int left, int right, int key) {
		int mid = (left + right) / 2;
		if (left > right) {
			return -1;
		}
		int value = keys[mid] & 0xFFFF;
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

}
