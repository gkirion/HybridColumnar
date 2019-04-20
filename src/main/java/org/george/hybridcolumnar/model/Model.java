package org.george.hybridcolumnar.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.george.hybridcolumnar.column.Column;
import org.george.hybridcolumnar.column.ColumnFactory;
import org.george.hybridcolumnar.column.ColumnType;
import org.george.hybridcolumnar.domain.Row;
import org.george.hybridcolumnar.domain.RowArray;
import org.george.hybridcolumnar.domain.Tuple2;

public class Model {
	
	
	private HashMap<Integer, ColumnType> encodings;
	
	public Model() {
		encodings = new HashMap<>();
		encodings.put(0, ColumnType.RLE);
		encodings.put(1, ColumnType.ROARING);
		encodings.put(2, ColumnType.DELTA);
	}
	
	@SuppressWarnings({ "rawtypes" })
	public static HashMap<Integer, ColumnType> findBestEncodingArray(List<RowArray> rows) {
		HashMap<Integer, ColumnType> bestEncoding = new HashMap<>();
		HashMap<Integer, Long> minSize = new HashMap<>();
		for (ColumnType columnType : ColumnType.values()) {
			if (columnType != ColumnType.RLE && columnType != ColumnType.DELTA && columnType != ColumnType.ROARING && columnType != ColumnType.PLAIN) {
				continue;
			}
			HashMap<Integer, Column<Comparable>> columns = new HashMap<>();
			for (RowArray row : rows) {
				int index = 0;
				for (Comparable item : row) { 
					if (columns.get(index) == null) {
						if (columnType == ColumnType.DELTA && !(item instanceof Integer)) {
							columns.put(index, ColumnFactory.createColumn(ColumnType.DELTA_DICTIONARY));
						}
						else {
							columns.put(index, ColumnFactory.createColumn(columnType));
						}
					}
					columns.get(index).add(item);
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
	
	@SuppressWarnings({ "rawtypes" })
	public static ColumnType findBestEncoding(Column<Comparable> column) {
		Long minSize = null;
		ColumnType bestEncoding = null;
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
				bestEncoding = compressedColumn.type();
			}
		}
		return bestEncoding;
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
	
/*	public void loadModel(String path) throws IOException, InvalidKerasConfigurationException, UnsupportedKerasConfigurationException {
		// load the model
		String simpleMlp = new ClassPathResource(path).getFile().getPath();
		System.out.println(simpleMlp);
		model = KerasModelImport.importKerasSequentialModelAndWeights(simpleMlp);
	}
	
	@SuppressWarnings("rawtypes")
	public List<ColumnType> predict(List<List<Row>> packs) {
		int samples = 0, sampleLength = 0;
		for (List<Row> pack : packs) {
			HashMap<String, Column<Comparable>> columns = Model.splitIntoColumns(pack);
			samples += columns.size();
			for (Column<Comparable> column : columns.values()) {
				if (column.length() > sampleLength) {
					sampleLength = column.length();
				}
			}
		}
		INDArray summary = Nd4j.zeros(samples, sampleLength);
		int i = 0;
		for (List<Row> pack : packs) {
			HashMap<String, Column<Comparable>> columns = Model.splitIntoColumns(pack);
			for (Column<Comparable> column : columns.values()) {
				INDArray col = Nd4j.zeros(sampleLength);
				int j = 0;
				for (Tuple2<Comparable, Integer> item : column) {
					col.putScalar(new int[] {j}, (Integer)item.getFirst());
					j++;
				}
				summary.addRowVector(col);
				i++;
			}
		}
		
		long start = System.currentTimeMillis();
		int[] prediction = model.predict(summary);
		long end = System.currentTimeMillis();
		System.out.println("prediction time: " + (end - start));
		
		List<ColumnType> predictions = new ArrayList<>();
		for (i = 0; i < prediction.length; i++) {
			predictions.add(encodings.get(prediction[i]));
		}
		return predictions;
	}
	
	@SuppressWarnings("rawtypes")
	public ColumnType predict(Column<Comparable> column) {
		int size = column.length();
		INDArray summary = Nd4j.zeros(size);
		int i = 0;
		long start = System.currentTimeMillis();
		for (Tuple2<Comparable, Integer> item : column) {
			summary.putScalar(new int[] {i}, (Integer)item.getFirst());
			i++;
		}
		long end = System.currentTimeMillis();
		System.out.println("loading time: " + (end - start));
		start = System.currentTimeMillis();
		INDArray prediction = model.output(summary);
		int[] test = model.predict(summary);
		Nd4j.zeros(10,10);
		for (i = 0; i < test.length; i++) {
			System.out.println(test[i]);
		}
		end = System.currentTimeMillis();
		System.out.println("prediction time: " + (end - start));
		Double max = null;
		int index = 0;
		for (i = 0; i < prediction.length(); i++) {
			if (max == null || prediction.getDouble(i) > max) {
				max = prediction.getDouble(i);
				index = i;
			}
		}
		return encodings.get(index);
	}*/
	
	@SuppressWarnings({ "rawtypes" })
	public static HashMap<Integer, Column<Comparable>> splitIntoColumns(List<RowArray> rows) {
		HashMap<Integer, Column<Comparable>> columns = new HashMap<>();
		for (RowArray row : rows) {
			int index = 0;
			for (Comparable item : row) { 
				if (columns.get(index) == null) {
					columns.put(index, ColumnFactory.createColumn(ColumnType.PLAIN));
				}
				columns.get(index).add(item);
				index++;
			}  
		}
		return columns;
	}
	
	@SuppressWarnings("rawtypes")
	public static Column<Comparable> compressColumn(Column<Comparable> column, ColumnType columnType) {
		Column<Comparable> compressedColumn = ColumnFactory.createColumn(columnType);
		for (Tuple2<Comparable, Integer> item : column) {
			compressedColumn.add(item.getFirst());
		}
		return compressedColumn;
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
