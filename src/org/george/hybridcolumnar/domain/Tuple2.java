package org.george.hybridcolumnar.domain;

import java.io.Serializable;

@SuppressWarnings({ "serial", "rawtypes" })
public class Tuple2<E1 extends Comparable, E2 extends Comparable>
		implements Comparable<Tuple2<E1, E2>>, Serializable {

	private E1 first;
	private E2 second;

	public Tuple2(E1 first, E2 second) {
		this.first = first;
		this.second = second;
	}

	public void setFirst(E1 first) {
		this.first = first;
	}

	public void setSecond(E2 second) {
		this.second = second;
	}

	public E1 getFirst() {
		return first;
	}

	public E2 getSecond() {
		return second;
	}

	public String toString() {
		return "(" + first + ", " + second + ")";
	}

	public int hashCode() {
		return 31 * (31 + (first == null ? 0 : first.hashCode())) + (second == null ? 0 : second.hashCode());
	}

	@SuppressWarnings("unchecked")
	public boolean equals(Object obj) {
		if (obj != null && this.getClass().equals(obj.getClass())) {
			Tuple2<E1, E2> t = (Tuple2<E1, E2>) obj;
			if (first.equals(t.getFirst()) && second.equals(t.getSecond())) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int compareTo(Tuple2<E1, E2> o) {
		if (first.compareTo(o.getFirst()) == 0) {
			return second.compareTo(o.getSecond());
		}
		return first.compareTo(o.getFirst());
	}

}
