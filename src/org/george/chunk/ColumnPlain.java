package org.george.chunk;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ColumnPlain<E extends Comparable<E>> implements Column<E>, Serializable {
	
	private ArrayList<E> arrayList;
	private String name;
	private Integer id;

	public ColumnPlain() {
		arrayList = new ArrayList<>();
		name = "";
		id = 0;
	}
	
	public ColumnPlain(String name) {
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
		arrayList.add(item);
		id++;
	}

	public Tuple2<E, Integer> get(int i) {
		return new Tuple2<E, Integer>(arrayList.get(i), 1);
	}

	public int getLength() {
		return arrayList.size();
	}

	public ArrayList<E> entrySet() {
		return arrayList;
	}
	
	public String toString() {
		return arrayList.toString();
	}

	@Override
	public Long sum(int start, int end) {
		Long sum = new Long(0);
		for (int i = start; i < end; i++) {
			//sum += (int)arrayList.get(i);
		}
		return sum;
	}

	@Override
	public Integer count(int start, int end) {
		return (end < id ? end : id) - start;
	}

	@Override
	public Double avg(int start, int end) {
		Long sum = sum(start, end);
		return sum / (double)count(start, end);
	}

	@Override
	public int getCardinality() {
		HashMap<E, Boolean> distinctMap = new HashMap<>();
		for (E value : arrayList) {
			distinctMap.put(value, true);
		}
		return distinctMap.size();
	}

}
