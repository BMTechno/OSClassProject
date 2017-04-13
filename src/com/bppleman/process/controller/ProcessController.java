/**
 * 
 */
package com.bppleman.process.controller;

import java.util.Vector;

import javax.swing.JScrollPane;

import com.bppleman.cpu.listener.TableModelListener;
import com.bppleman.process.ProcessSimulator;

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
	private String[] columnNames = { "进程名称", "到达时刻", "需执行次数", "已计算次数", "进程状态" };

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
		Object name = process.getName();
		Object arrivetime = process.getArriveTime();
		Object needexetime = process.getNeedExecutionTimes();
		Object hadexetime = process.getHadExecutionTimes();
		Object state = process.getState();
		int row = 0;
		Vector<Object> vector = tableModel.getDataVector();
		for (Object object : vector)
		{
			Vector<Object> v = (Vector<Object>) object;
			if (v.elementAt(0).equals(name))
			{
				row = vector.indexOf(object);
				break;
			}
		}
		tableModel.setValueAt(name, row, tableModel.findColumn("进程名称"));
		tableModel.setValueAt(arrivetime, row, tableModel.findColumn("到达时刻"));
		tableModel.setValueAt(needexetime, row, tableModel.findColumn("需执行次数"));
		tableModel.setValueAt(hadexetime, row, tableModel.findColumn("已计算次数"));
		tableModel.setValueAt(state, row, tableModel.findColumn("进程状态"));
	}

}
