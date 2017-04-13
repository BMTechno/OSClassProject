/**
 * 
 */
package com.bppleman.process.controller;

import javax.swing.JTable;
import javax.swing.table.TableModel;

/**
 * @author BppleMan
 *
 */
public class ProcessTable extends JTable
{
	MyTableModel myTableModel;

	/**
	 * 
	 */
	public ProcessTable(Object[][] data, Object[] columnNames)
	{
		super();
		myTableModel = new MyTableModel(data, columnNames);
		setModel(myTableModel);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see javax.swing.JTable#getModel()
	 */
	@Override
	public TableModel getModel()
	{
		return myTableModel;
	}
}
