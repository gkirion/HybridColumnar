package org.george.hybridcolumnar.domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Tuple5<E1 extends Comparable<E1>, E2 extends Comparable<E2>, E3 extends Comparable<E3>, E4 extends Comparable<E4>, E5 extends Comparable<E5>>
		implements Comparable<Tuple5<E1, E2, E3, E4, E5>>, Serializable {

	private E1 first;
	private E2 second;
	private E3 third;
	private E4 fourth;
	private E5 fifth;

	public Tuple5(E1 first, E2 second, E3 third, E4 fourth, E5 fifth) {
		this.first = first;
		this.second = second;
		this.third = third;
		this.fourth = fourth;
		this.fifth = fifth;
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

	public void setFifth(E5 fifth) {
		this.fifth = fifth;
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

	public E5 getFifth() {
		return fifth;
	}

	public String toString() {
		return "(" + first + ", " + second + ", " + third + ", " + fourth + ", " + fifth + ")";
	}

	public int hashCode() {
		return 31 * (31
				* (31 * (31 * (31 + (first == null ? 0 : first.hashCode())) + (second == null ? 0 : second.hashCode()))
						+ third.hashCode())
				+ fourth.hashCode()) + fifth.hashCode();
	}

	public boolean equals(Object obj) {
		if (obj != null && this.getClass().equals(obj.getClass())) {
			Tuple5<E1, E2, E3, E4, E5> t = (Tuple5<E1, E2, E3, E4, E5>) obj;
			if (first.equals(t.getFirst()) && second.equals(t.getSecond()) && third.equals(t.getThird())
					&& fourth.equals(t.getFourth()) && fifth.equals(t.getFifth())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int compareTo(Tuple5<E1, E2, E3, E4, E5> o) {
		if (first.compareTo(o.getFirst()) == 0) {
			if (second.compareTo(o.getSecond()) == 0) {
				if (third.compareTo(o.getThird()) == 0) {
					if (fourth.compareTo(o.getFourth()) == 0) {
						return fifth.compareTo(o.getFifth());
					}
					return fourth.compareTo(o.getFourth());
				}
				return third.compareTo(o.getThird());
			}
			return second.compareTo(o.getSecond());
		}
		return first.compareTo(o.getFirst());
	}

}
