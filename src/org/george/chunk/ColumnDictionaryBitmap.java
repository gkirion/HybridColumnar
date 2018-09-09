package org.george.chunk;

import java.io.Serializable;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;

public class ColumnDictionaryBitmap<E extends Comparable<E>> implements Column<E>, Serializable {

	private HashMap<Integer, BitSet> mappings;
	private Dictionary<E> dictionary;
	private String name;
	private Integer id;

	public ColumnDictionaryBitmap() {
		this("");
	}

	public ColumnDictionaryBitmap(String name) {
		mappings = new HashMap<>();
		dictionary = new Dictionary<>();
		this.name = name;
		id = 0;
	}

	public ColumnDictionaryBitmap(Dictionary<E> dictionary) {
		mappings = new HashMap<>();
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
		if (!mappings.containsKey(dictionary.insert(item))) {
			mappings.put(dictionary.insert(item), new BitSet());
		}
		mappings.get(dictionary.insert(item)).set(id);
		id++;
	}

	public Tuple2<E, Integer> get(int i) {
		for (Integer item : mappings.keySet()) {
			if (mappings.get(item).get(i)) {
				return new Tuple2<E, Integer>(dictionary.get(item), 1);
			}
		}
		return null;
	}

	private void add(Integer key, BitSet bitSet) {
		mappings.put(key, bitSet);
	}

	public Column<E> filter(Predicate<E> predicate) {
		ColumnDictionaryBitmap<E> newColumn = new ColumnDictionaryBitmap<>(dictionary);
		for (Integer value : mappings.keySet()) {
			if (predicate.test(dictionary.get(value))) {
				newColumn.add(value, mappings.get(value));
			}
		}
		return newColumn;
	}

	public BitSet select(Predicate<E> predicate) {
		BitSet bSet = new BitSet();
		for (Integer value : mappings.keySet()) {
			if (predicate.test(dictionary.get(value))) {
				bSet.or(mappings.get(value));
			}
		}
		return bSet;
	}

	@Override
	public BitSet selectEquals(E item) {
		Set<Integer> values = mappings.keySet();
		for (Integer value : values) {
			if (dictionary.get(value).equals(item)) {
				return (BitSet) mappings.get(value).clone();
			}
		}
		return new BitSet();
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
		BitSet bSet = new BitSet();
		for (Integer value : mappings.keySet()) {
			if (dictionary.get(value).compareTo(item) < 0) {
				bSet.or(mappings.get(value));
			}
		}
		return bSet;
	}

	@Override
	public BitSet selectLessThanOrEquals(E item) {
		BitSet bSet = new BitSet();
		for (Integer value : mappings.keySet()) {
			if (dictionary.get(value).compareTo(item) <= 0) {
				bSet.or(mappings.get(value));
			}
		}
		return bSet;
	}

	@Override
	public BitSet selectMoreThan(E item) {
		BitSet bSet = new BitSet();
		for (Integer value : mappings.keySet()) {
			if (dictionary.get(value).compareTo(item) > 0) {
				bSet.or(mappings.get(value));
			}
		}
		return bSet;
	}

	@Override
	public BitSet selectMoreThanOrEquals(E item) {
		BitSet bSet = new BitSet();
		for (Integer value : mappings.keySet()) {
			if (dictionary.get(value).compareTo(item) >= 0) {
				bSet.or(mappings.get(value));
			}
		}
		return bSet;
	}

	@Override
	public BitSet selectBetween(E from, E to) {
		BitSet bSet = new BitSet();
		for (Integer value : mappings.keySet()) {
			if (dictionary.get(value).compareTo(from) >= 0 && dictionary.get(value).compareTo(to) <= 0) {
				bSet.or(mappings.get(value));
			}
		}
		return bSet;
	}

	@Override
	public Double sum() {
		return sum(0, id);
	}

	public Double sum(int start, int end) {
		Double sum = 0.0;
		if (end - start == 1) {
			sum += ((Number) get(start).getFirst()).doubleValue() * get(start).getSecond();
			return sum;
		}
		for (Integer item : mappings.keySet()) {
			sum += ((Number) dictionary.get(item)).doubleValue() * mappings.get(item).get(start, end).cardinality();
		}
		return sum;
	}

	@Override
	public Double sum(BitSet bitSet) {
		Double sum = 0.0;
		BitSet bSet = new BitSet();
		for (Integer value : mappings.keySet()) {
			bSet.or(mappings.get(value)); // load bitset of value
			bSet.and(bitSet);
			sum += ((Number) dictionary.get(value)).doubleValue() * bSet.cardinality();
			bSet.clear();
		}
		return sum;
	}

	public Integer count(int start, int end) {
		return (end < id ? end : id) - start;
	}

	@Override
	public Double avg() {
		return avg(0, id);
	}

	public Double avg(int start, int end) {
		return sum(start, end) / (double) count(start, end);
	}

	@Override
	public Double avg(BitSet bitSet) {
		Long sum = new Long(0);
		BitSet bSet = new BitSet();
		for (Integer value : mappings.keySet()) {
			bSet.or(mappings.get(value)); // load bitset of value
			bSet.and(bitSet);
			sum += (Integer) dictionary.get(value) * bSet.cardinality();
			bSet.clear();
		}
		return sum / (double) bitSet.cardinality();
	}

	@Override
	public int length() {
		return id;
	}

	@Override
	public int getCardinality() {
		return mappings.size();
	}

	@Override
	public Iterator<Tuple2<E, Integer>> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

}
