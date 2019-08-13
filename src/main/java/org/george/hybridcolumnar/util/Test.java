package org.george.hybridcolumnar.util;

import java.util.ArrayList;

public class Test {
	
	private ArrayList<Comparable<?>> list = new ArrayList<>();
	
	public ArrayList<Comparable<?>> getList() {
		return list;
	}
	
	public void addItem(Comparable<?> item) {
		list.add(item);
	}
	
	public Comparable<?> getItem(int i) {
		return list.get(i);
	}

}
