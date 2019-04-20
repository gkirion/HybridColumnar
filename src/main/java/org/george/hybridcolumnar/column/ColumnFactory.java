package org.george.hybridcolumnar.column;

public class ColumnFactory {

	@SuppressWarnings("rawtypes")
	public <E extends Comparable> Column<E> createColumn(ColumnAnalyzer<Comparable> columnAnalyzer) {
		// rules
		return null;
	}

	@SuppressWarnings("rawtypes")
	public static Column<Comparable> createColumn(ColumnType columnType) {
		return createColumn(null, columnType, "", 1000);
	}
	
	@SuppressWarnings("rawtypes")
	public static Column<Comparable> createColumn(ColumnType columnType, String columnName) {
		return createColumn(null, columnType, columnName, 1000);
	}
	
	@SuppressWarnings("rawtypes")
	public static Column<Comparable> createColumn(ColumnType columnType, String columnName, int maxContainerSize) {
		return createColumn(null, columnType, columnName, maxContainerSize);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Column<Comparable> createColumn(ColumnAnalyzer<Comparable> columnAnalyzer, ColumnType columnType, String columnName, int maxContainerSize) {
		Column<Comparable> column;
		switch (columnType) {
		case RLE:
			column = new ColumnRle<Comparable>(columnName);
			break;
		case RLE_DICTIONARY:
			column = new ColumnDictionaryRle<Comparable>(columnName);
			break;
		case BITMAP:
			column = new ColumnBitmap<Comparable>(columnName);
			break;
		case ROARING:
			column = new ColumnBitmapRoaring<Comparable>(columnName, maxContainerSize);
			break;
		case DELTA:
			if (columnAnalyzer != null) {
				column = (Column) new ColumnDelta(columnName, maxContainerSize);
			} else {
				column = (Column) new ColumnDelta(columnName, maxContainerSize);
			}
			break;
		case DELTA_DICTIONARY:
			if (columnAnalyzer != null) {
				column = new ColumnDictionaryDelta<Comparable>(columnName, maxContainerSize);
			} else {
				column = new ColumnDictionaryDelta<Comparable>();
			}
			break;
		case BIT_PACKING:
			column = (Column) new ColumnBitPacking(columnName, maxContainerSize);
			break;
		case BIT_PACKING_DICTIONARY:
			column = new ColumnDictionaryBitPacking<>(columnName, maxContainerSize);
			break;
		case PLAIN_DICTIONARY:
			column = new ColumnDictionaryPlain<Comparable>();
			break;
		default:
			column = new ColumnPlain<Comparable>(columnName);
		}
		if (columnAnalyzer != null) {
			for (Comparable value : columnAnalyzer) {
				column.add(value);
			}
		}
		return column;
	}

}
