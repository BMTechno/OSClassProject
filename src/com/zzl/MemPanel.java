package com.zzl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MemPanel extends JPanel
{

	private MemVector<MemNode> memVector;
	private JFrame frame;

	public MemPanel(MemVector memVector, JFrame frame)
	{
		this.memVector = memVector;
		this.frame = frame;
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		// TODO 自动生成的方法存根
		super.paintComponent(g);
		// 设置面板大小随frame变化
		this.setPreferredSize(new Dimension(frame.getWidth() * 2 / 3, frame.getHeight()));
		Graphics2D g2D = (Graphics2D) g;
		int i;
		double l, begin;

		for (i = 0; i < memVector.size(); i++)
		{
			// 得出面板与内存大小对应比例关系
			l = (double) memVector.get(i).getSize() / (double) MemoryManager.getTotalMem();
			begin = (double) memVector.get(i).getBegin() / (double) MemoryManager.getTotalMem();
			// 加入红、绿色块代表内存使用情况
			if (memVector.get(i).isFlag() == false)
			{
				System.out.println(memVector.size());
				Rectangle2D rectangle2d = new Rectangle2D.Double(0, getHeight() * begin, getWidth(), getHeight() * l);
				memVector.get(i).setRect(rectangle2d);
				g2D.setColor(new Color(187, 212, 105));
				g2D.fill(rectangle2d);
				g2D.setColor(Color.WHITE);
				g2D.draw(rectangle2d);
			}
			else if (memVector.get(i).isFlag() == true)
			{
				Rectangle2D rectangle2d = new Rectangle2D.Double(0, getHeight() * begin, getWidth(), getHeight() * l);
				memVector.get(i).setRect(rectangle2d);
				g2D.setColor(new Color(192, 49, 34));
				g2D.fill(rectangle2d);
				g2D.setColor(Color.WHITE);
				g2D.draw(rectangle2d);
			}
		}
	}

}
