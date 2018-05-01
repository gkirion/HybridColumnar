package org.george.chunk;

import java.io.Serializable;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Set;
import java.util.function.Predicate;

public class ColumnBitmapRoaring<E extends Comparable<E>> implements Column<E>, Serializable {
	
	private HashMap<E, RoaringBitmap> mappings;
	private String name;
	private Integer id;
	
	public ColumnBitmapRoaring() {
		mappings = new HashMap<>();
		name = "";
		id = 0;
	}
	
	public ColumnBitmapRoaring(String name) {
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
			mappings.put(item, new RoaringBitmap());
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

	private void add(E key, RoaringBitmap roaringBitmap) {
		mappings.put(key, roaringBitmap);
	}

	public Column<E> filter(Predicate<E> predicate) {
		ColumnBitmapRoaring<E> newEColumn = new ColumnBitmapRoaring<>();
		for (E value : mappings.keySet()) {
			if (predicate.test(value)) {
				newEColumn.add(value, mappings.get(value));
			}
		}
		return newEColumn;
	}

	public BitSet select(Predicate<E> predicate) {
		RoaringBitmap bitmap = new RoaringBitmap();
		for (E value : mappings.keySet()) {
			if (predicate.test(value)) {
				bitmap.or(mappings.get(value));
			}
		}
		return  bitmap.convertToBitSet();
	}
	
	@Override
	public BitSet selectEquals(E item) {
		for (E value : mappings.keySet()) {
			if (value.equals(item)) {
				return mappings.get(value).convertToBitSet();
				//return mappings.get(value);
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
		RoaringBitmap bitmap = new RoaringBitmap();
		for (E value : mappings.keySet()) {
			if (value.compareTo(item) < 0) {
				bitmap.or(mappings.get(value));
			}
		}
		return bitmap.convertToBitSet();
	}

	@Override
	public BitSet selectLessThanOrEquals(E item) {
		RoaringBitmap bitmap = new RoaringBitmap();
		for (E value : mappings.keySet()) {
			if (value.compareTo(item) <= 0) {
				bitmap.or(mappings.get(value));
			}
		}
		return bitmap.convertToBitSet();
	}

	@Override
	public BitSet selectMoreThan(E item) {
		RoaringBitmap bitmap = new RoaringBitmap();
		for (E value : mappings.keySet()) {
			if (value.compareTo(item) > 0) {
				bitmap.or(mappings.get(value));
			}
		}
		return bitmap.convertToBitSet();
	}

	@Override
	public BitSet selectMoreThanOrEquals(E item) {
		RoaringBitmap bitmap = new RoaringBitmap();
		for (E value : mappings.keySet()) {
			if (value.compareTo(item) >= 0) {
				bitmap.or(mappings.get(value));
			}
		}
		return bitmap.convertToBitSet();
	}

	@Override
	public BitSet selectBetween(E from, E to) {
		RoaringBitmap bitmap = new RoaringBitmap();
		for (E value : mappings.keySet()) {
			if (value.compareTo(from) >= 0 && value.compareTo(to) <= 0) {
				bitmap.or(mappings.get(value));
			}
		}
		return bitmap.convertToBitSet();
	}

	@Override
	public Long sum(int start, int end) {
		Long sum = new Long(0);
		if (end - start == 1) {
			sum += (Integer)get(start).getFirst() * get(start).getSecond();
			return sum;
		}
		for (E item : mappings.keySet()) {
			sum += (Integer)item * mappings.get(item).get(start, end).cardinality();
		}
		return sum;
	}
	
	@Override
	public Long sum(BitSet bitSet) {
		Long sum = new Long(0);
		RoaringBitmap bitmap = new RoaringBitmap();
		Set<E> values = mappings.keySet();
		for (E value : values) {
			bitmap.or(mappings.get(value));
			bitmap.and(bitSet);
			sum += (Integer)value * bitmap.cardinality();
			bitmap.clear();
		}
		return sum;
	}

	@Override
	public Integer count(int start, int end) {
		return (end < id ? end : id) - start;
	}

	@Override
	public Double avg(int start, int end) {
		return sum(start, end) / (double)count(start, end);
	}

	@Override
	public int getLength() {
		return id;
	}

	@Override
	public int getCardinality() {
		return mappings.size();
	}

}
