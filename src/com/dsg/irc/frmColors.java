package com.dsg.irc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;

import javax.swing.JFrame;

public class frmColors implements Runnable{
	
	public static void main(String[] args){
		new frmColors().run();
	}

	@Override
	public void run() {
		JFrame frm = new JFrame("Color codes, usage in chat: ';5Hello There' replace 5 with a color code");
		
		Label lbl0 = new Label("0 White");
		lbl0.setBounds(10, 10, 20, 100);
		lbl0.setForeground(Color.white);
		
		Label lbl1 = new Label("1 Black");
		lbl1.setBounds(10, 10, 20, 100);
		lbl1.setForeground(Color.white);
		
		
		Label lbl2 = new Label("2 Dark Blue");
		lbl2.setBounds(10, 10, 20, 100);
		lbl2.setForeground(Color.blue);
		
		Label lbl3 = new Label("3 Dark Green");
		lbl3.setBounds(10, 10, 20, 100);
		lbl3.setForeground(Color.green);
		
		Label lbl4 = new Label("4 Red");
		lbl4.setBounds(10, 10, 20, 100);
		lbl4.setForeground(Color.red);

		Label lbl5 = new Label("5 Dark Red");
		lbl5.setBounds(10, 10, 20, 100);
		lbl5.setForeground(Color.red);

		Label lbl6 = new Label("6 Dark purple");
		lbl6.setBounds(10, 10, 20, 100);
		lbl6.setForeground(Color.magenta);

		Label lbl7 = new Label("7 Orange");
		lbl7.setBounds(10, 10, 20, 100);
		lbl7.setForeground(Color.orange);

		Label lbl8 = new Label("8 Yellow");
		lbl8.setBounds(10, 10, 20, 100);
		lbl8.setForeground(Color.yellow);
		
		Label lbl9 = new Label("9 Lime");
		lbl9.setBounds(10, 10, 20, 100);
		lbl9.setForeground(Color.green);

		Label lbl10 = new Label("10 light Blue");
		lbl10.setBounds(10, 10, 20, 100);
		lbl10.setForeground(Color.cyan);

		Label lbl11 = new Label("11 gray");
		lbl11.setBounds(10, 10, 20, 100);
		lbl11.setForeground(Color.gray);		
		
		Label lbl12 = new Label("12 Dark Blue");
		lbl12.setBounds(10, 10, 20, 100);
		lbl12.setForeground(Color.blue);

		Label lbl13 = new Label("13 Pink");
		lbl13.setBounds(10, 10, 20, 100);
		lbl13.setForeground(Color.pink);	
		
		Label lbl14 = new Label("14 Dark gray");
		lbl14.setBounds(10, 10, 20, 100);
		lbl14.setForeground(Color.gray);
		
		Label lbl15 = new Label("15 Light gray");
		lbl15.setBounds(10, 10, 20, 100);
		lbl15.setForeground(Color.lightGray);	
		
		Panel p0 = new Panel();
		p0.setLayout(new GridLayout(3,1));
		p0.add(lbl0);
		p0.add(lbl1);
		p0.add(lbl2);
		p0.add(lbl3);
		p0.add(lbl4);
		p0.add(lbl5);
		p0.add(lbl6);
		p0.add(lbl7);
		p0.add(lbl8);
		p0.add(lbl9);
		p0.add(lbl10);
		p0.add(lbl11);
		p0.add(lbl12);
		p0.add(lbl13);
		p0.add(lbl14);
		p0.add(lbl15);
		
		
		frm.add(p0, BorderLayout.NORTH);
		frm.setBackground(Color.black);
		frm.setLocation(0, 480);
		frm.setSize(525, 110);
		frm.setVisible(true);
		frm.setResizable(false);
		frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
	}
	
	public static void Close(){
		
	}
	
}
