package org.george.hybridcolumnar.domain;

public class RleReferenceTriple<E> implements RleTriple<E> {
	
	private E datum;
	
	private int runLength;
	
	private int index;
	
	public RleReferenceTriple() {
		datum = null;
		runLength = 0;
		index = 0;
	}
	
	public RleReferenceTriple(E datum, int runLength, int index) {
		this.datum = datum;
		this.runLength = runLength;
		this.index = index;
	}

	@Override
	public E getDatum() {
		return datum;
	}

	@Override
	public void setDatum(E datum) {
		this.datum = datum;
	}

	@Override
	public int getRunLength() {
		return runLength;
	}

	@Override
	public void setRunLength(int runLength) {
		this.runLength = runLength;
	}

	@Override
	public int getIndex() {
		return index;
	}

	@Override
	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public String toString() {
		return "RleTriple [datum=" + datum + ", runLength=" + runLength + ", index=" + index + "]";
	}
	
}
