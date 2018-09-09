package org.george.chunk;

import java.io.Serializable;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Iterator;

public class RoaringBitmap implements Iterable<Container>, Serializable {

	private short[] keys;
	private Container[] containers;
	private int size;
	private final int INITIAL_CAPACITY = 4;
	private final int MAX_CAPACITY = 65536;

	public RoaringBitmap() {
		containers = new Container[INITIAL_CAPACITY];
		keys = new short[INITIAL_CAPACITY];
		size = 0;
	}

	public RoaringBitmap(BitSet bitSet) {
		this();
		for (int i = bitSet.nextSetBit(0); i >= 0; i = bitSet.nextSetBit(i + 1)) {
			set(i);
			if (i == Integer.MAX_VALUE) {
				break;
			}
		}
	}

	public void set(int i) {
		short key = (short) (i / MAX_CAPACITY); // in which container i belongs
		Container container = getContainer(key);
		if (container == null) { // if container doesn't exist, create it
			container = new ContainerArray();
			container.setId(key & 0xFFFF);
			// container = new ContainerBitmap();
			addContainer(key, container);
		}
		container.set(i % MAX_CAPACITY);
		if (container instanceof ContainerArray && container.getCardinality() > 4096) { // container is dense, so
																						// convert it to bitmap
			Container newContainer = ((ContainerArray) container).convertToBitmap();
			newContainer.setId(container.getId());
			replaceContainer(key, newContainer);
		}
	}

	public void set(int start, int end) {
		int i = start;
		while (i < end) {
			short key = (short) (i / MAX_CAPACITY); // in which container i belongs
			Container container = getContainer(key);
			if (container == null) { // if container doesn't exist, create it
				if (end - start > 512) {
					container = new ContainerBitmap();
				} else {
					container = new ContainerArray();
				}
				container.setId(key & 0xFFFF);
				addContainer(key, container);
			}
			int from = Integer.max(key * MAX_CAPACITY, start) % MAX_CAPACITY; // start key inside container
			int to = Integer.min((key + 1) * MAX_CAPACITY, end) % MAX_CAPACITY; // end key inside container
			if (to <= from) {
				to = MAX_CAPACITY;
			}
			container.set(from, to);
			i += (to - from);
		}
	}

	public boolean get(int i) {
		short key = (short) (i / MAX_CAPACITY); // in which container i belongs
		Container container = getContainer(key);
		if (container == null) {
			return false;
		}
		return container.get(i % MAX_CAPACITY);
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
				// if (container.getCardinality() == 0)
				// System.out.println(container.getCardinality());
				roaringBitmap.addContainer((short) key, container);
			}
		}
		return roaringBitmap;
	}

	public Container[] getContainers() {
		return containers;
	}

	public short[] getKeys() {
		return keys;
	}

	public int getSize() {
		return size;
	}

	public void or(RoaringBitmap bitmap) {
		int n1 = getSize();
		int n2 = bitmap.getSize();
		short[] keys1 = getKeys();
		short[] keys2 = bitmap.getKeys();
		int i = 0, j = 0;
		Container[] containers2 = bitmap.getContainers();
		RoaringBitmap newBitmap = new RoaringBitmap();
		while (i < n1 && j < n2) {
			if ((keys1[i] & 0xFFFF) < (keys2[j] & 0xFFFF)) {
				newBitmap.addContainer(keys1[i], containers[i]);
				i++;
			} else if ((keys1[i] & 0xFFFF) == (keys2[j] & 0xFFFF)) {
				Container container = containers[i].or(containers2[j].get(0, containers2[j].getLength()));
				newBitmap.addContainer(keys1[i], container);
				i++;
				j++;
			} else {
				newBitmap.addContainer(keys2[j], containers2[j].get(0, containers2[j].getLength()));
				j++;
			}
		}
		for (int k = i; k < n1; k++) {
			newBitmap.addContainer(keys1[k], containers[k]);
		}
		for (int k = j; k < n2; k++) {
			newBitmap.addContainer(keys2[k], containers2[k].get(0, containers2[k].getLength()));
		}
		containers = newBitmap.getContainers();
		keys = newBitmap.getKeys();
		size = newBitmap.getSize();

		/*
		 * RoaringBitmap newBitmap = new RoaringBitmap(); Iterator<Container>
		 * firstIterator = this.iterator(); Iterator<Container> secondIterator =
		 * bitmap.iterator(); Container firstContainer = null; Container secondContainer
		 * = null; while (firstIterator.hasNext() && secondIterator.hasNext()) { if
		 * (firstContainer == null) { firstContainer = firstIterator.next(); } if
		 * (secondContainer == null) { secondContainer = secondIterator.next(); } if
		 * (firstContainer.getId() < secondContainer.getId()) {
		 * newBitmap.addContainer((short) firstContainer.getId(), firstContainer);
		 * firstContainer = null; } else if (firstContainer.getId() ==
		 * secondContainer.getId()) { Container container =
		 * firstContainer.or(secondContainer.get(0, secondContainer.getLength()));
		 * newBitmap.addContainer((short) firstContainer.getId(), container);
		 * firstContainer = null; secondContainer = null; } else {
		 * newBitmap.addContainer((short) secondContainer.getId(),
		 * secondContainer.get(0, secondContainer.getLength())); secondContainer = null;
		 * } } while (firstIterator.hasNext()) { if (firstContainer == null) {
		 * firstContainer = firstIterator.next(); } newBitmap.addContainer((short)
		 * firstContainer.getId(), firstContainer); } while (secondIterator.hasNext()) {
		 * if (secondContainer == null) { secondContainer = secondIterator.next(); }
		 * newBitmap.addContainer((short) secondContainer.getId(),
		 * secondContainer.get(0, secondContainer.getLength())); } containers =
		 * newBitmap.getContainers(); keys = newBitmap.getKeys(); size =
		 * newBitmap.getSize();
		 */
	}

	public void or(BitSet bitSet) {
		RoaringBitmap newBitmap = new RoaringBitmap();
		int i = bitSet.nextSetBit(0);
		int j = 0;
		while (i >= 0) {
			int key = (i / MAX_CAPACITY); // in which container i belongs
			if (j < size && (keys[j] & 0xFFFF) < (key & 0xFFFF)) { // roaring container is smaller, so insert it first
				newBitmap.addContainer(keys[j], containers[j]);
				j++;
			} else if (j < size && (keys[j] & 0xFFFF) == (key & 0xFFFF)) { // containers have the same index, so OR them
				containers[j] = containers[j].or(bitSet.get(key * MAX_CAPACITY, (key + 1) * MAX_CAPACITY));
				newBitmap.addContainer(keys[j], containers[j]);
				i = bitSet.nextSetBit(i + 1);
				if (i >= 0) {
					i = Integer.max(i, (key + 1) * MAX_CAPACITY);
				}
				j++;
			} else { // bitset is smaller, so create a container for it and insert it first
				Container container = new ContainerBitmap(bitSet.get(key * MAX_CAPACITY, (key + 1) * MAX_CAPACITY));
				newBitmap.addContainer((short) key, container);
				i = bitSet.nextSetBit(i + 1);
				if (i >= 0) {
					i = Integer.max(i, (key + 1) * MAX_CAPACITY);
				}
			}
			if (i == Integer.MAX_VALUE) {
				break;
			}
		}
		for (int k = j; k < size; k++) {
			newBitmap.addContainer(keys[k], containers[k]);
		}
		containers = newBitmap.getContainers();
		keys = newBitmap.getKeys();
		size = newBitmap.getSize();
	}

	public void and(BitSet bitSet) {
		RoaringBitmap newBitmap = new RoaringBitmap();
		int i = bitSet.nextSetBit(0);
		while (i >= 0) {
			int key = (short) (i / MAX_CAPACITY); // in which container i belongs
			Container container = getContainer((short) key);
			if (container != null) {
				container = container.and(bitSet.get(key * MAX_CAPACITY, (key + 1) * MAX_CAPACITY));
				newBitmap.addContainer((short) key, container);
			}
			if (i == Integer.MAX_VALUE) {
				break;
			}
			i = bitSet.nextSetBit(i + 1);
			if (i >= 0) {
				i = Integer.max(i, (key + 1) * MAX_CAPACITY);
			}
		}
		containers = newBitmap.getContainers();
		keys = newBitmap.getKeys();
		size = newBitmap.getSize();
	}

	public void and(RoaringBitmap bitmap) {
		int n1 = getSize();
		int n2 = bitmap.getSize();
		short[] keys1 = getKeys();
		short[] keys2 = bitmap.getKeys();
		int i = 0, j = 0;
		Container[] containers2 = bitmap.getContainers();
		RoaringBitmap newBitmap = new RoaringBitmap();
		while (i < n1 && j < n2) {
			if ((keys1[i] & 0xFFFF) < (keys2[j] & 0xFFFF)) {
				i++;
			} else if ((keys1[i] & 0xFFFF) == (keys2[j] & 0xFFFF)) {
				Container container = containers[i].and(containers2[j].get(0, containers2[j].getLength()));
				newBitmap.addContainer(keys1[i], container);
				i++;
				j++;
			} else {
				j++;
			}
		}
		containers = newBitmap.getContainers();
		keys = newBitmap.getKeys();
		size = newBitmap.getSize();
	}

	public BitSet convertToBitSet() {
		BitSet bitSet = new BitSet();
		for (int i = 0; i < size; i++) {
			int key = keys[i] & 0xFFFF;
			Container container = containers[i];
			for (int index : container) {
				bitSet.set(key * MAX_CAPACITY + index);
			}
		}
		return bitSet;
	}

	public void clear() {
		containers = new Container[INITIAL_CAPACITY];
		keys = new short[INITIAL_CAPACITY];
		size = 0;
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
			int newCapacity = keys.length < 2048 ? keys.length * 2
					: (int) (keys.length < 16384 ? keys.length * 1.5
							: Integer.min((int) (keys.length * 1.2), MAX_CAPACITY));
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
		} else if (value > key) {
			return find(left, mid - 1, key);
		} else {
			return mid;
		}
	}

	@Override
	public Iterator<Container> iterator() {
		return new RoaringBitmapIterator();
	}

	private class RoaringBitmapIterator implements Iterator<Container> {

		private int i;
		private Container container;

		public RoaringBitmapIterator() {
			i = 0;
		}

		@Override
		public boolean hasNext() {
			return i < size;
		}

		@Override
		public Container next() {
			container = containers[i];
			i++;
			return container;
		}

	}

}
