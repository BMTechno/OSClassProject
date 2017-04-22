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
		myTableModel = new MyTableModel(data, columnNames, this);
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
		DefaultTableCellRenderer finishRenderer = new DefaultTableCellRenderer();
		finishRenderer.setBackground(new Color(239, 174, 77, 255));
		DefaultTableCellRenderer blockRenderer = new DefaultTableCellRenderer();
		blockRenderer.setBackground(new Color(227, 121, 104, 255));
		if (getValueAt(row, column).equals(STATE.FINISH))
			return finishRenderer;
		if (getValueAt(row, column).equals(STATE.BLOCK))
			return blockRenderer;
		if (row % 2 != 0)
			return rowRenderer;
		return super.getCellRenderer(row, column);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see javax.swing.JTable#isCellSelected(int, int)
	 */
	@Override
	public boolean isCellSelected(int row, int column)
	{
		// TODO 自动生成的方法存根
		return false;
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
