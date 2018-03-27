package org.george.chunk;

import java.io.Serializable;
import java.util.BitSet;
import java.util.Iterator;

public class ContainerBitmap implements Container, Serializable {
	
	private BitSet bitSet;
	
	public ContainerBitmap() {
		bitSet = new BitSet();
	}
	
	public ContainerBitmap(BitSet bitSet) {
		this.bitSet = bitSet;
	}

	@Override
	public void add(short item) {
		bitSet.set(item & 0xFFFF);
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


}
