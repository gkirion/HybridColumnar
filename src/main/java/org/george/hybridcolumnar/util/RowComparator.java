package org.george.hybridcolumnar.util;

import java.util.Comparator;
import java.util.List;

import org.george.hybridcolumnar.domain.Row;

public class RowComparator implements Comparator<Row> {

	private List<String> orderList;

	public RowComparator(List<String> orderList) {
		this.orderList = orderList;
	}

	@Override
	@SuppressWarnings("unchecked")
	public int compare(Row o1, Row o2) {
		for (String key : orderList) {
			if (o1.get(key).compareTo(o2.get(key)) < 0) {
				return -1;
			} else if (o1.get(key).compareTo(o2.get(key)) > 0) {
				return 1;
			}
		}
		return 0;
	}

}