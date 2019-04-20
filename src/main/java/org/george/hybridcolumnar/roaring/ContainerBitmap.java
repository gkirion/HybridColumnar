package org.george.hybridcolumnar.roaring;

import java.io.Serializable;
import java.util.BitSet;
import java.util.Iterator;

public class ContainerBitmap implements Container, Serializable {

	private BitSet bitSet;
	private int key;

	public ContainerBitmap() {
		this(0);
	}

	public ContainerBitmap(int key) {
		this.key = key;
		bitSet = new BitSet();
	}

	public ContainerBitmap(BitSet bitSet) {
		this.bitSet = bitSet;
	}

	@Override
	public void setKey(int key) {
		this.key = key;
	}

	@Override
	public int getKey() {
		return key;
	}

	@Override
	public void set(int item) {
		bitSet.set(item & 0xFFFF);
	}

	@Override
	public void set(int start, int end) {
		bitSet.set(start, end);
	}

	@Override
	public boolean get(int i) {
		return bitSet.get(i & 0xFFFF);
	}

	@Override
	public Container get(int start, int end) {
		Container container = new ContainerBitmap(bitSet.get(start, end));
		container.setKey(key);
		return container;
	}

	public BitSet getBitSet() {
		return bitSet;
	}

	@Override
	public Container or(Container container) {
		
		  if (container instanceof ContainerArray) { return
		  or((ContainerArray)container); } else { return
		  or((ContainerBitmap)container); }
		 
/*		Container container3 = new ContainerBitmap();
		Iterator<Integer> iterator1 = this.iterator();
		Iterator<Integer> iterator2 = container.iterator();
		while (iterator1.hasNext() && iterator2.hasNext()) {
			int val1 = iterator1.next();
			int val2 = iterator2.next();
			if (val1 < val2) {
				container3.set(val1);
				container3.set(val2);
			} else if (val1 > val2) {
				container3.set(val2);
				container3.set(val1);
			} else {
				container3.set(val1);
			}
		}
		while (iterator1.hasNext()) {
			container3.set(iterator1.next());
		}
		while (iterator2.hasNext()) {
			container3.set(iterator2.next());
		}
		return container3;*/
	}

	@Override
	public Container and(Container container) {
		/*
		 * if (container instanceof ContainerArray) { return
		 * and((ContainerArray)container); } else { return
		 * and((ContainerBitmap)container); }
		 */
		Container container3 = new ContainerArray();
		for (Integer element : this) {
			if (container.get(element)) {
				container3.set(element);
			}
		}
		return container3;
	}

	@Override
	public Container or(ContainerBitmap containerBitmap) {
		bitSet.or(containerBitmap.getBitSet());
		return this;
	}

	@Override
	public Container or(ContainerArray containerArray) {
		short[] array = containerArray.getArray();
		int n = containerArray.getCardinality();
		for (int i = 0; i < n; i++) {
			bitSet.set(array[i] & 0xFFFF);
		}
		return this;
	}

	@Override
	public Container or(BitSet bitSet) {
		this.bitSet.or(bitSet);
		return this;
	}

	@Override
	public Container and(ContainerBitmap containerBitmap) {
		bitSet.and(containerBitmap.getBitSet());
		return this;
	}

	@Override
	public Container and(ContainerArray containerArray) {
		short[] array = containerArray.getArray();
		int n = containerArray.getCardinality();
		short[] array2 = new short[n];
		int k = 0;
		for (int i = 0; i < n; i++) {
			if (bitSet.get(array[i] & 0xFFFF)) {
				array2[k] = array[i];
				k++;
			}
		}
		return new ContainerArray(array2, k);
	}

	@Override
	public Container and(BitSet bitSet) {
		this.bitSet.and(bitSet);
		return this;
	}

	@Override
	public int getCardinality() {
		return bitSet.cardinality();
	}

	@Override
	public int getSize() {
		return 20 + bitSet.cardinality() / 8;
	}

	@Override
	public int getLength() {
		return bitSet.length();
	}

	@Override
	public void clear() {
		bitSet.clear();
	}

	@Override
	public Iterator<Integer> iterator() {
		return new ContainerBitmapIterator();
	}

	private class ContainerBitmapIterator implements Iterator<Integer> {

		private int index;

		public ContainerBitmapIterator() {
			index = bitSet.nextSetBit(0);
		}

		@Override
		public boolean hasNext() {
			return index >= 0;
		}

		@Override
		public Integer next() {
			int value = index;
			if (index == Integer.MAX_VALUE) {
				index = -1;
			}
			else {
				index = bitSet.nextSetBit(index + 1);
			}
			return value;
		}

	}

}
