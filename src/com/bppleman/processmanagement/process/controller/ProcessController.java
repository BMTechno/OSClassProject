/**
 * 
 */
package com.bppleman.processmanagement.process.controller;

import javax.swing.JScrollPane;

import com.bppleman.listener.TableModelListener;
import com.bppleman.processmanagement.process.ProcessSimulator;

/**
 * @author BppleMan
 *
 */
public class ProcessController implements TableModelListener
{
	private ProcessControllerView processControllerView;
	private JScrollPane scrollPane;
	private ProcessTable processTable;
	private MyTableModel tableModel;
	private String[] columnNames = { "进程名称", "到达时刻", "需执行次数", "已执行次数", "进程优先权", "需申请内存", "进程状态" };

	/**
	 * 
	 */
	public ProcessController()
	{
		processControllerView = new ProcessControllerView("进程管理器");
		processTable = new ProcessTable(null, columnNames);
		tableModel = (MyTableModel) processTable.getModel();
		scrollPane = new JScrollPane(processTable);
		processControllerView.add(scrollPane);
	}

	public void setProcessControllerViewVisible(boolean visible)
	{
		processControllerView.setVisible(visible);
	}

	public ProcessTable getProcessTable()
	{
		return processTable;
	}

	public void setProcessTable(ProcessTable processTable)
	{
		this.processTable = processTable;
	}

	public TableModelListener getTableModelListener()
	{
		return this;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see
	 * com.bppleman.cpu.listener.TableModelListener#addNewRowValue(com.bppleman.
	 * process.ProcessSimulator)
	 */
	@Override
	public void newRowValue(ProcessSimulator process)
	{
		tableModel.addRow(process);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see
	 * com.bppleman.cpu.listener.TableModelListener#updateRowValue(com.bppleman.
	 * process.ProcessSimulator)
	 */
	@Override
	public void rowValueChanged(ProcessSimulator process)
	{
		tableModel.updateValue(process);
	}

}
