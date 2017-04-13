/**
 * 
 */
package com.bppleman.process.schedule;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import com.bppleman.cpu.CPUSimulator;
import com.bppleman.cpu.listener.TableModelListener;
import com.bppleman.process.ProcessSimulator;

/**
 * @author BppleMan
 *
 */
public class ProcessPanel extends JPanel implements ActionListener
{
	private JLabel head;
	private JLabel needExecutionTimesLabel;
	private JLabel needExecutionMemoryLabel;
	private JTextField needExecutionTimesText;
	private JTextField needExecutionMemoryText;
	private JButton requestMemoryBtn;

	private ProcessSimulator process;

	private ActionListener actionListener;
	private TableModelListener tableModelListener;

	private ProcessScheduler processScheduler;

	private CPUSimulator cpu;

	/**
	 * 
	 */
	public ProcessPanel(TableModelListener tableModelListener, ProcessScheduler processScheduler)
	{
		this.cpu = CPUSimulator.getInstance();
		this.tableModelListener = tableModelListener;
		this.processScheduler = processScheduler;
		setLayout(null);
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		int left = 10;
		int top = 10;
		int lwidth = 130;
		int twidth = 70;
		int bwidth = 120;
		int hwidth = 100;
		int height = 30;
		process = new ProcessSimulator(this, ProcessSimulator.count++, tableModelListener);
		setPreferredSize(new Dimension(left + lwidth + twidth + left, top + height * 4 + top));

		head = new JLabel(process.name);
		head.setHorizontalAlignment(JLabel.CENTER);
		head.setBounds((int) ((getPreferredSize().getWidth() - hwidth) / 2), top, hwidth, height);

		needExecutionTimesLabel = new JLabel("所需申请执行次数:");
		needExecutionTimesLabel.setBounds(left, top + height, lwidth, height);

		needExecutionTimesText = new JTextField();
		needExecutionTimesText.setBounds(left + lwidth, top + height, twidth, height);

		needExecutionMemoryLabel = new JLabel("所需申请内存:");
		needExecutionMemoryLabel.setBounds(left, top + height * 2, lwidth, height);

		needExecutionMemoryText = new JTextField();
		needExecutionMemoryText.setBounds(left + lwidth, top + height * 2, twidth, height);

		requestMemoryBtn = new JButton("送入就绪队列");
		requestMemoryBtn.setBounds((int) ((getPreferredSize().getWidth() - bwidth) / 2), top + height * 3, bwidth,
				height);
		requestMemoryBtn.addActionListener(this);

		add(head);
		add(needExecutionTimesLabel);
		add(needExecutionTimesText);
		add(needExecutionMemoryLabel);
		add(needExecutionMemoryText);
		add(requestMemoryBtn);
	}

	public ProcessSimulator getProcess()
	{
		return process;
	}

	public void setProcess(ProcessSimulator process)
	{
		this.process = process;
	}

	public JTextField getNeedExecutionTimesText()
	{
		return needExecutionTimesText;
	}

	public void setNeedExecutionTimesText(String value)
	{
		this.needExecutionTimesText.setText(value);
	}

	public JTextField getNeedExecutionMemoryText()
	{
		return needExecutionMemoryText;
	}

	public void setNeedExecutionMemoryText(String value)
	{
		this.needExecutionMemoryText.setText(value);
	}

	public JButton getRequestMemoryBtn()
	{
		return requestMemoryBtn;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{

		String str = needExecutionTimesText.getText();
		long times = Long.valueOf(str);
		long mem = 0;
		process.setProperty(times, mem, cpu.getTime());
		tableModelListener.newRowValue(process);
		process.setReady();
		processScheduler.readyQueue.add(process);
	}

}
