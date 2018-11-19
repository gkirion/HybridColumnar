package org.george.hybridcolumnar.column;

import java.util.BitSet;
import java.util.function.Predicate;

import org.george.hybridcolumnar.domain.Tuple2;

public interface Column<E extends Comparable<E>> extends Iterable<Tuple2<E, Integer>> {

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

	Column<E> filter(BitSet bitSet);

	Double sum();

	Double sum(int start, int end);

	Double sum(BitSet bitSet);
	
	Double sum(int start, int end, BitSet bitSet);

	Integer count(int start, int end);

	Double avg();

	Double avg(int start, int end);

	Double avg(BitSet bitSet);

	int length();

	int cardinality();

	ColumnType type();

	String toString();

}