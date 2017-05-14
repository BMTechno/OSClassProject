/**
 * 
 */
package com.bppleman.memory;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JRadioButton;

import com.zzl.MemoryManager.ManagerMode;

/**
 * @author BppleMan
 *
 */
public class MemorySelector implements ActionListener
{
	private JFrame memorySelectorWindow;

	private JRadioButton FF_Button;
	private JRadioButton BF_Button;
	private JRadioButton WF_Button;

	private ButtonGroup buttonGroup;

	private MemorySelectorLinstener delegate;

	/**
	 * 
	 */
	public MemorySelector(ManagerMode mode, MemorySelectorLinstener delegate)
	{
		initWindow();
		initButton();
		this.delegate = delegate;
		memorySelectorWindow.add(FF_Button);
		memorySelectorWindow.add(BF_Button);
		memorySelectorWindow.add(WF_Button);
		switch (mode)
		{
			case FF:
				FF_Button.setSelected(true);
				break;
			case BF:
				BF_Button.setSelected(true);
				break;
			case WF:
				WF_Button.setSelected(true);
				break;
		}
	}

	protected void initWindow()
	{
		memorySelectorWindow = new JFrame("内存分配算法选择");
		memorySelectorWindow.setBounds(300, 100, 200, 150);
		memorySelectorWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		memorySelectorWindow.setLayout(new FlowLayout());
	}

	public void setMemorySelectorViewVisible(boolean b, ManagerMode mode)
	{
		switch (mode)
		{
			case FF:
				FF_Button.setSelected(true);
				break;
			case BF:
				BF_Button.setSelected(true);
				break;
			case WF:
				WF_Button.setSelected(true);
				break;
		}
		memorySelectorWindow.setVisible(b);
	}

	protected void initButton()
	{
		FF_Button = new JRadioButton("首次适应算法");
		BF_Button = new JRadioButton("最佳适应算法");
		WF_Button = new JRadioButton("最坏适应算法");
		buttonGroup = new ButtonGroup();
		buttonGroup.add(FF_Button);
		buttonGroup.add(BF_Button);
		buttonGroup.add(WF_Button);
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
		JRadioButton button = (JRadioButton) e.getSource();
		if (button == FF_Button)
		{
			delegate.didChangeTheMemoryButton(ManagerMode.FF);
		}
		else if (button == BF_Button)
		{
			delegate.didChangeTheMemoryButton(ManagerMode.BF);
		}
		else if (button == WF_Button)
		{
			delegate.didChangeTheMemoryButton(ManagerMode.WF);
		}
	}

}
