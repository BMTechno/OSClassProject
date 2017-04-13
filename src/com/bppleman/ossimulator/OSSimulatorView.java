/**
 * 
 */
package com.bppleman.ossimulator;

import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;

/**
 * @author BppleMan
 *
 */
public class OSSimulatorView extends JFrame
{
	private Toolkit tk = Toolkit.getDefaultToolkit();
	public double screenWidth;
	public double screenHeight;

	/**
	 * 
	 */
	public OSSimulatorView()
	{
		screenWidth = tk.getScreenSize().getWidth();
		screenHeight = tk.getScreenSize().getHeight();
		int w = 800;
		int h = 800;
		setBounds((int) ((screenWidth - w) / 2), (int) ((screenHeight - h) / 2), w, h);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new FlowLayout());
	}
}
