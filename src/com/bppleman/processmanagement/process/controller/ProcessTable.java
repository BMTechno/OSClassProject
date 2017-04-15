/**
 * 
 */
package com.bppleman.processmanagement.process.controller;

import java.awt.Color;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import com.bppleman.processmanagement.process.ProcessSimulator.STATE;

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
	public ProcessTable(Object[][] data, String[] columnNames)
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

	/*
	 * （非 Javadoc）
	 * 
	 * @see javax.swing.JTable#getCellRenderer(int, int)
	 */
	@Override
	public TableCellRenderer getCellRenderer(int row, int column)
	{
		DefaultTableCellRenderer rowRenderer = new DefaultTableCellRenderer();
		rowRenderer.setBackground(new Color(245, 245, 245, 255));
		DefaultTableCellRenderer stateRenderer = new DefaultTableCellRenderer();
		stateRenderer.setBackground(new Color(239, 174, 77, 255));
		if (getValueAt(row, column).equals(STATE.FINISH))
			return stateRenderer;
		if (row % 2 != 0)
			return rowRenderer;
		return super.getCellRenderer(row, column);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see javax.swing.JTable#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int row, int column)
	{
		return false;
	}
}
