package org.george.hybridcolumnar.column;

public class ColumnFactory {


	@SuppressWarnings("rawtypes")
	public static Column<Comparable> createColumn(ColumnType columnType) {
		return createColumn(columnType, "", 1000);
	}
	
	@SuppressWarnings("rawtypes")
	public static Column<Comparable> createColumn(ColumnType columnType, String columnName) {
		return createColumn(columnType, columnName, 1000);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Column<Comparable> createColumn(ColumnType columnType, String columnName, int maxContainerSize) {
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
			column = (Column) new ColumnDelta(columnName, maxContainerSize);
			break;
		case DELTA_DICTIONARY:
			column = new ColumnDictionaryDelta<Comparable>(columnName, maxContainerSize);
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
		return column;
	}

}
