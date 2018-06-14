package kr.jaen.java;

import java.util.*;


import java.io.*;
/** ���� ����Ÿ�� ����, �����ϴ� Ŭ���� */
public class  CustomerDAO{
	private List<Customer> list;
	File f=new File("cus.ob");
	public CustomerDAO(){
		list=new ArrayList<Customer>(); 
	}
	public void save() throws IOException { //save�� ������ �� Main���� �ҷ��µ�... �̰� �ܺΰ� �˾ƾ� �ұ�? -> Throws
		Thread t=new SaveThread();
		t.start(); //run�θ��� �� �ƴ϶� start�� �ҷ��� �����ٷ��� ��ϵȴ�.
	}
	
	//�����ϴ� �۾��� �����ϴ� thread(�˹ٻ�) �����
	class SaveThread extends Thread{ //super�� Thread�� throws�� ���µ� sub�� throws�� �� ����. ->try/catch�� ��ƾ� ��
		public void run() { //�˹ٻ��� �� ���� run �޼ҵ� �������̵����� ��Ű��
			try {
				FileOutputStream fos=new FileOutputStream(f);
				ObjectOutputStream oos=new ObjectOutputStream(fos);
				for(Customer c:list) {//list�� ������ŭ
					oos.writeObject(c);
				}
				oos.close();
				fos.close();
			}catch(IOException e) {
				System.out.println(e.toString()); //e.getMessage()�� ����
			}
			//finally�ؼ� close()�� ������ �� �� �ִµ� �׷��� ��� �ٱ��� ���� fos, oos�� null�� ������ ����, close()�� try/catch ����+null üũ�� �ؾ� �ϹǷ� ������. 
		}   //������ �����ӿ�ũ�� ������� �����ӿ�ũ�� ���� try/catch�� ó�����ֹǷ� ����.
	}
	
	//��Ƽ������ ����� ���, �۾��� ��ũ ��ġ ������ ����ؾ� ��
	//�ҷ����� ���� Thread���� ��Ű�� ������ ����... �۾��� ����� �������� ���� ��ũ�� �ȸ´� ��..
	public void load() throws FileNotFoundException, IOException, ClassNotFoundException {
		if(!f.exists()) return; //������ �������� ������ �۾� �ߴ�
		
		ObjectInputStream ois=new ObjectInputStream(new FileInputStream(f));
		
		try {
			Customer c=(Customer) ois.readObject();
			while(c !=null) {
				list.add(c);
				c=(Customer) ois.readObject();
			}
		}catch(EOFException e){ //End of File Exception. ������ ������ �о���. �̰Ŵ� ���ο��� ��� ���� ��.
			System.out.println("���� �б� ����");
		}
		ois.close();
	}

/** ���޵� ���� ������ �߰��Ѵ�.
 * @throws Exception */
	public void addCust( String name,String phone,int hotKey) throws DupException{//���⵵ DupException���� �ٲ��
		try {
			search(hotKey); //search���� ������ �߻��ϸ� catch�� �̵�
			throw new DupException();  //�ߺ� ���� �߻�. ������ Ŭ����! VM���� �ƴ϶� �����ڵ� ������ �߻���ų �� �ִ�! -throws�� �˸���~~ //�ٵ� Exception�� �ʹ� super���� ��ȣ�ѵ�... Exception�� ��ӹ޾� ���� �ʿ��� �͸� ����! => DupException
		} catch (RecordNotFoundException e) { //������ �߻��ؾ�(���� �������� �ʾƾ�) �߰��� �� �ִ�
			list.add(new Customer(name, phone, hotKey)); 
		}
	}
/** ���� ��� ������ �����Ѵ�.*/
	public List<Customer> allCust(){
		return list;
	}
	/** �̸� �˻� �޼ҵ�
	 * @throws RecordNotFoundException */
	public Customer search(String name) throws RecordNotFoundException{
		for(Customer c: list) {
			if(name.equals(c.getName())) return c;
		}
		throw new RecordNotFoundException();
		//return null; �����߻��ÿ��� ������ �߻����� �����Ƿ� ���Ϲ��� �ʿ����
	}
	/** ����Ű �˻� �޼ҵ�
	 * @throws RecordNotFoundException */
	public Customer search(int hotKey) throws RecordNotFoundException{ //�����ε�(�޼��� �̸��� ���� �Ķ���Ͱ� Ʋ��)
		for(Customer c: list) {
			if(hotKey==c.getHotKey()) return c;
		}
		throw new RecordNotFoundException();
	}
	/** �̸� �˻�, ���� �޼ҵ� 
	 * @throws RecordNotFoundException */
	public void delete(String name) throws RecordNotFoundException{
		Customer cu=search(name);
		list.remove(cu);
	}
	/** ����Ű�� �˻�, ���� �޼ҵ�
	 * @throws RecordNotFoundException */
	public void delete(int hotKey) throws RecordNotFoundException{
		Customer cu=search(hotKey);
		list.remove(cu);
	}
	/** �̸� �˻��Ͽ� ��ȣ�� �����Ѵ�. �������� ���� ����
	 * @throws RecordNotFoundException */
	public void updateCust( String name,String phone,int hotKey) throws RecordNotFoundException{
		Customer cu=search(name);
		cu.setPhone(phone);
	}
}
