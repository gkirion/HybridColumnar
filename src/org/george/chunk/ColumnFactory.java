package org.george.chunk;

public class ColumnFactory<E extends Comparable<E>> {
	
	public Column<E> createColumn(ColumnAnalyzer<E> columnAnalyzer) {
		// rules
		return null;
	}
	
	public Column<E> createColumn(ColumnAnalyzer<E> columnAnalyzer, ColumnType columnType) {
		switch (columnType) {	
			case RLE:
				
				break;
			case BITMAP:
				
				break;
			case ROARING:
				
				break;
			case DELTA:
				
				break;
			default:
				// PLAIN
				break;
		}
		return null;
	}

}
