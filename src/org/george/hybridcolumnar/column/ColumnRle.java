package org.george.hybridcolumnar.column;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import org.george.hybridcolumnar.domain.Tuple2;
import org.george.hybridcolumnar.domain.Tuple3;

public class ColumnRle<E extends Comparable<E>> implements Column<E>, Serializable {

	private ArrayList<Tuple3<E, Integer, Integer>> arrayList;
	private String name;
	private Integer id;

	public ColumnRle() {
		this("");
	}

	public ColumnRle(String name) {
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
		int size = arrayList.size();
		if (size > 0 && arrayList.get(size - 1).getFirst().equals(item)) {
			arrayList.get(size - 1).setSecond(arrayList.get(size - 1).getSecond() + 1);
		} else {
			arrayList.add(new Tuple3<E, Integer, Integer>(item, 1, id));
		}
		id++;
	}

	public Tuple2<E, Integer> get(int i) {
		int key = find(i, 0, arrayList.size() - 1);
		if (key == -1) {
			return null;
		}
		return new Tuple2<E, Integer>(arrayList.get(key).getFirst(),
				arrayList.get(key).getThird() + arrayList.get(key).getSecond() - i);
	}

	protected int find(int i, int left, int right) {
		if (left > right) {
			return -1;
		}
		int mid = (left + right) / 2;
		if (arrayList.get(mid).getThird() > i) {
			return find(i, left, mid - 1);
		} else if (arrayList.get(mid).getSecond() + arrayList.get(mid).getThird() <= i) {
			return find(i, mid + 1, right);
		} else {
			return mid;
		}
	}

	public String toString() {
		return arrayList.toString();
	}

	private void add(Tuple3<E, Integer, Integer> tuple) {
		arrayList.add(tuple);
		id = tuple.getThird() + tuple.getSecond();
	}

	public Column<E> filter(Predicate<E> predicate) {
		ColumnRle<E> newColumn = new ColumnRle<>();
		for (Tuple3<E, Integer, Integer> value : arrayList) { // for each value of column
			if (predicate.test(value.getFirst())) { // if value matches predicate
				newColumn.add(value); // insert it into new column
			}
		}
		return newColumn;
	}

	public Column<E> filterLessThan(E item) {
		ColumnRle<E> newColumn = new ColumnRle<>();
		for (Tuple3<E, Integer, Integer> value : arrayList) { // for each value of column
			if (value.getFirst().compareTo(item) < 0) { // if value matches predicate
				newColumn.add(value); // insert it into new column
			}
		}
		return newColumn;
	}

	public BitSet select(Predicate<E> predicate) {
		BitSet bitSet = new BitSet();
		for (Tuple3<E, Integer, Integer> tuple : arrayList) {
			if (predicate.test(tuple.getFirst())) {
				bitSet.set(tuple.getThird(), tuple.getThird() + tuple.getSecond());
			}
		}
		return bitSet;
	}

	@Override
	public BitSet selectEquals(E item) {
		BitSet bitSet = new BitSet();
		int n = arrayList.size();
		Tuple3<E, Integer, Integer> tuple;
		for (int i = 0; i < n; i++) {
			tuple = arrayList.get(i);
			if (tuple.getFirst().equals(item)) {
				bitSet.set(tuple.getThird(), tuple.getThird() + tuple.getSecond());
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
		int n = arrayList.size();
		Tuple3<E, Integer, Integer> tuple;
		for (int i = 0; i < n; i++) {
			tuple = arrayList.get(i);
			if (tuple.getFirst().compareTo(item) < 0) {
				bitSet.set(tuple.getThird(), tuple.getThird() + tuple.getSecond());
			}
		}
		return bitSet;
	}

	@Override
	public BitSet selectLessThanOrEquals(E item) {
		BitSet bitSet = new BitSet();
		int n = arrayList.size();
		Tuple3<E, Integer, Integer> tuple;
		for (int i = 0; i < n; i++) {
			tuple = arrayList.get(i);
			if (tuple.getFirst().compareTo(item) <= 0) {
				bitSet.set(tuple.getThird(), tuple.getThird() + tuple.getSecond());
			}
		}
		return bitSet;
	}

	@Override
	public BitSet selectMoreThan(E item) {
		BitSet bitSet = new BitSet();
		int n = arrayList.size();
		Tuple3<E, Integer, Integer> tuple;
		for (int i = 0; i < n; i++) {
			tuple = arrayList.get(i);
			if (tuple.getFirst().compareTo(item) > 0) {
				bitSet.set(tuple.getThird(), tuple.getThird() + tuple.getSecond());
			}
		}
		return bitSet;
	}

	@Override
	public BitSet selectMoreThanOrEquals(E item) {
		BitSet bitSet = new BitSet();
		int n = arrayList.size();
		Tuple3<E, Integer, Integer> tuple;
		for (int i = 0; i < n; i++) {
			tuple = arrayList.get(i);
			if (tuple.getFirst().compareTo(item) >= 0) {
				bitSet.set(tuple.getThird(), tuple.getThird() + tuple.getSecond());
			}
		}
		return bitSet;
	}

	@Override
	public BitSet selectBetween(E from, E to) {
		BitSet bitSet = new BitSet();
		int n = arrayList.size();
		Tuple3<E, Integer, Integer> tuple;
		for (int i = 0; i < n; i++) {
			tuple = arrayList.get(i);
			if (tuple.getFirst().compareTo(from) >= 0 && tuple.getFirst().compareTo(to) <= 0) {
				bitSet.set(tuple.getThird(), tuple.getThird() + tuple.getSecond());
			}
		}
		return bitSet;
	}

	@Override
	public Column<E> filter(BitSet bitSet) {
		List<E> filteredList = new ArrayList<>();
		for (int i = 0; i < id; i++) {
			if (bitSet.get(i)) {
				filteredList.add(get(i).getFirst());
			}
		}
		return null;
	}

	@Override
	public Double sum() {
		return sum(0, id);
	}

	@Override
	public Double sum(int start, int end) {
		Double sum = 0.0;
		int i = start;
		Tuple3<E, Integer, Integer> val;
		int index = find(start, 0, arrayList.size() - 1);
		while (i < end) {
			val = arrayList.get(index);
			sum += ((Number) val.getFirst()).doubleValue() * (i + val.getSecond() <= end ? val.getSecond() : end - i);
			i += val.getSecond();
			index++;
		}
		return sum;
	}

	@Override
	public Double sum(BitSet bitSet) {
		Double sum = 0.0;
		int n = arrayList.size();
		Tuple3<E, Integer, Integer> tuple;
		for (int i = 0; i < n; i++) {
			tuple = arrayList.get(i);
			sum += ((Number) tuple.getFirst()).doubleValue()
					* bitSet.get(tuple.getThird(), tuple.getThird() + tuple.getSecond()).cardinality();
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
		int n = arrayList.size();
		Tuple3<E, Integer, Integer> tuple;
		for (int i = 0; i < n; i++) {
			tuple = arrayList.get(i);
			sum += (Integer) tuple.getFirst()
					* bitSet.get(tuple.getThird(), tuple.getThird() + tuple.getSecond()).cardinality();
		}
		return sum / (double) bitSet.cardinality();
	}

	@Override
	public int length() {
		if (arrayList.isEmpty()) {
			return 0;
		}
		int size = arrayList.size();
		return arrayList.get(size - 1).getThird() + arrayList.get(size - 1).getSecond();
	}

	@Override
	public int cardinality() {
		HashMap<E, Boolean> distinctMap = new HashMap<>();
		for (int i = 0; i < id; i++) {
			distinctMap.put(arrayList.get(i).getFirst(), true);
		}
		return distinctMap.size();
	}

	@Override
	public ColumnType type() {
		return ColumnType.RLE;
	}

	public Class<? extends ArrayList<?>> storageType() {
		return (Class<? extends ArrayList<?>>) arrayList.getClass();
	}

	public Column<E> filter2(BitSet bitSet) {
		ColumnRle<E> newColumn = new ColumnRle<>();
		int n = arrayList.size();
		Tuple3<E, Integer, Integer> tuple;
		for (int i = 0; i < n; i++) {
			tuple = arrayList.get(i);
			if (bitSet.get(tuple.getThird(), tuple.getThird() + tuple.getSecond()).cardinality() > 0) {
				newColumn.add(tuple);
			}
		}
		return newColumn;
	}

	@Override
	public Iterator<Tuple2<E, Integer>> iterator() {
		return new ColumnRleIterator();
	}

	private class ColumnRleIterator implements Iterator<Tuple2<E, Integer>> {

		private int i;
		private Tuple3<E, Integer, Integer> value;

		public ColumnRleIterator() {
			i = 0;
		}

		@Override
		public boolean hasNext() {
			return i < arrayList.size();
		}

		@Override
		public Tuple2<E, Integer> next() {
			value = arrayList.get(i);
			i++;
			return new Tuple2<E, Integer>(value.getFirst(), value.getSecond());
		}

	}

}