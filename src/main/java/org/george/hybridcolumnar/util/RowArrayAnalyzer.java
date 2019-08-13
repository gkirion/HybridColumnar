package org.george.hybridcolumnar.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.george.hybridcolumnar.domain.RowArray;
import org.george.hybridcolumnar.domain.Tuple2;

public class RowArrayAnalyzer {

	public static List<Integer> getColumnsOrderedByCardinality(List<RowArray> rows) {
		HashMap<Integer, HashMap<Comparable<?>, Boolean>> uniqueElements = new HashMap<>();
		for (RowArray row : rows) {
			int i = 0;
			for (Comparable<?> item : row) {
				if (uniqueElements.get(i) == null) {
					uniqueElements.put(i, new HashMap<>());
				}
				uniqueElements.get(i).put(item, true);
				i++;
			}
		}
		ArrayList<Tuple2<Integer, Integer>> cardinalities = new ArrayList<>();
		for (int i = 0; i < uniqueElements.size(); i++) {
			cardinalities.add(new Tuple2<Integer, Integer>(i, uniqueElements.get(i).size()));
		}
		cardinalities.sort(new Comparator<Tuple2<Integer, Integer>>() {

			@Override
			public int compare(Tuple2<Integer, Integer> o1, Tuple2<Integer, Integer> o2) {
				return o1.getSecond().compareTo(o2.getSecond());
			}

		});
		List<Integer> orderList = new ArrayList<>();
		for (Tuple2<Integer, Integer> tuple : cardinalities) {
			orderList.add(tuple.getFirst());
		}
		return orderList;
		
	}
	
}
