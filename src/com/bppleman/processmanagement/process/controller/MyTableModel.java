/**
 * 
 */
package com.bppleman.processmanagement.process.controller;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import com.bppleman.processmanagement.process.ProcessSimulator;

/**
 * @author BppleMan
 *
 */
public class MyTableModel extends DefaultTableModel
{
	private String[] columnNames;
	private ProcessTable processTable;

	public MyTableModel(Object[][] data, String[] columnNames, ProcessTable processTable)
	{
		super(data, columnNames);
		this.columnNames = columnNames;
		this.processTable = processTable;
	}

	public void addRow(ProcessSimulator process)
	{
		synchronized (getDataVector())
		{
			Object[] rowData = new Object[getColumnCount()];
			rowData[0] = process.getName();
			rowData[1] = process.getArriveTime();
			rowData[2] = process.getNeedExecutionTimes();
			rowData[3] = process.getHadExecutionTimes();
			rowData[4] = process.getPriority();
			rowData[5] = process.getNeedMemories();
			rowData[6] = process.getState();
			addRow(rowData);
		}
		processTable.validate();
		processTable.repaint();
	}

	public void updateValue(ProcessSimulator process)
	{
		synchronized (getDataVector())
		{
			Object name = process.getName();
			Object arrivetime = process.getArriveTime();
			Object needexetime = process.getNeedExecutionTimes();
			Object hadexetime = process.getHadExecutionTimes();
			Object priority = process.getPriority();
			Object needmem = process.getNeedMemories();
			Object state = process.getState();
			int row = 0;
			Vector<Object> vector = getDataVector();
			for (Object object : vector)
			{
				Vector<Object> v = (Vector<Object>) object;
				if (v.elementAt(0).equals(name))
				{
					row = vector.indexOf(object);
					break;
				}
			}
			setValueAt(name, row, findColumn(columnNames[0]));
			setValueAt(arrivetime, row, findColumn(columnNames[1]));
			setValueAt(needexetime, row, findColumn(columnNames[2]));
			setValueAt(hadexetime, row, findColumn(columnNames[3]));
			setValueAt(priority, row, findColumn(columnNames[4]));
			setValueAt(needmem, row, findColumn(columnNames[5]));
			setValueAt(state, row, findColumn(columnNames[6]));
		}
		processTable.validate();
		processTable.repaint();
	}

}
