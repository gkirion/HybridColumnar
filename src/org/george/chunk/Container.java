package org.george.chunk;

import java.util.Iterator;

public interface Container extends Iterable<Integer> {
	
	void add(short item);
	
	boolean get(short i);
	
	int getCardinality();

	int getSize();

}
