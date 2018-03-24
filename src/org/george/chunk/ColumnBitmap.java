package org.george.chunk;

import java.io.Serializable;
import java.util.BitSet;
import java.util.HashMap;

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

	public Long sum(int start, int end) {
		Long sum = new Long(0);
		for (int i = start; i < end; i++) {
			//sum += (int)get(i).getFirst();
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
