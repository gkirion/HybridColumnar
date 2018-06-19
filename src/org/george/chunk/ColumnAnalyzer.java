package org.george.chunk;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ColumnAnalyzer<E> implements Serializable {
	
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
		HashMap<E, Boolean> distinctMap = new HashMap<>();
		for (E value : arrayList) {
			distinctMap.put(value, true);
		}
		return distinctMap.size();
	}
	
	public int runCount() {
		if (arrayList.size() < 2) {
			return 1;
		}
		E previous = arrayList.get(0);
		int runCount = 1;
		for (E value : arrayList) {
			if (!value.equals(previous)) {
				runCount++;
				previous = value;
			}
		}
		return runCount;
	}
	
	public double avgRunLength() {
		if (arrayList.size() < 2) {
			return 1;
		}
		E previous = arrayList.get(0);
		int runLength = 0;
		int runLengthSum = 0;
		int runCount = 0;
		for (E value : arrayList) {
			if (value.equals(previous)) {
				runLength++;
			}
			else {
				runLengthSum += runLength;
				runLength = 1;
				runCount++;
				previous = value;
			}
		}
		runLengthSum += runLength;
		runCount++;
		return runLengthSum / runCount;
	}
	
	public int maxDelta() {
		int previous = 0;
		int maxDelta = 0;
		for (E value : arrayList) {
			if (((Integer)value - previous) > maxDelta) {
				maxDelta = (Integer) value - previous;
			}
			previous = (Integer) value;
		}
		return maxDelta;
	}
	
	public int minDelta() {
		int previous = 0;
		int minDelta = 0;
		for (E value : arrayList) {
			if (((Integer)value - previous) < minDelta) {
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

}
