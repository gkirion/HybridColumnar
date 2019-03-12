package org.george.hybridcolumnar.column;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import org.george.hybridcolumnar.delta.DeltaContainer;
import org.george.hybridcolumnar.domain.Tuple2;

@SuppressWarnings("serial")
public class ColumnDelta implements Column<Integer>, Serializable {

	private List<DeltaContainer> deltaContainers;
	private DeltaContainer currentContainer;
	private int maxContainerSize;
	private String name;
	private Integer id;
	private int last;

	public ColumnDelta() {
		this("");
	}
	
	public ColumnDelta(String name) {
		this(name, 1000);
	}
	
	public ColumnDelta(int maxContainerSize) {
		this("", maxContainerSize);
	}
	
	public ColumnDelta(String name, int maxContainerSize) {
		deltaContainers = new ArrayList<>();
		currentContainer = null;
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
	public void add(Integer item) {
		if (id == 0) {
			last = item;
		}
		if (currentContainer == null || currentContainer.size() >= maxContainerSize || item < last) {
			if (currentContainer != null) {
				currentContainer.flush();
			}
			currentContainer = new DeltaContainer(maxContainerSize);
			deltaContainers.add(currentContainer);
		}
		currentContainer.add(item);
		last = item;
		id++;
	}

	@Override
	public Tuple2<Integer, Integer> get(int i) {
		int maxIndex = 0;
		for (DeltaContainer deltaContainer : deltaContainers) {
			maxIndex += deltaContainer.size();
			if (i < maxIndex) {
				return new Tuple2<>(deltaContainer.get(i - maxIndex - deltaContainer.size()), 1);
			}
		}
		return null;
	}

	@Override
	public BitSet select(Predicate<Integer> predicate) {
		BitSet bitSet = new BitSet();
		int i = 0;
		for (DeltaContainer deltaContainer : deltaContainers) {
			for (Integer element : deltaContainer) {
				if (predicate.test(element)) {
					bitSet.set(i);
				}
				i++;
			}
		}
		return bitSet;
	}

	@Override
	public BitSet selectEquals(Integer item) {
		BitSet bitSet = new BitSet();
		int i = 0;
		for (DeltaContainer deltaContainer : deltaContainers) {
			for (Integer element : deltaContainer) {
				if (element == item) {
					bitSet.set(i);
				}
				i++;
			}
		}
		return bitSet;
	}

	@Override
	public BitSet selectNotEquals(Integer item) {
		BitSet bitSet = new BitSet();
		int i = 0;
		for (DeltaContainer deltaContainer : deltaContainers) {
			for (Integer element : deltaContainer) {
				if (element == item) {
					bitSet.set(i);
				}
				i++;
			}
		}
		return bitSet;
	}

	@Override
	public BitSet selectLessThan(Integer item) {
		BitSet bitSet = new BitSet();
		int i = 0;
		for (DeltaContainer deltaContainer : deltaContainers) {
			for (Integer element : deltaContainer) {
				if (element < item) {
					bitSet.set(i);
				}
				i++;
			}
		}
		return bitSet;
	}

	@Override
	public BitSet selectLessThanOrEquals(Integer item) {
		BitSet bitSet = new BitSet();
		int i = 0;
		for (DeltaContainer deltaContainer : deltaContainers) {
			for (Integer element : deltaContainer) {
				if (element <= item) {
					bitSet.set(i);
				}
				i++;
			}
		}
		return bitSet;
	}

	@Override
	public BitSet selectMoreThan(Integer item) {
		BitSet bitSet = new BitSet();
		int i = 0;
		for (DeltaContainer deltaContainer : deltaContainers) {
			for (Integer element : deltaContainer) {
				if (element > item) {
					bitSet.set(i);
				}
				i++;
			}
		}
		return bitSet;
	}

	@Override
	public BitSet selectMoreThanOrEquals(Integer item) {
		BitSet bitSet = new BitSet();
		int i = 0;
		for (DeltaContainer deltaContainer : deltaContainers) {
			for (Integer element : deltaContainer) {
				if (element >= item) {
					bitSet.set(i);
				}
				i++;
			}
		}
		return bitSet;
	}

	@Override
	public BitSet selectBetween(Integer from, Integer to) {
		BitSet bitSet = new BitSet();
		int i = 0;
		for (DeltaContainer deltaContainer : deltaContainers) {
			for (Integer element : deltaContainer) {
				if (element >= from && element <= to) {
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
					sum += element;
				}
				i++;
			}
		}
		return sum;
	}

	@Override
	public Double sum(BitSet bitSet) {
		int i = 0;
		Double sum = 0.0;
		for (DeltaContainer deltaContainer : deltaContainers) {
			for (Integer element : deltaContainer) {
				if (bitSet.get(i)) {
					sum += element;
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
	public Double avg(BitSet bitSet) {
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
		return ColumnType.DELTA;
	}

	@Override
	public long sizeEstimation() {
		long size = 0;
		for (DeltaContainer deltaContainer : deltaContainers) {
			size += deltaContainer.sizeEstimation();
		}
		return size;
	}

	@Override
	public Iterator<Tuple2<Integer, Integer>> iterator() {
		return new ColumnDeltaIterator();
	}

	private class ColumnDeltaIterator implements Iterator<Tuple2<Integer, Integer>> {

		private int i;
		private int value;
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
		public Tuple2<Integer, Integer> next() {
			value = deltaContainerIterator.next();
			i++;
			if (!deltaContainerIterator.hasNext() && hasNext()) {
				deltaContainerIterator = deltaContainersIterator.next().iterator();
			}
			return new Tuple2<Integer, Integer>(value, 1);
		}

	}

	@Override
	public Column<Integer> filter(BitSet bitSet) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double sum(int start, int end, BitSet bitSet) {
		// TODO Auto-generated method stub
		return null;
	}

}
