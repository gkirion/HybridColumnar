package org.george.hybridcolumnar.domain;

import java.io.Serializable;

public class Tuple4<E1 extends Comparable<E1>, E2 extends Comparable<E2>, E3 extends Comparable<E3>, E4 extends Comparable<E4>> implements Comparable<Tuple4<E1, E2, E3, E4>>, Serializable {
	
	private E1 first;
	private E2 second;
	private E3 third;
	private E4 fourth;
	
	public Tuple4(E1 first, E2 second, E3 third, E4 fourth) {
		this.first = first;
		this.second = second;
		this.third = third;
		this.fourth = fourth;
	}
	
	public void setFirst(E1 first) {
		this.first = first;
	}
	
	public void setSecond(E2 second) {
		this.second = second;
	}
	
	public void setThird(E3 third) {
		this.third = third;
	}
	
	public void setFourth(E4 fourth) {
		this.fourth = fourth;
	}
	
	public E1 getFirst() {
		return first;
	}
	
	public E2 getSecond() {
		return second;
	}
	
	public E3 getThird() {
		return third;
	}
	
	public E4 getFourth() {
		return fourth;
	}
	
	public String toString() {
		return "(" + first + ", " + second + ", " + third + ", " + fourth + ")";
	}
	
	public int hashCode() {
		return 31 * (31 * (31 * (31 + (first == null ? 0 : first.hashCode())) + (second == null ? 0 : second.hashCode())) + third.hashCode()) + fourth.hashCode();
	}
	
	public boolean equals(Object obj) {
		if (obj != null && this.getClass().equals(obj.getClass())) {
			Tuple4<E1, E2, E3, E4> t = (Tuple4<E1, E2, E3, E4>) obj;
			if (first.equals(t.getFirst()) && second.equals(t.getSecond()) && third.equals(t.getThird()) && fourth.equals(t.getFourth())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int compareTo(Tuple4<E1, E2, E3, E4> o) {
		if (first.compareTo(o.getFirst()) == 0) {
			if (second.compareTo(o.getSecond()) == 0) {
				if (third.compareTo(o.getThird()) == 0) {
					return fourth.compareTo(o.getFourth());
				}
				return third.compareTo(o.getThird());
			}
			return second.compareTo(o.getSecond());
		}
		return first.compareTo(o.getFirst());
	}
	
}
