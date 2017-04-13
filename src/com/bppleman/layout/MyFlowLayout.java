/**
 * 
 */
package com.bppleman.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Insets;

/**
 * @author BppleMan
 *
 */
public class MyFlowLayout extends FlowLayout
{
	/*
	 * （非 Javadoc）
	 * 
	 * @see java.awt.FlowLayout#layoutContainer(java.awt.Container)
	 */
	@Override
	public void layoutContainer(Container target)
	{
		super.layoutContainer(target);
		System.out.println(target.getSize());
		synchronized (target)
		{
			Insets insets = target.getInsets();
			int left = insets.left;
			int top = insets.top;
			int twidth = target.getWidth();
			int theight = target.getHeight();
			for (int i = 0; i < target.getComponentCount(); i++)
			{
				Component c = target.getComponent(i);
				int width = (int) c.getPreferredSize().getWidth();
				int height = (int) c.getPreferredSize().getHeight();
				c.setBounds(left, top, width, height);
				left += width;
				if (left + width > twidth)
				{
					left = insets.left;
					top += height;
				}
			}
		}
	}
}
