package org.george.hybridcolumnar.chunk;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import org.george.hybridcolumnar.column.Column;
import org.george.hybridcolumnar.column.ColumnRle;
import org.george.hybridcolumnar.domain.Tuple2;
import org.george.hybridcolumnar.domain.Tuple5;

public class Chunk3<T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>>
		implements Iterable<Tuple5<T1, T2, T3, Integer, Integer>>, Serializable {

	private Column<T1> col1;
	private Column<T2> col2;
	private Column<T3> col3;
	private int id;

	public Chunk3(String col1Name, String col2Name, String col3Name) {
		col1 = new ColumnRle<T1>(col1Name);
		col2 = new ColumnRle<T2>(col2Name);
		col3 = new ColumnRle<T3>(col3Name);
		id = 0;
	}

	public Chunk3(Column<T1> col1, Column<T2> col2, Column<T3> col3) {
		this.col1 = col1;
		this.col2 = col2;
		this.col3 = col3;
		id = col1.length();
	}

	public void add(T1 val1, T2 val2, T3 val3) {
		col1.add(val1);
		col2.add(val2);
		col3.add(val3);
		id++;
	}

	public Tuple5<T1, T2, T3, Integer, Integer> get(int i) {
		Tuple2<T1, Integer> val1 = col1.get(i);
		Tuple2<T2, Integer> val2 = col2.get(i);
		Tuple2<T3, Integer> val3 = col3.get(i);
		int min = val1.getSecond() < val2.getSecond() ? val1.getSecond() : val2.getSecond();
		min = min < val3.getSecond() ? min : val3.getSecond();
		return new Tuple5<T1, T2, T3, Integer, Integer>(val1.getFirst(), val2.getFirst(), val3.getFirst(), i, min);
	}

	public ArrayList<Tuple5<T1, T2, T3, Integer, Integer>> getAll() {
		ArrayList<Tuple5<T1, T2, T3, Integer, Integer>> rows = new ArrayList<>();
		Tuple5<T1, T2, T3, Integer, Integer> row;
		int i = 0;
		while (i < id) {
			row = get(i);
			rows.add(row);
			i += row.getFifth();
		}
		return rows;
	}

	public ArrayList<Tuple5<T1, T2, T3, Integer, Integer>> getAllImproved() {
		ArrayList<Tuple5<T1, T2, T3, Integer, Integer>> rows = new ArrayList<>();
		Tuple2<T1, Integer> val1 = null;
		Tuple2<T2, Integer> val2 = null;
		Tuple2<T3, Integer> val3 = null;
		int[] length = new int[3];
		int min;
		int i = 0;
		length[0] = 0;
		length[1] = 0;
		length[2] = 0;
		while (i < id) {
			if (length[0] <= i) {
				val1 = col1.get(i);
				length[0] = i + val1.getSecond();
			}
			if (length[1] <= i) {
				val2 = col2.get(i);
				length[1] = i + val2.getSecond();
			}
			if (length[2] <= i) {
				val3 = col3.get(i);
				length[2] = i + val3.getSecond();
			}
			min = length[0] - i < length[1] - i ? length[0] - i : length[1] - i;
			min = min < length[2] - i ? min : length[2] - i;
			rows.add(new Tuple5<T1, T2, T3, Integer, Integer>(val1.getFirst(), val2.getFirst(), val3.getFirst(), i,
					min));
			i += min;
		}
		return rows;
	}

	public Column<T1> getFirstColumn() {
		return col1;
	}

	public Column<T2> getSecondColumn() {
		return col2;
	}

	public Column<T3> getThirdColumn() {
		return col3;
	}

	public String toString() {
		ArrayList<Tuple5<T1, T2, T3, Integer, Integer>> rows = new ArrayList<>();
		Tuple5<T1, T2, T3, Integer, Integer> row;
		int i = 0;
		while (i < id) {
			row = get(i);
			rows.add(row);
			i += row.getFifth();
		}
		return rows.toString();
	}

	@Override
	public Iterator<Tuple5<T1, T2, T3, Integer, Integer>> iterator() {
		return new ChunkIterator();
	}

	private class ChunkIterator implements Iterator<Tuple5<T1, T2, T3, Integer, Integer>> {

		private int index;
		private int[] length;
		private Tuple2<T1, Integer> val1;
		private Tuple2<T2, Integer> val2;
		private Tuple2<T3, Integer> val3;

		public ChunkIterator() {
			index = 0;
			length = new int[3];
			length[0] = 0;
			length[1] = 0;
			length[2] = 0;
			val1 = null;
			val2 = null;
			val3 = null;
		}

		@Override
		public boolean hasNext() {
			return index < id;
		}

		public Tuple5<T1, T2, T3, Integer, Integer> get(int i) {
			int min;
			if (length[0] <= i) {
				val1 = col1.get(i);
				length[0] = i + val1.getSecond();
			}
			if (length[1] <= i) {
				val2 = col2.get(i);
				length[1] = i + val2.getSecond();
			}
			if (length[2] <= i) {
				val3 = col3.get(i);
				length[2] = i + val3.getSecond();
			}
			min = length[0] - i < length[1] - i ? length[0] - i : length[1] - i;
			min = min < length[2] - i ? min : length[2] - i;
			return new Tuple5<T1, T2, T3, Integer, Integer>(val1.getFirst(), val2.getFirst(), val3.getFirst(), i, min);
		}

		@Override
		public Tuple5<T1, T2, T3, Integer, Integer> next() {
			Tuple5<T1, T2, T3, Integer, Integer> value;
			value = get(index);
			index += value.getFifth();
			return value;
		}

	}

}
