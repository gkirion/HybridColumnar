package org.george.hybridcolumnar.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.george.hybridcolumnar.column.Column;
import org.george.hybridcolumnar.column.ColumnFactory;
import org.george.hybridcolumnar.column.ColumnType;
import org.george.hybridcolumnar.domain.Row;
import org.george.hybridcolumnar.domain.RowArray;
import org.george.hybridcolumnar.domain.Tuple2;

public class Encoder {
	
	@SuppressWarnings({ "rawtypes" })
	public static HashMap<Integer, Column<Comparable>> splitIntoColumns(List<RowArray> rows) {
		HashMap<Integer, Column<Comparable>> columns = new HashMap<>();
		for (RowArray row : rows) {
			int index = 0;
			for (Comparable item : row) { 
				if (columns.get(index) == null) {
					columns.put(index, ColumnFactory.createColumn(ColumnType.RLE));
				}
				for (int i = 0; i < row.getRunLength(); i++) {
					columns.get(index).add(item);
				}
				index++;
			}  
		}
		return columns;
	}
	
	@SuppressWarnings({ "rawtypes" })
	public static ColumnType findBestEncoding(Column<Comparable> column) {
		Long minSize = null;
		ColumnType bestEncoding = null;
		double avgRunLength = 0;
		int runCount = 0;
		for (Tuple2<Comparable, Integer> item : column) {
			avgRunLength += item.getSecond();
			runCount++;
		}
		avgRunLength = avgRunLength / runCount;
		for (ColumnType columnType : ColumnType.values()) {
			if (columnType != ColumnType.RLE && columnType != ColumnType.DELTA && columnType != ColumnType.ROARING && columnType != ColumnType.BIT_PACKING) {
				continue;
			}
			if (avgRunLength > 100) {
				columnType = ColumnType.RLE;
			}
			if (columnType == ColumnType.BIT_PACKING && !(column.get(0).getFirst() instanceof Integer)) {
				columnType = ColumnType.BIT_PACKING_DICTIONARY;
			} 
			else if (columnType == ColumnType.DELTA && !(column.get(0).getFirst() instanceof Integer)) {
				columnType = ColumnType.DELTA_DICTIONARY;
			}
			else if (columnType == ColumnType.RLE && !(column.get(0).getFirst() instanceof Number)) {
				columnType = ColumnType.RLE_DICTIONARY;
			}
			
			Column<Comparable> compressedColumn;
			compressedColumn = ColumnFactory.createColumn(columnType, column.getName(), 1000);
			for (Tuple2<Comparable, Integer> item : column) {
				for (int i = 0; i < item.getSecond(); i++) {
					compressedColumn.add(item.getFirst());
				}
			}
			long sizeEstimation = compressedColumn.sizeEstimation();
			if (minSize == null || sizeEstimation < minSize) {
				minSize = sizeEstimation;
				bestEncoding = compressedColumn.type();
			}
		}
		return bestEncoding;
	}
	
	@SuppressWarnings({ "rawtypes" })
	public static HashMap<Integer, ColumnType> findBestEncodingArray(List<RowArray> rows) {
		HashMap<Integer, ColumnType> bestEncoding = new HashMap<>();
		HashMap<Integer, Long> minSize = new HashMap<>();
		double avgRunLength = 0;
		for (RowArray row : rows) {
			avgRunLength += row.getRunLength();
		}
		avgRunLength = avgRunLength / rows.size();
		
		for (ColumnType columnType : ColumnType.values()) {
			if (columnType != ColumnType.RLE && columnType != ColumnType.DELTA && columnType != ColumnType.ROARING && columnType != ColumnType.BIT_PACKING) {
				continue;
			}
			if (avgRunLength > 100) {
				columnType = ColumnType.RLE;
			}
			HashMap<Integer, Column<Comparable>> columns = new HashMap<>();
			for (RowArray row : rows) {
				int index = 0;
				for (Comparable item : row) { 
					if (columns.get(index) == null) {
						if (columnType == ColumnType.BIT_PACKING && !(item instanceof Integer)) {
							columnType = ColumnType.BIT_PACKING_DICTIONARY;
						} 
						else if (columnType == ColumnType.DELTA && !(item instanceof Integer)) {
							columnType = ColumnType.DELTA_DICTIONARY;
						}
						else if (columnType == ColumnType.RLE && !(item instanceof Number)) {
							columnType = ColumnType.RLE_DICTIONARY;
						}
						columns.put(index, ColumnFactory.createColumn(columnType));
					}
					for (int i = 0; i < row.getRunLength(); i++) {
						columns.get(index).add(item);
					}
					index++;
				}
			}
			for (Integer column : columns.keySet()) {
				long sizeEstimation = columns.get(column).sizeEstimation();
				if (minSize.get(column) == null || minSize.get(column) > sizeEstimation) {
					minSize.put(column, sizeEstimation);
					bestEncoding.put(column, columns.get(column).type());
				}
			}
		}
		return bestEncoding;
	}
	
	@SuppressWarnings({ "rawtypes" })
	public static HashMap<String, ColumnType> findBestEncoding(List<Row> rows) {
		HashMap<String, ColumnType> bestEncoding = new HashMap<>();
		HashMap<String, Long> minSize = new HashMap<>();
		for (ColumnType columnType : ColumnType.values()) {
			if (columnType != ColumnType.RLE && columnType != ColumnType.DELTA && columnType != ColumnType.ROARING && columnType != ColumnType.PLAIN) {
				continue;
			}
			HashMap<String, Column<Comparable>> columns = new HashMap<>();
			for (Row row : rows) {
				for (String key : row) { 
					if (columns.get(key) == null) {
						if (columnType == ColumnType.DELTA && !(row.get(key) instanceof Integer)) {
							columns.put(key, ColumnFactory.createColumn(ColumnType.DELTA_DICTIONARY));
						}
						else {
							columns.put(key, ColumnFactory.createColumn(columnType));
						}
					}
					columns.get(key).add(row.get(key));
				}
			}
			for (String column : columns.keySet()) {
				long sizeEstimation = columns.get(column).sizeEstimation();
				if (minSize.get(column) == null || minSize.get(column) > sizeEstimation) {
					minSize.put(column, sizeEstimation);
					bestEncoding.put(column, columns.get(column).type());
				}
			}
		}
		return bestEncoding;
	}
	
	@SuppressWarnings("rawtypes")
	public static Column<Comparable> compressColumn(Column<Comparable> column, ColumnType columnType) {
		Column<Comparable> compressedColumn = ColumnFactory.createColumn(columnType);
		for (Tuple2<Comparable, Integer> item : column) {
			for (int i = 0; i < item.getSecond(); i++) {
				compressedColumn.add(item.getFirst());
			}
		}
		return compressedColumn;
	}
	
	@SuppressWarnings({ "rawtypes" })
	public static Column<Comparable> compressColumn(Column<Comparable> column) {
		Long minSize = null;
		Column<Comparable> bestEncoding = null;
		for (ColumnType columnType : ColumnType.values()) {
			if (columnType != ColumnType.RLE && columnType != ColumnType.RLE_DICTIONARY && columnType != ColumnType.DELTA && columnType != ColumnType.BIT_PACKING && columnType != ColumnType.BIT_PACKING_DICTIONARY) {
				continue;
			}
			Column<Comparable> compressedColumn;
			if (columnType == ColumnType.DELTA && !(column.get(0).getFirst() instanceof Integer)) {
				compressedColumn = ColumnFactory.createColumn(ColumnType.DELTA_DICTIONARY, column.getName(), 1000);
			}
			else if (columnType == ColumnType.BIT_PACKING && !(column.get(0).getFirst() instanceof Integer)) {
				compressedColumn = ColumnFactory.createColumn(ColumnType.BIT_PACKING_DICTIONARY, column.getName(), 1000);
			}
			else if (columnType == ColumnType.RLE && !(column.get(0).getFirst() instanceof Integer)) {
				compressedColumn = ColumnFactory.createColumn(ColumnType.RLE_DICTIONARY, column.getName(), 1000);
			}
			else {
				compressedColumn = ColumnFactory.createColumn(columnType, column.getName(), 1000);
			}
			for (Tuple2<Comparable, Integer> item : column) {
				compressedColumn.add(item.getFirst());
			}
			long sizeEstimation = compressedColumn.sizeEstimation();
			if (minSize == null || sizeEstimation < minSize) {
				minSize = sizeEstimation;
				bestEncoding = compressedColumn;
			}
		}
		return bestEncoding;
	}
	
	public static List<List<RowArray>> splitIntoPacks(List<RowArray> rows, int packSize) {
		List<List<RowArray>> packs = new ArrayList<>();
		List<RowArray> pack = null;
		int i = 0;
		for (RowArray row : rows) {
			if (i % packSize == 0) {
				pack = new ArrayList<>();
				packs.add(pack);
			}
			pack.add(row);
			i++;
		}
		return packs;
	}

}
