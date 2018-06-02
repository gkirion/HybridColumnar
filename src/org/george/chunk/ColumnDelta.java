package org.george.chunk;

import java.io.Serializable;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Predicate;

public class ColumnDelta implements Column<Integer>, Iterable<Integer>, Serializable {
	
	private BitPacking bitPacking;
	private String name;
	private int offset;
	private int id;
	private int last;
	private int first;
	
	public ColumnDelta() {
		bitPacking = new BitPacking();
		name = "";
		offset = 0;
		id = 0;
		last = 0;
		first = -1;
	}
	
	public ColumnDelta(int range) {
		int numberOfBits = Integer.SIZE - Integer.numberOfLeadingZeros((range - 1));
		bitPacking = new BitPacking(numberOfBits);
		name = "";
		offset = 0;
		last = 0;
		first = -1;
	}
	
	public ColumnDelta(int range, int offset) {
		int numberOfBits = Integer.SIZE - Integer.numberOfLeadingZeros((range - 1));
		bitPacking = new BitPacking(numberOfBits);
		name = "";
		this.offset = offset;
		id = 0;
		last = -1;
	}
	
	public ColumnDelta(String name) {
		bitPacking = new BitPacking();
		this.name = name;
		last = 0;
		first = -1;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void add(Integer item) {
		if (id == 0) {
			first = item;
			last = item;
		}
		bitPacking.add(item - last + offset);
		last = item;
		id++;
	}

	@Override
	public Tuple2<Integer, Integer> get(int i) {
		int value = first;
		for (int j = 0; j <= i; j++) {
			value += bitPacking.get(j) - offset;
		}
		return new Tuple2<>(value, 1);
	}

	public BitSet select(Predicate<Integer> predicate) {
		BitSet bitSet = new BitSet();
		int i = 0;
		int value = first;
		for (Integer val : bitPacking) {
			value += val - offset;
			if (predicate.test(value)) {
				bitSet.set(i);
			}
			i++;
		}
		return bitSet;
	}

	public BitSet selectEquals(Integer item) {
		BitSet bitSet = new BitSet();
		int i = 0;
		int value = first;
		for (Integer val : bitPacking) {
			value += val - offset;
			if (value == item) {
				bitSet.set(i);
			}
			i++;
		}
		return bitSet;
	}

	public BitSet selectNotEquals(Integer item) {
		BitSet bitSet = new BitSet();
		int i = 0;
		int value = first;
		for (Integer val : bitPacking) {
			value += val - offset;
			if (value == item) {
				bitSet.set(i);
			}
			i++;
		}
		return bitSet;
	}

	public BitSet selectLessThan(Integer item) {
		BitSet bitSet = new BitSet();
		int i = 0;
		int value = first;
		for (Integer val : bitPacking) {
			value += val - offset;
			if (value < item) {
				bitSet.set(i);
			}
			i++;
		}
		return bitSet;
	}

	public BitSet selectLessThanOrEquals(Integer item) {
		BitSet bitSet = new BitSet();
		int i = 0;
		int value = first;
		for (Integer val : bitPacking) {
			value += val - offset;
			if (value <= item) {
				bitSet.set(i);
			}
			i++;
		}
		return bitSet;
	}

	public BitSet selectMoreThan(Integer item) {
		BitSet bitSet = new BitSet();
		int i = 0;
		int value = first;
		for (Integer val : bitPacking) {
			value += val - offset;
			if (value > item) {
				bitSet.set(i);
			}
			i++;
		}
		return bitSet;
	}

	public BitSet selectMoreThanOrEquals(Integer item) {
		BitSet bitSet = new BitSet();
		int i = 0;
		int value = first;
		for (Integer val : bitPacking) {
			value += val - offset;
			if (value >= item) {
				bitSet.set(i);
			}
			i++;
		}
		return bitSet;
	}

	public BitSet selectBetween(Integer from, Integer to) {
		BitSet bitSet = new BitSet();
		int i = 0;
		int value = first;
		for (Integer val : bitPacking) {
			value += val - offset;
			if (value >= from && value <= to) {
				bitSet.set(i);
			}
			i++;
		}
		return bitSet;
	}

	public Long sum(int start, int end) {
		int i = 0;
		Long sum = new Long(0);
		int value = first;
		for (Integer val : bitPacking) {
			value += val - offset;
			if (i >= start && i < end) {
				sum += value;
			}
			i++;
		}
		return sum;
	}

	public Long sum(BitSet bitSet) {
		int i = 0;
		int value = first;
		Long sum = new Long(0);
		for (Integer val : bitPacking) {
			value += val - offset;
			if (bitSet.get(i)) {
				sum += value;
			}
			i++;
		}
		return sum;
	}

	public Integer count(int start, int end) {
		return (end < id ? end : id) - start;
	}

	public Double avg(int start, int end) {
		return sum(start, end) / (double)count(start, end);
	}

	public int getLength() {
		return bitPacking.size();
	}

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
	public Iterator<Integer> iterator() {
		return new ColumnDeltaIterator();
	}
	
	private class ColumnDeltaIterator implements Iterator<Integer> {
		
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
		public Integer next() {
			value += bitPacking.get(i) - offset;
			i++;
			return value;
		}
		
	}

}
