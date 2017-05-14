/**
 * 
 */
package com.bppleman.ossimulator;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.bppleman.listener.TableModelListener;
import com.bppleman.memory.MemorySelector;
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
	private MemorySelector memorySelector;

	private ProcessController processController;
	private ProcessScheduler processScheduler;
	private JButton processControllerBtn;
	private JButton processSchedulerBtn;
	private JButton memoryManageBtn;
	private JButton memorySelectorBtn;

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
		/*
		 * 参数1:CPU主频 单位：HZ 参数2:CPU时间片大小 单位：ms
		 */
		CPUSimulator.initCPUSimulator(1000, 10);
		cpu = CPUSimulator.getInstance();
		cpu.startTimerTask();
		memoryManager = new MemoryManager(ManagerMode.WF);
		processController = new ProcessController();
		tableModelListener = processController.getTableModelListener();
		processScheduler = new ProcessScheduler(memoryManager, tableModelListener);
		processScheduler.start();
		cpu.addTableModelListener(tableModelListener);
		memoryManager.start();
		memorySelector = new MemorySelector(memoryManager.getManagerMode(), memoryManager);
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

		memoryManageBtn = new JButton("内存管理器");
		memoryManageBtn.addActionListener(this);
		memoryManageBtn.setPreferredSize(d);
		oSSimulatorView.add(memoryManageBtn);

		memorySelectorBtn = new JButton("内存算法选择器");
		memorySelectorBtn.addActionListener(this);
		memorySelectorBtn.setPreferredSize(d);
		oSSimulatorView.add(memorySelectorBtn);
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
		if (btn == memoryManageBtn)
			memoryManager.setMemoryManagerViewVisible(true);
		if (btn == memorySelectorBtn)
			memorySelector.setMemorySelectorViewVisible(true, memoryManager.getManagerMode());
	}
}
