package org.george.chunk;

import java.io.Serializable;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Predicate;

public class ColumnDictionaryDelta<E extends Comparable<E>> implements Column<E>, Iterable<E>, Serializable {

	private BitPacking bitPacking;
	private Dictionary<E> dictionary;
	private String name;
	private int offset;
	private Integer id;
	private int last;
	private int first;

	public ColumnDictionaryDelta() {
		this(2);
	}

	public ColumnDictionaryDelta(int range) {
		this(range, 0);
	}

	public ColumnDictionaryDelta(int range, int offset) {
		int numberOfBits = Integer.SIZE - Integer.numberOfLeadingZeros((range - 1));
		bitPacking = new BitPacking(numberOfBits);
		dictionary = new Dictionary<>();
		name = "";
		this.offset = offset;
		id = 0;
		last = -1;
	}

	public ColumnDictionaryDelta(String name) {
		this(2);
		this.name = name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void add(E item) {
		Integer key = dictionary.insert(item);
		if (id == 0) {
			first = key;
			last = key;
		}
		bitPacking.add(key - last + offset);
		last = key;
		id++;
	}

	@Override
	public Tuple2<E, Integer> get(int i) {
		int value = first;
		for (int j = 0; j <= i; j++) {
			value += bitPacking.get(j) - offset;
		}
		return new Tuple2<>(dictionary.get(value), 1);
	}

	@Override
	public BitSet select(Predicate<E> predicate) {
		BitSet bitSet = new BitSet();
		int i = 0;
		int value = first;
		for (Integer val : bitPacking) {
			value += val - offset;
			if (predicate.test(dictionary.get(value))) {
				bitSet.set(i);
			}
			i++;
		}
		return bitSet;
	}

	@Override
	public BitSet selectEquals(E item) {
		BitSet bitSet = new BitSet();
		int i = 0;
		int value = first;
		for (Integer val : bitPacking) {
			value += val - offset;
			if (dictionary.get(value).equals(item)) {
				bitSet.set(i);
			}
			i++;
		}
		return bitSet;
	}

	@Override
	public BitSet selectNotEquals(E item) {
		BitSet bitSet = new BitSet();
		int i = 0;
		int value = first;
		for (Integer val : bitPacking) {
			value += val - offset;
			if (!dictionary.get(value).equals(item)) {
				bitSet.set(i);
			}
			i++;
		}
		return bitSet;
	}

	@Override
	public BitSet selectLessThan(E item) {
		BitSet bitSet = new BitSet();
		int i = 0;
		int value = first;
		for (Integer val : bitPacking) {
			value += val - offset;
			if (dictionary.get(value).compareTo(item) < 0) {
				bitSet.set(i);
			}
			i++;
		}
		return bitSet;
	}

	@Override
	public BitSet selectLessThanOrEquals(E item) {
		BitSet bitSet = new BitSet();
		int i = 0;
		int value = first;
		for (Integer val : bitPacking) {
			value += val - offset;
			if (dictionary.get(value).compareTo(item) <= 0) {
				bitSet.set(i);
			}
			i++;
		}
		return bitSet;
	}

	@Override
	public BitSet selectMoreThan(E item) {
		BitSet bitSet = new BitSet();
		int i = 0;
		int value = first;
		for (Integer val : bitPacking) {
			value += val - offset;
			if (dictionary.get(value).compareTo(item) > 0) {
				bitSet.set(i);
			}
			i++;
		}
		return bitSet;
	}

	@Override
	public BitSet selectMoreThanOrEquals(E item) {
		BitSet bitSet = new BitSet();
		int i = 0;
		int value = first;
		for (Integer val : bitPacking) {
			value += val - offset;
			if (dictionary.get(value).compareTo(item) >= 0) {
				bitSet.set(i);
			}
			i++;
		}
		return bitSet;
	}

	@Override
	public BitSet selectBetween(E from, E to) {
		BitSet bitSet = new BitSet();
		int i = 0;
		int value = first;
		for (Integer val : bitPacking) {
			value += val - offset;
			if (dictionary.get(value).compareTo(from) >= 0 && dictionary.get(value).compareTo(to) <= 0) {
				bitSet.set(i);
			}
			i++;
		}
		return bitSet;
	}

	@Override
	public Double sum() {
		return sum(0, id);
	}

	@Override
	public Double sum(int start, int end) {
		int i = 0;
		Double sum = 0.0;
		int value = first;
		for (Integer val : bitPacking) {
			value += val - offset;
			if (i >= start && i < end) {
				sum += ((Number) dictionary.get(value)).doubleValue();
			}
			i++;
		}
		return sum;
	}

	@Override
	public Double sum(BitSet bitSet) {
		int i = 0;
		int value = first;
		Double sum = 0.0;
		for (Integer val : bitPacking) {
			value += val - offset;
			if (bitSet.get(i)) {
				sum += ((Number) dictionary.get(value)).doubleValue();
			}
			i++;
		}
		return sum;
	}

	@Override
	public Integer count(int start, int end) {
		return (end < id ? end : id) - start;
	}

	@Override
	public Double avg() {
		return avg(0, id);
	}

	@Override
	public Double avg(int start, int end) {
		return sum(start, end) / (double) count(start, end);
	}

	@Override
	public Double avg(BitSet bitSet) {
		int i = 0;
		int value = first;
		Long sum = new Long(0);
		for (Integer val : bitPacking) {
			value += val - offset;
			if (bitSet.get(i)) {
				sum += (Integer) dictionary.get(value);
			}
			i++;
		}
		return sum / (double) bitSet.cardinality();
	}

	@Override
	public int length() {
		return id;
	}

	@Override
	public int getCardinality() {
		HashMap<Integer, Boolean> distinctMap = new HashMap<>();
		int value = first;
		for (Integer val : bitPacking) {
			value += val - offset;
			distinctMap.put(value, true);
		}
		return distinctMap.size();
	}

	@Override
	public Iterator<E> iterator() {
		return new ColumnDeltaIterator();
	}

	private class ColumnDeltaIterator implements Iterator<E> {

		private int i;
		private int value;

		public ColumnDeltaIterator() {
			i = 0;
			value = first;
		}

		@Override
		public boolean hasNext() {
			return i < id;
		}

		@Override
		public E next() {
			value += bitPacking.get(i) - offset;
			i++;
			return dictionary.get(value);
		}

	}

}
