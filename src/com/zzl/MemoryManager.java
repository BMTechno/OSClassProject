package com.zzl;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.JFrame;

import com.bppleman.memory.MemorySelectorLinstener;
import com.bppleman.processmanagement.process.ProcessSimulator;
import com.bppleman.processmanagement.process.ProcessSimulator.STATE;

public class MemoryManager extends Thread implements MouseListener, MemorySelectorLinstener
{
	// 内存表
	private MemVector<MemNode> memVector;
	// 对应关系表
	private Vector<BindNode> bindVector;
	// 空闲链表
	private FreeVector<FreeNode> freeVector;
	private JFrame frame;
	private MemPanel memPanel;
	private AttributePanel attributePanel;

	public enum ManagerMode
	{
		FF, BF, WF
	}

	// 內存空间大小
	private static long totalMem = 1000000;
	// 算法标志
	private ManagerMode managerMode;
	int count = 0;

	public MemoryManager(ManagerMode managerMode)
	{
		super();
		this.managerMode = managerMode;
		MemNode memNode = new MemNode("", 0, totalMem, false);
		FreeNode freeNode = new FreeNode(0, totalMem);
		// 初始化内存表
		memVector = new MemVector<>(memNode);
		// 初始化空闲链表
		freeVector = new FreeVector<>(freeNode);
		// 初始化对应关系表
		bindVector = new Vector<>();
		// 管理界面初始化
		initFrame();
	}

	public ManagerMode getManagerMode()
	{
		return managerMode;
	}

	public void setManagerMode(ManagerMode managerMode)
	{
		this.managerMode = managerMode;
	}

	public void initFrame()
	{
		frame = new JFrame("内存管理");
		memPanel = new MemPanel(memVector, frame);
		memPanel.addMouseListener(this);
		attributePanel = new AttributePanel(frame);
		frame.getContentPane().add(memPanel, BorderLayout.CENTER);
		frame.getContentPane().add(attributePanel, BorderLayout.EAST);
		frame.setBounds(0, 0, 500, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight());
		frame.setMinimumSize(new Dimension(500, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()));
		// 设置面板大小随frame变化
		attributePanel.setPreferredSize(new Dimension(frame.getWidth() / 3, frame.getHeight()));
	}

	public void setMemoryManagerViewVisible(boolean b)
	{
		frame.setVisible(b);
	}

	// 响应进程，申请内存
	public boolean requestMem(ProcessSimulator process)
	{
		switch (managerMode)
		{
			case FF:
				return FF(process);
			case BF:
				return BF(process);
			case WF:
				return WF(process);
		}
		return false;
	}

	// 首次适应算法
	private boolean FF(ProcessSimulator process)
	{

		boolean flag = false;
		// 同步锁
		synchronized (memVector)
		{
			for (int i = 0; i < memVector.size(); i++)
			{
				// 查找首个适合的空闲块
				if (memVector.get(i).isFlag() == false && process.getNeedMemories() <= memVector.get(i).getSize())
				{
					long size = memVector.get(i).getSize() - process.getNeedMemories();
					long begin = memVector.get(i).getBegin() + process.getNeedMemories();
					// 分出剩于空闲内存块
					if (size > 0)
					{
						MemNode memNode = new MemNode("", begin, size, false);
						memVector.insertElementAt(memNode, i + 1);
					}
					// 更新内存块信息
					memVector.get(i).setName(process.getName());
					memVector.get(i).setSize(process.getNeedMemories());
					memVector.get(i).setFlag(true);
					// 刷新界面
					memPanel.repaint();
					// 进程与内存块关系记录
					bindVector.add(new BindNode(process, memVector.get(i)));
					flag = true;

					break;
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
		synchronized (memVector)
		{

			for (i = 0; i < freeVector.size(); i++)
			{
				// 找到第一个能够放入进程的空闲块
				if (freeVector.get(i).getSize() > process.getNeedMemories())
				{

					for (j = 0; j < memVector.size(); j++)
					{
						// 查找与空闲块对应的内存块
						if (memVector.get(j).getBegin() == freeVector.get(i).getBegin())
						{
							size = memVector.get(j).getSize() - process.getNeedMemories();
							begin = memVector.get(j).getBegin() + process.getNeedMemories();
							// 更新内存块信息
							memVector.get(j).setName(process.getName());
							memVector.get(j).setFlag(true);
							memVector.get(j).setSize(process.getNeedMemories());
							// 记录对应关系
							bindVector.add(new BindNode(process, memVector.get(j)));
							MemNode memNode = new MemNode("", begin, size, false);
							memVector.insertElementAt(memNode, j + 1);
							FreeNode freeNode = new FreeNode(begin, size);
							// 将新空闲块加入空闲链表
							for (k = 0; k < freeVector.size(); k++)
							{
								if (freeVector.get(k).getSize() > size)
								{
									freeVector.insertElementAt(freeNode, k);
									break;
								}
								if (k == freeVector.size() - 1)
								{
									freeVector.add(freeNode);
									break;
								}
							}
							// 刷新
							memPanel.repaint();
							// 删除非空闲块
							freeVector.remove(i + 1);
							flag = true;
							break;
						}
					}
					break;
				}
			}
		}
		return flag;
	}

	// 最坏适应算法
	private boolean WF(ProcessSimulator process)
	{
		boolean flag = false;
		int j, k;
		long size, begin;
		synchronized (memVector)
		{
			if (freeVector.get(0).getSize() > process.getNeedMemories())
			{
				for (j = 0; j < memVector.size(); j++)
				{

					if (memVector.get(j).getBegin() == freeVector.get(0).getBegin())
					{
						size = memVector.get(j).getSize() - process.getNeedMemories();
						begin = memVector.get(j).getBegin() + process.getNeedMemories();
						memVector.get(j).setName(process.getName());
						memVector.get(j).setFlag(true);
						memVector.get(j).setSize(process.getNeedMemories());
						bindVector.add(new BindNode(process, memVector.get(j)));
						MemNode memNode = new MemNode("", begin, size, false);
						memVector.insertElementAt(memNode, j + 1);
						memPanel.repaint();
						FreeNode freeNode = new FreeNode(begin, size);
						for (k = 0; k < freeVector.size(); k++)
						{
							if (freeVector.get(k).getSize() < size)
							{
								freeVector.insertElementAt(freeNode, k);
								break;
							}
							if (k == freeVector.size() - 1)
							{
								freeVector.add(freeNode);
								break;
							}
						}
						memPanel.repaint();
						freeVector.remove(0);
						flag = true;
						break;
					}
				}
			}
		}
		return flag;
	}

	@Override
	public void run()
	{

		super.run();
		while (true)
		{
			Freemem();
		}

	}

	// 回收内存
	private void Freemem()
	{
		long size;
		int j;
		for (int k = 0; k < bindVector.size(); k++)
		{
			if (bindVector.get(k).getProcess().getState() == STATE.FINISH)
			{
				synchronized (memVector)
				{
					for (int i = 0; i < memVector.size(); i++)
					{
						if (bindVector.get(k).getMemNode().getBegin() == memVector.get(i).getBegin())
						{
							if (i != 0 && i != memVector.size() - 1)
							{
								// 上下分区是否空闲
								if (memVector.get(i - 1).isFlag() == false && memVector.get(i + 1).isFlag() == false)
								{
									size = memVector.get(i - 1).getSize() + memVector.get(i).getSize()
											+ memVector.get(i + 1).getSize();
									// 使用BF或WF算法时调用
									if (managerMode == ManagerMode.BF || managerMode == ManagerMode.WF)
									{
										FreeNode freeNode = new FreeNode(memVector.get(i - 1).getBegin(), size);

										for (j = 0; j < freeVector.size(); j++)
										{
											if (managerMode == ManagerMode.BF && freeVector.get(j).getSize() > size)
											{
												freeVector.insertElementAt(freeNode, j);
												break;
											}
											if (managerMode == ManagerMode.WF && freeVector.get(j).getSize() < size)
											{
												freeVector.insertElementAt(freeNode, j);
												break;
											}
											if (j == freeVector.size() - 1)
											{
												freeVector.add(freeNode);
												break;
											}

										}
										// 删去合并的空闲区
										for (j = 0; j < freeVector.size(); j++)
										{
											if (freeNode != freeVector.get(j)
													&& freeVector.get(j).getBegin() == memVector.get(i - 1).getBegin())
											{

												freeVector.remove(j);
												break;
											}
										}
										for (j = 0; j < freeVector.size(); j++)
										{
											if (freeNode != freeVector.get(j)
													&& freeVector.get(j).getBegin() == memVector.get(i + 1).getBegin())
											{
												freeVector.remove(j);
												break;
											}
										}
									} // BF空闲链表操作结束
										// 合并内存表上下空闲分区
									memVector.remove(i + 1);
									memPanel.repaint();
									memVector.remove(i);
									memPanel.repaint();
									memVector.get(i - 1).setSize(size);
								}
								// 合并上空闲分区
								else if (memVector.get(i - 1).isFlag() == false
										&& memVector.get(i + 1).isFlag() == true)
								{
									size = memVector.get(i - 1).getSize() + memVector.get(i).getSize();
									// 使用BF算法调用
									if (managerMode == ManagerMode.BF || managerMode == ManagerMode.WF)
									{
										FreeNode freeNode = new FreeNode(memVector.get(i - 1).getBegin(), size);
										for (j = 0; j < freeVector.size(); j++)
										{
											if (managerMode == ManagerMode.BF && freeVector.get(j).getSize() > size)
											{
												freeVector.insertElementAt(freeNode, j);
												break;
											}
											if (managerMode == ManagerMode.WF && freeVector.get(j).getSize() < size)
											{
												freeVector.insertElementAt(freeNode, j);
												break;
											}
											if (j == freeVector.size() - 1)
											{
												freeVector.add(freeNode);
												break;
											}
										}
										for (j = 0; j < freeVector.size(); j++)
										{
											if (freeNode != freeVector.get(j)
													&& freeVector.get(j).getBegin() == memVector.get(i - 1).getBegin())
											{
												freeVector.remove(j);
												break;
											}
										}
									} // 调用结束
										// 合并内存表上空闲分区
									memVector.remove(i);
									memVector.get(i - 1).setSize(size);
								}
								// 合并下空闲分区
								else if (memVector.get(i - 1).isFlag() == true
										&& memVector.get(i + 1).isFlag() == false)
								{
									size = memVector.get(i).getSize() + memVector.get(i + 1).getSize();
									// BF算法调用
									if (managerMode == ManagerMode.BF || managerMode == ManagerMode.WF)
									{
										FreeNode freeNode = new FreeNode(memVector.get(i).getBegin(), size);
										for (j = 0; j < freeVector.size(); j++)
										{
											if (managerMode == ManagerMode.BF && freeVector.get(j).getSize() > size)
											{
												freeVector.insertElementAt(freeNode, j);
												break;
											}
											if (managerMode == ManagerMode.WF && freeVector.get(j).getSize() < size)
											{
												freeVector.insertElementAt(freeNode, j);
												break;
											}
											if (j == freeVector.size() - 1)
											{
												freeVector.add(freeNode);
												break;
											}
										}
										for (j = 0; j < freeVector.size(); j++)
										{
											if (freeNode != freeVector.get(j)
													&& freeVector.get(j).getBegin() == memVector.get(i + 1).getBegin())
											{

												freeVector.remove(j);
												break;
											}
										}
									} // 调用结束
										// 合并内存表下空闲风区
									memVector.remove(i + 1);
									memVector.get(i).setSize(size);
									memVector.get(i).setName("");
									memVector.get(i).setFlag(false);
								}
								// 上下分区不空闲
								else
								{
									// BF算法调用
									if (managerMode == ManagerMode.BF || managerMode == ManagerMode.WF)
									{
										FreeNode freeNode = new FreeNode(memVector.get(i).getBegin(),
												memVector.get(i).getSize());
										for (j = 0; j < freeVector.size(); j++)
										{
											if (managerMode == ManagerMode.BF
													&& freeVector.get(j).getSize() > memVector.get(i).getSize())
											{
												freeVector.insertElementAt(freeNode, j);
												break;
											}
											if (managerMode == ManagerMode.WF
													&& freeVector.get(j).getSize() < memVector.get(i).getSize())
											{
												freeVector.insertElementAt(freeNode, j);
												break;
											}
											if (j == freeVector.size() - 1)
											{
												freeVector.add(freeNode);
												break;
											}
										}
									} // 调用结束
										// 释放内存
									memVector.get(i).setName(" ");
									memVector.get(i).setFlag(false);
								}
							}
							// 第一块
							else if (i == 0 && i != memVector.size() - 1)
							{

								if (memVector.get(i + 1).isFlag() == false)
								{
									size = memVector.get(i + 1).getSize() + memVector.get(i).getSize();
									// BF调用
									if (managerMode == ManagerMode.BF || managerMode == ManagerMode.WF)
									{
										FreeNode freeNode = new FreeNode(memVector.get(i).getBegin(), size);

										for (j = 0; j < freeVector.size(); j++)
										{

											if (managerMode == ManagerMode.BF && freeVector.get(j).getSize() > size)
											{
												freeVector.insertElementAt(freeNode, j);
												break;
											}
											if (managerMode == ManagerMode.WF && freeVector.get(j).getSize() < size)
											{
												freeVector.insertElementAt(freeNode, j);
												break;
											}
											if (j == freeVector.size() - 1)
											{
												freeVector.add(freeNode);
												break;
											}
										}
										for (j = 0; j < freeVector.size(); j++)
										{
											if (freeNode != freeVector.get(j)
													&& freeVector.get(j).getBegin() == memVector.get(i + 1).getBegin())
											{

												freeVector.remove(j);
												break;
											}
										}
									} // 调用结束
										// 合并下空闲区
									memVector.remove(i + 1);
									memVector.get(i).setSize(size);
									memVector.get(i).setName("");
									memVector.get(i).setFlag(false);
								}
								else
								{
									// BF调用
									if (managerMode == ManagerMode.BF || managerMode == ManagerMode.WF)
									{
										FreeNode freeNode = new FreeNode(memVector.get(i).getBegin(),
												memVector.get(i).getSize());
										for (j = 0; j < freeVector.size(); j++)
										{
											if (managerMode == ManagerMode.BF
													&& freeVector.get(j).getSize() > memVector.get(i).getSize())
											{
												freeVector.insertElementAt(freeNode, j);
												break;
											}
											if (managerMode == ManagerMode.WF
													&& freeVector.get(j).getSize() < memVector.get(i).getSize())
											{
												freeVector.insertElementAt(freeNode, j);
												break;
											}
											if (j == freeVector.size() - 1)
											{
												freeVector.add(freeNode);
												break;
											}
										}
									} // 调用结束
										// 释放内存
									memVector.get(i).setName("");
									memVector.get(i).setFlag(false);
								}
							}
							// 最后一块
							else if (i == memVector.size() - 1 && i != 0)
							{

								if (memVector.get(i - 1).isFlag() == false)
								{
									size = memVector.get(i).getSize() + memVector.get(i - 1).getSize();
									// BF调用
									if (managerMode == ManagerMode.BF || managerMode == ManagerMode.WF)
									{
										FreeNode freeNode = new FreeNode(memVector.get(i - 1).getBegin(), size);
										for (j = 0; j < freeVector.size(); j++)
										{
											if (managerMode == ManagerMode.BF && freeVector.get(j).getSize() > size)
											{
												freeVector.insertElementAt(freeNode, j);
												break;
											}
											if (managerMode == ManagerMode.WF && freeVector.get(j).getSize() < size)
											{
												freeVector.insertElementAt(freeNode, j);
												break;
											}
											if (j == freeVector.size() - 1)
											{
												freeVector.add(freeNode);
												break;
											}
										}
										for (j = 0; j < freeVector.size(); j++)
										{
											if (freeNode != freeVector.get(j)
													&& freeVector.get(j).getBegin() == memVector.get(i - 1).getBegin())
											{
												freeVector.remove(j);
												break;
											}
										}
									} // 调用结束
										// 合并上空闲区
									memVector.remove(i);
									memVector.get(i - 1).setSize(size);
								}
								else
								{
									// BF调用
									if (managerMode == ManagerMode.BF || managerMode == ManagerMode.WF)
									{
										FreeNode freeNode = new FreeNode(memVector.get(i).getBegin(),
												memVector.get(i).getSize());
										for (j = 0; j < freeVector.size(); j++)
										{
											if (managerMode == ManagerMode.BF
													&& freeVector.get(j).getSize() > memVector.get(i).getSize())
											{
												freeVector.insertElementAt(freeNode, j);
												break;
											}
											if (managerMode == ManagerMode.WF
													&& freeVector.get(j).getSize() < memVector.get(i).getSize())
											{
												freeVector.insertElementAt(freeNode, j);
												break;
											}
											if (j == freeVector.size() - 1)
											{
												freeVector.add(freeNode);
												break;
											}
										}
									} // 调用结束
										// 释放内存
									memVector.get(i).setName("");
									memVector.get(i).setFlag(false);
								}
							}
							else
							{
								// 释放内存
								memVector.get(i).setName("");
								memVector.get(i).setFlag(false);
							}
							memPanel.repaint();
							// 进程与对应区间关系结束
							bindVector.remove(k);
							k--;
							break;
						}
					}
				}
			}

		}
	}

	public static long getTotalMem()
	{
		return totalMem;
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		// TODO 自动生成的方法存根

	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		int i;
		Point p = e.getPoint();
		for (i = 0; i < memVector.size(); i++)
		{
			if (memVector.get(i).getRect().contains(p))
			{
				attributePanel.setAttribute(memVector.get(i));
				attributePanel.repaint();
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{

	}

	@Override
	public void mouseEntered(MouseEvent e)
	{

	}

	@Override
	public void mouseExited(MouseEvent e)
	{

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see
	 * com.bppleman.memory.MemorySelectorLinstener#didChangeTheMemoryButton(com.
	 * zzl.MemoryManager.ManagerMode)
	 */
	@Override
	public void didChangeTheMemoryButton(ManagerMode mode)
	{
		this.managerMode = mode;
	}
}
