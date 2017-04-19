package com.bppleman.processmanagement.cpu;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

import com.bppleman.listener.TableModelListener;
import com.bppleman.processmanagement.process.ProcessSimulator;

public class CPUSimulator
{

	/**
	 * 表示CPU模拟器是否初始化
	 */
	private static volatile boolean isInitial = false;
	/**
	 * 表示CPU时钟频率
	 */
	private int clockFrequency;
	/**
	 * 表示CPU时间片长度
	 */
	private int timeSlice;

	/**
	 * 一个单例的自身引用，外界不可访问，需要先初始化
	 */
	private static CPUSimulator instance;

	/**
	 * 表示CPU是否空闲
	 */
	private volatile boolean isCPUFree = true;
	/**
	 * 时钟中断计数器
	 */
	private volatile long count = 0;

	/**
	 * 计时器
	 */
	private volatile long TIME = 0;
	/**
	 * 时钟器和时钟体
	 */
	private Timer timer;
	private TimerTask timerTask;

	/*
	 * 进程数据更新代理器
	 */
	private TableModelListener tableModelListener;

	/**
	 * @param clockFrequency
	 *            时钟频率
	 * @param timeSlice
	 *            时间片长度
	 */
	public static void initCPUSimulator(int clockFrequency, int timeSlice)
	{
		/**
		 * 利用双重校验锁，是instance的初始化是同步线程安全的
		 */
		if (instance == null)
		{
			synchronized (CPUSimulator.class)
			{
				if (instance == null)
				{
					instance = new CPUSimulator(clockFrequency, timeSlice);
					isInitial = true;
				}
			}
		}
	}

	/**
	 * @return 返回一个单例CPUSimulator
	 */
	public static CPUSimulator getInstance()
	{
		if (isInitial)
			return instance;
		else
			throw new NullPointerException("this CPUSimulator hadn't initial");
	}

	/**
	 * @param clockFrequency
	 * @param timeSlice
	 * @param executionQueue
	 */
	private CPUSimulator(int clockFrequency, int timeSlice)
	{
		this.clockFrequency = clockFrequency;
		this.timeSlice = timeSlice;
		initTimerTask();
	}

	/**
	 * 初始化时钟体
	 */
	public void initTimerTask()
	{
		timer = new Timer();
		timerTask = new TimerTask()
		{

			@Override
			public void run()
			{
				TIME++;
				count++;
			}
		};
		timer.schedule(timerTask, 0, timeSlice);
	}

	/**
	 * 开启时钟
	 */
	public void startTimerTask()
	{
		timerTask.run();
	}

	/**
	 * @return 返回一个当前时间
	 */
	public long getTime()
	{
		return TIME;
	}

	/**
	 * @return 返回时钟频率
	 */
	public int getClockFrequency()
	{
		return clockFrequency;
	}

	/**
	 * 请求执行
	 * 
	 * @param processSimulator
	 *            请求者
	 * 
	 * @param readyQueue
	 *            就绪队列
	 * 
	 * @param blockingQueue
	 *            阻塞队列
	 */
	public void requestRun(ProcessSimulator processSimulator, LinkedBlockingQueue<ProcessSimulator> readyQueue)
	{
		// 此时CPU将不空闲
		isCPUFree = false;
		// 计数器用于中断一个时间片
		count = 0;
		processSimulator.setExecution();
		while (count < timeSlice)
		{
			processSimulator.hadExecutionTimes++;
			if (processSimulator.hadExecutionTimes == processSimulator.needExecutionTimes)
			{
				processSimulator.setFinish();
				readyQueue.remove(processSimulator);
				tableModelListener.rowValueChanged(processSimulator);
				isCPUFree = true;
				return;
			}
			long n = 0;
			while (n < clockFrequency)
				n++;
			tableModelListener.rowValueChanged(processSimulator);
			// count++;
		}
		processSimulator.setReady();
		isCPUFree = true;
	}

	public void addTableModelListener(TableModelListener tableModelListener)
	{
		this.tableModelListener = tableModelListener;
	}

	public boolean isCPUFree()
	{
		return isCPUFree;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.lang.Thread#run()
	 */
	// public void run()
	// {
	// // super.run();
	// while (isInitial)
	// {
	// while (!executionQueue.isEmpty())
	// {
	// ProcessSimulator now = executionQueue.getHead();
	// now.setHadExecutionTimes(now.getHadExecutionTimes() + clockFrequency /
	// timeSlice);
	// executionQueue.add(now);
	// executionQueue.delete();
	// try
	// {
	// sleep(timeSlice);
	// }
	// catch (InterruptedException e)
	// {
	// e.printStackTrace();
	// }
	// }
	// try
	// {
	// sleep(timeSlice);
	// }
	// catch (InterruptedException e)
	// {
	// e.printStackTrace();
	// }
	// }
	// }

}
