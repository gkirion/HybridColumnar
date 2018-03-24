package org.george.chunk;

import java.io.Serializable;

public class Tuple3<E1 extends Comparable<E1>, E2 extends Comparable<E2>, E3 extends Comparable<E3>> implements Comparable<Tuple3<E1, E2, E3>>, Serializable {
	
	private E1 first;
	private E2 second;
	private E3 third;
	
	public Tuple3(E1 first, E2 second, E3 third) {
		this.first = first;
		this.second = second;
		this.third = third;
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
	
	public E1 getFirst() {
		return first;
	}
	
	public E2 getSecond() {
		return second;
	}
	
	public E3 getThird() {
		return third;
	}
	
	public String toString() {
		return "(" + first + ", " + second + ", " + third + ")";
	}
	
	public int hashCode() {
		return 31 * (31 * (31 + (first == null ? 0 : first.hashCode())) + (second == null ? 0 : second.hashCode())) + third.hashCode();
	}
	
	public boolean equals(Object obj) {
		if (obj != null && this.getClass().equals(obj.getClass())) {
			Tuple3<E1, E2, E3> t = (Tuple3<E1, E2, E3>) obj;
			if (first.equals(t.getFirst()) && second.equals(t.getSecond()) && third.equals(t.getThird())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int compareTo(Tuple3<E1, E2, E3> o) {
		if (first.compareTo(o.getFirst()) == 0) {
			if (second.compareTo(o.getSecond()) == 0) {
				return third.compareTo(o.getThird());
			}
			return second.compareTo(o.getSecond());
		}
		return first.compareTo(o.getFirst());
	}

}
