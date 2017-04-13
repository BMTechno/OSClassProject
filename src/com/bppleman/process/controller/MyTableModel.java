/**
 * 
 */
package com.bppleman.process.controller;

import javax.swing.table.DefaultTableModel;

import com.bppleman.process.ProcessSimulator;

/**
 * @author BppleMan
 *
 */
public class MyTableModel extends DefaultTableModel
{
	public MyTableModel(Object[][] data, Object[] columnNames)
	{
		super(data, columnNames);
	}

	public void addRow(ProcessSimulator process)
	{
		Object[] rowData = new Object[getColumnCount()];
		rowData[0] = process.getName();
		rowData[1] = process.getArriveTime();
		rowData[2] = process.getNeedExecutionTimes();
		rowData[3] = process.getHadExecutionTimes();
		rowData[4] = process.getState();
		addRow(rowData);
	}

	public void updateValue(ProcessSimulator process)
	{
		Object[] rowData = new Object[getColumnCount()];
		rowData[0] = process.getName();
		rowData[1] = process.getArriveTime();
		rowData[2] = process.getNeedExecutionTimes();
		rowData[3] = process.getHadExecutionTimes();
		rowData[4] = process.getState();

	}

}
