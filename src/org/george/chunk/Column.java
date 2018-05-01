package org.george.chunk;

import java.util.BitSet;
import java.util.function.Predicate;

public interface Column<E extends Comparable<E>> {

	void setName(String name);

	String getName();

	void add(E item);

	Tuple2<E, Integer> get(int i);

	BitSet select(Predicate<E> predicate);
	
	BitSet selectEquals(E item);
	
	BitSet selectNotEquals(E item);
	
	BitSet selectLessThan(E item);
	
	BitSet selectLessThanOrEquals(E item);
	
	BitSet selectMoreThan(E item);
	
	BitSet selectMoreThanOrEquals(E item);
	
	BitSet selectBetween(E from, E to);
	
	Long sum(int start, int end);
	
	Long sum(BitSet bitSet);
	
	Integer count(int start, int end);
	
	Double avg(int start, int end);

	int getLength();
	
	int getCardinality();

	String toString();

}