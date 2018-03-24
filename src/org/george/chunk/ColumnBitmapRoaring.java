package org.george.chunk;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;

public class ColumnBitmapRoaring<E extends Comparable<E>> implements Column<E>, Serializable {
	
	private HashMap<E, Container[]> mappings;
	private HashMap<E, int[]> keys;
	private HashMap<E, Integer> sizes;
	private String name;
	private Integer id;
	private final int INITIAL_CAPACITY = 4;
	private final int MAX_CAPACITY = 65536;
	
	public ColumnBitmapRoaring() {
		mappings = new HashMap<>();
		keys = new HashMap<>();
		sizes = new HashMap<>();
		name = "";
		id = 0;
	}
	
	public ColumnBitmapRoaring(String name) {
		mappings = new HashMap<>();
		keys = new HashMap<>();
		sizes = new HashMap<>();
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
			mappings.put(item, new Container[INITIAL_CAPACITY]);
			keys.put(item, new int[INITIAL_CAPACITY]);
			sizes.put(item, 0);
		}
		int key = id / 32768;
		int index = Arrays.binarySearch(keys.get(item), 0, sizes.get(item), key);
		if (index < 0) { // if container doesn't exist, create it
			if (sizes.get(item) >= keys.get(item).length) { // if size is not enough
				int newCapacity = keys.get(item).length < 2048 ? keys.get(item).length * 2 : (int) (keys.get(item).length < 16384 ? keys.get(item).length * 1.5 : Integer.min((int) (keys.get(item).length * 1.2), MAX_CAPACITY));
				Container[] newContainer = Arrays.copyOf(mappings.get(item), newCapacity);
				int[] newKeys = Arrays.copyOf(keys.get(item), newCapacity);
				mappings.put(item, newContainer);
				keys.put(item, newKeys);
			}
			int pos = sizes.get(item);
			Container[] containers = mappings.get(item);
			int[] k = keys.get(item);
			containers[pos] = new ContainerArray();
			k[pos] = key;
			sizes.put(item, pos + 1);
			index = pos;
		}
		Container[] containers = mappings.get(item);
		containers[index].add((short) (id % 32768));
		/*if (container.getClass().equals(ContainerArray.class) && container.getCardinality() > 2048) { // container is dense, so convert it to bitmap
			Container newContainer = new ContainerBitmap();
			for (Integer i : container) {
				newContainer.add(i);
			}
			mappings.get(item)[id / 65536] = newContainer;
		}*/
		id++;
	}

	@Override
	public Tuple2<E, Integer> get(int i) {
		Container container;
		for (E item : mappings.keySet()) {
			container = mappings.get(item)[i / 32768];
			if (container != null && container.get((short) (i % 32768))) {
			//if (mappings.get(item)[0].get(i)) {
				return new Tuple2<E, Integer>(item, 1);
			}
		}
		return null;
	}

	@Override
	public Long sum(int start, int end) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer count(int start, int end) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double avg(int start, int end) {
		// TODO Auto-generated method stub
		return null;
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
