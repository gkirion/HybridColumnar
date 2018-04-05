package org.george.chunk;

import java.io.Serializable;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Set;

public class ColumnBitmap<E extends Comparable<E>> implements Column<E>, Serializable {

	private HashMap<E, BitSet> mappings;
	private String name;
	private Integer id;
	
	public ColumnBitmap() {
		mappings = new HashMap<>();
		name = "";
		id = 0;
	}
	
	public ColumnBitmap(String name) {
		mappings = new HashMap<>();
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
		if (!mappings.containsKey(item)) {
			mappings.put(item, new BitSet());
		}
		mappings.get(item).set(id);
		id++;
	}

	public Tuple2<E, Integer> get(int i) {
		for (E item : mappings.keySet()) {
			if (mappings.get(item).get(i)) {
				return new Tuple2<E, Integer>(item, 1);
			}
		}
		return null;
	}
	
	@Override
	public BitSet selectEquals(E item) {
		Set<E> values = mappings.keySet();
		for (E value : values) {
			if (value.equals(item)) {
				return (BitSet)mappings.get(value).clone();
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
		for (E value : mappings.keySet()) {
			if (value.compareTo(item) < 0) {
				bSet.or(mappings.get(value));
			}
		}
		return bSet;
	}

	@Override
	public BitSet selectLessThanOrEquals(E item) {
		BitSet bSet = new BitSet();
		for (E value : mappings.keySet()) {
			if (value.compareTo(item) <= 0) {
				bSet.or(mappings.get(value));
			}
		}
		return bSet;
	}

	@Override
	public BitSet selectMoreThan(E item) {
		BitSet bSet = new BitSet();
		for (E value : mappings.keySet()) {
			if (value.compareTo(item) > 0) {
				bSet.or(mappings.get(value));
			}
		}
		return bSet;
	}

	@Override
	public BitSet selectMoreThanOrEquals(E item) {
		BitSet bSet = new BitSet();
		for (E value : mappings.keySet()) {
			if (value.compareTo(item) >= 0) {
				bSet.or(mappings.get(value));
			}
		}
		return bSet;
	}

	@Override
	public BitSet selectBetween(E from, E to) {
		BitSet bSet = new BitSet();
		for (E value : mappings.keySet()) {
			if (value.compareTo(from) >= 0 && value.compareTo(to) <= 0) {
				bSet.or(mappings.get(value));
			}
		}
		return bSet;
	}

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
		BitSet bSet = new BitSet();
		for (E value : mappings.keySet()) {
			bSet.or(mappings.get(value)); // load bitset of value
			bSet.and(bitSet);
			sum += (Integer)value * bSet.cardinality();
			bSet.clear();
		}
		return sum;
	}

	public Integer count(int start, int end) {
		return (end < id ? end : id) - start;
	}

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
