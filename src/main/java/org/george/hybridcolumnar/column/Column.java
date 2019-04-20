package org.george.hybridcolumnar.column;

import java.util.function.Predicate;

import org.george.hybridcolumnar.domain.BitSetExtended;
import org.george.hybridcolumnar.domain.Tuple2;

@SuppressWarnings("rawtypes")
public interface Column<E extends Comparable> extends Iterable<Tuple2<E, Integer>> {

	void setName(String name);

	String getName();

	void add(E item);

	Tuple2<E, Integer> get(int i);

	BitSetExtended select(Predicate<E> predicate);

	BitSetExtended selectEquals(E item);

	BitSetExtended selectNotEquals(E item);

	BitSetExtended selectLessThan(E item);

	BitSetExtended selectLessThanOrEquals(E item);

	BitSetExtended selectMoreThan(E item);

	BitSetExtended selectMoreThanOrEquals(E item);

	BitSetExtended selectBetween(E from, E to);

	Column<E> filter(BitSetExtended bitSet);

	Double sum();

	Double sum(int start, int end);

	Double sum(BitSetExtended bitSet);

	Double sum(int start, int end, BitSetExtended bitSet);

	Integer count(int start, int end);

	Double avg();

	Double avg(int start, int end);

	Double avg(BitSetExtended bitSet);
	
	Column<E> convertToPlain();

	int length();

	int cardinality();

	long sizeEstimation();

	ColumnType type();

	@Override
	String toString();

}