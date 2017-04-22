/**
 * 
 */
package com.bppleman.processmanagement.process;

import com.bppleman.listener.TableModelListener;
import com.bppleman.processmanagement.process.schedule.ProcessPanel;

/**
 * @author BppleMan
 *
 */
/**
 * @author BppleMan
 *
 */
/**
 * @author BppleMan
 *
 */
public class ProcessSimulator implements Comparable<ProcessSimulator>
{

	private TableModelListener tableModelListener;

	/*
	 * 进程状态枚举变量
	 */
	public enum STATE
	{
		EXECUTION, READY, BLOCK, FINISH, CRASH
	}

	/**
	 * 表示该进程需要请求CPU执行的次数
	 */
	public long needExecutionTimes;

	/*
	 * 表示该进程已经执行的次数
	 */
	public long hadExecutionTimes;

	/**
	 * 表示该进程需要请求的内存资源
	 */
	public long needMemories;
	/*
	 * 进程名称
	 */
	public String name;

	/*
	 * 表示进程的状态
	 */
	public STATE state;

	/*
	 * 进程id
	 */
	public int id = 0;

	/**
	 * 静态变量作为进程数量计数器
	 */
	public static int count = 0;

	/*
	 * 表示进程优先权
	 */
	public int priority;

	/**
	 * 表示持有本进程的processpanel
	 */
	public ProcessPanel processPanel;

	/**
	 * 到达时刻
	 */
	public long arriveTime;

	/**
	 * 表示是否被送进就绪队列
	 */
	public boolean isInQueue = false;

	/**
	 * @param processPanel
	 *            表示持有本进程的processpanel
	 * @param id
	 *            进程id
	 */
	public ProcessSimulator(ProcessPanel processPanel, int id, TableModelListener tableModelListener)
	{
		this.processPanel = processPanel;
		this.name = "进程" + id;
		this.id = id;
		this.needExecutionTimes = -1;
		this.needMemories = -1;
		this.tableModelListener = tableModelListener;
		this.state = STATE.READY;
	}

	/**
	 * @param needExecutionTimes
	 *            表示该进程需要请求CPU执行的次数
	 * @param needMemories
	 *            表示该进程需要请求的内存资源
	 */
	public void setProperty(long needExecutionTimes, long needMemories, long arriveTime)
	{
		this.needExecutionTimes = needExecutionTimes;
		this.needMemories = needMemories;
		this.arriveTime = arriveTime;
	}

	/**
	 * 设置进程为崩溃状态
	 */
	public void setCrash()
	{
		this.state = STATE.CRASH;
		tableModelListener.rowValueChanged(this);
	}

	/**
	 * 设置进程为就绪状态
	 */
	public void setReady()
	{
		this.state = STATE.READY;
		tableModelListener.rowValueChanged(this);
	}

	/**
	 * 设置进程为执行状态
	 */
	public void setExecution()
	{
		this.state = STATE.EXECUTION;
		tableModelListener.rowValueChanged(this);
	}

	/**
	 * 设置进程为完成状态
	 */
	public void setFinish()
	{
		this.state = STATE.FINISH;
		tableModelListener.rowValueChanged(this);
	}

	/*
	 * 设置进程为阻塞状态
	 */
	public void setBlock()
	{
		this.state = STATE.BLOCK;
		tableModelListener.rowValueChanged(this);
	}

	/**
	 * @return 获取进程状态
	 */
	public STATE getState()
	{
		return state;
	}

	public long getNeedMemories()
	{
		return needMemories;
	}

	public void setNeedMemories(long needMemories)
	{
		this.needMemories = needMemories;
	}

	public int getPriority()
	{
		return priority;
	}

	public void setPriority(Integer priority)
	{
		this.priority = priority;
	}

	public ProcessPanel getProcessPanel()
	{
		return processPanel;
	}

	public void setProcessPanel(ProcessPanel processPanel)
	{
		this.processPanel = processPanel;
	}

	public long getArriveTime()
	{
		return arriveTime;
	}

	public void setArriveTime(long arriveTime)
	{
		this.arriveTime = arriveTime;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public long getNeedExecutionTimes()
	{
		return needExecutionTimes;
	}

	public void setNeedExecutionTimes(int needExecutionTimes)
	{
		this.needExecutionTimes = needExecutionTimes;
	}

	public long getHadExecutionTimes()
	{
		return hadExecutionTimes;
	}

	public void setHadExecutionTimes(int hadExecutionTimes)
	{
		this.hadExecutionTimes = hadExecutionTimes;
	}

	public boolean isInQueue()
	{
		return isInQueue;
	}

	public void setInQueue(boolean isInQueue)
	{
		this.isInQueue = isInQueue;
	}

	@Override
	public String toString()
	{
		return "ProcessSimulator [needExecutionTimes=" + needExecutionTimes + ", hadExecutionTimes=" + hadExecutionTimes
				+ ", needMemories=" + needMemories + ", name=" + name + ", state=" + state + ", id=" + id
				+ ", priority=" + priority + ", arriveTime=" + arriveTime + ", processPanel=" + processPanel + "]";
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(ProcessSimulator o)
	{
		return this.priority > o.priority ? -1 : 1;
	}
}
