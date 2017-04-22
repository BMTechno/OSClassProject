/**
 * 
 */
package com.bppleman.processmanagement.process.schedule;

import java.util.concurrent.LinkedBlockingQueue;

import com.bppleman.listener.TableModelListener;
import com.bppleman.processmanagement.cpu.CPUSimulator;
import com.bppleman.processmanagement.process.ProcessSimulator;
import com.zzl.MemoryManager;

/**
 * @author BppleMan
 *
 */
public class ProcessScheduler extends Thread
{
	private ProcessSchedulerView processSchedulerView;
	public LinkedBlockingQueue<ProcessSimulator> readyQueue;
	public LinkedBlockingQueue<ProcessSimulator> blockQueue;
	private TableModelListener tableModelListener;
	private CPUSimulator cpu;
	private MemoryManager memoryManager;
	private volatile boolean isRun = false;

	private Thread requestMemoryThread;

	/**
	 * 
	 */
	public ProcessScheduler(MemoryManager memoryManager, TableModelListener tableModelListener)
	{
		this.memoryManager = memoryManager;
		this.tableModelListener = tableModelListener;
		try
		{
			cpu = CPUSimulator.getInstance();
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();
		}
		initQueue();
		initThread();
		initView();
	}

	public Thread getRequestThread()
	{
		return requestMemoryThread;
	}

	public void initThread()
	{
		requestMemoryThread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				while (true)
				{
					synchronized (requestMemoryThread)
					{
						if (blockQueue.isEmpty())
						{
							try
							{
								requestMemoryThread.wait();
							}
							catch (InterruptedException e)
							{
								e.printStackTrace();
							}
						}
					}
					while (!blockQueue.isEmpty())
					{
						synchronized (blockQueue)
						{
							ProcessSimulator processSimulator = blockQueue.peek();
							if (processSimulator.getNeedMemories() > memoryManager.getTotalMem())
							{
<<<<<<< HEAD
								ProcessSimulator processSimulator = blockQueue.peek();

								if (memoryManager.requestMem(processSimulator) == true)
								{
									try
									{
										readyQueue.put(processSimulator);
										processSimulator.setReady();
										processSimulator.setInQueue(true);
										blockQueue.remove(processSimulator);
									}
									catch (InterruptedException e)
									{
										e.printStackTrace();
									}
								}
=======
								System.out.println("to large");
								processSimulator.setCrash();
								blockQueue.remove(processSimulator);
								break;
>>>>>>> refs/remotes/origin/master
							}
							if (memoryManager.requestMem(processSimulator) == true)
							{
								try
								{
									readyQueue.put(processSimulator);
									processSimulator.setReady();
									processSimulator.setInQueue(true);
									blockQueue.remove(processSimulator);
								}
								catch (InterruptedException e)
								{
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		});
		requestMemoryThread.start();

	}

	public void initQueue()
	{
		readyQueue = new LinkedBlockingQueue<>();
		blockQueue = new LinkedBlockingQueue<>();
	}

	public void initView()
	{
		processSchedulerView = new ProcessSchedulerView("进程调度器", tableModelListener, this);
	}

	public void setProcessSchedulerViewVisible(boolean visible)
	{
		processSchedulerView.setVisible(visible);
	}

	public ProcessSchedulerView getProcessSchedulerView()
	{
		return processSchedulerView;
	}

	public void setProcessSchedulerView(ProcessSchedulerView processSchedulerView)
	{
		this.processSchedulerView = processSchedulerView;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.lang.Thread#start()
	 */
	@Override
	public synchronized void start()
	{
		isRun = true;
		super.start();
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run()
	{
		while (isRun)
		{
			synchronized (cpu)
			{
				if (cpu.isCPUFree())
				{
					if (readyQueue.isEmpty())
						continue;
					calcuPriority();
					/*
					 * 从就绪队列中取出优先级最高的进程，向CPU申请执行
					 */
					ProcessSimulator process = getMaxPriority();
					/*
					 * 每等待一个时间片优先权＋1
					 */
					for (ProcessSimulator p : readyQueue)
					{
						if (p != process)
						{
							p.setPriority(p.getPriority() + 1);
							tableModelListener.rowValueChanged(p);
						}
					}
					cpu.requestRun(process, readyQueue);
					/*
					 * 每完成一个时间片优先权－3
					 */
					process.setPriority(process.getPriority() - 10);
					tableModelListener.rowValueChanged(process);
				}
				else
				{
					try
					{
						cpu.wait();
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}

	/*
	 * 取出最大优先权的进程
	 */
	public ProcessSimulator getMaxPriority()
	{
		synchronized (blockQueue)
		{
			int max = 1 << 31;
			ProcessSimulator result = null;
			for (ProcessSimulator processSimulator : readyQueue)
			{
				if (processSimulator.getPriority() > max)
				{
					max = processSimulator.getPriority();
					result = processSimulator;
				}
			}
			return result;
		}
	}

	/*
	 * 计算优先权
	 */
	public void calcuPriority()
	{
		synchronized (blockQueue)
		{
			ProcessSimulator p1 = readyQueue.peek();
			long minTotalTimes = p1.getNeedExecutionTimes();
			ProcessSimulator p2 = readyQueue.peek();
			long minNeedTimes = p2.getNeedExecutionTimes() - p2.getHadExecutionTimes();
			for (ProcessSimulator process : readyQueue)
			{
				// 短进程优先权＋1
				if (process.getNeedExecutionTimes() < minTotalTimes)
				{
					minTotalTimes = process.getNeedExecutionTimes();
					p1 = process;
				}
				// 最快结束进程优先权＋1
				if (process.getNeedExecutionTimes() - process.getHadExecutionTimes() < minNeedTimes)
				{
					minNeedTimes = process.getNeedExecutionTimes() - process.getHadExecutionTimes();
					p2 = process;
				}
			}
			p1.setPriority(p1.getPriority() + 1);
			p2.setPriority(p2.getPriority() + 1);
		}
	}
}
