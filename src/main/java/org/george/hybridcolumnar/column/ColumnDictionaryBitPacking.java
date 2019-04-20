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
import org.george.hybridcolumnar.util.Dictionary;

@SuppressWarnings({ "serial", "rawtypes" })
public class ColumnDictionaryBitPacking<E extends Comparable> implements Column<E>, Serializable {

	private Dictionary<E> dictionary;
	private List<BitPackingContainer> bitPackingContainers;
	private BitPackingContainer currentContainer;
	private int maxContainerSize;
	private String name;
	private Integer id;
	
	public ColumnDictionaryBitPacking() {
		this("");
	}
	
	public ColumnDictionaryBitPacking(String name) {
		this(name, 1000);
	}
	
	public ColumnDictionaryBitPacking(int maxContainerSize) {
		this("", maxContainerSize);
	}
	
	public ColumnDictionaryBitPacking(String name, int maxContainerSize) {
		dictionary = new Dictionary<>();
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
	public void add(E item) {
		if (currentContainer.size() >= maxContainerSize) {
			currentContainer.flush();
			currentContainer = new BitPackingContainer(maxContainerSize);
			bitPackingContainers.add(currentContainer);
		}
		currentContainer.add(dictionary.insert(item));
		id++;
	}

	@Override
	public Tuple2<E, Integer> get(int i) {
		int containerIndex = i / maxContainerSize;
		if (containerIndex >= bitPackingContainers.size()) {
			return null;
		}
		Integer key = bitPackingContainers.get(containerIndex).get(i % maxContainerSize);
		return new Tuple2<>(dictionary.get(key), 1);
	}

	@Override
	public BitSetExtended select(Predicate<E> predicate) {
		BitSetExtended bitSet = new BitSetExtended();
		int i = 0;
		for (BitPackingContainer bitPackingContainer : bitPackingContainers) {
			for (Integer element : bitPackingContainer) {
				if (predicate.test(dictionary.get(element))) {
					bitSet.set(i);
				}
				i++;
			}
		}
		return bitSet;
	}

	@Override
	public BitSetExtended selectEquals(E item) {
		BitSetExtended bitSet = new BitSetExtended();
		int i = 0;
		for (BitPackingContainer bitPackingContainer : bitPackingContainers) {
			for (Integer element : bitPackingContainer) {
				if (dictionary.get(element).equals(item)) {
					bitSet.set(i);
				}
				i++;
			}
		}
		return bitSet;
	}

	@Override
	public BitSetExtended selectNotEquals(E item) {
		BitSetExtended bitSet = new BitSetExtended();
		int i = 0;
		for (BitPackingContainer bitPackingContainer : bitPackingContainers) {
			for (Integer element : bitPackingContainer) {
				if (!dictionary.get(element).equals(item)) {
					bitSet.set(i);
				}
				i++;
			}
		}
		return bitSet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BitSetExtended selectLessThan(E item) {
		BitSetExtended bitSet = new BitSetExtended();
		int i = 0;
		for (BitPackingContainer bitPackingContainer : bitPackingContainers) {
			for (Integer element : bitPackingContainer) {
				if (dictionary.get(element).compareTo(item) < 0) {
					bitSet.set(i);
				}
				i++;
			}
		}
		return bitSet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BitSetExtended selectLessThanOrEquals(E item) {
		BitSetExtended bitSet = new BitSetExtended();
		int i = 0;
		for (BitPackingContainer bitPackingContainer : bitPackingContainers) {
			for (Integer element : bitPackingContainer) {
				if (dictionary.get(element).compareTo(item) <= 0) {
					bitSet.set(i);
				}
				i++;
			}
		}
		return bitSet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BitSetExtended selectMoreThan(E item) {
		BitSetExtended bitSet = new BitSetExtended();
		int i = 0;
		for (BitPackingContainer bitPackingContainer : bitPackingContainers) {
			for (Integer element : bitPackingContainer) {
				if (dictionary.get(element).compareTo(item) > 0) {
					bitSet.set(i);
				}
				i++;
			}
		}
		return bitSet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BitSetExtended selectMoreThanOrEquals(E item) {
		BitSetExtended bitSet = new BitSetExtended();
		int i = 0;
		for (BitPackingContainer bitPackingContainer : bitPackingContainers) {
			for (Integer element : bitPackingContainer) {
				if (dictionary.get(element).compareTo(item) >= 0) {
					bitSet.set(i);
				}
				i++;
			}
		}
		return bitSet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BitSetExtended selectBetween(E from, E to) {
		BitSetExtended bitSet = new BitSetExtended();
		int i = 0;
		for (BitPackingContainer bitPackingContainer : bitPackingContainers) {
			for (Integer element : bitPackingContainer) {
				if (dictionary.get(element).compareTo(from) >= 0 && dictionary.get(element).compareTo(to) <= 0) {
					bitSet.set(i);
				}
				i++;
			}
		}
		return bitSet;
	}

	@Override
	public Column<E> filter(BitSetExtended bitSet) {
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
					sum += ((Number)dictionary.get(element)).doubleValue();
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
					sum += ((Number)dictionary.get(element)).doubleValue();
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
	public Column<E> convertToPlain() {
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
		long size = dictionary.sizeEstimation();
		for (BitPackingContainer bitPackingContainer : bitPackingContainers) {
			size += bitPackingContainer.sizeEstimation();
		}
		return size;
	}

	@Override
	public ColumnType type() {
		return ColumnType.BIT_PACKING_DICTIONARY;
	}
	
	@Override
	public Iterator<Tuple2<E, Integer>> iterator() {
		return new ColumnBitPackingIterator();
	}
	
	private class ColumnBitPackingIterator implements Iterator<Tuple2<E, Integer>> {
		
		private int i = 0;

		@Override
		public boolean hasNext() {
			return i < id;
		}

		@Override
		public Tuple2<E, Integer> next() {
			Tuple2<E, Integer> value = get(i);
			i++;
			return value;
		}
		
	}

}
