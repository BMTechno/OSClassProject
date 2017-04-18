package com.zzl;

import java.util.Vector;

public class FreeVector<E> extends Vector <E>{
	private FreeNode freeNode;

	public FreeVector(E e) {
		this.freeNode = (FreeNode) e;
		this.addElement(e);
	}
}
