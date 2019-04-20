package org.george.hybridcolumnar.column;

import java.io.Serializable;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;

import org.george.hybridcolumnar.domain.BitSetExtended;
import org.george.hybridcolumnar.domain.Tuple2;

@SuppressWarnings({ "rawtypes", "serial" })
public class ColumnBitmap<E extends Comparable> implements Column<E>, Serializable {

	private HashMap<E, BitSet> mappings;
	private String name;
	private Integer id;

	public ColumnBitmap() {
		this("");
	}

	public ColumnBitmap(String name) {
		mappings = new HashMap<>();
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
		if (!mappings.containsKey(item)) {
			mappings.put(item, new BitSet());
		}
		mappings.get(item).set(id);
		id++;
	}

	@Override
	public Tuple2<E, Integer> get(int i) {
		for (E item : mappings.keySet()) {
			if (mappings.get(item).get(i)) {
				return new Tuple2<E, Integer>(item, 1);
			}
		}
		return null;
	}

	private void add(E key, BitSet bitSet) {
		mappings.put(key, bitSet);
	}

	public Column<E> filter(Predicate<E> predicate) {
		ColumnBitmap<E> newColumn = new ColumnBitmap<>();
		for (E value : mappings.keySet()) {
			if (predicate.test(value)) {
				newColumn.add(value, mappings.get(value));
			}
		}
		return newColumn;
	}

	@Override
	public BitSetExtended select(Predicate<E> predicate) {
		BitSetExtended bSet = new BitSetExtended();
		for (E value : mappings.keySet()) {
			if (predicate.test(value)) {
				bSet.or(mappings.get(value));
			}
		}
		return bSet;
	}

	@Override
	public BitSetExtended selectEquals(E item) {
		Set<E> values = mappings.keySet();
		for (E value : values) {
			if (value.equals(item)) {
				return (BitSetExtended) mappings.get(value).clone();
			}
		}
		return new BitSetExtended();
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
		BitSetExtended bSet = new BitSetExtended();
		for (E value : mappings.keySet()) {
			if (value.compareTo(item) < 0) {
				bSet.or(mappings.get(value));
			}
		}
		return bSet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BitSetExtended selectLessThanOrEquals(E item) {
		BitSetExtended bSet = new BitSetExtended();
		for (E value : mappings.keySet()) {
			if (value.compareTo(item) <= 0) {
				bSet.or(mappings.get(value));
			}
		}
		return bSet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BitSetExtended selectMoreThan(E item) {
		BitSetExtended bSet = new BitSetExtended();
		for (E value : mappings.keySet()) {
			if (value.compareTo(item) > 0) {
				bSet.or(mappings.get(value));
			}
		}
		return bSet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BitSetExtended selectMoreThanOrEquals(E item) {
		BitSetExtended bSet = new BitSetExtended();
		for (E value : mappings.keySet()) {
			if (value.compareTo(item) >= 0) {
				bSet.or(mappings.get(value));
			}
		}
		return bSet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BitSetExtended selectBetween(E from, E to) {
		BitSetExtended bSet = new BitSetExtended();
		for (E value : mappings.keySet()) {
			if (value.compareTo(from) >= 0 && value.compareTo(to) <= 0) {
				bSet.or(mappings.get(value));
			}
		}
		return bSet;
	}

	@Override
	public Double sum() {
		return sum(0, id);
	}

	@Override
	public Double sum(int start, int end) {
		Double sum = 0.0;
		if (end - start == 1) {
			sum += ((Number) get(start).getFirst()).doubleValue() * get(start).getSecond();
			return sum;
		}
		for (E item : mappings.keySet()) {
			sum += ((Number) item).doubleValue() * mappings.get(item).get(start, end).cardinality();
		}
		return sum;
	}

	@Override
	public Double sum(BitSetExtended bitSet) {
		Double sum = 0.0;
		BitSetExtended bSet = new BitSetExtended();
		for (E value : mappings.keySet()) {
			bSet.or(mappings.get(value)); // load bitset of value
			bSet.and(bitSet);
			sum += ((Number) value).doubleValue() * bSet.cardinality();
			bSet.clear();
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
		BitSetExtended bSet = new BitSetExtended();
		for (E value : mappings.keySet()) {
			bSet.or(mappings.get(value)); // load bitset of value
			bSet.and(bitSet);
			sum += (Integer) value * bSet.cardinality();
			bSet.clear();
		}
		return sum / (double) bitSet.cardinality();
	}

	@Override
	public int length() {
		return id;
	}

	@Override
	public int cardinality() {
		return mappings.size();
	}

	@Override
	public ColumnType type() {
		return ColumnType.BITMAP;
	}

	@Override
	public long sizeEstimation() {
		long size = 0;
		for (E key : mappings.keySet()) {
			BitSet bitSet = mappings.get(key);
			size += (32 + bitSet.cardinality() / 8);
		}
		return size;
	}

	@Override
	public Iterator<Tuple2<E, Integer>> iterator() {
		return new ColumnBitmapIterator();
	}

	private class ColumnBitmapIterator implements Iterator<Tuple2<E, Integer>> {

		private int i;
		private Tuple2<E, Integer> value;

		public ColumnBitmapIterator() {
			i = 0;
		}

		@Override
		public boolean hasNext() {
			return i < id;
		}

		@Override
		public Tuple2<E, Integer> next() {
			value = get(i);
			i++;
			return value;
		}

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
