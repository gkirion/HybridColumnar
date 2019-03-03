package org.george.hybridcolumnar.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.george.hybridcolumnar.column.Column;
import org.george.hybridcolumnar.column.ColumnFactory;
import org.george.hybridcolumnar.column.ColumnType;
import org.george.hybridcolumnar.domain.Row;
import org.george.hybridcolumnar.domain.Tuple2;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.io.ClassPathResource;

public class Model {
	
	private MultiLayerNetwork model;
	
	private HashMap<Integer, ColumnType> encodings;
	
	public Model() {
		encodings = new HashMap<>();
		encodings.put(0, ColumnType.RLE);
		encodings.put(1, ColumnType.ROARING);
		encodings.put(2, ColumnType.DELTA);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap<String, ColumnType> findBestEncoding(List<Row> rows) {
		HashMap<String, ColumnType> bestEncoding = new HashMap<>();
		HashMap<String, Long> minSize = new HashMap<>();
		for (ColumnType columnType : ColumnType.values()) {
			if (columnType != ColumnType.RLE && columnType != ColumnType.DELTA && columnType != ColumnType.ROARING) {
				continue;
			}
			HashMap<String, Column<Comparable>> columns = new HashMap<>();
			for (Row row : rows) {
				for (String key : row) { 
					if (columns.get(key) == null) {
						columns.put(key, ColumnFactory.createColumn(columnType));
					}
					columns.get(key).add(row.get(key));
				}
			}
			for (String column : columns.keySet()) {
				long sizeEstimation = columns.get(column).sizeEstimation();
				if (minSize.get(column) == null || minSize.get(column) > sizeEstimation) {
					minSize.put(column, sizeEstimation);
					bestEncoding.put(column, columnType);
				}
				System.out.println(columnType + ": " + sizeEstimation);
			}
		}
		return bestEncoding;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ColumnType findBestEncoding(Column<Comparable> column) {
		Long minSize = null;
		ColumnType bestEncoding = null;
		for (ColumnType columnType : ColumnType.values()) {
			if (columnType != ColumnType.RLE && columnType != ColumnType.DELTA && columnType != ColumnType.ROARING) {
				continue;
			}
			Column<Comparable> compressedColumn = ColumnFactory.createColumn(columnType);
			for (Tuple2<Comparable, Integer> item : column) {
				compressedColumn.add(item.getFirst());
			}
			long sizeEstimation = compressedColumn.sizeEstimation();
			if (minSize == null || sizeEstimation < minSize) {
				minSize = sizeEstimation;
				bestEncoding = columnType;
			}
		}
		return bestEncoding;
	}
	
	public void loadModel(String path) throws IOException, InvalidKerasConfigurationException, UnsupportedKerasConfigurationException {
		// load the model
		String simpleMlp = new ClassPathResource(path).getFile().getPath();
		System.out.println(simpleMlp);
		model = KerasModelImport.importKerasSequentialModelAndWeights(simpleMlp);
	}
	
	@SuppressWarnings("rawtypes")
	public ColumnType predict(Column<Comparable> column) {
		int size = column.length();
		INDArray summary = Nd4j.zeros(size);
		int i = 0;
		for (Tuple2<Comparable, Integer> item : column) {
			summary.putScalar(new int[] {i}, (Integer)item.getFirst());
			i++;
		}
		INDArray prediction = model.output(summary);
		Double max = null;
		int index = 0;
		for (i = 0; i < prediction.length(); i++) {
			if (max == null || prediction.getDouble(i) > max) {
				max = prediction.getDouble(i);
				index = i;
			}
		}
		return encodings.get(index);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap<String, Column<Comparable>> splitIntoColumns(List<Row> rows) {
		HashMap<String, Column<Comparable>> columns = new HashMap<>();
		for (Row row : rows) {
			for (String key : row) { 
				if (columns.get(key) == null) {
					columns.put(key, ColumnFactory.createColumn(ColumnType.PLAIN));
				}
				columns.get(key).add(row.get(key));
			}
		}
		return columns;
	}
	
	public static List<List<Row>> splitIntoPacks(List<Row> rows, int packSize) {
		List<List<Row>> packs = new ArrayList<>();
		List<Row> pack = null;
		int i = 0;
		for (Row row : rows) {
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
