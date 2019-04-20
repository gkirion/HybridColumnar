package org.george.hybridcolumnar.column;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import org.george.hybridcolumnar.bitpacking.BitPackingContainer;
import org.george.hybridcolumnar.domain.BitSetExtended;
import org.george.hybridcolumnar.domain.Tuple2;

@SuppressWarnings("serial")
public class ColumnBitPacking implements Column<Integer>, Serializable {
	
	private List<BitPackingContainer> bitPackingContainers;
	private BitPackingContainer currentContainer;
	private int maxContainerSize;
	private String name;
	private Integer id;
	
	public ColumnBitPacking() {
		this("");
	}
	
	public ColumnBitPacking(String name) {
		this(name, 1000);
	}
	
	public ColumnBitPacking(String name, int maxContainerSize) {
		bitPackingContainers = new ArrayList<>();
		currentContainer = new BitPackingContainer(maxContainerSize);
		bitPackingContainers.add(currentContainer);
		this.name = name;
		this.maxContainerSize = maxContainerSize;
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
	public void add(Integer item) {
		if (currentContainer.size() >= maxContainerSize) {
			currentContainer.flush();
			currentContainer = new BitPackingContainer(maxContainerSize);
			bitPackingContainers.add(currentContainer);
		}
		currentContainer.add(item);
		id++;
	}

	@Override
	public Tuple2<Integer, Integer> get(int i) {
		int containerIndex = i / maxContainerSize;
		if (containerIndex >= bitPackingContainers.size()) {
			return null;
		}
		return new Tuple2<>(bitPackingContainers.get(containerIndex).get(i % maxContainerSize), 1);
	}

	@Override
	public BitSetExtended select(Predicate<Integer> predicate) {
		BitSetExtended bitSet = new BitSetExtended();
		int i = 0;
		for (BitPackingContainer bitPackingContainer :  bitPackingContainers) {
			for (Integer element : bitPackingContainer) {
				if (predicate.test(element)) {
					bitSet.set(i);
				}
				i++;
			}
		}
		return bitSet;
	}

	@Override
	public BitSetExtended selectEquals(Integer item) {
		BitSetExtended bitSet = new BitSetExtended();
		int i = 0;
		for (BitPackingContainer bitPackingContainer : bitPackingContainers) {
			for (Integer element : bitPackingContainer) {
				if (element == item) {
					bitSet.set(i);
				}
				i++;
			}
		}
		return bitSet;
	}

	@Override
	public BitSetExtended selectNotEquals(Integer item) {
		BitSetExtended bitSet = new BitSetExtended();
		int i = 0;
		for (BitPackingContainer bitPackingContainer : bitPackingContainers) {
			for (Integer element : bitPackingContainer) {
				if (element != item) {
					bitSet.set(i);
				}
				i++;
			}
		}
		return bitSet;
	}

	@Override
	public BitSetExtended selectLessThan(Integer item) {
		BitSetExtended bitSet = new BitSetExtended();
		int i = 0;
		for (BitPackingContainer bitPackingContainer : bitPackingContainers) {
			for (Integer element : bitPackingContainer) {
				if (element < item) {
					bitSet.set(i);
				}
				i++;
			}
		}
		return bitSet;
	}

	@Override
	public BitSetExtended selectLessThanOrEquals(Integer item) {
		BitSetExtended bitSet = new BitSetExtended();
		int i = 0;
		for (BitPackingContainer bitPackingContainer : bitPackingContainers) {
			for (Integer element : bitPackingContainer) {
				if (element <= item) {
					bitSet.set(i);
				}
				i++;
			}
		}
		return bitSet;
	}

	@Override
	public BitSetExtended selectMoreThan(Integer item) {
		BitSetExtended bitSet = new BitSetExtended();
		int i = 0;
		for (BitPackingContainer bitPackingContainer : bitPackingContainers) {
			for (Integer element : bitPackingContainer) {
				if (element > item) {
					bitSet.set(i);
				}
				i++;
			}
		}
		return bitSet;
	}

	@Override
	public BitSetExtended selectMoreThanOrEquals(Integer item) {
		BitSetExtended bitSet = new BitSetExtended();
		int i = 0;
		for (BitPackingContainer bitPackingContainer : bitPackingContainers) {
			for (Integer element : bitPackingContainer) {
				if (element >= item) {
					bitSet.set(i);
				}
				i++;
			}
		}
		return bitSet;
	}

	@Override
	public BitSetExtended selectBetween(Integer from, Integer to) {
		BitSetExtended bitSet = new BitSetExtended();
		int i = 0;
		for (BitPackingContainer bitPackingContainer : bitPackingContainers) {
			for (Integer element : bitPackingContainer) {
				if (element >= from && element <= to) {
					bitSet.set(i);
				}
				i++;
			}
		}
		return bitSet;
	}

	@Override
	public Column<Integer> filter(BitSetExtended bitSet) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double sum() {
		return sum(0, id);
	}

	@Override
	public Double sum(int start, int end) {
		int i = 0;
		Double sum = 0.0;
		for (BitPackingContainer bitPackingContainer : bitPackingContainers) {
			for (Integer element : bitPackingContainer) {
				if (i >= start && i < end) {
					sum += element;
				}
				i++;
			}
		}
		return sum;
	}

	@Override
	public Double sum(BitSetExtended bitSet) {
		int i = 0;
		Double sum = 0.0;
		for (BitPackingContainer bitPackingContainer : bitPackingContainers) {
			for (Integer element : bitPackingContainer) {
				if (bitSet.get(i)) {
					sum += element;
				}
				i++;
			}
		}
		return sum;
	}

	@Override
	public Double sum(int start, int end, BitSetExtended bitSet) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer count(int start, int end) {
		return (end < id ? end : id) - start;
	}

	@Override
	public Double avg() {
		return avg(0, id);
	}

	@Override
	public Double avg(int start, int end) {
		return sum(start, end) / (double) count(start, end);
	}

	@Override
	public Double avg(BitSetExtended bitSet) {
		return sum(bitSet) / bitSet.cardinality();
	}

	@Override
	public Column<Integer> convertToPlain() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int length() {
		return id;
	}

	@Override
	public int cardinality() {
		HashMap<Integer, Boolean> distinctMap = new HashMap<>();
		for (BitPackingContainer bitPackingContainer : bitPackingContainers) {
			for (Integer element : bitPackingContainer) {
				distinctMap.put(element, true);
			}
		}
		return distinctMap.size();
	}

	@Override
	public long sizeEstimation() {
		long size = 0;
		for (BitPackingContainer bitPackingContainer : bitPackingContainers) {
			size += bitPackingContainer.sizeEstimation();
		}
		return size;
	}

	@Override
	public ColumnType type() {
		return ColumnType.BIT_PACKING;
	}
	
	@Override
	public Iterator<Tuple2<Integer, Integer>> iterator() {
		return new ColumnBitPackingIterator();
	}
	
	private class ColumnBitPackingIterator implements Iterator<Tuple2<Integer, Integer>> {

		private int i = 0;
		
		@Override
		public boolean hasNext() {
			return i < id;
		}

		@Override
		public Tuple2<Integer, Integer> next() {
			Tuple2<Integer, Integer> value = get(i);
			i++;
			return value;
		}
		
	}

}
