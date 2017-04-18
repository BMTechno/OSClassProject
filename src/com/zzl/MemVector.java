package com.zzl;

import java.util.Vector;

public class MemVector<E> extends Vector<E> {
	private MemNode memNode;
	public MemVector(E e) {
		this.memNode=(MemNode) e;
		this.addElement(e);
	}
}
