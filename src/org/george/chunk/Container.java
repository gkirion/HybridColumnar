package org.george.chunk;

import java.util.BitSet;

public interface Container extends Iterable<Integer> {
	
	void add(short item);
	
	void set(int start, int end);
	
	boolean get(short i);
	
	BitSet getBitSet(int offset);
	
	Container get(int start, int end);
	
	int getCardinality();

	int getSize();
	
	int getLength();
	
	void clear();
	
	Container or(Container container);
	
	Container or(ContainerBitmap containerBitmap);
	
	Container or(ContainerArray containerArray);

	Container or(BitSet bitSet);
	
	Container and(Container container);
	
	Container and(ContainerBitmap containerBitmap);
	
	Container and(ContainerArray containerArray);
	
	Container and(BitSet bitSet);

}
