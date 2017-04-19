package com.zzl;
import java.util.Scanner;

/**
 * 
 */

/**
 * @author BppleMan
 *
 */
public class ThreadTest extends Thread
{
	public volatile boolean flag = false;

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run()
	{
		ThreadTest tt = this;

		while (true)
		{
			synchronized (tt)
			{
				System.out.println("执行");
				long n = ~(1 << 63);
				while (n-- > 0)
				{
				}
				if (flag == true)
				{
					try
					{
						System.out.println("挂起");
						tt.wait();
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}

			}

		}
	}

	public static void main(String[] args) throws InterruptedException
	{
		ThreadTest tt = new ThreadTest();
		Thread t = new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				Scanner sc = new Scanner(System.in);
				while (sc.hasNext())
				{
					int n = sc.nextInt();
					if (n == 1)
					{
						tt.flag = true;
						synchronized (tt)// 申请锁
						{
							n = sc.nextInt();
							if (n == 2)
							{
								tt.flag = false;
								// tt.notify();
								tt.notify();
							}
						}
					}
				}
			}
		});
		t.start();
		tt.start();
	}
}
