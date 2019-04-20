package org.george.hybridcolumnar.column;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Predicate;

import org.george.hybridcolumnar.domain.BitSetExtended;
import org.george.hybridcolumnar.domain.RleIntTriple;
import org.george.hybridcolumnar.domain.RleTriple;
import org.george.hybridcolumnar.domain.Tuple2;
import org.george.hybridcolumnar.util.Dictionary;

@SuppressWarnings({ "serial", "rawtypes" })
public class ColumnDictionaryRle<E extends Comparable> implements Column<E>, Serializable {

	private ArrayList<RleTriple<Integer>> arrayList;
	private Dictionary<E> dictionary;
	private String name;
	private Integer id;

	public ColumnDictionaryRle() {
		this("");
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
		int size = arrayList.size();
		if (size > 0 && dictionary.get(arrayList.get(size - 1).getDatum()).equals(item)) {
			arrayList.get(size - 1).setRunLength(arrayList.get(size - 1).getRunLength() + 1);
		} else {
			arrayList.add(new RleIntTriple(dictionary.insert(item), 1, id));
		}
		id++;
	}

	@Override
	public Tuple2<E, Integer> get(int i) {
		int key = find(i, 0, arrayList.size() - 1);
		if (key == -1) {
			return null;
		}
		return new Tuple2<E, Integer>(dictionary.get(arrayList.get(key).getDatum()),
				arrayList.get(key).getIndex() + arrayList.get(key).getRunLength() - i);
	}

	protected int find(int i, int left, int right) {
		if (left > right) {
			return -1;
		}
		int mid = (left + right) / 2;
		if (arrayList.get(mid).getIndex() > i) {
			return find(i, left, mid - 1);
		} else if (arrayList.get(mid).getRunLength() + arrayList.get(mid).getIndex() <= i) {
			return find(i, mid + 1, right);
		} else {
			return mid;
		}
	}

	@Override
	public String toString() {
		return arrayList.toString();
	}

	private void add(RleTriple<Integer> tuple) {
		arrayList.add(tuple);
	}

	public Column<E> filter(Predicate<E> predicate) {
		ColumnDictionaryRle<E> newColumn = new ColumnDictionaryRle<>(dictionary);
		for (RleTriple<Integer> value : arrayList) { // for each value of column
			if (predicate.test(dictionary.get(value.getDatum()))) { // if value matches predicate
				newColumn.add(value); // insert it into new column
			}
		}
		return newColumn;
	}

	@Override
	public BitSetExtended select(Predicate<E> predicate) {
		BitSetExtended bitSet = new BitSetExtended();
		for (RleTriple<Integer> tuple : arrayList) {
			if (predicate.test(dictionary.get(tuple.getDatum()))) {
				bitSet.set(tuple.getIndex(), tuple.getIndex() + tuple.getRunLength());
			}
		}
		return bitSet;
	}

	@Override
	public BitSetExtended selectEquals(E item) {
		BitSetExtended bitSet = new BitSetExtended();
		int n = arrayList.size();
		RleTriple<Integer> tuple;
		for (int i = 0; i < n; i++) {
			tuple = arrayList.get(i);
			if (dictionary.get(tuple.getDatum()).equals(item)) {
				bitSet.set(tuple.getIndex(), tuple.getIndex() + tuple.getRunLength());
			}
		}
		return bitSet;
	}

	@Override
	public BitSetExtended selectNotEquals(E item) {
		BitSetExtended bitSet = selectEquals(item);
		BitSetExtended bSet = new BitSetExtended();
		bSet.set(0, id); // set all to 1
		bSet.andNot(bitSet);
		return bSet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BitSetExtended selectLessThan(E item) {
		BitSetExtended bitSet = new BitSetExtended();
		int n = arrayList.size();
		RleTriple<Integer> tuple;
		for (int i = 0; i < n; i++) {
			tuple = arrayList.get(i);
			if (dictionary.get(tuple.getDatum()).compareTo(item) < 0) {
				bitSet.set(tuple.getIndex(), tuple.getIndex() + tuple.getRunLength());
			}
		}
		return bitSet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BitSetExtended selectLessThanOrEquals(E item) {
		BitSetExtended bitSet = new BitSetExtended();
		int n = arrayList.size();
		RleTriple<Integer> tuple;
		for (int i = 0; i < n; i++) {
			tuple = arrayList.get(i);
			if (dictionary.get(tuple.getDatum()).compareTo(item) <= 0) {
				bitSet.set(tuple.getIndex(), tuple.getIndex() + tuple.getRunLength());
			}
		}
		return bitSet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BitSetExtended selectMoreThan(E item) {
		BitSetExtended bitSet = new BitSetExtended();
		int n = arrayList.size();
		RleTriple<Integer> tuple;
		for (int i = 0; i < n; i++) {
			tuple = arrayList.get(i);
			if (dictionary.get(tuple.getDatum()).compareTo(item) > 0) {
				bitSet.set(tuple.getIndex(), tuple.getIndex() + tuple.getRunLength());
			}
		}
		return bitSet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BitSetExtended selectMoreThanOrEquals(E item) {
		BitSetExtended bitSet = new BitSetExtended();
		int n = arrayList.size();
		RleTriple<Integer> tuple;
		for (int i = 0; i < n; i++) {
			tuple = arrayList.get(i);
			if (dictionary.get(tuple.getDatum()).compareTo(item) >= 0) {
				bitSet.set(tuple.getIndex(), tuple.getIndex() + tuple.getRunLength());
			}
		}
		return bitSet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BitSetExtended selectBetween(E from, E to) {
		BitSetExtended bitSet = new BitSetExtended();
		int n = arrayList.size();
		RleTriple<Integer> tuple;
		for (int i = 0; i < n; i++) {
			tuple = arrayList.get(i);
			if (dictionary.get(tuple.getDatum()).compareTo(from) >= 0
					&& dictionary.get(tuple.getDatum()).compareTo(to) <= 0) {
				bitSet.set(tuple.getIndex(), tuple.getIndex() + tuple.getRunLength());
			}
		}
		return bitSet;
	}

	@Override
	public Double sum() {
		return sum(0, id);
	}

	@Override
	public Double sum(int start, int end) {
		Double sum = 0.0;
		int i = start;
		RleTriple<Integer> val;
		int index = find(start, 0, arrayList.size() - 1);
		while (i < end) {
			val = arrayList.get(index);
			sum += ((Number) dictionary.get(val.getDatum())).doubleValue()
					* (i + val.getRunLength() <= end ? val.getRunLength() : end - i);
			i += val.getRunLength();
			index++;
		}
		return sum;
	}

	@Override
	public Double sum(BitSetExtended bitSet) {
		Double sum = 0.0;
		int n = arrayList.size();
		RleTriple<Integer> tuple;
		for (int i = 0; i < n; i++) {
			tuple = arrayList.get(i);
			sum += ((Number) dictionary.get(tuple.getDatum())).doubleValue()
					* bitSet.get(tuple.getIndex(), tuple.getIndex() + tuple.getRunLength()).cardinality();
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
	public Double avg(BitSetExtended bitSet) {
		Long sum = new Long(0);
		int n = arrayList.size();
		RleTriple<Integer> tuple;
		for (int i = 0; i < n; i++) {
			tuple = arrayList.get(i);
			sum += (Integer) dictionary.get(tuple.getDatum())
					* bitSet.get(tuple.getIndex(), tuple.getIndex() + tuple.getRunLength()).cardinality();
		}
		return sum / (double) bitSet.cardinality();
	}

	@Override
	public int length() {
		if (arrayList.isEmpty()) {
			return 0;
		}
		int size = arrayList.size();
		return arrayList.get(size - 1).getIndex() + arrayList.get(size - 1).getRunLength();
	}

	@Override
	public int cardinality() {
		HashMap<E, Boolean> distinctMap = new HashMap<>();
		for (int i = 0; i < id; i++) {
			distinctMap.put(dictionary.get(arrayList.get(i).getDatum()), true);
		}
		return distinctMap.size();
	}

	@Override
	public ColumnType type() {
		return ColumnType.RLE_DICTIONARY;
	}

	@Override
	public long sizeEstimation() {
		return dictionary.sizeEstimation() + arrayList.size() * 24;
	}
	
	@Override
	public Column<E> filter(BitSetExtended bitSet) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double sum(int start, int end, BitSetExtended bitSet) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Column<E> convertToPlain() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Tuple2<E, Integer>> iterator() {
		return new ColumnRleIterator();
	}
	
	private class ColumnRleIterator implements Iterator<Tuple2<E, Integer>> {
		
		private int i;
		private RleTriple<Integer> value;
		
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
			return new Tuple2<E, Integer>(dictionary.get(value.getDatum()), value.getRunLength());
		}
		
	}



}
