package org.george.chunk;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.function.Predicate;

public class ColumnDictionaryRle<E extends Comparable<E>> implements Column<E>, Serializable {

	private ArrayList<Tuple3<Integer, Integer, Integer>> arrayList;
	private Dictionary<E> dictionary;
	private String name;
	private Integer id;

	public ColumnDictionaryRle() {
		arrayList = new ArrayList<>();
		dictionary = new Dictionary<>();
		name = "";
		id = 0;
	}

	public ColumnDictionaryRle(String name) {
		arrayList = new ArrayList<>();
		dictionary = new Dictionary<>();
		this.name = name;
		id = 0;
	}

	public ColumnDictionaryRle(Dictionary<E> dictionary) {
		arrayList = new ArrayList<>();
		this.dictionary = dictionary;
		name = "";
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
		if (size > 0 && dictionary.get(arrayList.get(size - 1).getFirst()).equals(item)) {
			arrayList.get(size - 1).setSecond(arrayList.get(size - 1).getSecond() + 1);
		} else {
			arrayList.add(new Tuple3<Integer, Integer, Integer>(dictionary.insert(item), 1, id));
		}
		id++;
	}

	public Tuple2<E, Integer> get(int i) {
		int key = find(i, 0, arrayList.size() - 1);
		if (key == -1) {
			return null;
		}
		return new Tuple2<E, Integer>(dictionary.get(arrayList.get(key).getFirst()),
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

	private void add(Tuple3<Integer, Integer, Integer> tuple) {
		arrayList.add(tuple);
	}

	public Column<E> filter(Predicate<E> predicate) {
		ColumnDictionaryRle<E> newColumn = new ColumnDictionaryRle<>(dictionary);
		for (Tuple3<Integer, Integer, Integer> value : arrayList) { // for each value of column
			if (predicate.test(dictionary.get(value.getFirst()))) { // if value matches predicate
				newColumn.add(value); // insert it into new column
			}
		}
		return newColumn;
	}

	public BitSet select(Predicate<E> predicate) {
		BitSet bitSet = new BitSet();
		for (Tuple3<Integer, Integer, Integer> tuple : arrayList) {
			if (predicate.test(dictionary.get(tuple.getFirst()))) {
				bitSet.set(tuple.getThird(), tuple.getThird() + tuple.getSecond());
			}
		}
		return bitSet;
	}

	@Override
	public BitSet selectEquals(E item) {
		BitSet bitSet = new BitSet();
		int n = arrayList.size();
		Tuple3<Integer, Integer, Integer> tuple;
		for (int i = 0; i < n; i++) {
			tuple = arrayList.get(i);
			if (dictionary.get(tuple.getFirst()).equals(item)) {
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
		Tuple3<Integer, Integer, Integer> tuple;
		for (int i = 0; i < n; i++) {
			tuple = arrayList.get(i);
			if (dictionary.get(tuple.getFirst()).compareTo(item) < 0) {
				bitSet.set(tuple.getThird(), tuple.getThird() + tuple.getSecond());
			}
		}
		return bitSet;
	}

	@Override
	public BitSet selectLessThanOrEquals(E item) {
		BitSet bitSet = new BitSet();
		int n = arrayList.size();
		Tuple3<Integer, Integer, Integer> tuple;
		for (int i = 0; i < n; i++) {
			tuple = arrayList.get(i);
			if (dictionary.get(tuple.getFirst()).compareTo(item) <= 0) {
				bitSet.set(tuple.getThird(), tuple.getThird() + tuple.getSecond());
			}
		}
		return bitSet;
	}

	@Override
	public BitSet selectMoreThan(E item) {
		BitSet bitSet = new BitSet();
		int n = arrayList.size();
		Tuple3<Integer, Integer, Integer> tuple;
		for (int i = 0; i < n; i++) {
			tuple = arrayList.get(i);
			if (dictionary.get(tuple.getFirst()).compareTo(item) > 0) {
				bitSet.set(tuple.getThird(), tuple.getThird() + tuple.getSecond());
			}
		}
		return bitSet;
	}

	@Override
	public BitSet selectMoreThanOrEquals(E item) {
		BitSet bitSet = new BitSet();
		int n = arrayList.size();
		Tuple3<Integer, Integer, Integer> tuple;
		for (int i = 0; i < n; i++) {
			tuple = arrayList.get(i);
			if (dictionary.get(tuple.getFirst()).compareTo(item) >= 0) {
				bitSet.set(tuple.getThird(), tuple.getThird() + tuple.getSecond());
			}
		}
		return bitSet;
	}

	@Override
	public BitSet selectBetween(E from, E to) {
		BitSet bitSet = new BitSet();
		int n = arrayList.size();
		Tuple3<Integer, Integer, Integer> tuple;
		for (int i = 0; i < n; i++) {
			tuple = arrayList.get(i);
			if (dictionary.get(tuple.getFirst()).compareTo(from) >= 0
					&& dictionary.get(tuple.getFirst()).compareTo(to) <= 0) {
				bitSet.set(tuple.getThird(), tuple.getThird() + tuple.getSecond());
			}
		}
		return bitSet;
	}

	@Override
	public Long sum() {
		return sum(0, id);
	}

	@Override
	public Long sum(int start, int end) {
		Long sum = new Long(0);
		int i = start;
		Tuple3<Integer, Integer, Integer> val;
		int index = find(start, 0, arrayList.size() - 1);
		while (i < end) {
			val = arrayList.get(index);
			sum += (Integer) dictionary.get(val.getFirst()) * (i + val.getSecond() <= end ? val.getSecond() : end - i);
			i += val.getSecond();
			index++;
		}
		return sum;
	}

	@Override
	public Long sum(BitSet bitSet) {
		Long sum = new Long(0);
		int n = arrayList.size();
		Tuple3<Integer, Integer, Integer> tuple;
		for (int i = 0; i < n; i++) {
			tuple = arrayList.get(i);
			sum += (Integer) dictionary.get(tuple.getFirst())
					* bitSet.get(tuple.getThird(), tuple.getThird() + tuple.getSecond()).cardinality();
		}
		return sum;
	}

	@Override
	public Integer count(int start, int end) {
		return (end < id ? end : id) - start;
	}

	@Override
	public Double avg(int start, int end) {
		return sum(start, end) / (double) count(start, end);
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
	public int getCardinality() {
		HashMap<E, Boolean> distinctMap = new HashMap<>();
		for (int i = 0; i < id; i++) {
			distinctMap.put(dictionary.get(arrayList.get(i).getFirst()), true);
		}
		return distinctMap.size();
	}

}
