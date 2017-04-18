/**
 * 
 */
package com.bppleman.processmanagement.process.schedule;

import java.util.Vector;

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
	public Vector<ProcessSimulator> readyQueue;
	public Vector<ProcessSimulator> blockQueue;
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

	public void initThread()
	{
		requestMemoryThread = new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				while (true)
				{
					if (!blockQueue.isEmpty())
					{
						synchronized (blockQueue)
						{
							int n = blockQueue.size();
							for (int i = 0; i < n; i++)
							{
								ProcessSimulator processSimulator = blockQueue.get(i);
								System.out.println(processSimulator);
								if (memoryManager.requestMem(processSimulator) == true)
								{
									System.out.println(processSimulator.getName() + "申请到内存");
									readyQueue.addElement(processSimulator);
									processSimulator.setInQueue(true);
									blockQueue.remove(processSimulator);
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
		readyQueue = new Vector<>();
		blockQueue = new Vector<>();
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
			// try
			// {
			// sleep(cpu.getClockFrequency() / 1000);
			// }
			// catch (InterruptedException e1)
			// {
			// // TODO 自动生成的 catch 块
			// e1.printStackTrace();
			// }
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
						p.setPriority(p.getPriority() + 1);
				}
				cpu.requestRun(process, readyQueue);
				/*
				 * 每完成一个时间片优先权－3
				 */
				process.setPriority(process.getPriority() - 8);
			}
		}
	}

	/*
	 * 取出最大优先权的进程
	 */
	public ProcessSimulator getMaxPriority()
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

	/*
	 * 计算优先权
	 */
	public void calcuPriority()
	{
		ProcessSimulator p1 = readyQueue.get(0);
		long minTotalTimes = p1.getNeedExecutionTimes();
		ProcessSimulator p2 = readyQueue.get(0);
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
