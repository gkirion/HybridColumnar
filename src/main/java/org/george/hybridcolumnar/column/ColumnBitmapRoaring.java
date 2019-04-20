package org.george.hybridcolumnar.column;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;

import org.george.hybridcolumnar.domain.BitSetExtended;
import org.george.hybridcolumnar.domain.Tuple2;
import org.george.hybridcolumnar.roaring.Container;
import org.george.hybridcolumnar.roaring.RoaringBitmap;

@SuppressWarnings({ "serial", "rawtypes" })
public class ColumnBitmapRoaring<E extends Comparable> implements Column<E>, Serializable {

	private HashMap<E, RoaringBitmap> mappings;
	private int containerCapacity;
	private String name;
	private Integer id;

	public ColumnBitmapRoaring() {
		this("");
	}
	
	public ColumnBitmapRoaring(int containerCapacity) {
		this("", containerCapacity);
	}

	public ColumnBitmapRoaring(String name) {
		this(name, 65536);
	}
	
	public ColumnBitmapRoaring(String name, int containerCapacity) {
		mappings = new HashMap<>();
		this.name = name;
		this.containerCapacity = containerCapacity;
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
			mappings.put(item, new RoaringBitmap(containerCapacity));
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

	@Override
	public BitSetExtended select(Predicate<E> predicate) {
		RoaringBitmap bitmap = new RoaringBitmap();
		for (E value : mappings.keySet()) {
			if (predicate.test(value)) {
				bitmap.or(mappings.get(value));
			}
		}
		return bitmap.convertToBitSet();
	}

	@Override
	public BitSetExtended selectEquals(E item) {
		for (E value : mappings.keySet()) {
			if (value.equals(item)) {
				return mappings.get(value).convertToBitSet();
				// return mappings.get(value);
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
		RoaringBitmap bitmap = new RoaringBitmap();
		for (E value : mappings.keySet()) {
			if (value.compareTo(item) < 0) {
				bitmap.or(mappings.get(value));
			}
		}
		return bitmap.convertToBitSet();
	}

	@SuppressWarnings("unchecked")
	@Override
	public BitSetExtended selectLessThanOrEquals(E item) {
		RoaringBitmap bitmap = new RoaringBitmap();
		for (E value : mappings.keySet()) {
			if (value.compareTo(item) <= 0) {
				bitmap.or(mappings.get(value));
			}
		}
		return bitmap.convertToBitSet();
	}

	@SuppressWarnings("unchecked")
	@Override
	public BitSetExtended selectMoreThan(E item) {
		RoaringBitmap bitmap = new RoaringBitmap();
		for (E value : mappings.keySet()) {
			if (value.compareTo(item) > 0) {
				bitmap.or(mappings.get(value));
			}
		}
		return bitmap.convertToBitSet();
	}

	@SuppressWarnings("unchecked")
	@Override
	public BitSetExtended selectMoreThanOrEquals(E item) {
		RoaringBitmap bitmap = new RoaringBitmap();
		for (E value : mappings.keySet()) {
			if (value.compareTo(item) >= 0) {
				bitmap.or(mappings.get(value));
			}
		}
		return bitmap.convertToBitSet();
	}

	@SuppressWarnings("unchecked")
	@Override
	public BitSetExtended selectBetween(E from, E to) {
		RoaringBitmap bitmap = new RoaringBitmap();
		for (E value : mappings.keySet()) {
			if (value.compareTo(from) >= 0 && value.compareTo(to) <= 0) {
				bitmap.or(mappings.get(value));
			}
		}
		return bitmap.convertToBitSet();
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
		RoaringBitmap bitmap = new RoaringBitmap();
		Set<E> values = mappings.keySet();
		for (E value : values) {
/*			bitmap.or(mappings.get(value));
			bitmap.and(bitSet);
			sum += ((Number) value).doubleValue() * bitmap.cardinality();
			bitmap.clear();*/
			int i = 0;
			for (Container container : mappings.get(value)) {
				i = container.getKey() * containerCapacity;
				for (Integer index : container) {
					if (bitSet.get(i + index)) {
						sum += ((Number) value).doubleValue();
					}
				}
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
		RoaringBitmap bitmap = new RoaringBitmap();
		Set<E> values = mappings.keySet();
		for (E value : values) {
			bitmap.or(mappings.get(value));
			bitmap.and(bitSet);
			sum += (Integer) value * bitmap.cardinality();
			bitmap.clear();
		}
		return sum / (double) bitSet.cardinality();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Column<E> convertToPlain() {
		ColumnPlain<E> colPlain = new ColumnPlain<>(length());
		for (E value : mappings.keySet()) {
			RoaringBitmap bitmap = mappings.get(value);
			int i = 0;
			for (Container container : bitmap) {
				i = container.getKey() * containerCapacity;
				for (Integer index : container) {
					colPlain.add(value, i + index);
				}
			}
		}
		return colPlain;
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
		return ColumnType.ROARING;
	}

	@Override
	public long sizeEstimation() {
		long size = 0;
		for (E key : mappings.keySet()) {
			RoaringBitmap roaringBitmap = mappings.get(key);
			if (key instanceof String) {
				size += (42 + ((String)key).length() * 2);
			}
			else {
				size += 32;
			}
			for (Container container : roaringBitmap) {
				size += container.getSize();
			}
		}
		return size;
	}

	@Override
	public Iterator<Tuple2<E, Integer>> iterator() {
		return new ColumnBitmapRoaringIterator();
	}

	private class ColumnBitmapRoaringIterator implements Iterator<Tuple2<E, Integer>> {

		private int i;
		private Tuple2<E, Integer> value;

		public ColumnBitmapRoaringIterator() {
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

}
