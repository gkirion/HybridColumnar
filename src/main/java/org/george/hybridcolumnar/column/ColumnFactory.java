package org.george.hybridcolumnar.column;

public class ColumnFactory {

	public <E extends Comparable> Column<E> createColumn(ColumnAnalyzer<E> columnAnalyzer) {
		// rules
		return null;
	}

	public static <E extends Comparable<E>> Column<E> createColumn(ColumnType columnType) {
		return createColumn(null, columnType);
	}

	public static <E extends Comparable<E>> Column<E> createColumn(ColumnAnalyzer<E> columnAnalyzer, ColumnType columnType) {
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
			if (columnAnalyzer != null) {
				column = (Column<E>) new ColumnDelta();
			} else {
				column = (Column<E>) new ColumnDelta();
			}
			break;
		case DELTA_DICTIONARY:
			if (columnAnalyzer != null) {
				column = new ColumnDictionaryDelta<E>(columnAnalyzer.range(), columnAnalyzer.minDelta() * (-1));
			} else {
				column = new ColumnDictionaryDelta<E>();
			}
			break;
		case PLAIN_DICTIONARY:
			column = new ColumnDictionaryPlain<E>();
			break;
		default:
			column = new ColumnPlain<E>();
		}
		if (columnAnalyzer != null) {
			for (E value : columnAnalyzer) {
				column.add(value);
			}
		}
		return column;
	}

}
