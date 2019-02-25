package org.george.hybridcolumnar.delta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.george.hybridcolumnar.bitpacking.BitPacking;

@SuppressWarnings("serial")
public class DeltaContainer implements Iterable<Integer>, Serializable {
	
	private List<Integer> buffer;
	private BitPacking bitPacking;
	private int first;
	private int size;
	
	private int maxBufferSize;
	
	private Integer minDelta;
	private Integer maxDelta;
	private Integer previous;
	
	public DeltaContainer() {
		this(1000);
	}
	
	public DeltaContainer(int maxBufferSize) {
		buffer = new ArrayList<>();
		this.maxBufferSize = maxBufferSize;
		bitPacking = null;
		minDelta = null;
		maxDelta = null;
		previous = null;
		size = 0;
	}
	
	public void add(int item) {
		buffer.add(item);
		if (previous == null) {
			first = item;
			minDelta = 0;
			maxDelta = 0;
		}
		else {
			if (item - previous < minDelta) {
				minDelta = item - previous;
			}
			if (item - previous > maxDelta) {
				maxDelta = item - previous;
			}
		}
		previous = item;
		size++;
		if (buffer.size() >= maxBufferSize) {
			int numberOfBits = Integer.SIZE - Integer.numberOfLeadingZeros((maxDelta - minDelta));
			numberOfBits = numberOfBits == 0 ? 1 : numberOfBits;
			bitPacking = new BitPacking(numberOfBits);
			previous = null;
			for (Integer element : buffer) {
				if (previous == null) {
					bitPacking.add(0);
				}
				else {
					bitPacking.add(element - previous);
				}
				previous = element;
			}
			buffer = null;
		}
	}
	
	public Integer get(int i) {
		if (bitPacking == null) {
			return buffer.get(i);
		}
		else {
			int value = first;
			for (int j = 0; j <= i; j++) {
				value += bitPacking.get(j);
			}
			return value;
		}
	}
	
	public int size() {
		return size;
	}
	
	public long sizeEstimation() {
		if (bitPacking == null) {
			return size();
		}
		return bitPacking.sizeEstimation();
	}

	@Override
	public Iterator<Integer> iterator() {
		return new DeltaContainerIterator();
	}
	
	private class DeltaContainerIterator implements Iterator<Integer> {

		private int i;
		private int value;

		public DeltaContainerIterator() {
			i = 0;
			value = first;
		}

		@Override
		public boolean hasNext() {
			return i < size;
		}

		@Override
		public Integer next() {
			if (bitPacking == null) {
				value = buffer.get(i);
			}
			else {
				value += bitPacking.get(i);
			}
			i++;
			return value;
		}

	}
	
}
