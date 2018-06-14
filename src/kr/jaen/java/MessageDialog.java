package kr.jaen.java;

import java.awt.*;
import java.awt.event.*;
/** 메세지를 지정하여 경고 Dialog를 보여줄 수 있다 */
public class MessageDialog extends Dialog{
	 Label mel;
	 Button okb;
	 Panel mep, okp;
/** Dialog GUI 생성 */
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
/** Event 등록 및 Handling...*/
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
/** Dialog 보여주기 */
	public void show(String message){
	 mel.setText(message);
	 setSize(200,150);
	 show();
	}

}

