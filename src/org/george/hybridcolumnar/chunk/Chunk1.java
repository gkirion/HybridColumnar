package org.george.hybridcolumnar.chunk;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import org.george.hybridcolumnar.column.Column;
import org.george.hybridcolumnar.column.ColumnRle;
import org.george.hybridcolumnar.domain.Tuple2;
import org.george.hybridcolumnar.domain.Tuple3;

public class Chunk1<T extends Comparable<T>> implements Iterable<Tuple3<T, Integer, Integer>>, Serializable {

	private Column<T> col;
	private int id;

	public Chunk1(String colName) {
		col = new ColumnRle<T>(colName);
		id = 0;
	}

	public Chunk1(Column<T> col) {
		this.col = col;
		id = col.length();
	}

	public void add(T val) {
		col.add(val);
		id++;
	}

	public Tuple3<T, Integer, Integer> get(int i) {
		Tuple2<T, Integer> val = col.get(i);
		return new Tuple3<T, Integer, Integer>(val.getFirst(), i, val.getSecond());
	}

	public ArrayList<Tuple3<T, Integer, Integer>> getAll() {
		ArrayList<Tuple3<T, Integer, Integer>> rows = new ArrayList<>();
		Tuple3<T, Integer, Integer> row;
		int i = 0;
		while (i < id) {
			row = get(i);
			rows.add(row);
			i += row.getThird();
		}
		return rows;
	}

	public ArrayList<Tuple3<T, Integer, Integer>> getAllImproved() {
		ArrayList<Tuple3<T, Integer, Integer>> rows = new ArrayList<>();
		Tuple2<T, Integer> val1 = null;
		int[] length = new int[1];
		int min;
		int i = 0;
		length[0] = 0;
		while (i < id) {
			if (length[0] <= i) {
				val1 = col.get(i);
				length[0] = i + val1.getSecond();
			}
			min = length[0] - i;
			rows.add(new Tuple3<T, Integer, Integer>(val1.getFirst(), i, min));
			i += min;
		}
		return rows;
	}

	public Column<T> getFirstColumn() {
		return col;
	}

	public ArrayList<ArrayList<Tuple2<Integer, Integer>>> join(Chunk1<T> otherChunk) {
		ArrayList<Tuple2<Integer, Integer>> thisPositions = new ArrayList<>();
		ArrayList<Tuple2<Integer, Integer>> otherPositions = new ArrayList<>();
		for (Tuple3<T, Integer, Integer> row : this) {
			for (Tuple3<T, Integer, Integer> otherRow : otherChunk) {
				if (row.getFirst().equals(otherRow.getFirst())) {
					thisPositions.add(new Tuple2<Integer, Integer>(row.getSecond(), row.getSecond() + row.getThird()));
					otherPositions.add(new Tuple2<Integer, Integer>(otherRow.getSecond(),
							otherRow.getSecond() + otherRow.getThird()));
				}
			}
		}
		ArrayList<ArrayList<Tuple2<Integer, Integer>>> result = new ArrayList<>();
		result.add(thisPositions);
		result.add(otherPositions);
		return result;
	}

	public String toString() {
		ArrayList<Tuple3<T, Integer, Integer>> rows = new ArrayList<>();
		Tuple3<T, Integer, Integer> row;
		int i = 0;
		while (i < id) {
			row = get(i);
			rows.add(row);
			i += row.getThird();
		}
		return rows.toString();
	}

	@Override
	public Iterator<Tuple3<T, Integer, Integer>> iterator() {
		return new ChunkIterator();
	}

	private class ChunkIterator implements Iterator<Tuple3<T, Integer, Integer>> {

		private int index;
		private int[] length;
		private Tuple2<T, Integer> val1;

		public ChunkIterator() {
			index = 0;
			length = new int[1];
			length[0] = 0;
			val1 = null;
		}

		@Override
		public boolean hasNext() {
			return index < id;
		}

		public Tuple3<T, Integer, Integer> get(int i) {
			int min;
			if (length[0] <= i) {
				val1 = col.get(i);
				length[0] = i + val1.getSecond();
			}
			min = length[0] - i;
			return new Tuple3<T, Integer, Integer>(val1.getFirst(), i, min);
		}

		@Override
		public Tuple3<T, Integer, Integer> next() {
			Tuple3<T, Integer, Integer> value;
			value = get(index);
			index += value.getThird();
			return value;
		}

	}

}
