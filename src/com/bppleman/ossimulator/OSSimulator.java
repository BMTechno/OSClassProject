/**
 * 
 */
package com.bppleman.ossimulator;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.bppleman.listener.TableModelListener;
import com.bppleman.processmanagement.cpu.CPUSimulator;
import com.bppleman.processmanagement.process.controller.ProcessController;
import com.bppleman.processmanagement.process.schedule.ProcessScheduler;
import com.zzl.MemoryManager;
import com.zzl.MemoryManager.ManagerMode;

/**
 * @author BppleMan
 *
 */
public class OSSimulator implements ActionListener
{
	private OSSimulatorView oSSimulatorView;
	private CPUSimulator cpu;
	private MemoryManager memoryManager;

	private ProcessController processController;
	private ProcessScheduler processScheduler;
	private JButton processControllerBtn;
	private JButton processSchedulerBtn;

	private TableModelListener tableModelListener;

	/**
	 * 
	 */
	public OSSimulator()
	{
		initKenel();
		initView();
	}

	private void initKenel()
	{
		CPUSimulator.initCPUSimulator(1000, 100);
		cpu = CPUSimulator.getInstance();
		cpu.startTimerTask();
		memoryManager = new MemoryManager(ManagerMode.FF);
		processController = new ProcessController();
		tableModelListener = processController.getTableModelListener();
		processScheduler = new ProcessScheduler(memoryManager, tableModelListener);
		processScheduler.start();
		cpu.addTableModelListener(tableModelListener);
		memoryManager.start();
	}

	private void initView()
	{
		oSSimulatorView = new OSSimulatorView();
		initButton();
		oSSimulatorView.setVisible(true);
	}

	private void initButton()
	{
		Dimension d = new Dimension(150, 150);
		processControllerBtn = new JButton("进程管理器");
		processControllerBtn.addActionListener(this);
		processControllerBtn.setPreferredSize(d);
		oSSimulatorView.add(processControllerBtn);

		processSchedulerBtn = new JButton("进程调度器");
		processSchedulerBtn.addActionListener(this);
		processSchedulerBtn.setPreferredSize(d);
		oSSimulatorView.add(processSchedulerBtn);
	}

	public static void main(String[] args)
	{
		new OSSimulator();
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
