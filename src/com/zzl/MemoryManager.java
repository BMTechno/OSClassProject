package com.zzl;

import java.util.Vector;

import com.bppleman.processmanagement.process.ProcessSimulator;
import com.bppleman.processmanagement.process.ProcessSimulator.STATE;

public class MemoryManager extends Thread
{
	MemVector<MemNode> memVector;
	Vector<BindNode> bindVector;
	FreeVector<FreeNode> freeVector;

	public enum ManagerMode
	{
		FF, NF, BF, WF
	}

	// 內存空间大小
	private long totalMem = 100000;
	ManagerMode managerMode;
	int count = 0;

	public MemoryManager(ManagerMode managerMode)
	{
		super();
		this.managerMode = managerMode;
		MemNode memNode = new MemNode("", 0, totalMem, false);
		FreeNode freeNode = new FreeNode(0, totalMem);
		memVector = new MemVector<>(memNode);
		freeVector = new FreeVector<>(freeNode);
		bindVector = new Vector<>();
	}

	public boolean requestMem(ProcessSimulator process)
	{
		switch (managerMode)
		{
			case FF:
				return FF(process);
			case NF:
				return NF(process);
			case BF:
				return BF(process);
		}
		return false;
	}

	// 首次适应算法
	private boolean FF(ProcessSimulator process)
	{
		boolean flag = false;
		for (int i = 0; i < memVector.size(); i++)
		{
			if (memVector.get(i).isFlag() == false && process.getNeedMemories() <= memVector.get(i).getSize())
			{
				long size = memVector.get(i).getSize() - process.getNeedMemories();
				long begin = memVector.get(i).getBegin() + process.getNeedMemories();
				if (size > 0)
				{
					MemNode memNode = new MemNode("", begin, size, false);
					memVector.insertElementAt(memNode, i + 1);
				}
				bindVector.add(new BindNode(process, memVector.get(i)));
				memVector.get(i).setName(process.getName());
				memVector.get(i).setSize(process.getNeedMemories());
				memVector.get(i).setFlag(true);
				flag = true;
				break;
			}
			if (i == memVector.size() - 1)
			{
				flag = false;
			}
		}
		return flag;
	}

	// 循环首次适应算法
	private boolean NF(ProcessSimulator process)
	{
		int t;
		boolean flag = false;
		for (int i = count; i < memVector.size(); i++)
		{
			if (memVector.get(i).isFlag() == false && memVector.get(i).getSize() >= process.getNeedMemories())
			{
				if (memVector.get(i).getSize() == process.getNeedMemories())
				{
					memVector.get(i).setName(process.getName());
					memVector.get(i).setFlag(true);
				}
				else
				{
					long begin = memVector.get(i).getBegin() + process.getNeedMemories();
					MemNode memNode = new MemNode(process.getName(), begin, process.getNeedMemories(), false);
					memVector.insertElementAt(memNode, i + 1);
					memVector.get(i).setSize(memVector.get(i).getSize() - process.getNeedMemories());
				}
				bindVector.add(new BindNode(process, memVector.get(i)));
				flag = true;
				count = i + 1;
				break;
			}
			t = count;
			if (i == memVector.size() - 1)
			{
				for (int j = 0; j < t; j++)
				{
					if (memVector.get(j).isFlag() == false && memVector.get(j).getSize() >= process.getNeedMemories())
					{
						if (memVector.get(j).getSize() == process.getNeedMemories())
						{
							memVector.get(j).setName(process.getName());
							memVector.get(j).setFlag(true);
						}
						else
						{
							long begin = memVector.get(j).getBegin() + process.getNeedMemories();
							MemNode memNode = new MemNode(process.getName(), begin, process.getNeedMemories(), false);
							memVector.insertElementAt(memNode, j + 1);
							memVector.get(j).setSize(memVector.get(j).getSize() - process.getNeedMemories());
						}
						bindVector.add(new BindNode(process, memVector.get(j)));
						flag = true;
						count = j + 1;
						break;
					}
					if (j == t - 1)
					{
						flag = false;
					}
				}
			}
		}
		return flag;
	}

	// 最佳适应算法
	private boolean BF(ProcessSimulator process)
	{
		int i, j, k;
		long size, begin;
		boolean flag = false;
		for (i = 0; i < freeVector.size(); i++)
		{
			// 找到第一个能够放入进程的空闲块
			if (freeVector.get(i).getSize() >= process.getNeedMemories())
			{
				for (j = 0; j < memVector.size(); j++)
				{
					// 查找与空闲块对应的内存块
					if (memVector.get(j).getBegin() == freeVector.get(i).getBegin())
					{
						memVector.get(j).setName(process.getName());
						memVector.get(j).setFlag(true);
						freeVector.remove(i);
						if (memVector.get(j).getSize() > process.getNeedMemories())
						{
							size = memVector.get(j).getSize() - process.getNeedMemories();
							begin = memVector.get(j).getBegin() + process.getNeedMemories();
							MemNode memNode = new MemNode("", begin, size, false);
							memVector.insertElementAt(memNode, j + 1);
							FreeNode freeNode = new FreeNode(begin, size);
							for (k = 0; k < freeVector.size(); k++)
							{
								if (freeVector.get(k).getSize() > size)
								{
									freeVector.insertElementAt(freeNode, k);
									break;
								}
								if (k == freeVector.size() - 1)
								{
									freeVector.insertElementAt(freeNode, k);
								}
							}
						}
						flag = true;
						break;
					}
					if (j == memVector.size() - 1)
					{
						flag = false;
					}
				}
				break;
			}
			if (i == freeVector.size() - 1)
			{
				flag = false;
			}
		}
		return flag;
	}

	@Override
	public void run()
	{
		// TODO 自动生成的方法存根
		super.run();
		while (true)
		{
			for (int i = 0; i < bindVector.size(); i++)
			{
				if (bindVector.get(i).getProcess().getState() == STATE.FINISH)
				{

				}
			}
		}

	}

	// 回收内存
	private boolean Freemem(ProcessSimulator process)
	{
		long size;
		boolean flag = false;
		int j;
		if (process.getState() == STATE.FINISH)
		{
			for (int i = 0; i < memVector.size(); i++)
			{
				if (memVector.get(i).getName().equals(process.getName()))
				{
					if (i != 0 && i != memVector.size() - 1)
					{
						// 上下分区是否空闲
						if (memVector.get(i - 1).isFlag() == false && memVector.get(i + 1).isFlag() == false)
						{
							size = memVector.get(i - 1).getSize() + memVector.get(i).getSize()
									+ memVector.get(i + 1).getSize();
							// 删去空闲链表中空闲分区
							if (managerMode.equals("NF"))
							{
								for (j = 0; j < freeVector.size(); j++)
								{
									if (memVector.get(i - 1).getBegin() == freeVector.get(j).getBegin())
									{
										freeVector.remove(j);
										break;
									}
								}
								for (j = 0; j < freeVector.size(); j++)
								{
									if (memVector.get(i + 1).getBegin() == freeVector.get(j).getBegin())
									{
										freeVector.remove(j);
										break;
									}
								}
								// 将新空闲分区加入空闲链表
								FreeNode freeNode = new FreeNode(memVector.get(i - 1).getBegin(), size);
								for (j = 0; j < freeVector.size(); j++)
								{
									if (freeVector.get(j).getSize() > size)
									{
										freeVector.insertElementAt(freeNode, j);
										break;
									}
									if (j == freeVector.size() - 1)
									{
										freeVector.add(freeNode);
									}
								}
							}
							// 合并内存表上下空闲分区
							memVector.remove(i);
							memVector.remove(i);
							memVector.get(i - 1).setSize(size);
						}
						// 合并上空闲分区
						else if (memVector.get(i - 1).isFlag() == false && memVector.get(i + 1).isFlag() == true)
						{
							size = memVector.get(i - 1).getSize() + memVector.get(i).getSize();
							if (managerMode.equals("NF"))
							{
								for (j = 0; j < freeVector.size(); j++)
								{
									if (freeVector.get(j).getBegin() == memVector.get(i - 1).getBegin())
									{
										freeVector.remove(j);
										break;
									}
								}
								FreeNode freeNode = new FreeNode(memVector.get(i - 1).getBegin(), size);
								for (j = 0; j < freeVector.size(); j++)
								{
									if (freeVector.get(j).getSize() > size)
									{
										freeVector.insertElementAt(freeNode, j);
										break;
									}
									if (j == freeVector.size() - 1)
									{
										freeVector.add(freeNode);
									}
								}
							}
							memVector.remove(i);
							memVector.get(i - 1).setSize(size);
						}
						// 合并下空闲分区
						else if (memVector.get(i - 1).isFlag() == true && memVector.get(i + 1).isFlag() == false)
						{
							size = memVector.get(i).getSize() + memVector.get(i + 1).getSize();
							if (managerMode.equals("NF"))
							{
								for (j = 0; j < freeVector.size(); j++)
								{
									if (freeVector.get(j).getBegin() == memVector.get(j).getBegin())
									{
										freeVector.get(j).setSize(size);
										break;
									}
								}
							}
							memVector.remove(i + 1);
							memVector.get(i).setSize(size);
							memVector.get(i).setName("");
							memVector.get(i).setFlag(false);
						}
						// 上下分区不空闲
						else
						{
							memVector.get(i).setName(" ");
							memVector.get(i).setFlag(false);
						}
					}
					// 第一块
					else if (i == 0)
					{
						if (memVector.get(i + 1).isFlag() == false)
						{
							size = memVector.get(i + 1).getSize();
							memVector.remove(i + 1);
							memVector.get(i).setSize(memVector.get(i).getSize() + size);
							memVector.get(i).setName("");
							memVector.get(i).setFlag(false);
						}
						else if (memVector.get(i + 1).isFlag() == true)
						{
							memVector.get(i).setName("");
							memVector.get(i).setFlag(false);
						}
						else
						{
							memVector.get(i).setName("");
							memVector.get(i).setFlag(false);
						}
					}
					// 最后一块
					else if (i == memVector.size() - 1)
					{
						if (memVector.get(i - 1).isFlag() == false)
						{
							size = memVector.get(i).getSize();
							memVector.remove(i);
							memVector.get(i).setSize(memVector.get(i).getSize() + size);
						}
						else if (memVector.get(i - 1).isFlag() == true)
						{
							memVector.get(i).setName("");
							memVector.get(i).setFlag(false);
						}
						else
						{
							memVector.get(i).setName("");
							memVector.get(i).setFlag(false);
						}
					}
					flag = true;
					break;
				}
				if (i == memVector.size() - 1)
				{
					flag = false;
				}
			}
		}
		return flag;
	}
}
