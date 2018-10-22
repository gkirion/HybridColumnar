package org.george.hybridcolumnar.domain;

import java.util.ArrayList;

public class Row {

	private ArrayList<Comparable<?>> tuple;
	private Integer index;
	private Integer runLength;

	public Row() {
		tuple = new ArrayList<>();
		index = 0;
		runLength = 0;
	}

	public void add(Comparable<?> data) {
		tuple.add(data);
	}

	public void add(int index, Comparable<?> data) {
		tuple.set(index, data);
	}

	public Comparable<?> get(int index) {
		return tuple.get(index);
	}

	public ArrayList<Comparable<?>> getTuple() {
		return tuple;
	}

	public void setTuple(ArrayList<Comparable<?>> tuple) {
		this.tuple = tuple;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Integer getRunLength() {
		return runLength;
	}

	public void setRunLength(Integer runLength) {
		this.runLength = runLength;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tuple == null) ? 0 : tuple.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Row other = (Row) obj;
		if (tuple == null) {
			if (other.tuple != null)
				return false;
		} else if (!tuple.equals(other.tuple))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return tuple.toString();
	}

}
