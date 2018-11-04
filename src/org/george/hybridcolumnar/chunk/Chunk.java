package org.george.hybridcolumnar.chunk;

import java.io.Serializable;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;

import org.george.hybridcolumnar.column.Column;
import org.george.hybridcolumnar.domain.Row;
import org.george.hybridcolumnar.domain.Tuple2;

@SuppressWarnings("serial")
public class Chunk implements Iterable<Row>, Serializable {

	@SuppressWarnings("rawtypes")
	private HashMap<String, Column> columns;
	private Integer id;

	public Chunk() {
		columns = new HashMap<>();
		id = null;
	}

	@SuppressWarnings("rawtypes")
	public void addColumn(String name, Column column) {
		columns.put(name, column);
		id = (id == null || id > column.length() ? column.length() : id);
	}

	@SuppressWarnings("rawtypes")
	public Column getColumn(String name) {
		return columns.get(name);
	}

	@SuppressWarnings("unchecked")
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

		public ChunkIterator() {
			index = 0;
		}

		@Override
		public boolean hasNext() {
			return index < id;
		}

		@Override
		public Row next() {
			Row value = get(index);
			index += (Integer) value.getRunLength();
			return value;
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
			index += (Integer) value.getRunLength();
			if (index < Integer.MAX_VALUE) {
				index = bitset.nextSetBit(index);
			}
			return value;
		}

	}

}
