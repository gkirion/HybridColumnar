package org.george.hybridcolumnar.chunk;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import org.george.hybridcolumnar.column.Column;
import org.george.hybridcolumnar.domain.Row;
import org.george.hybridcolumnar.domain.Tuple2;

public class Chunk implements Iterable<Row>, Serializable {

	private ArrayList<Column<?>> columns;
	private Integer id;

	public Chunk() {
		columns = new ArrayList<>();
		id = null;
	}

	public void addColumn(Column<?> column) {
		columns.add(column);
		id = (id == null || id > column.length() ? column.length() : id);
	}

	public Column<?> getColumn(int i) {
		return columns.get(i);
	}

	public Row get(int i) {
		Row result = new Row();
		Tuple2<?, Integer> tuple;
		Integer minLength = null;
		for (Column<?> column : columns) {
			tuple = column.get(i);
			if (minLength == null || tuple.getSecond() < minLength) {
				minLength = tuple.getSecond();
			}
			result.add(tuple.getFirst());
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

}
