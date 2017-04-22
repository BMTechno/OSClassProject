package com.zzl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

public class MemPanel extends JPanel
{

	private MemVector<MemNode> memVector;

	public MemPanel(MemVector memVector)
	{
		this.memVector = memVector;
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		// TODO 自动生成的方法存根
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D) g;
		int i;
		double l;

		for (i = 0; i < memVector.size(); i++)
		{
			l = memVector.get(i).getSize() / MemoryManager.getTotalMem();
			if (memVector.get(i).isFlag() == false)
			{
				Rectangle2D rectangle2d = new Rectangle2D.Double(0, 0, getWidth(), getHeight() * l);
				memVector.get(i).setRect(rectangle2d);
				g2D.setColor(new Color(187, 212, 105));
				g2D.fill(rectangle2d);
			}
		}
	}

}
