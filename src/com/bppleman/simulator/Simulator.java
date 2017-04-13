/**
 * 
 */
package com.bppleman.simulator;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.bppleman.cpu.CPUSimulator;
import com.bppleman.cpu.listener.TableModelListener;
import com.bppleman.process.controller.ProcessController;
import com.bppleman.process.schedule.ProcessScheduler;

/**
 * @author BppleMan
 *
 */
public class Simulator implements ActionListener
{
	private SimulatorView simulatorView;
	private CPUSimulator cpu;

	private ProcessController processController;
	private ProcessScheduler processScheduler;
	private JButton processControllerBtn;
	private JButton processSchedulerBtn;

	private TableModelListener tableModelListener;

	/**
	 * 
	 */
	public Simulator()
	{
		initKenel();
		initView();
	}

	private void initKenel()
	{
		CPUSimulator.initCPUSimulator(1000, 100);
		cpu = CPUSimulator.getInstance();
		cpu.startTimerTask();
		processController = new ProcessController();
		tableModelListener = processController.getTableModelListener();
		processScheduler = new ProcessScheduler(tableModelListener);
		processScheduler.start();
		cpu.addTableModelListener(tableModelListener);
	}

	private void initView()
	{
		simulatorView = new SimulatorView();
		initButton();
		simulatorView.setVisible(true);
	}

	private void initButton()
	{
		Dimension d = new Dimension(150, 150);
		processControllerBtn = new JButton("进程管理器");
		processControllerBtn.addActionListener(this);
		processControllerBtn.setPreferredSize(d);
		simulatorView.add(processControllerBtn);

		processSchedulerBtn = new JButton("进程调度器");
		processSchedulerBtn.addActionListener(this);
		processSchedulerBtn.setPreferredSize(d);
		simulatorView.add(processSchedulerBtn);
	}

	public static void main(String[] args)
	{
		new Simulator();
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
		JButton btn = (JButton) e.getSource();
		if (btn == processControllerBtn)
			processController.setProcessControllerViewVisible(true);
		if (btn == processSchedulerBtn)
			processScheduler.setProcessSchedulerViewVisible(true);
	}
}
