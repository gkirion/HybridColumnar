package org.george.hybridcolumnar.util;

import java.util.Comparator;
import java.util.List;

import org.george.hybridcolumnar.domain.RowArray;

public class RowArrayComparator implements Comparator<RowArray> {

	private List<Integer> orderList;

	public RowArrayComparator(List<Integer> orderList) {
		this.orderList = orderList;
	}

	@Override
	@SuppressWarnings("unchecked")
	public int compare(RowArray o1, RowArray o2) {
		for (Integer index : orderList) {
			if (o1.get(index).compareTo(o2.get(index)) < 0) {
				return -1;
			} else if (o1.get(index).compareTo(o2.get(index)) > 0) {
				return 1;
			}
		}
		return 0;
	}

}

