package org.george.hybridcolumnar.domain;

public class RleIntTriple implements RleTriple<Integer> {
	
	private int datum;
	
	private int runLength;
	
	private int index;
	
	public RleIntTriple() {
		datum = 0;
		runLength = 0;
		index = 0;
	}
	
	public RleIntTriple(Integer datum, int runLength, int index) {
		this.datum = datum;
		this.runLength = runLength;
		this.index = index;
	}

	@Override
	public Integer getDatum() {
		return datum;
	}

	@Override
	public void setDatum(Integer datum) {
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
