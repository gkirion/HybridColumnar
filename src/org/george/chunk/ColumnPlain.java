package org.george.chunk;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.function.Predicate;

public class ColumnPlain<E extends Comparable<E>> implements Column<E>, Serializable {

	private ArrayList<E> arrayList;
	private String name;
	private Integer id;

	public ColumnPlain() {
		this("");
	}

	public ColumnPlain(String name) {
		arrayList = new ArrayList<>();
		this.name = name;
		id = 0;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void add(E item) {
		arrayList.add(item);
		id++;
	}

	public Tuple2<E, Integer> get(int i) {
		return new Tuple2<E, Integer>(arrayList.get(i), 1);
	}

	public ArrayList<E> entrySet() {
		return arrayList;
	}

	public String toString() {
		return arrayList.toString();
	}

	public Column<E> filter(Predicate<E> predicate) {
		ColumnPlain<E> newColumn = new ColumnPlain<>();
		for (E value : arrayList) {
			if (predicate.test(value)) {
				newColumn.add(value);
			}
		}
		return newColumn;
	}

	public BitSet select(Predicate<E> predicate) {
		BitSet bitSet = new BitSet();
		for (int i = 0; i < id; i++) {
			if (predicate.test(arrayList.get(i))) {
				bitSet.set(i);
			}
		}
		return bitSet;
	}

	@Override
	public BitSet selectEquals(E item) {
		BitSet bitSet = new BitSet();
		for (int i = 0; i < id; i++) {
			if (arrayList.get(i).equals(item)) {
				bitSet.set(i);
			}
		}
		return bitSet;
	}

	@Override
	public BitSet selectNotEquals(E item) {
		BitSet bitSet = selectEquals(item);
		BitSet bSet = new BitSet();
		bSet.set(0, id); // set all to 1
		bSet.andNot(bitSet);
		return bSet;
	}

	@Override
	public BitSet selectLessThan(E item) {
		BitSet bitSet = new BitSet();
		for (int i = 0; i < id; i++) {
			if (arrayList.get(i).compareTo(item) < 0) {
				bitSet.set(i);
			}
		}
		return bitSet;
	}

	@Override
	public BitSet selectLessThanOrEquals(E item) {
		BitSet bitSet = new BitSet();
		for (int i = 0; i < id; i++) {
			if (arrayList.get(i).compareTo(item) <= 0) {
				bitSet.set(i);
			}
		}
		return bitSet;
	}

	@Override
	public BitSet selectMoreThan(E item) {
		BitSet bitSet = new BitSet();
		for (int i = 0; i < id; i++) {
			if (arrayList.get(i).compareTo(item) > 0) {
				bitSet.set(i);
			}
		}
		return bitSet;
	}

	@Override
	public BitSet selectMoreThanOrEquals(E item) {
		BitSet bitSet = new BitSet();
		for (int i = 0; i < id; i++) {
			if (arrayList.get(i).compareTo(item) >= 0) {
				bitSet.set(i);
			}
		}
		return bitSet;
	}

	@Override
	public BitSet selectBetween(E from, E to) {
		BitSet bitSet = new BitSet();
		for (int i = 0; i < id; i++) {
			if (arrayList.get(i).compareTo(from) >= 0 && arrayList.get(i).compareTo(to) <= 0) {
				bitSet.set(i);
			}
		}
		return bitSet;
	}

	public Integer count() {
		return arrayList.size();
	}

	@Override
	public Double sum() {
		Double sum = 0.0;
		for (E value : arrayList) {
			sum += (Double) value;
		}
		return sum;
	}

	@Override
	public Double sum(int start, int end) {
		Double sum = 0.0;
		for (int i = start; i < end; i++) {
			sum += ((Number) arrayList.get(i)).doubleValue();
		}
		return sum;
	}

	@Override
	public Double sum(BitSet bitSet) {
		Double sum = 0.0;
		for (int i = 0; i < id; i++) {
			if (bitSet.get(i)) {
				sum += ((Number) arrayList.get(i)).doubleValue();
			}
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
		Long sum = new Long(0);
		for (int i = 0; i < id; i++) {
			if (bitSet.get(i)) {
				sum += (Integer) arrayList.get(i);
			}
		}
		return sum / (double) bitSet.cardinality();
	}

	@Override
	public int length() {
		return arrayList.size();
	}

	@Override
	public int getCardinality() {
		HashMap<E, Boolean> distinctMap = new HashMap<>();
		for (E value : arrayList) {
			distinctMap.put(value, true);
		}
		return distinctMap.size();
	}

}
