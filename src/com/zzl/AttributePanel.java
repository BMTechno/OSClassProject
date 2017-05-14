package com.zzl;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class AttributePanel extends JPanel
{
	private JFrame frame;
	private JLabel Lstart;
	private JLabel Lproname;
	private JLabel Lsize;
	private JLabel Loccupation;
	private JLabel start;
	private JLabel proname;
	private JLabel size;
	private JLabel occupation;

	public AttributePanel(JFrame frame)
	{
		this.frame = frame;
		Lstart = new JLabel("起始地址:", SwingConstants.RIGHT);
		Lproname = new JLabel("进程名:", SwingConstants.RIGHT);
		Lsize = new JLabel("内存大小:", SwingConstants.RIGHT);
		Loccupation = new JLabel("是否占用:", SwingConstants.RIGHT);
		start = new JLabel();
		proname = new JLabel();
		size = new JLabel();
		occupation = new JLabel();
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		// 清空面板
		g.clearRect(0, 0, getWidth(), getHeight());
		// 设置面板大小随frame变化
		this.setPreferredSize(new Dimension(frame.getWidth() / 3, frame.getHeight()));
		this.setLayout(null);
		add(Lstart);
		add(Lproname);
		add(Lsize);
		add(Loccupation);
		add(start);
		add(proname);
		add(size);
		add(occupation);
		Lstart.setBounds(getWidth() / 4, getHeight() / 3, 60, 20);
		Lproname.setBounds(getWidth() / 4, getHeight() / 3 + 20, 60, 20);
		Lsize.setBounds(getWidth() / 4, getHeight() / 3 + 40, 60, 20);
		Loccupation.setBounds(getWidth() / 4, getHeight() / 3 + 60, 60, 20);
		start.setBounds(getWidth() / 4 + 60, getHeight() / 3, 60, 20);
		proname.setBounds(getWidth() / 4 + 60, getHeight() / 3 + 20, 60, 20);
		size.setBounds(getWidth() / 4 + 60, getHeight() / 3 + 40, 60, 20);
		occupation.setBounds(getWidth() / 4 + 60, getHeight() / 3 + 60, 60, 20);
	}

	// 显示属性信息
	public void setAttribute(MemNode memNode)
	{
		start.setText(Long.toString(memNode.getBegin()));
		proname.setText(memNode.getName());
		size.setText(Long.toString(memNode.getSize()));
		occupation.setText(String.valueOf(memNode.isFlag()));
	}
}
