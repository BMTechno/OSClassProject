/**
 * 
 */
package com.bppleman.listener;

import com.bppleman.processmanagement.process.ProcessSimulator;

/**
 * @author BppleMan
 *
 */
public interface TableModelListener
{
	/**
	 * @param process
	 *            新的行数据
	 */
	public void newRowValue(ProcessSimulator process);

	/**
	 * @param process
	 *            行数据变更
	 */
	public void rowValueChanged(ProcessSimulator process);
}
