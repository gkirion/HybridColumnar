package org.george.chunk;

import java.io.Serializable;
import java.util.BitSet;
import java.util.Iterator;

public class ContainerBitmap implements Container, Iterable<Integer>, Serializable {
	
	private BitSet bitSet;
	
	public ContainerBitmap() {
		bitSet = new BitSet();
	}
	
	public ContainerBitmap(BitSet bitSet) {
		this.bitSet = bitSet;
	}
	
	public BitSet getBitSet(int offset) {
		BitSet bSet = new BitSet();
		for (int i = bitSet.nextSetBit(0); i >= 0; i = bitSet.nextSetBit(i + 1)) {
			bSet.set(offset + i);
		}
		return bSet;
	}

	@Override
	public void add(short item) {
		bitSet.set(item & 0xFFFF);
	}
	
	@Override
	public void set(int start, int end) {
		bitSet.set(start, end);
	}

	@Override
	public boolean get(short i) {
		return bitSet.get(i & 0xFFFF);
	}
	
	@Override
	public Container get(int start, int end) {
		return new ContainerBitmap(bitSet.get(start, end));
	}

	@Override
	public int getCardinality() {
		return bitSet.cardinality();
	}

	@Override
	public int getSize() {
		return bitSet.length();
	}
	
	public BitSet getBitSet() {
		return bitSet;
	}
	
	@Override
	public Container or(Container container) {
		if (container instanceof ContainerArray) {
			return or((ContainerArray)container);
		}
		else {
			return or((ContainerBitmap)container);
		}
	}

	@Override
	public Container and(Container container) {
		if (container instanceof ContainerArray) {
			return and((ContainerArray)container);
		}
		else {
			return and((ContainerBitmap)container);
		}
	}

	@Override
	public Container or(ContainerBitmap containerBitmap) {
		bitSet.or(containerBitmap.getBitSet());
		return this;
	}

	@Override
	public Container or(ContainerArray containerArray) {
		short[] array = containerArray.getArray();
		int n = containerArray.getSize();
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
		int n = containerArray.getSize();
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
	public void clear() {
		bitSet.clear();
	}

	@Override
	public int getLength() {
		return bitSet.length();
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
			index = bitSet.nextSetBit(index + 1);
			return value;
		}
		
	}

}
