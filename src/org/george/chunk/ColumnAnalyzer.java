package org.george.chunk;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class ColumnAnalyzer<E extends Comparable<E>> implements Iterable<E>, Serializable {

	private ArrayList<E> arrayList;
	private E type;

	public ColumnAnalyzer() {
		arrayList = new ArrayList<>();
	}

	public void add(E item) {
		arrayList.add(item);
	}

	public int length() {
		return arrayList.size();
	}

	public int cardinality() {
		ArrayList<E> distinctList = (ArrayList<E>) arrayList.clone();
		distinctList.sort(null);
		E prev = null;
		int count = 0;
		for (E item : distinctList) {
			if (!item.equals(prev)) {
				count++;
				prev = item;
			}
		}
		return count;
	}

	public int runCount() {
		E prev = null;
		int count = 0;
		for (E value : arrayList) {
			if (!value.equals(prev)) {
				count++;
				prev = value;
			}
		}
		return count;
	}

	public double avgRunLength() {
		E prev = null;
		int count = 0;
		int runLength = 0;
		int runLengthTotal = 0;
		for (E value : arrayList) {
			if (!value.equals(prev)) {
				count++;
				runLengthTotal += runLength;
				runLength = 0;
				prev = value;
			}
			runLength++;
		}
		runLengthTotal += runLength;
		return runLengthTotal / (double) count;
	}

	public int maxDelta() {
		if (arrayList.isEmpty()) {
			return 0;
		}
		int previous = (Integer) arrayList.get(0);
		int maxDelta = 0;
		for (E value : arrayList) {
			if (((Integer) value - previous) > maxDelta) {
				maxDelta = (Integer) value - previous;
			}
			previous = (Integer) value;
		}
		return maxDelta;
	}

	public int minDelta() {
		if (arrayList.isEmpty()) {
			return 0;
		}
		int previous = (Integer) arrayList.get(0);
		int minDelta = 0;
		for (E value : arrayList) {
			if (((Integer) value - previous) < minDelta) {
				minDelta = (Integer) value - previous;
			}
			previous = (Integer) value;
		}
		return minDelta;
	}

	public int range() {
		return maxDelta() - minDelta() + 1;
	}

	public E dummy() {
		if (this.type instanceof Integer) {

		}
		return arrayList.get(0);
	}

	@Override
	public Iterator<E> iterator() {
		return new ColumnAnalyzerIterator();
	}

	private class ColumnAnalyzerIterator implements Iterator<E> {

		private int i;

		public ColumnAnalyzerIterator() {
			i = 0;
		}

		@Override
		public boolean hasNext() {
			return i < length();
		}

		@Override
		public E next() {
			E value = arrayList.get(i);
			i++;
			return value;
		}

	}

}
