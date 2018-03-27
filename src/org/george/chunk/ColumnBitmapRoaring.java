package org.george.chunk;

import java.io.Serializable;
import java.util.HashMap;

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

	@Override
	public Long sum(int start, int end) {
		Long sum = new Long(0);
		for (E item : mappings.keySet()) {
			sum += (int)item * mappings.get(item).get(start, end).cardinality();
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
