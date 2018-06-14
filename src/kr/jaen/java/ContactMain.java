package kr.jaen.java;

import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;

// AWT �� �̿��Ͽ� �ּҷϿ� ���/����/���� ����� ������ ����.

//0607 �����κ�  ��

public class ContactMain implements ActionListener,ItemListener{ //Ŭ������ ���� ��ӹ޾���
	Frame f=new Frame("Phone Book");
	Label custl=new Label("Phone Book",Label.CENTER);
	Label namel=new Label("��  ��",Label.CENTER);
	Label phonel=new Label("�ڵ���",Label.CENTER);
	Label hotKeyl=new Label("����Ű",Label.CENTER);
	Label status=new Label("����ǥ����");
	
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
		md=new MessageDialog(f,"��  ��");//Dialog ����(owner,title)
		try {
			dao.load();
		} catch (Exception e) {
			showDialog(e.getMessage());
		} //��
		createGUI();
		addEvent();
		showList();
	}
/** GUI �� �����Ѵ�.*/
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
/** �̺�Ʈ�� ��� �Ǵ� ó���Ѵ�. */
	public void addEvent(){
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) { //�ݱ�(x)��ư�� ������ ����.
				try {
					dao.save();
				} catch (IOException e1) {
					showDialog(e1.getMessage());
				} //��
				System.exit(0);//0�� �ָ� ���������� �ý��� ����. 0�� �ƴ� �ٸ� ���� �ָ� ������ ���� ����Ǿ��ٴ� �ǹ�
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
/** ��ư�� �����ų� Ŭ���Ǿ� ActionEvent�� �߻����� �� ����ȴ�. 
ActionListener �� actionPerformed method Overrinding*/
	public void actionPerformed(ActionEvent e){

		Object ob=e.getSource();//���� ��ư(event source) ������
		if(ob==insertb) {//insert ��ư�� ���ȳ�
			insertRecord();
		}else if(ob==deleteb){//delete
			deleteRecord();
		}else if(ob==updateb) {//update
			updateRecord();
		}else if(ob==searchb) {//search
			searchRecord();
		}else if(ob==clearb) {//clear
			clear();
		}else {//exit��ư
			try {
				dao.save();
			} catch (IOException e1) {
				showDialog(e1.getMessage());
			} //��
			System.exit(0);
		}
		
		
		

	}
/** TextField�� ������ �����. */
	public void clear(){
		nametf.setText(" ");
		phonetf.setText(" ");
		hotKeytf.setText(" ");
	}
	public  void showDialog(String msg){
		md.show(msg);
	}
/** Insert Button�� ������ �� ActionPerformed Method�� ���� ȣ��ȴ�.*/
	public void insertRecord(){
		//int<==String   
		//trim()...���ڿ��� ���� ����
		String phone=phonetf.getText().trim(); //trim:String�� �޼��� �� �ϳ�. ������ ������ ���Ž�Ű�� �޼���
		String name=nametf.getText().trim();
		String hotKey=hotKeytf.getText().trim();
		if(phone.equals("")||name.equals("")||hotKey.equals("")){
			//showDialog("����ִ� �׸��� �ֽ��ϴ�");
			status.setText("����ִ� �׸��� �ֽ��ϴ�.");
			return;
		}
		
		//���⼭ ������ �߻� �� ��Ƽ� ǥ���ؾ� ��-> try/catch
		try {
			dao.addCust(name,phone,Integer.parseInt(hotKey));
			//�ؿ� �� ���� ���⿡ �־�� ������ �߻��� �Ŀ��� �Է��ߴ� ������ �״�� ���� ����.
			showList();//�Է��ϸ� ������� ��
			clear();
		} catch (Exception e) { //��������� ���ٸ� ���� ���� �ʿ䰡 ����. super�� ��Ƽ� �ذ�
			status.setText(e.getMessage());
		}
		//catch (NumberFormatException e) { 
		//	status.setText(e.getMessage());
		//} catch (DupException e) { //DupException�� �����ͼ�
		//	status.setText(e.getMessage()); //getMessage�ϸ� �������� �޽����� ������.
		//}
		
	}
/** Delete Button�� ������ �� ActionPerformed Method�� ���� ȣ��ȴ�.*/
	public void deleteRecord(){
		String phone=phonetf.getText().trim();
		String name=nametf.getText().trim();
		String hotKey=hotKeytf.getText().trim();
		if(phone.equals("")||name.equals("")||hotKey.equals("")){
			showDialog("����ִ� �׸��� �ֽ��ϴ�");
			return; 
		}
		
		if(hotKey.equals(String.valueOf(hotKey))) {
			try {
				dao.delete(Integer.parseInt(hotKey));
			} catch (NumberFormatException e) {
				status.setText("���ڷ� �Է����ּ���");
			} catch (RecordNotFoundException e) {
				status.setText(e.getMessage());
			}
		}
		showList();
		clear();

	}
/** Update Button�� ������ �� ActionPerformed Method�� ���� ȣ��ȴ�.*/
	public void updateRecord(){
		String phone=phonetf.getText().trim();
		String name=nametf.getText().trim();
		String hotKey=hotKeytf.getText().trim();
		if(phone.equals("")||name.equals("")||hotKey.equals("")){
			showDialog("����ִ� �׸��� �ֽ��ϴ�");
			return;
		}
		
		try {
			dao.updateCust(name, phone, Integer.parseInt(hotKey));
		} catch (NumberFormatException e) {
			status.setText("���ڷ� �Է����ּ���");
		} catch (RecordNotFoundException e) {
			status.setText(e.getMessage());
		}
		showList();
		clear();
		
	}
/** Search Button�� ������ �� ActionPerformed Method�� ���� ȣ��ȴ�.*/
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
				status.setText("���ڷ� �Է����ּ���");
			} catch (RecordNotFoundException e) {
				status.setText(e.getMessage());
			}
		}else {
			//System.out.println("������ �߸��Ǿ����ϴ�");
			showDialog("������ �߸��Ǿ����ϴ�");
			return;
		}
		if(c==null){
			showDialog("ã�� �� �����ϴ�");
			return;
		}
		phonetf.setText(c.getPhone());
		nametf.setText(c.getName());
		hotKeytf.setText(c.getHotKey()+"");//String<=int
	}
/** ArrayList�� �ִ� ����Ÿ�� List �� ǥ���Ѵ�.*/
	public void showList(){
		java.util.List<Customer> v=dao.allCust();
		li.removeAll();
		for(Customer cv: v){
			li.add(cv.toString());
		}
	}
/** List�� �׸��� ����(Ŭ��)�Ǿ� ItemEvent�� �߻� ���� �� ����ȴ�. 
ItemListener �� itemStateChanged method Overrinding */
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
