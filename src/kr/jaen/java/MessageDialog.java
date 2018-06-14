package kr.jaen.java;

import java.awt.*;
import java.awt.event.*;
/** �޼����� �����Ͽ� ��� Dialog�� ������ �� �ִ� */
public class MessageDialog extends Dialog{
	 Label mel;
	 Button okb;
	 Panel mep, okp;
/** Dialog GUI ���� */
	public MessageDialog(Frame f, String title){
		super(f,title);
		mel=new Label();
		okb=new Button("O K");
		mep=new Panel();
		okp=new Panel();
		okp.add(okb);
		mep.add(mel);
		setLayout(new GridLayout(2,1));
		add(mep);
		add(okp);
		
		addEvent();
	}
/** Event ��� �� Handling...*/
	public void addEvent(){
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				dispose();
			}
		});
		okb.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				dispose();
			}
		});
	}
/** Dialog �����ֱ� */
	public void show(String message){
	 mel.setText(message);
	 setSize(200,150);
	 show();
	}

}

