package org.george.hybridcolumnar.domain;

import java.util.BitSet;

public class BitSetExtended extends BitSet {
	
	
	public BitSetExtended and(BitSetExtended set) {
		super.and(set);
		return this;
	}
	
	public BitSetExtended or(BitSetExtended set) {
		super.or(set);
		return this;
	}
	
	public BitSetExtended andNot(BitSetExtended set) {
		super.andNot(set);
		return this;
	}
	
}
