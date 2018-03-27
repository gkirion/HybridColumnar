package org.george.chunk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.io.Serializable;
import java.util.Map.Entry;

public class ColumnRle<E extends Comparable<E>> implements Column<E>, Serializable {
	
	private TreeMap<Integer, Tuple2<E, Integer>> treeMap;
	private ArrayList<Tuple3<E, Integer, Integer>> arrayList;
	private String name;
	private Integer id;
	
	public ColumnRle() {
		//treeMap = new TreeMap<>();
		arrayList = new ArrayList<>();
		name = "";
		id = 0;
	}
	
	public ColumnRle(String name) {
		//treeMap = new TreeMap<>();
		arrayList = new ArrayList<>();
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
		/*if (treeMap.lastEntry() != null && treeMap.lastEntry().getValue().getFirst().equals(item)) {
			treeMap.get(treeMap.lastKey()).setSecond(treeMap.get(treeMap.lastKey()).getSecond() + 1);
		}
		else {
			treeMap.put(id, new Tuple2<E, Integer>(item, 1));
		}*/
		int size = arrayList.size();
		if (size > 0 && arrayList.get(size - 1).getFirst().equals(item)) {
			arrayList.get(size - 1).setSecond(arrayList.get(size - 1).getSecond() + 1);
		}
		else {
			arrayList.add(new Tuple3<E, Integer, Integer>(item, 1, id));
		}
		id++;
	}
	
	public Tuple2<E, Integer> get(int i) {
		/*Integer key = treeMap.floorKey(i);
		if (key == null) {
			return null;
		}
		if (i < key + treeMap.get(key).getSecond()) {
			return new Tuple2<E, Integer>(treeMap.get(key).getFirst(), key + treeMap.get(key).getSecond() - i);
		} */
		int key = find(i, 0, arrayList.size() - 1);
		if (key == -1) {
			return null;
		}
		return new Tuple2<E, Integer>(arrayList.get(key).getFirst(), arrayList.get(key).getThird() + arrayList.get(key).getSecond() - i);
		//return null;
	}
	
	protected int find(int i, int left, int right) {
		if (left > right) {
			return -1;
		}
		int mid = (left + right) / 2;
		if (arrayList.get(mid).getThird() > i) {
			return find(i, left, mid -1);
		}
		else if (arrayList.get(mid).getSecond() + arrayList.get(mid).getThird() <= i) {
			return find(i, mid + 1, right);
		}
		else {
			return mid;
		}
		
	}
	
	@Override
	public int getLength() {
		/*if (treeMap.isEmpty()) {
			return 0;
		}
		Integer key = treeMap.lastKey();
		return key + (key == null ? 0 : treeMap.get(key).getSecond());*/
		if (arrayList.isEmpty()) {
			return 0;
		}
		int size = arrayList.size();
		return arrayList.get(size - 1).getThird() + arrayList.get(size - 1).getSecond();
	}
	
	public Set<Entry<Integer, Tuple2<E, Integer>>> entrySet() {
		return treeMap.entrySet();
		//return arrayList;
	}
	
	public String toString() {
		//return treeMap.values().toString();
		return arrayList.toString();
	}

	@Override
	public Long sum(int start, int end) {
		Long sum = new Long(0);
		int i = start;
		Tuple3<E, Integer, Integer> val;
		//Tuple2<E, Integer> val = get(i);
		int index = find(start, 0, arrayList.size() - 1);
		while (i < end) {
			val = arrayList.get(index);
			sum += (int)val.getFirst() * (i + val.getSecond() <= end ? val.getSecond() : end - i);
			//sum += (int)get(i).getFirst() * (i + get(i).getSecond() <= end ? get(i).getSecond() : end - i);
			i += val.getSecond();
			//val = treeMap.get(i);
			index++;
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
