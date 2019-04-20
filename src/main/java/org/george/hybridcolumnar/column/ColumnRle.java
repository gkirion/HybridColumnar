package org.george.hybridcolumnar.column;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import org.george.hybridcolumnar.domain.BitSetExtended;
import org.george.hybridcolumnar.domain.RleReferenceTriple;
import org.george.hybridcolumnar.domain.RleTriple;
import org.george.hybridcolumnar.domain.Tuple2;

@SuppressWarnings({ "rawtypes", "serial" })
public class ColumnRle<E extends Comparable> implements Column<E>, Serializable {

	private ArrayList<RleTriple<E>> arrayList;
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
		if (size > 0 && arrayList.get(size - 1).getDatum().equals(item)) {
			arrayList.get(size - 1).setRunLength(arrayList.get(size - 1).getRunLength() + 1);
		} else {
			arrayList.add(new RleReferenceTriple<E>(item, 1, id));
		}
		id++;
	}

	@Override
	public Tuple2<E, Integer> get(int i) {
		int key = find(i, 0, arrayList.size() - 1);
		if (key == -1) {
			return null;
		}
		return new Tuple2<E, Integer>(arrayList.get(key).getDatum(),
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

	private void add(RleTriple<E> tuple) {
		arrayList.add(tuple);
		id = tuple.getIndex() + tuple.getRunLength();
	}

	public Column<E> filter(Predicate<E> predicate) {
		ColumnRle<E> newColumn = new ColumnRle<>();
		for (RleTriple<E> value : arrayList) { // for each value of column
			if (predicate.test(value.getDatum())) { // if value matches predicate
				newColumn.add(value); // insert it into new column
			}
		}
		return newColumn;
	}

	@SuppressWarnings("unchecked")
	public Column<E> filterLessThan(E item) {
		ColumnRle<E> newColumn = new ColumnRle<>();
		for (RleTriple<E> value : arrayList) { // for each value of column
			if (value.getDatum().compareTo(item) < 0) { // if value matches predicate
				newColumn.add(value); // insert it into new column
			}
		}
		return newColumn;
	}

	@Override
	public BitSetExtended select(Predicate<E> predicate) {
		BitSetExtended bitSet = new BitSetExtended();
		for (RleTriple<E> tuple : arrayList) {
			if (predicate.test(tuple.getDatum())) {
				bitSet.set(tuple.getIndex(), tuple.getIndex() + tuple.getRunLength());
			}
		}
		return bitSet;
	}

	@Override
	public BitSetExtended selectEquals(E item) {
		BitSetExtended bitSet = new BitSetExtended();
		int n = arrayList.size();
		RleTriple<E> tuple;
		for (int i = 0; i < n; i++) {
			tuple = arrayList.get(i);
			if (tuple.getDatum().equals(item)) {
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
		RleTriple<E> tuple;
		for (int i = 0; i < n; i++) {
			tuple = arrayList.get(i);
			if (tuple.getDatum().compareTo(item) < 0) {
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
		RleTriple<E> tuple;
		for (int i = 0; i < n; i++) {
			tuple = arrayList.get(i);
			if (tuple.getDatum().compareTo(item) <= 0) {
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
		RleTriple<E> tuple;
		for (int i = 0; i < n; i++) {
			tuple = arrayList.get(i);
			if (tuple.getDatum().compareTo(item) > 0) {
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
		RleTriple<E> tuple;
		for (int i = 0; i < n; i++) {
			tuple = arrayList.get(i);
			if (tuple.getDatum().compareTo(item) >= 0) {
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
		RleTriple<E> tuple;
		for (int i = 0; i < n; i++) {
			tuple = arrayList.get(i);
			if (tuple.getDatum().compareTo(from) >= 0 && tuple.getDatum().compareTo(to) <= 0) {
				bitSet.set(tuple.getIndex(), tuple.getIndex() + tuple.getRunLength());
			}
		}
		return bitSet;
	}

	@Override
	public Column<E> filter(BitSetExtended bitSet) {
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
		RleTriple<E> val;
		int index = find(start, 0, arrayList.size() - 1);
		while (i < end) {
			val = arrayList.get(index);
			sum += ((Number) val.getDatum()).doubleValue() * (i + val.getRunLength() <= end ? val.getRunLength() : end - i);
			i += val.getRunLength();
			index++;
		}
		return sum;
	}

	@Override
	public Double sum(BitSetExtended bitSet) {
		Double sum = 0.0;
		int n = arrayList.size();
		RleTriple<E> tuple;
		for (int i = 0; i < n; i++) {
			tuple = arrayList.get(i);
			sum += ((Number) tuple.getDatum()).doubleValue()
					* bitSet.get(tuple.getIndex(), tuple.getIndex() + tuple.getRunLength()).cardinality();
		}
		return sum;
	}

	@Override
	public Double sum(int start, int end, BitSetExtended bitSet) {
		Double sum = 0.0;
		int startIndex = find(start, 0, arrayList.size() - 1);
		int endIndex = find(end, 0, arrayList.size() - 1);
		RleTriple<E> tuple;
		for (int i = startIndex; i <= endIndex; i++) {
			tuple = arrayList.get(i);
			sum += ((Number) tuple.getDatum()).doubleValue()
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
		RleTriple<E> tuple;
		for (int i = 0; i < n; i++) {
			tuple = arrayList.get(i);
			sum += (Integer) tuple.getDatum()
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
			distinctMap.put(arrayList.get(i).getDatum(), true);
		}
		return distinctMap.size();
	}

	@Override
	public ColumnType type() {
		return ColumnType.RLE;
	}

	@Override
	public long sizeEstimation() {
		long size = 0;
		for (RleTriple<E> item : arrayList) {
			if (item.getDatum() instanceof String) {
				size += (32 + ((String)item.getDatum()).length() * 2);
			}
			else {
				size += 24;
			}
		}
		return size;
	}


	public Column<E> filter2(BitSet bitSet) {
		ColumnRle<E> newColumn = new ColumnRle<>();
		int n = arrayList.size();
		RleTriple<E> tuple;
		for (int i = 0; i < n; i++) {
			tuple = arrayList.get(i);
			if (bitSet.get(tuple.getIndex(), tuple.getIndex() + tuple.getRunLength()).cardinality() > 0) {
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
		private RleTriple<E> value;

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
			return new Tuple2<E, Integer>(value.getDatum(), value.getRunLength());
		}

	}

	@Override
	public Column<E> convertToPlain() {
		// TODO Auto-generated method stub
		return null;
	}

}
