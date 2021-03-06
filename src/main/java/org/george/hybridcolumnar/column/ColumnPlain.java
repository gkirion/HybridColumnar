package org.george.hybridcolumnar.column;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Predicate;

import org.george.hybridcolumnar.domain.BitSetExtended;
import org.george.hybridcolumnar.domain.Tuple2;

@SuppressWarnings({ "serial", "rawtypes" })
public class ColumnPlain<E extends Comparable> implements Column<E>, Serializable {

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
	
	public ColumnPlain(int size) {
		arrayList = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			arrayList.add(null);
		}
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
		arrayList.add(item);
		id++;
	}
	
	public void add(E item, int position) {
		arrayList.set(position, item);
		id++;
	}

	@Override
	public Tuple2<E, Integer> get(int i) {
		return new Tuple2<E, Integer>(arrayList.get(i), 1);
	}

	public ArrayList<E> entrySet() {
		return arrayList;
	}

	@Override
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

	@Override
	public BitSetExtended select(Predicate<E> predicate) {
		BitSetExtended bitSet = new BitSetExtended();
		for (int i = 0; i < id; i++) {
			if (predicate.test(arrayList.get(i))) {
				bitSet.set(i);
			}
		}
		return bitSet;
	}

	@Override
	public BitSetExtended selectEquals(E item) {
		BitSetExtended bitSet = new BitSetExtended();
		for (int i = 0; i < id; i++) {
			if (arrayList.get(i).equals(item)) {
				bitSet.set(i);
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
		for (int i = 0; i < id; i++) {
			if (arrayList.get(i).compareTo(item) < 0) {
				bitSet.set(i);
			}
		}
		return bitSet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BitSetExtended selectLessThanOrEquals(E item) {
		BitSetExtended bitSet = new BitSetExtended();
		for (int i = 0; i < id; i++) {
			if (arrayList.get(i).compareTo(item) <= 0) {
				bitSet.set(i);
			}
		}
		return bitSet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BitSetExtended selectMoreThan(E item) {
		BitSetExtended bitSet = new BitSetExtended();
		for (int i = 0; i < id; i++) {
			if (arrayList.get(i).compareTo(item) > 0) {
				bitSet.set(i);
			}
		}
		return bitSet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BitSetExtended selectMoreThanOrEquals(E item) {
		BitSetExtended bitSet = new BitSetExtended();
		for (int i = 0; i < id; i++) {
			if (arrayList.get(i).compareTo(item) >= 0) {
				bitSet.set(i);
			}
		}
		return bitSet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BitSetExtended selectBetween(E from, E to) {
		BitSetExtended bitSet = new BitSetExtended();
		for (int i = 0; i < id; i++) {
			if (arrayList.get(i).compareTo(from) >= 0 && arrayList.get(i).compareTo(to) <= 0) {
				bitSet.set(i);
			}
		}
		return bitSet;
	}

	@Override
	public Column<E> filter(BitSetExtended bitSet) {
		Column<E> column = new ColumnPlain<>();
		for (int i = 0; i < id; i++) {
			if (bitSet.get(i)) {
				column.add(arrayList.get(i));
			}
		}
		return column;
	}

	public Integer count() {
		return arrayList.size();
	}

	@Override
	public Double sum() {
		Double sum = 0.0;
		for (E value : arrayList) {
			sum += ((Number) value).doubleValue();
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
	public Double sum(BitSetExtended bitSet) {
		Double sum = 0.0;
		for (int i = 0; i < id; i++) {
			if (bitSet.get(i)) {
				sum += ((Number) arrayList.get(i)).doubleValue();
			}
		}
		return sum;
	}

	@Override
	public Double sum(int start, int end, BitSetExtended bitSet) {
		Double sum = 0.0;
		for (int i = start; i < end; i++) {
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
	public Double avg(BitSetExtended bitSet) {
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
	public int cardinality() {
		HashMap<E, Boolean> distinctMap = new HashMap<>();
		for (E value : arrayList) {
			distinctMap.put(value, true);
		}
		return distinctMap.size();
	}

	@Override
	public ColumnType type() {
		return ColumnType.PLAIN;
	}

	@Override
	public long sizeEstimation() {
		long size = 0;
		for (E item : arrayList) {
			if (item instanceof String) {
				size += (24 + ((String)item).length() * 2);
			}
			else {
				size += 16;
			}
		}
		return size;
	}

	@Override
	public Iterator<Tuple2<E, Integer>> iterator() {
		return new ColumnPlainIterator();
	}

	private class ColumnPlainIterator implements Iterator<Tuple2<E, Integer>> {

		private int i;
		private E value;

		public ColumnPlainIterator() {
			i = 0;
		}

		@Override
		public boolean hasNext() {
			return i < id;
		}

		@Override
		public Tuple2<E, Integer> next() {
			value = arrayList.get(i);
			i++;
			return new Tuple2<E, Integer>(value, 1);
		}

	}

	@Override
	public Column<E> convertToPlain() {
		// TODO Auto-generated method stub
		return null;
	}

}
