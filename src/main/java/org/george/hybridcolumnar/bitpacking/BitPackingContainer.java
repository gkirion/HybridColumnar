package org.george.hybridcolumnar.bitpacking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("serial")
public class BitPackingContainer implements Iterable<Integer>, Serializable {
	
	private List<Integer> buffer;
	private BitPacking bitPacking;
	
	private int size;
	private int maxBufferSize;
	private int minElement;
	private int maxElement;
	
	public BitPackingContainer() {
		this(1000);
	}
	
	public BitPackingContainer(int maxBufferSize) {
		buffer = new ArrayList<>();
		bitPacking = null;
		this.maxBufferSize = maxBufferSize;
		size = 0;
		minElement = 0;
		maxElement = 0;
	}
	
	public void add(int item) {
		if (size < maxBufferSize) {
			if (item > maxElement) {
				maxElement = item;
			}
			if (item < minElement) {
				minElement = item;
			}
			buffer.add(item);
			size++;
		}

	}
	
	public void flush() {
		int numberOfBits = Integer.SIZE - Integer.numberOfLeadingZeros((maxElement - minElement));
		numberOfBits = numberOfBits == 0 ? 1 : numberOfBits;
		bitPacking = new BitPacking(numberOfBits);
		for (Integer value : buffer) {
			bitPacking.add(value - minElement);
		}
		buffer = null;
	}
	
	public Integer get(int i) {
		if (bitPacking == null) {
			return buffer.get(i);
		}
		return bitPacking.get(i) + minElement;
	}
	
	public int size() {
		return size;
	}
	
	public long sizeEstimation() {
		if (bitPacking == null) {
			return 48 + size() * 16;
		}
		return 48 + bitPacking.sizeEstimation();
	}
	

	@Override
	public Iterator<Integer> iterator() {
		return new BitPackingContainerIterator();
	}
	
	private class BitPackingContainerIterator implements Iterator<Integer> {
		
		private int index = 0;

		@Override
		public boolean hasNext() {
			return index < size;
		}

		@Override
		public Integer next() {
			Integer value = get(index);
			index++;
			return value;
		}
		
	}

}
