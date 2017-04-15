/**
 * 
 */
package com.bppleman.listener;

/**
 * @author BppleMan
 *
 */
public interface CPUListener
{
	/**
	 * 表示CPU已执行完一次时间片 处于空闲可以执行新的时间片
	 */
	public void cpufree();
}
