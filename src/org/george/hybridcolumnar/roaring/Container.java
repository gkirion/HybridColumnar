package org.george.hybridcolumnar.roaring;

import java.util.BitSet;

public interface Container extends Iterable<Integer> {

	void setKey(int key);

	int getKey();

	void set(int item);

	void set(int start, int end);

	boolean get(int i);

	Container get(int start, int end);

	Container or(Container container);

	Container or(ContainerBitmap containerBitmap);

	Container or(ContainerArray containerArray);

	Container or(BitSet bitSet);

	Container and(Container container);

	Container and(ContainerBitmap containerBitmap);

	Container and(ContainerArray containerArray);

	Container and(BitSet bitSet);

	int getCardinality();

	int getSize();

	int getLength();

	void clear();

}
