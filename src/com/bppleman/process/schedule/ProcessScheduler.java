/**
 * 
 */
package com.bppleman.process.schedule;

import java.util.concurrent.PriorityBlockingQueue;

import com.bppleman.cpu.CPUSimulator;
import com.bppleman.cpu.listener.TableModelListener;
import com.bppleman.process.ProcessSimulator;

/**
 * @author BppleMan
 *
 */
public class ProcessScheduler extends Thread
{
	private ProcessSchedulerView processSchedulerView;
	public PriorityBlockingQueue<ProcessSimulator> readyQueue;
	public PriorityBlockingQueue<ProcessSimulator> blockQueue;
	private TableModelListener tableModelListener;
	private CPUSimulator cpu;
	private Thread checkProcessThread;
	private volatile boolean isRun = false;

	/**
	 * 
	 */
	public ProcessScheduler(TableModelListener tableModelListener)
	{
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
		initView();
		initThread();
	}

	public void initQueue()
	{
		readyQueue = new PriorityBlockingQueue<>(2048);
		blockQueue = new PriorityBlockingQueue<>(2048);
	}

	public void initView()
	{
		processSchedulerView = new ProcessSchedulerView("进程调度器", tableModelListener, this);
	}

	public void initThread()
	{
		checkProcessThread = new Thread(new Runnable()
		{

			@Override
			public void run()
			{

			}
		});
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

	public PriorityBlockingQueue<ProcessSimulator> getReadyQueue()
	{
		return readyQueue;
	}

	public PriorityBlockingQueue<ProcessSimulator> getBlockQueue()
	{
		return blockQueue;
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
			try
			{
				sleep(cpu.getClockFrequency() / 1000);
			}
			catch (InterruptedException e1)
			{
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
			if (cpu.isCPUFree())
			{
				if (readyQueue.isEmpty())
				{
					/*
					 * 如果就绪队列空且阻塞队列不为空 则从阻塞队列中取出一个优先级最高的进程 加入至就绪队列中
					 */
					if (!blockQueue.isEmpty())
					{
						while (!blockQueue.isEmpty())
						{
							try
							{
								blockQueue.element().setReady();
								readyQueue.add(blockQueue.take());
							}
							catch (InterruptedException e)
							{
								e.printStackTrace();
							}
						}
					}
					else
					{
						continue;
					}
				}
				try
				{
					/*
					 * 从就绪队列中取出优先级最高的进程，向CPU申请执行
					 */
					ProcessSimulator process = readyQueue.take();
					cpu.requestRun(process, readyQueue, blockQueue);
					for (ProcessSimulator processSimulator : readyQueue)
					{
						processSimulator.setPriority(processSimulator.getPriority() + 1);
					}
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

}
