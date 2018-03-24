package org.george.chunk;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class Chunk2<T1 extends Comparable<T1>, T2 extends Comparable<T2>> implements Iterable<Tuple4<T1, T2, Integer, Integer>>, Serializable {

	private Column<T1> col1;
	private Column<T2> col2;
	private int id;
	
	public Chunk2(String col1Name, String col2Name) {
		col1 = new ColumnRle<T1>(col1Name);
		col2 = new ColumnRle<T2>(col2Name);
		id = 0;
	}
	
	public Chunk2(Column<T1> col1, Column<T2> col2) {
		this.col1 = col1;
		this.col2 = col2;
		id = col1.getLength();
	}
	
	public void add(T1 val1, T2 val2) {
		col1.add(val1);
		col2.add(val2);
		id++;
	}
	
	public Tuple4<T1, T2, Integer, Integer> get(int i) {
		Tuple2<T1, Integer> val1 = col1.get(i);
		Tuple2<T2, Integer> val2 = col2.get(i);
		int min = val1.getSecond() < val2.getSecond() ? val1.getSecond() : val2.getSecond();
		return new Tuple4<T1, T2, Integer, Integer>(val1.getFirst(), val2.getFirst(), i, min);
	}
	
	public ArrayList<Tuple4<T1, T2, Integer, Integer>> getAll() {
		ArrayList<Tuple4<T1, T2, Integer, Integer>> rows = new ArrayList<>();
		Tuple4<T1, T2, Integer, Integer> row;
		int i = 0;
		while (i < id) {
			row = get(i);
			rows.add(row);
			i += row.getFourth();
		}
		return rows;
	}
	
	public ArrayList<Tuple4<T1, T2, Integer, Integer>> getAllImproved() {
		ArrayList<Tuple4<T1, T2, Integer, Integer>> rows = new ArrayList<>();
		Tuple2<T1, Integer> val1 = null;
		Tuple2<T2, Integer> val2 = null;
		int[] length = new int[2];
		int min;
		int i = 0;
		length[0] = 0;
		length[1] = 0;
		while (i < id) {
			if (length[0] <= i) {
				val1 = col1.get(i);
				length[0] = i + val1.getSecond();
			}
			if (length[1] <= i) {
				val2 = col2.get(i);
				length[1] = i + val2.getSecond();
			}
			min = length[0] - i < length[1] - i ? length[0] - i : length[1] - i;
			rows.add(new Tuple4<T1, T2, Integer, Integer>(val1.getFirst(), val2.getFirst(), i, min));		
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
	
	/*
	public ArrayList<ArrayList<Integer>> select(String colNames) {
		ArrayList<ArrayList<Integer>> rows = new ArrayList<>();
		ArrayList<Integer> row;
		ArrayList<Column<Integer>> cols = new ArrayList<>();
		if (colNames.equals("*")) {
			for (String name : columns.keySet()) {
				cols.add(columns.get(name));
			}
		}
		else {
			String[] names = colNames.split(",");
			for (String name : names) {
				cols.add(columns.get(name.trim()));
			}
		}
		for (int i = 0; i < id; i++) {
			row = new ArrayList<>();
			for (Column<Integer> col : cols) {
				row.add(col.get(i).getFirst());
			}
			rows.add(row);
		}
		return rows;
	}
	
	public HashMap<Object, Integer> groupBy(String groupCols) {
		String[] colNames = groupCols.split(",");
		ArrayList<Column<Integer>> cols = new ArrayList<>();
		for (String colName : colNames) {
			cols.add(columns.get(colName));
		}
		HashMap<Object, Integer> result = new HashMap<>();
		ArrayList<Integer> row;
		for (int i = 0; i < id; i++) {
			row = new ArrayList<>();
			for (Column<Integer> col : cols) {
				row.add(col.get(i).getFirst());
			}
			if (result.get(row) == null) {
				result.put(row, 1);
			}
			else {
				result.put(row, result.get(row) + 1);
			}
		}
		return result;
	}*/
	
	public String toString() {
		ArrayList<Tuple4<T1, T2, Integer, Integer>> rows = new ArrayList<>();
		Tuple4<T1, T2, Integer, Integer> row;
		int i = 0;
		while (i < id) {
			row = get(i);
			rows.add(row);
			i += row.getFourth();
		}
		return rows.toString();
	}

	@Override
	public Iterator<Tuple4<T1, T2, Integer, Integer>> iterator() {
		return new ChunkIterator();
	}
	
	private class ChunkIterator implements Iterator<Tuple4<T1, T2, Integer, Integer>> {

		private int index;
		private int[] length;
		private Tuple2<T1, Integer> val1;
		private Tuple2<T2, Integer> val2;
		
		public ChunkIterator() {
			index = 0;
			length = new int[2];
			length[0] = 0;
			length[1] = 0;
			val1 = null;
			val2 = null;
		}
		
		@Override
		public boolean hasNext() {
			return index < id;
		}
		
		public Tuple4<T1, T2, Integer, Integer> get(int i) {
			int min;
			if (length[0] <= i) {
				val1 = col1.get(i);
				length[0] = i + val1.getSecond();
			}
			if (length[1] <= i) {
				val2 = col2.get(i);
				length[1] = i + val2.getSecond();
			}
			min = length[0] - i < length[1] - i ? length[0] - i : length[1] - i;
			return new Tuple4<T1, T2, Integer, Integer>(val1.getFirst(), val2.getFirst(), i, min);
		}

		@Override
		public Tuple4<T1, T2, Integer, Integer> next() {
			Tuple4<T1, T2, Integer, Integer> value;
			value = get(index);
			index += value.getFourth();
			return value;
		}
		
	}
	
}
