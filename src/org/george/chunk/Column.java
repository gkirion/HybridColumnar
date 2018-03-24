package org.george.chunk;


public interface Column<E extends Comparable<E>> {

	void setName(String name);

	String getName();

	void add(E item);

	Tuple2<E, Integer> get(int i);
	
	Long sum(int start, int end);
	
	Integer count(int start, int end);
	
	Double avg(int start, int end);

	int getLength();
	
	int getCardinality();

	String toString();

}