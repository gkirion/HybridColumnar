package org.george.chunk;

import java.io.Serializable;
import java.util.BitSet;
import java.util.function.Predicate;

public class ColumnDelta implements Serializable {
	
	private BitPacking bitPacking;
	private String name;
	private int id;
	private int last;
	
	public ColumnDelta() {
		bitPacking = new BitPacking();
		name = "";
		id = 0;
		last = 0;
	}
	
	public ColumnDelta(int numberOfBits) {
		bitPacking = new BitPacking(numberOfBits);
		name = "";
		id = 0;
		last = 0;
	}
	
	public ColumnDelta(String name) {
		bitPacking = new BitPacking();
		this.name = name;
		id = 0;
		last = 0;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void add(int item) {
		bitPacking.add(item - last);
		last = item;
		id++;
	}

	public int get(int i) {
		int value = 0;
		for (int j = 0; j <= i; j++) {
			value += bitPacking.get(j);
		}
		return value;
	}

	public BitSet select(Predicate<Integer> predicate) {
		// TODO Auto-generated method stub
		return null;
	}

	public BitSet selectEquals(Integer item) {
		// TODO Auto-generated method stub
		return null;
	}

	public BitSet selectNotEquals(Integer item) {
		// TODO Auto-generated method stub
		return null;
	}

	public BitSet selectLessThan(Integer item) {
		// TODO Auto-generated method stub
		return null;
	}

	public BitSet selectLessThanOrEquals(Integer item) {
		// TODO Auto-generated method stub
		return null;
	}

	public BitSet selectMoreThan(Integer item) {
		// TODO Auto-generated method stub
		return null;
	}

	public BitSet selectMoreThanOrEquals(Integer item) {
		// TODO Auto-generated method stub
		return null;
	}

	public BitSet selectBetween(Integer from, Integer to) {
		// TODO Auto-generated method stub
		return null;
	}

	public Long sum(int start, int end) {
		// TODO Auto-generated method stub
		return null;
	}

	public Long sum(BitSet bitSet) {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer count(int start, int end) {
		// TODO Auto-generated method stub
		return null;
	}

	public Double avg(int start, int end) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getCardinality() {
		// TODO Auto-generated method stub
		return 0;
	} 

}
