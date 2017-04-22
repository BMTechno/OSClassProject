package com.zzl;

import java.awt.geom.Rectangle2D;

public class MemNode
{
	private String name;// 进程名
	private long begin;// 內存存放的起始位置
	private long size;// 内存资源大小
	private boolean flag;// 是否空闲
	private Rectangle2D rect;

	public MemNode(String name, long begin, long size, boolean flag)
	{
		super();
		this.name = name;
		this.begin = begin;
		this.size = size;
		this.flag = flag;
	}

	public Rectangle2D getRect()
	{
		return rect;
	}

	public void setRect(Rectangle2D rect)
	{
		this.rect = rect;
	}

	// aaa
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public long getBegin()
	{
		return begin;
	}

	public void setBegin(long begin)
	{
		this.begin = begin;
	}

	public long getSize()
	{
		return size;
	}

	public void setSize(long size)
	{
		this.size = size;
	}

	public boolean isFlag()
	{
		return flag;
	}

	public void setFlag(boolean flag)
	{
		this.flag = flag;
	}

	@Override
	public String toString()
	{
		return "MemNode [name=" + name + ", begin=" + begin + ", size=" + size + ", flag=" + flag + "]";
	}
}
