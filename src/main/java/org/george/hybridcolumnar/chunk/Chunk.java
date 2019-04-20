package org.george.hybridcolumnar.chunk;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.george.hybridcolumnar.column.Column;
import org.george.hybridcolumnar.column.ColumnRle;
import org.george.hybridcolumnar.column.ColumnType;
import org.george.hybridcolumnar.domain.Row;
import org.george.hybridcolumnar.domain.Tuple2;

@SuppressWarnings("serial")
public class Chunk implements Iterable<Row>, Serializable {

	@SuppressWarnings("rawtypes")
	private HashMap<String, Column<Comparable>> columns;
	private Integer id;

	public Chunk() {
		columns = new HashMap<>();
		id = null;
	}
	
	@SuppressWarnings("rawtypes")
	public static List<Chunk> chunkify(List<Row> partition, int chunkSize) {
		Chunk chunk = null;
		Row row;
		List<Chunk> chunkList = new ArrayList<>();
		for (int i = 0; i < partition.size(); i++) {
			row = partition.get(i);
			if (i % chunkSize == 0) {
				chunk = new Chunk();
				for (String key : row) {
					chunk.addColumn(key, new ColumnRle());
				}
				chunkList.add(chunk);
			}
			chunk.add(row);
		}
		return chunkList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addColumn(String name, Column column) {
		columns.put(name, column);
		id = (id == null || id > column.length() ? column.length() : id);
	}

	@SuppressWarnings("rawtypes")
	public Column<Comparable> getColumn(String name) {
		return columns.get(name);
	}

	public void add(Row row) {
		for (String key : row) {
			columns.get(key).add(row.get(key));
		}
		id++;
	}

	public Row get(int i) {
		Row result = new Row();
		Tuple2<?, Integer> tuple;
		Integer minLength = null;
		for (String colName : columns.keySet()) {
			Column<?> column = columns.get(colName);
			tuple = column.get(i);
			if (minLength == null || tuple.getSecond() < minLength) {
				minLength = tuple.getSecond();
			}
			result.add(colName, tuple.getFirst());
		}
		result.setIndex(i);
		result.setRunLength(minLength);
		return result;
	}

	public int numColumns() {
		return columns.size();
	}

	@Override
	public Iterator<Row> iterator() {
		return new ChunkIterator();
	}

	public Iterator<Row> iterator(BitSet bitset) {
		return new ChunkFilteredIterator(bitset);
	}

	private class ChunkIterator implements Iterator<Row> {

		private int index;
		@SuppressWarnings("rawtypes")
		private HashMap<String, Iterator<Tuple2<Comparable, Integer>>> iterators;
		private HashMap<String, Integer> nextIndex;
		private Row previous;

		public ChunkIterator() {
			index = 0;
			iterators = new HashMap<>();
			nextIndex = new HashMap<>();
			previous = null;
			for (String colName : columns.keySet()) {
				if (columns.get(colName).type() == ColumnType.ROARING) {
					iterators.put(colName, columns.get(colName).convertToPlain().iterator());
				}
				else {
					iterators.put(colName, columns.get(colName).iterator());
				}
				nextIndex.put(colName, 0);
			}
		}

		@Override
		public boolean hasNext() {
			return index < id;
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Row next() {
			Row result = new Row();
			Integer minIndex = null;
			for (String colName : columns.keySet()) {
				Comparable value;
				if (nextIndex.get(colName) <= index) {
					Tuple2<Comparable, Integer> tuple = iterators.get(colName).next();
					value = tuple.getFirst();
					nextIndex.put(colName, nextIndex.get(colName) + tuple.getSecond());
				}
				else {
					value = previous.get(colName);
				}
				if (minIndex == null || nextIndex.get(colName) < minIndex) {
					minIndex = nextIndex.get(colName);
				}
				result.add(colName, value);
			}
			result.setIndex(index);
			result.setRunLength(minIndex - index);
			previous = result;
			index = minIndex;
			return result;
		}

	}

	private class ChunkFilteredIterator implements Iterator<Row> {

		private int index;
		private BitSet bitset;

		public ChunkFilteredIterator(BitSet bitset) {
			this.bitset = bitset;
			index = bitset.nextSetBit(0);
		}

		@Override
		public boolean hasNext() {
			return index >= 0 && index < id;
		}

		@Override
		public Row next() {
			Row value = get(index);
			index += value.getRunLength();
			if (index < Integer.MAX_VALUE) {
				index = bitset.nextSetBit(index);
			}
			return value;
		}

	}

}
