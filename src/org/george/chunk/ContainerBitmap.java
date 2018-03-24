package org.george.chunk;

import java.util.BitSet;
import java.util.Iterator;

public class ContainerBitmap implements Container {
	
	private BitSet bitSet;
	
	public ContainerBitmap() {
		bitSet = new BitSet();
	}

	@Override
	public void add(short item) {
		bitSet.set(item);
	}

	@Override
	public boolean get(short i) {
		return bitSet.get(i);
	}

	@Override
	public int getCardinality() {
		return bitSet.cardinality();
	}

	@Override
	public int getSize() {
		return bitSet.length();
	}

	@Override
	public Iterator<Integer> iterator() {
		return null;
	}
}
