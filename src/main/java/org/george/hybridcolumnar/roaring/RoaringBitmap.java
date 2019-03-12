package org.george.hybridcolumnar.roaring;

import java.io.Serializable;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Iterator;

public class RoaringBitmap implements Iterable<Container>, Serializable {

	private static final long serialVersionUID = 1L;
	private Container[] containers;
	private int size;
	private final int INITIAL_SIZE = 4; // initial capacity for containers
	private final int MAX_CONTAINER_CAPACITY = 65536;
	private int containerCapacity; // max capacity of container

	public RoaringBitmap() {
		this(1000);
	}
	
	public RoaringBitmap(int containerCapacity) {
		containers = new Container[INITIAL_SIZE];
		this.containerCapacity = containerCapacity < MAX_CONTAINER_CAPACITY ?  containerCapacity : MAX_CONTAINER_CAPACITY;
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
		int key = i / containerCapacity; // in which container i belongs
		Container container = getContainer(key);
		if (container == null) { // if container doesn't exist, create it
			container = new ContainerArray();
			//container = new ContainerBitmap();
			container.setKey(key);
			addContainer(key, container);
		}
		container.set(i % containerCapacity);
		if (container instanceof ContainerArray && container.getCardinality() > containerCapacity * 0.05) { // container is dense, so
																						// convert it to bitmap
			Container newContainer = ((ContainerArray) container).convertToBitmap();
			replaceContainer(key, newContainer);
		}
	}

	public void set(int start, int end) {
		int i = start;
		while (i < end) {
			int key = i / containerCapacity; // in which container i belongs
			Container container = getContainer(key);
			if (container == null) { // if container doesn't exist, create it
				container = new ContainerArray();
				container.setKey(key);
				addContainer(key, container);
			}
			int from = Integer.max(key * containerCapacity, start) % containerCapacity; // start key inside container
			int to = Integer.min((key + 1) * containerCapacity, end) % containerCapacity; // end key inside container
			if (to <= from) {
				to = containerCapacity;
			}
			container.set(from, to);
			if (container instanceof ContainerArray && container.getCardinality() > containerCapacity * 0.05) { // container is dense, so
				// convert it to bitmap
				Container newContainer = ((ContainerArray) container).convertToBitmap();
				replaceContainer(key, newContainer);
			}
			i += (to - from);
		}
	}

	public boolean get(int i) {
		int key = i / containerCapacity; // in which container i belongs
		Container container = getContainer(key);
		if (container == null) {
			return false;
		}
		return container.get(i % containerCapacity);
	}

	public RoaringBitmap get(int start, int end) {
		RoaringBitmap roaringBitmap = new RoaringBitmap();
		for (int i = 0; i < size; i++) {
			int key = containers[i].getKey();
			if ((key + 1) * containerCapacity > start && key * containerCapacity < end) {
				int from = Integer.max(key * containerCapacity, start) % containerCapacity;
				int to = Integer.min((key + 1) * containerCapacity, end) % containerCapacity;
				if (to <= from) {
					to = containerCapacity;
				}
				Container container = containers[i].get(from, to);
				// if (container.getCardinality() == 0)
				// System.out.println(container.getCardinality());
				roaringBitmap.addContainer(key, container);
			}
		}
		return roaringBitmap;
	}

	public Container[] getContainers() {
		return containers;
	}

	public int getSize() {
		return size;
	}

	public void or(RoaringBitmap bitmap) {
		int otherSize = bitmap.getSize();
		int i = 0, j = 0;
		Container[] otherContainers = bitmap.getContainers();
		RoaringBitmap newBitmap = new RoaringBitmap();
		while (i < size && j < otherSize) {
			if (containers[i].getKey() < otherContainers[j].getKey()) {
				newBitmap.addContainer(containers[i].getKey(), containers[i]);
				i++;
			} else if (containers[i].getKey() == otherContainers[j].getKey()) {
				Container container = containers[i].or(otherContainers[j]);
				newBitmap.addContainer(container.getKey(), container);
				i++;
				j++;
			} else {
				newBitmap.addContainer(otherContainers[j].getKey(), otherContainers[j].get(0, containerCapacity));
				j++;
			}
		}
		for (int k = i; k < size; k++) {
			newBitmap.addContainer(containers[k].getKey(), containers[k]);
		}
		for (int k = j; k < otherSize; k++) {
			newBitmap.addContainer(otherContainers[k].getKey(), otherContainers[k].get(0, containerCapacity));
		}
		containers = newBitmap.getContainers();
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
			int key = i / containerCapacity; // in which container i belongs
			if (j < size && (containers[j].getKey() < key)) { // roaring container is smaller, so insert it first
				newBitmap.addContainer(containers[j].getKey(), containers[j]);
				j++;
			} else if (j < size && (containers[j].getKey() == key)) { // containers have the same index, so OR them
				Container container = containers[j]
						.or(bitSet.get(key * containerCapacity, (key + 1) * containerCapacity));
				newBitmap.addContainer(container.getKey(), container);
				i = bitSet.nextSetBit(i + 1);
				if (i >= 0) {
					i = Integer.max(i, (key + 1) * containerCapacity);
				}
				j++;
			} else { // bitset is smaller, so create a container for it and insert it first
				Container container = new ContainerBitmap(
						bitSet.get(key * containerCapacity, (key + 1) * containerCapacity));
				container.setKey(key);
				newBitmap.addContainer(container.getKey(), container);
				i = bitSet.nextSetBit(i + 1);
				if (i >= 0) {
					i = Integer.max(i, (key + 1) * containerCapacity);
				}
			}
			if (i == Integer.MAX_VALUE) {
				break;
			}
		}
		for (int k = j; k < size; k++) {
			newBitmap.addContainer(containers[k].getKey(), containers[k]);
		}
		containers = newBitmap.getContainers();
		size = newBitmap.getSize();
	}

	public void and(BitSet bitSet) {
		RoaringBitmap newBitmap = new RoaringBitmap();
		int i = bitSet.nextSetBit(0);
		while (i >= 0) {
			int key = i / containerCapacity; // in which container i belongs
			Container container = getContainer(key);
			if (container != null) {
				container = container.and(bitSet.get(key * containerCapacity, (key + 1) * containerCapacity));
				newBitmap.addContainer(key, container);
			}
			if (i == Integer.MAX_VALUE) {
				break;
			}
			i = bitSet.nextSetBit(i + 1);
			if (i >= 0) {
				i = Integer.max(i, (key + 1) * containerCapacity);
			}
		}
		containers = newBitmap.getContainers();
		size = newBitmap.getSize();
	}

	public void and(RoaringBitmap bitmap) {
		int otherSize = bitmap.getSize();
		int i = 0, j = 0;
		Container[] otherContainers = bitmap.getContainers();
		RoaringBitmap newBitmap = new RoaringBitmap();
		while (i < size && j < otherSize) {
			if (containers[i].getKey() < otherContainers[j].getKey()) {
				i++;
			} else if (containers[i].getKey() == otherContainers[j].getKey()) {
				Container container = containers[i].and(otherContainers[j]);
				newBitmap.addContainer(container.getKey(), container);
				i++;
				j++;
			} else {
				j++;
			}
		}
		containers = newBitmap.getContainers();
		size = newBitmap.getSize();
	}

	public BitSet convertToBitSet() {
		BitSet bitSet = new BitSet();
		for (int i = 0; i < size; i++) {
			Container container = containers[i];
			for (int index : container) {
				bitSet.set(container.getKey() * containerCapacity + index);
			}
		}
		return bitSet;
	}

	public void clear() {
		containers = new Container[INITIAL_SIZE];
		size = 0;
	}

	public Container getContainer(int key) {
		int index = find(0, size - 1, key);
		if (index >= 0) {
			return containers[index];
		}
		return null;
	}

	public void addContainer(int key, Container container) {
		if (size >= containers.length) { // if size is not enough
			int newCapacity = containers.length < 2048 ? containers.length * 2
					: (int) (containers.length < 16384 ? containers.length * 1.5
							: Integer.min((int) (containers.length * 1.2), containerCapacity));
			containers = Arrays.copyOf(containers, newCapacity);
		}
		containers[size] = container;
		size++;
	}

	public void replaceContainer(int key, Container container) {
		int index = find(0, size - 1, key);
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
		int value = containers[mid].getKey();
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
