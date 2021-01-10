package org.george.hybridcolumnar.column.analyzer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ColumnAnalyzer {

	public static<T> int cardinality(List<T> list) {
		Set<T> set = new HashSet<>(list);
		return set.size();
	}

	public static<T> long avgItemSize(List<T> list) {
		long size = 0;
		for (T value : list) {
			if (value instanceof String) {
				size += (16 + ((String)value).length() * 2);
			} else {
				size += 16;
			}
		}
		return size / (list.isEmpty() ? 1 : list.size());
	}

	public static<T> int numberOfRuns(List<T> list) {
		T previous = null;
		int count = 0;
		for (T value : list) {
			if (previous == null || !value.equals(previous)) {
				count++;
				previous = value;
			}
		}
		return count;
	}

	public static<T> double avgRunLength(List<T> list) {
		return list.size() / (double) numberOfRuns(list);
	}

	public static<T extends Number> long maxDelta(List<T> list) {
		Long previous = null;
		long maxDelta = 0;
		for (T value : list) {
			if (previous != null) {
				long diff = Math.abs(value.longValue() - previous);
				if (diff > maxDelta) {
					maxDelta = diff;
				}
			}
			previous = value.longValue();
		}
		return maxDelta;
	}

	public static<T extends Number> long maxNumber(List<T> list) {
		Long maxNumber = null;
		for (T value : list) {
			if (maxNumber == null || value.longValue() > maxNumber) {
				maxNumber = value.longValue();
			}
		}
		return (maxNumber == null ? 0 : maxNumber);
	}

	public static<T> ColumnAnalysisResult analyze(List<T> list) {
		int numberOfRuns = numberOfRuns(list);
		long avgItemSize = avgItemSize(list);
		int cardinality = cardinality(list);
		long maxNumber = 0, maxDelta = 0;
		if (!list.isEmpty() && list.get(0) instanceof Number) {
			maxNumber = ColumnAnalyzer.maxNumber((List<? extends Number>) list);
			maxDelta = ColumnAnalyzer.maxDelta((List<? extends Number>) list);
		}
		ColumnAnalysisResult result = new ColumnAnalysisResult();
		result.setSize(list.size());
		result.setNumberOfRuns(numberOfRuns);
		result.setAvgItemSize(avgItemSize);
		result.setCardinality(cardinality);
		result.setMaxNumber(maxNumber);
		result.setMaxDelta(maxDelta);
		return result;
	}

}
