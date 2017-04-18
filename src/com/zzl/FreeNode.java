package com.zzl;

public class FreeNode {
	private long begin;
	private long size;

	public FreeNode(long begin, long size) {
		super();
		this.begin = begin;
		this.size = size;
	}

	public long getBegin() {
		return begin;
	}

	public void setBegin(long begin) {
		this.begin = begin;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "FreeNode [begin=" + begin + ", size=" + size + "]";
	}

}
