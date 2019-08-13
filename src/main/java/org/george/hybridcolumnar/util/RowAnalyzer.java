package org.george.hybridcolumnar.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.george.hybridcolumnar.domain.Row;
import org.george.hybridcolumnar.domain.Tuple2;

public class RowAnalyzer {
	
	public static List<String> getColumnsOrderedByCardinality(List<Row> rows) {
		HashMap<String, HashMap<Comparable<?>, Boolean>> uniqueElements = new HashMap<>();
		for (Row row : rows) {
			for (String key : row) {
				if (uniqueElements.get(key) == null) {
					uniqueElements.put(key, new HashMap<>());
				}
				uniqueElements.get(key).put(row.get(key), true);
			}
		}
		ArrayList<Tuple2<String, Integer>> cardinalities = new ArrayList<>();
		for (String key : uniqueElements.keySet()) {
			cardinalities.add(new Tuple2<String, Integer>(key, uniqueElements.get(key).size()));
		}
		cardinalities.sort(new Comparator<Tuple2<String, Integer>>() {

			@Override
			public int compare(Tuple2<String, Integer> o1, Tuple2<String, Integer> o2) {
				return o1.getSecond().compareTo(o2.getSecond());
			}

		});
		List<String> orderList = new ArrayList<>();
		for (Tuple2<String, Integer> tuple : cardinalities) {
			orderList.add(tuple.getFirst());
		}
		return orderList;

	}
	
}
