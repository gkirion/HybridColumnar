package org.george.chunk;

public interface Container {
	
	void add(short item);
	
	boolean get(short i);
	
	Container get(int start, int end);
	
	int getCardinality();

	int getSize();

}
