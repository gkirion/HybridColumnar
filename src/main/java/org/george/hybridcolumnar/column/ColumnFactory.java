package org.george.hybridcolumnar.column;

public class ColumnFactory {

	public <E extends Comparable> Column<E> createColumn(ColumnAnalyzer<E> columnAnalyzer) {
		// rules
		return null;
	}

	public static <E extends Comparable<E>> Column<E> createColumn(ColumnType columnType) {
		return createColumn(null, columnType, "", 1000);
	}
	
	public static <E extends Comparable<E>> Column<E> createColumn(ColumnType columnType, String columnName) {
		return createColumn(null, columnType, columnName, 1000);
	}
	
	public static <E extends Comparable<E>> Column<E> createColumn(ColumnType columnType, String columnName, int maxContainerSize) {
		return createColumn(null, columnType, columnName, maxContainerSize);
	}

	public static <E extends Comparable<E>> Column<E> createColumn(ColumnAnalyzer<E> columnAnalyzer, ColumnType columnType, String columnName, int maxContainerSize) {
		Column<E> column;
		switch (columnType) {
		case RLE:
			column = new ColumnRle<E>(columnName);
			break;
		case RLE_DICTIONARY:
			column = new ColumnDictionaryRle<E>();
			break;
		case BITMAP:
			column = new ColumnBitmap<E>(columnName);
			break;
		case ROARING:
			column = new ColumnBitmapRoaring<E>(columnName, maxContainerSize);
			break;
		case DELTA:
			if (columnAnalyzer != null) {
				column = (Column<E>) new ColumnDelta(columnName, maxContainerSize);
			} else {
				column = (Column<E>) new ColumnDelta(columnName, maxContainerSize);
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
			column = new ColumnPlain<E>(columnName);
		}
		if (columnAnalyzer != null) {
			for (E value : columnAnalyzer) {
				column.add(value);
			}
		}
		return column;
	}

}
