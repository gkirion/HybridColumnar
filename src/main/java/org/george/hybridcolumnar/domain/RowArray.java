package org.george.hybridcolumnar.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings({ "serial", "rawtypes" })
public class RowArray implements Comparable<RowArray>, Iterable<Comparable>, Serializable {

	private List<Comparable> tuple;
	private Integer index;
	private Integer runLength;

	public RowArray() {
		tuple = new ArrayList<>();
		index = 0;
		runLength = 0;
	}

	public void add(Comparable data) {
		tuple.add(data);
	}

	public Comparable get(int index) {
		return tuple.get(index);
	}

	public List<Comparable> getTuple() {
		return tuple;
	}

	public void setTuple(List<Comparable> tuple) {
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
		RowArray other = (RowArray) obj;
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
	public int compareTo(RowArray o) {
		List<Comparable> otherTuple = o.getTuple();
		for (int i = 0; i < tuple.size(); i++) {
			if (tuple.get(i).compareTo(otherTuple.get(i)) < 0) {
				return -1;
			} else if (tuple.get(i).compareTo(otherTuple.get(i)) > 0) {
				return 1;
			}
		}
		return 0;
	}

	@Override
	public Iterator<Comparable> iterator() {
		return new RowArrayIterator();
	}
	
	private class RowArrayIterator implements Iterator<Comparable> {
		
		private Iterator<Comparable> iterator = tuple.iterator();

		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public Comparable next() {
			return iterator.next();
		}
		
	}
	
}
