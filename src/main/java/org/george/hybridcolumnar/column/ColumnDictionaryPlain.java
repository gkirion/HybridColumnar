package org.george.hybridcolumnar.column;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Predicate;

import org.george.hybridcolumnar.domain.BitSetExtended;
import org.george.hybridcolumnar.domain.Tuple2;
import org.george.hybridcolumnar.util.Dictionary;

@SuppressWarnings({ "serial", "rawtypes" })
public class ColumnDictionaryPlain<E extends Comparable> implements Column<E>, Serializable {

	private ArrayList<Integer> arrayList;
	private Dictionary<E> dictionary;
	private String name;
	private Integer id;

	public ColumnDictionaryPlain() {
		this("");
	}

	public ColumnDictionaryPlain(String name) {
		arrayList = new ArrayList<>();
		dictionary = new Dictionary<>();
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
		Integer key = dictionary.insert(item);
		arrayList.add(key);
		id++;
	}

	@Override
	public Tuple2<E, Integer> get(int i) {
		return new Tuple2<E, Integer>(dictionary.get(arrayList.get(i)), 1);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[\n");
		for (Integer value : arrayList) {

		}
		sb.append("]");
		return arrayList.toString();
	}

	public Column<E> filter(Predicate<E> predicate) {
		ColumnDictionaryPlain<E> newColumn = new ColumnDictionaryPlain<>();
		for (Integer value : arrayList) {
			if (predicate.test(dictionary.get(value))) {
				newColumn.add(dictionary.get(value));
			}
		}
		return newColumn;
	}

	@Override
	public BitSetExtended select(Predicate<E> predicate) {
		BitSetExtended bitSet = new BitSetExtended();
		for (int i = 0; i < id; i++) {
			if (predicate.test(dictionary.get(arrayList.get(i)))) {
				bitSet.set(i);
			}
		}
		return bitSet;
	}

	@Override
	public BitSetExtended selectEquals(E item) {
		BitSetExtended bitSet = new BitSetExtended();
		for (int i = 0; i < id; i++) {
			if (dictionary.get(arrayList.get(i)).equals(item)) {
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
			if (dictionary.get(arrayList.get(i)).compareTo(item) < 0) {
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
			if (dictionary.get(arrayList.get(i)).compareTo(item) <= 0) {
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
			if (dictionary.get(arrayList.get(i)).compareTo(item) > 0) {
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
			if (dictionary.get(arrayList.get(i)).compareTo(item) >= 0) {
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
			if (dictionary.get(arrayList.get(i)).compareTo(from) >= 0
					&& dictionary.get(arrayList.get(i)).compareTo(to) <= 0) {
				bitSet.set(i);
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
		for (int i = start; i < end; i++) {
			sum += ((Number) dictionary.get(arrayList.get(i))).doubleValue();
		}
		return sum;
	}

	@Override
	public Double sum(BitSetExtended bitSet) {
		Double sum = 0.0;
		for (int i = 0; i < id; i++) {
			if (bitSet.get(i)) {
				sum += ((Number) dictionary.get(arrayList.get(i))).doubleValue();
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
				sum += (Integer) dictionary.get(arrayList.get(i));
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
		for (Integer value : arrayList) {
			distinctMap.put(dictionary.get(value), true);
		}
		return distinctMap.size();
	}

	@Override
	public ColumnType type() {
		return ColumnType.PLAIN_DICTIONARY;
	}

	@Override
	public long sizeEstimation() {
		return dictionary.sizeEstimation() +  arrayList.size() * 16;
	}

	@Override
	public Iterator<Tuple2<E, Integer>> iterator() {
		// TODO Auto-generated method stub
		return null;
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

}
