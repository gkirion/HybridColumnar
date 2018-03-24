package org.george.chunk;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.io.Serializable;
import java.util.Map.Entry;

public class ColumnRle<E extends Comparable<E>> implements Column<E>, Serializable {
	
	private TreeMap<Integer, Tuple2<E, Integer>> treeMap;
	private String name;
	private Integer id;
	
	public ColumnRle() {
		treeMap = new TreeMap<>();
		name = "";
		id = 0;
	}
	
	public ColumnRle(String name) {
		treeMap = new TreeMap<>();
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
		if (treeMap.lastEntry() != null && treeMap.lastEntry().getValue().getFirst().equals(item)) {
			treeMap.get(treeMap.lastKey()).setSecond(treeMap.get(treeMap.lastKey()).getSecond() + 1);
		}
		else {
			treeMap.put(id, new Tuple2<E, Integer>(item, 1));
		}
		id++;
	}
	
	public Tuple2<E, Integer> get(int i) {
		Integer key = treeMap.floorKey(i);
		if (key == null) {
			return null;
		}
		if (i < key + treeMap.get(key).getSecond()) {
			return new Tuple2<E, Integer>(treeMap.get(key).getFirst(), key + treeMap.get(key).getSecond() - i);
		} 
		return null;
	}
	
	@Override
	public int getLength() {
		if (treeMap.isEmpty()) {
			return 0;
		}
		Integer key = treeMap.lastKey();
		return key + (key == null ? 0 : treeMap.get(key).getSecond());
	}
	
	public Set<Entry<Integer, Tuple2<E, Integer>>> entrySet() {
		return treeMap.entrySet();
	}
	
	public String toString() {
		return treeMap.values().toString();
	}

	@Override
	public Long sum(int start, int end) {
		Long sum = new Long(0);
		int i = start;
		Tuple2<E, Integer> val = get(i);
		while (i < end) {
			//sum += (int)val.getFirst() * (i + val.getSecond() <= end ? val.getSecond() : end - i);
			i += val.getSecond();
			val = treeMap.get(i);
		}
		return sum;
	}

	@Override
	public Integer count(int start, int end) {
		return (getLength() < end ? getLength() : end) - start;
	}

	@Override
	public Double avg(int start, int end) {
		Long sum = sum(start, end);
		return sum / (double)count(start, end);
	}

	@Override
	public int getCardinality() {
		HashMap<E, Boolean> distinctMap = new HashMap<>();
		for (Tuple2<E, Integer> value : treeMap.values()) {
			distinctMap.put(value.getFirst(), true);
		}
		return distinctMap.size();
	}
	
}
