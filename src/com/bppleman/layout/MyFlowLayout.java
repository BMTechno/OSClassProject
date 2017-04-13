/**
 * 
 */
package com.bppleman.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;

/**
 * @author BppleMan
 *
 */
public class MyFlowLayout extends FlowLayout
{
	private int preferredWidth = 906;
	private int preferredHeight = 735;

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.awt.FlowLayout#layoutContainer(java.awt.Container)
	 */
	@Override
	public void layoutContainer(Container target)
	{
		super.layoutContainer(target);
		synchronized (target)
		{
			Insets insets = target.getInsets();
			int left = insets.left;
			int top = insets.top;

			for (int i = 0; i < target.getComponentCount(); i++)
			{
				Component c = target.getComponent(i);
				int width = (int) c.getPreferredSize().getWidth();
				int height = (int) c.getPreferredSize().getHeight();
				int twidth = target.getWidth();
				int theigh = target.getHeight();
				c.setBounds(left, top, width, height);
				left += width;
				if (left + width > twidth)
				{
					left = insets.left;
					top += height;
				}
				if (top + height > preferredHeight)
					preferredHeight = top + height;
			}
			target.setPreferredSize(new Dimension(preferredWidth, preferredHeight));
		}
	}
}
