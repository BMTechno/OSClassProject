/**
 * 
 */
package com.bppleman.process.schedule;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.bppleman.cpu.listener.TableModelListener;

/**
 * @author BppleMan
 *
 */
public class ProcessSchedulerView extends JFrame implements ActionListener
{
	private Toolkit tk = Toolkit.getDefaultToolkit();
	public int screenWidth;
	public int screenHeight;
	private JButton addButton;
	private JButton randomButton;
	private JButton readyAllButton;
	private JPanel prcPanelContainer;
	private JPanel btnContainer;
	private JScrollPane scrollPane;

	private ArrayList<ProcessPanel> processPanels;

	private TableModelListener tableModelListener;

	private ProcessScheduler processScheduler;

	/**
	 * 
	 */
	public ProcessSchedulerView(String name, TableModelListener tableModelListener, ProcessScheduler processScheduler)
	{
		super(name);
		screenWidth = (int) tk.getScreenSize().getWidth();
		screenHeight = (int) tk.getScreenSize().getHeight();
		int w = 880;
		int h = 800;
		setBounds((screenWidth - w) / 2 + w / 2, (screenHeight - h) / 2, w, h);

		this.tableModelListener = tableModelListener;
		this.processScheduler = processScheduler;

		processPanels = new ArrayList<>();
		/*
		 * 初始化按钮容器
		 */
		btnContainer = new JPanel();
		/*
		 * 初始化添加按钮
		 */
		addButton = new JButton("启动新进程");
		addButton.addActionListener(this);
		btnContainer.add(addButton);

		/*
		 * 初始化随机按钮
		 */
		randomButton = new JButton("随机启动10个进程");
		randomButton.addActionListener(this);
		btnContainer.add(randomButton);

		/*
		 * 初始化readyAllButton
		 */
		readyAllButton = new JButton("将所有进程送入就绪队列");
		readyAllButton.addActionListener(this);
		btnContainer.add(readyAllButton);

		/*
		 * 将按钮容器加入父容器
		 */
		getContentPane().add(btnContainer, BorderLayout.SOUTH);

		/*
		 * 
		 */
		prcPanelContainer = new JPanel();
		// prcPanelContainer.setLayout(new MyFlowLayout());
		scrollPane = new JScrollPane(prcPanelContainer);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
	}

	public JButton getAddButton()
	{
		return addButton;
	}

	public void setAddButton(JButton addButton)
	{
		this.addButton = addButton;
	}

	public JPanel getContainer()
	{
		return prcPanelContainer;
	}

	public long nextLong(Random rng, long n)
	{
		// error checking and 2^x checking removed for simplicity.
		long bits, val;
		do
		{
			bits = (rng.nextLong() << 1) >>> 1;
			val = bits % n;
		}
		while (bits - val + (n - 1) < 0L);
		return val;
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
		if (btn == addButton)
		{
			ProcessPanel panel = new ProcessPanel(tableModelListener, processScheduler);
			processPanels.add(panel);
			prcPanelContainer.add(panel);
			prcPanelContainer.validate();
			// prcPanelContainer.repaint();
			scrollPane.validate();
			scrollPane.repaint();
		}
		if (btn == randomButton)
		{
			Random random = new Random();
			long bound = 99999;
			for (int i = 0; i < 10; i++)
			{
				ProcessPanel panel = new ProcessPanel(tableModelListener, processScheduler);
				long exeTimes = nextLong(random, bound);
				panel.setNeedExecutionTimesText(String.valueOf(exeTimes));
				long mem = nextLong(random, bound);
				panel.setNeedExecutionMemoryText(String.valueOf(mem));
				processPanels.add(panel);
				prcPanelContainer.add(panel);
				prcPanelContainer.validate();
				// prcPanelContainer.repaint();
				scrollPane.validate();
				;
				scrollPane.repaint();
			}
		}
		if (btn == readyAllButton)
		{
			for (ProcessPanel processPanel : processPanels)
			{
				if (processPanel.getRequestMemoryBtn().isEnabled())
					processPanel.getRequestMemoryBtn().doClick();
			}
		}
	}

}
