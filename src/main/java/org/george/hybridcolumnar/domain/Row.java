package org.george.hybridcolumnar.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

@SuppressWarnings({ "serial", "rawtypes" })
public class Row implements Comparable<Row>, Iterable<String>, Serializable {

	private HashMap<String, Comparable> tuple;
	private Integer index;
	private Integer runLength;

	public Row() {
		tuple = new HashMap<>();
		index = 0;
		runLength = 0;
	}

	public void add(String key, Comparable data) {
		tuple.put(key, data);
	}

	public Comparable get(String key) {
		return tuple.get(key);
	}

	public HashMap<String, Comparable> getTuple() {
		return tuple;
	}

	public void setTuple(HashMap<String, Comparable> tuple) {
		this.tuple = tuple;
	}

	public int size() {
		return tuple.size();
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

	@Override
	@SuppressWarnings({ "unchecked" })
	public int compareTo(Row o) {
		HashMap<String, Comparable> otherTuple = o.getTuple();
		for (String key : tuple.keySet()) {
			if (tuple.get(key).compareTo(otherTuple.get(key)) < 0) {
				return -1;
			} else if (tuple.get(key).compareTo(otherTuple.get(key)) > 0) {
				return 1;
			}
		}
		return 0;
	}

	@Override
	public Iterator<String> iterator() {
		return new RowIterator();
	}

	private class RowIterator implements Iterator<String> {

		private Iterator<String> iterator = tuple.keySet().iterator();

		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public String next() {
			return iterator.next();
		}

	}

}
