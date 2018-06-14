package kr.jaen.java;

import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;

// AWT 를 이용하여 주소록에 등록/수정/삭제 기능을 구현해 본다.

//0607 수정부분  ※

public class ContactMain implements ActionListener,ItemListener{ //클래스가 직접 상속받았음
	Frame f=new Frame("Phone Book");
	Label custl=new Label("Phone Book",Label.CENTER);
	Label namel=new Label("이  름",Label.CENTER);
	Label phonel=new Label("핸드폰",Label.CENTER);
	Label hotKeyl=new Label("단축키",Label.CENTER);
	Label status=new Label("상태표시줄");
	
	Button insertb=new Button("INSERT");
	Button deleteb=new Button("DELETE");
	Button updateb=new Button("UPDATE");
	Button searchb=new Button("SEARCH");
	Button clearb=new Button("CLEAR");
	Button exitb=new Button("EXIT");
	List li=new List();
	TextField nametf=new TextField();
	TextField phonetf=new TextField();
	TextField hotKeytf=new TextField();
	Panel p1=new Panel();
	Panel p2=new Panel();
	Panel p2n=new Panel();
	Panel p2c=new Panel();
	Panel p2s=new Panel();
	CustomerDAO dao;
	MessageDialog md;
	public ContactMain(){
		dao=new CustomerDAO();
		md=new MessageDialog(f,"경  고");//Dialog 생성(owner,title)
		try {
			dao.load();
		} catch (Exception e) {
			showDialog(e.getMessage());
		} //※
		createGUI();
		addEvent();
		showList();
	}
/** GUI 를 생성한다.*/
	public void createGUI(){
		f.setLayout(new GridLayout(2,1,5,5));
		p1.setLayout(new BorderLayout());
		p2.setLayout(new BorderLayout());

		p1.add(custl,BorderLayout.NORTH);
		p1.add(li);
		p1.add(status, "South");//

		p2n.add(insertb);
		p2n.add(deleteb);
		p2n.add(updateb);
		p2n.add(searchb);

		p2c.setLayout(new GridLayout(3,2,5,7));
		p2c.add(namel);
		p2c.add(nametf);
		p2c.add(phonel);
		p2c.add(phonetf);
		p2c.add(hotKeyl);
		p2c.add(hotKeytf);
		
		p2s.add(clearb);
		p2s.add(exitb);

		p2.add(p2n,"North");
		p2.add(p2c);
		p2.add(p2s,"South");

		f.add(p1);
		f.add(p2);
		
		f.setSize(350,350);
		f.setVisible(true);
	}
/** 이벤트를 등록 또는 처리한다. */
	public void addEvent(){
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) { //닫기(x)버튼을 눌러서 종료.
				try {
					dao.save();
				} catch (IOException e1) {
					showDialog(e1.getMessage());
				} //※
				System.exit(0);//0을 주면 정상적으로 시스템 종료. 0이 아닌 다름 값을 주면 에러가 나서 종료되었다는 의미
			}
		});
		insertb.addActionListener(this);
		deleteb.addActionListener(this);
		updateb.addActionListener(this);
		searchb.addActionListener(this);
		clearb.addActionListener(this);
		exitb.addActionListener(this);
		li.addItemListener(this);	
	}
/** 버튼이 눌리거나 클릭되어 ActionEvent가 발생했을 때 실행된다. 
ActionListener 의 actionPerformed method Overrinding*/
	public void actionPerformed(ActionEvent e){

		Object ob=e.getSource();//눌린 버튼(event source) 꺼내기
		if(ob==insertb) {//insert 버튼이 눌렸냐
			insertRecord();
		}else if(ob==deleteb){//delete
			deleteRecord();
		}else if(ob==updateb) {//update
			updateRecord();
		}else if(ob==searchb) {//search
			searchRecord();
		}else if(ob==clearb) {//clear
			clear();
		}else {//exit버튼
			try {
				dao.save();
			} catch (IOException e1) {
				showDialog(e1.getMessage());
			} //※
			System.exit(0);
		}
		
		
		

	}
/** TextField의 내용을 지운다. */
	public void clear(){
		nametf.setText(" ");
		phonetf.setText(" ");
		hotKeytf.setText(" ");
	}
	public  void showDialog(String msg){
		md.show(msg);
	}
/** Insert Button이 눌렸을 때 ActionPerformed Method에 의해 호출된다.*/
	public void insertRecord(){
		//int<==String   
		//trim()...문자열의 공백 제거
		String phone=phonetf.getText().trim(); //trim:String의 메서드 중 하나. 공백이 있으면 제거시키는 메서드
		String name=nametf.getText().trim();
		String hotKey=hotKeytf.getText().trim();
		if(phone.equals("")||name.equals("")||hotKey.equals("")){
			//showDialog("비어있는 항목이 있습니다");
			status.setText("비어있는 항목이 있습니다.");
			return;
		}
		
		//여기서 에러가 발생 시 잡아서 표시해야 함-> try/catch
		try {
			dao.addCust(name,phone,Integer.parseInt(hotKey));
			//밑에 두 줄이 여기에 있어야 에러가 발생한 후에도 입력했던 정보가 그대로 남아 있음.
			showList();//입력하면 보여줘야 함
			clear();
		} catch (Exception e) { //복구방법이 같다면 굳이 나눌 필요가 없음. super로 잡아서 해결
			status.setText(e.getMessage());
		}
		//catch (NumberFormatException e) { 
		//	status.setText(e.getMessage());
		//} catch (DupException e) { //DupException을 가져와서
		//	status.setText(e.getMessage()); //getMessage하면 생성자의 메시지를 가져옴.
		//}
		
	}
/** Delete Button이 눌렸을 때 ActionPerformed Method에 의해 호출된다.*/
	public void deleteRecord(){
		String phone=phonetf.getText().trim();
		String name=nametf.getText().trim();
		String hotKey=hotKeytf.getText().trim();
		if(phone.equals("")||name.equals("")||hotKey.equals("")){
			showDialog("비어있는 항목이 있습니다");
			return; 
		}
		
		if(hotKey.equals(String.valueOf(hotKey))) {
			try {
				dao.delete(Integer.parseInt(hotKey));
			} catch (NumberFormatException e) {
				status.setText("숫자로 입력해주세요");
			} catch (RecordNotFoundException e) {
				status.setText(e.getMessage());
			}
		}
		showList();
		clear();

	}
/** Update Button이 눌렸을 때 ActionPerformed Method에 의해 호출된다.*/
	public void updateRecord(){
		String phone=phonetf.getText().trim();
		String name=nametf.getText().trim();
		String hotKey=hotKeytf.getText().trim();
		if(phone.equals("")||name.equals("")||hotKey.equals("")){
			showDialog("비어있는 항목이 있습니다");
			return;
		}
		
		try {
			dao.updateCust(name, phone, Integer.parseInt(hotKey));
		} catch (NumberFormatException e) {
			status.setText("숫자로 입력해주세요");
		} catch (RecordNotFoundException e) {
			status.setText(e.getMessage());
		}
		showList();
		clear();
		
	}
/** Search Button이 눌렸을 때 ActionPerformed Method에 의해 호출된다.*/
	public void searchRecord(){
		Customer c=null;
		String hotKey=hotKeytf.getText().trim();
		String name=nametf.getText().trim();
		if(!(name.equals(""))){
			try {
				c=dao.search(name);
			} catch (RecordNotFoundException e) {
				status.setText(e.getMessage());
			}
		}else if(!(hotKey.equals(""))){
			try {
				c=dao.search(Integer.parseInt(hotKey));
			} catch (NumberFormatException e) {
				status.setText("숫자로 입력해주세요");
			} catch (RecordNotFoundException e) {
				status.setText(e.getMessage());
			}
		}else {
			//System.out.println("선택이 잘못되었습니다");
			showDialog("선택이 잘못되었습니다");
			return;
		}
		if(c==null){
			showDialog("찾을 수 없습니다");
			return;
		}
		phonetf.setText(c.getPhone());
		nametf.setText(c.getName());
		hotKeytf.setText(c.getHotKey()+"");//String<=int
	}
/** ArrayList에 있는 데이타를 List 에 표시한다.*/
	public void showList(){
		java.util.List<Customer> v=dao.allCust();
		li.removeAll();
		for(Customer cv: v){
			li.add(cv.toString());
		}
	}
/** List의 항목이 선택(클릭)되어 ItemEvent가 발생 했을 때 실행된다. 
ItemListener 의 itemStateChanged method Overrinding */
	public void itemStateChanged(ItemEvent e){
			String str=li.getSelectedItem();
			String[] ss=str.split("   ");
			nametf.setText(ss[0]);
			phonetf.setText(ss[1]);
			hotKeytf.setText(ss[2]);

	}
	public static void main(String[] args){
		new ContactMain();
	}
}
