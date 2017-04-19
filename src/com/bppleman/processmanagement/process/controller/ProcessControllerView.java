/**
 * 
 */
package com.bppleman.processmanagement.process.controller;

import java.awt.Toolkit;

import javax.swing.JFrame;

/**
 * @author BppleMan
 *
 */
public class ProcessControllerView extends JFrame
{
	private Toolkit tk = Toolkit.getDefaultToolkit();
	public int screenWidth;
	public int screenHeight;

	/**
	 * 
	 */
	public ProcessControllerView(String name)
	{
		super(name);
		screenWidth = (int) tk.getScreenSize().getWidth();
		screenHeight = (int) tk.getScreenSize().getHeight();
		// int w = 800;
		// int h = 800;
		int w = 600;
		int h = 600;
		setBounds((screenWidth - w) / 2 - w / 2, (screenHeight - h) / 2, w, h);
	}

}
