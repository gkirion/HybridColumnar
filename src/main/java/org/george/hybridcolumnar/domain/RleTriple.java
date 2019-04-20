package org.george.hybridcolumnar.domain;

import java.io.Serializable;

public interface RleTriple<E> extends Serializable {

	public E getDatum();
	
	public void setDatum(E datum);
	
	public int getRunLength();
	
	public void setRunLength(int runLength);
	
	public int getIndex();
	
	public void setIndex(int index);
	
}
