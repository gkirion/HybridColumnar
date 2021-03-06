package org.george.hybridcolumnar.column;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import org.george.hybridcolumnar.delta.DeltaContainer;
import org.george.hybridcolumnar.domain.BitSetExtended;
import org.george.hybridcolumnar.domain.ContainerClosedException;
import org.george.hybridcolumnar.domain.Tuple2;
import org.george.hybridcolumnar.util.Dictionary;

@SuppressWarnings({ "serial", "rawtypes" })
public class ColumnDictionaryDelta<E extends Comparable> implements Column<E>, Serializable {

	private Dictionary<E> dictionary;
	private List<DeltaContainer> deltaContainers;
	private DeltaContainer currentContainer;
	private int maxContainerSize;
	private String name;
	private Integer id;
	private int last;

	public ColumnDictionaryDelta() {
		this("");
	}

	public ColumnDictionaryDelta(String name) {
		this(name, 1000);
	}
	
	public ColumnDictionaryDelta(int maxContainerSize) {
		this("", maxContainerSize);
	}
	
	public ColumnDictionaryDelta(String name, int maxContainerSize) {
		dictionary = new Dictionary<>();
		deltaContainers = new ArrayList<>();
		currentContainer = new DeltaContainer(maxContainerSize);
		deltaContainers.add(currentContainer);
		this.name = name;
		this.maxContainerSize = maxContainerSize;
		id = 0;
		last = -1;
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
		Integer key = dictionary.insert(item);
		if (id == 0) {
			last = key;
		}
		try {
			currentContainer.add(key);
		} catch (ContainerClosedException e) {
			currentContainer = new DeltaContainer(maxContainerSize);
			deltaContainers.add(currentContainer);
		}
		last = key;
		id++;
	}

	@Override
	public Tuple2<E, Integer> get(int i) {
		int maxIndex = 0;
		for (DeltaContainer deltaContainer : deltaContainers) {
			maxIndex += deltaContainer.size();
			if (i < maxIndex) {
				Integer key = deltaContainer.get(i - (maxIndex - deltaContainer.size()));
				return new Tuple2<>(dictionary.get(key), 1);
			}
		}
		return null;
	}

	@Override
	public BitSetExtended select(Predicate<E> predicate) {
		BitSetExtended bitSet = new BitSetExtended();
		int i = 0;
		for (DeltaContainer deltaContainer : deltaContainers) {
			for (Integer element : deltaContainer) {
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
		for (DeltaContainer deltaContainer : deltaContainers) {
			for (Integer element : deltaContainer) {
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
		for (DeltaContainer deltaContainer : deltaContainers) {
			for (Integer element : deltaContainer) {
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
		for (DeltaContainer deltaContainer : deltaContainers) {
			for (Integer element : deltaContainer) {
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
		for (DeltaContainer deltaContainer : deltaContainers) {
			for (Integer element : deltaContainer) {
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
		for (DeltaContainer deltaContainer : deltaContainers) {
			for (Integer element : deltaContainer) {
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
		for (DeltaContainer deltaContainer : deltaContainers) {
			for (Integer element : deltaContainer) {
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
		for (DeltaContainer deltaContainer : deltaContainers) {
			for (Integer element : deltaContainer) {
				if (dictionary.get(element).compareTo(from) >= 0 && dictionary.get(element).compareTo(to) <= 0) {
					bitSet.set(i);
				}
				i++;
			}
		}
		return bitSet;
	}

	@Override
	public Double sum() {
		return sum(0, id);
	}

	@Override
	public Double sum(int start, int end) {
		int i = 0;
		Double sum = 0.0;
		for (DeltaContainer deltaContainer : deltaContainers) {
			for (Integer element : deltaContainer) {
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
		for (DeltaContainer deltaContainer : deltaContainers) {
			for (Integer element : deltaContainer) {
				if (bitSet.get(i)) {
					sum += ((Number)dictionary.get(element)).doubleValue();
				}
				i++;
			}
		}
		return sum;
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
	public int length() {
		return id;
	}

	@Override
	public int cardinality() {
		HashMap<Integer, Boolean> distinctMap = new HashMap<>();
		for (DeltaContainer deltaContainer : deltaContainers) {
			for (Integer element : deltaContainer) {
				distinctMap.put(element, true);
			}
		}
		return distinctMap.size();
	}

	@Override
	public ColumnType type() {
		return ColumnType.DELTA_DICTIONARY;
	}

	@Override
	public long sizeEstimation() {
		long size = dictionary.sizeEstimation();
		for (DeltaContainer deltaContainer : deltaContainers) {
			size += deltaContainer.sizeEstimation();
		}
		return size;
	}

	@Override
	public Iterator<Tuple2<E, Integer>> iterator() {
		return new ColumnDeltaIterator();
	}

	private class ColumnDeltaIterator implements Iterator<Tuple2<E, Integer>> {

		private int i;
		private int key;
		private Iterator<DeltaContainer> deltaContainersIterator;
		private Iterator<Integer> deltaContainerIterator;

		public ColumnDeltaIterator() {
			i = 0;
			deltaContainersIterator = deltaContainers.iterator();
			deltaContainerIterator = deltaContainersIterator.next().iterator();
		}

		@Override
		public boolean hasNext() {
			return i < id;
		}

		@Override
		public Tuple2<E, Integer> next() {
			key = deltaContainerIterator.next();
			i++;
			if (!deltaContainerIterator.hasNext() && hasNext()) {
				deltaContainerIterator = deltaContainersIterator.next().iterator();
			}
			return new Tuple2<E, Integer>(dictionary.get(key), 1);
		}

	}

	@Override
	public Column<E> filter(BitSetExtended bitSet) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double sum(int start, int end, BitSetExtended bitSet) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Column<E> convertToPlain() {
		// TODO Auto-generated method stub
		return null;
	}

}
