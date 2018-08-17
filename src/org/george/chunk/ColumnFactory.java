package org.george.chunk;

public class ColumnFactory<E extends Comparable<E>> {

	public Column<E> createColumn(ColumnAnalyzer<E> columnAnalyzer) {
		// rules
		return null;
	}

	public Column<E> createColumn(ColumnAnalyzer<E> columnAnalyzer, ColumnType columnType) {
		Column<E> column;
		switch (columnType) {
		case RLE:
			column = new ColumnRle<E>();
			break;
		case RLE_DICTIONARY:
			column = new ColumnDictionaryRle<E>();
			break;
		case BITMAP:
			column = new ColumnBitmap<E>();
			break;
		case ROARING:
			column = new ColumnBitmapRoaring<E>();
			break;
		case DELTA:
			column = (Column<E>) new ColumnDelta(columnAnalyzer.range(), columnAnalyzer.minDelta() * (-1));
			break;
		case DELTA_DICTIONARY:
			column = new ColumnDictionaryDelta<E>(columnAnalyzer.range(), columnAnalyzer.minDelta() * (-1));
			break;
		case PLAIN_DICTIONARY:
			column = new ColumnDictionaryPlain<E>();
			break;
		default:
			column = new ColumnPlain<E>();
		}
		for (E value : columnAnalyzer) {
			column.add(value);
		}
		return column;
	}

}
