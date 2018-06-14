package kr.jaen.java;

import java.util.*;


import java.io.*;
/** 고객의 데이타를 관리, 저장하는 클래스 */
public class  CustomerDAO{
	private List<Customer> list;
	File f=new File("cus.ob");
	public CustomerDAO(){
		list=new ArrayList<Customer>(); 
	}
	public void save() throws IOException { //save를 종료할 때 Main에서 불렀는데... 이걸 외부가 알아야 할까? -> Throws
		Thread t=new SaveThread();
		t.start(); //run부르는 게 아니라 start를 불러야 스케줄러에 등록된다.
	}
	
	//저장하는 작업을 수행하는 thread(알바생) 만들기
	class SaveThread extends Thread{ //super인 Thread에 throws가 없는데 sub가 throws할 수 없음. ->try/catch로 잡아야 함
		public void run() { //알바생의 할 일을 run 메소드 오버라이딩으로 시키기
			try {
				FileOutputStream fos=new FileOutputStream(f);
				ObjectOutputStream oos=new ObjectOutputStream(fos);
				for(Customer c:list) {//list의 개수만큼
					oos.writeObject(c);
				}
				oos.close();
				fos.close();
			}catch(IOException e) {
				System.out.println(e.toString()); //e.getMessage()도 같은
			}
			//finally해서 close()를 밖으로 뺄 수 있는데 그러면 블록 바깥에 변수 fos, oos를 null로 설정해 놓고, close()의 try/catch 수정+null 체크도 해야 하므로 복잡함. 
		}   //요즘은 프레임워크를 기반으로 프레임워크가 전부 try/catch를 처리해주므로 간편.
	}
	
	//멀티스레드 기반인 경우, 작업의 싱크 일치 문제도 고려해야 함
	//불러오는 것을 Thread에게 시키면 문제가 생김... 작업된 결과를 가져오는 동안 싱크가 안맞는 등..
	public void load() throws FileNotFoundException, IOException, ClassNotFoundException {
		if(!f.exists()) return; //파일이 존재하지 않으면 작업 중단
		
		ObjectInputStream ois=new ObjectInputStream(new FileInputStream(f));
		
		try {
			Customer c=(Customer) ois.readObject();
			while(c !=null) {
				list.add(c);
				c=(Customer) ois.readObject();
			}
		}catch(EOFException e){ //End of File Exception. 파일을 끝까지 읽었음. 이거는 내부에서 잡고 가야 함.
			System.out.println("파일 읽기 종료");
		}
		ois.close();
	}

/** 전달된 고객의 정보를 추가한다.
 * @throws Exception */
	public void addCust( String name,String phone,int hotKey) throws DupException{//여기도 DupException으로 바꿔야
		try {
			search(hotKey); //search에서 에러가 발생하면 catch로 이동
			throw new DupException();  //중복 에러 발생. 에러도 클래스! VM만이 아니라 개발자도 에러를 발생시킬 수 있다! -throws로 알리기~~ //근데 Exception은 너무 super여서 모호한데... Exception을 상속받아 내가 필요한 것만 쓰자! => DupException
		} catch (RecordNotFoundException e) { //에러가 발생해야(값이 존재하지 않아야) 추가할 수 있다
			list.add(new Customer(name, phone, hotKey)); 
		}
	}
/** 고객의 모든 정보를 리턴한다.*/
	public List<Customer> allCust(){
		return list;
	}
	/** 이름 검색 메소드
	 * @throws RecordNotFoundException */
	public Customer search(String name) throws RecordNotFoundException{
		for(Customer c: list) {
			if(name.equals(c.getName())) return c;
		}
		throw new RecordNotFoundException();
		//return null; 에러발생시에는 리턴이 발생하지 않으므로 리턴문이 필요없음
	}
	/** 단축키 검색 메소드
	 * @throws RecordNotFoundException */
	public Customer search(int hotKey) throws RecordNotFoundException{ //오버로딩(메서드 이름이 같고 파라미터가 틀린)
		for(Customer c: list) {
			if(hotKey==c.getHotKey()) return c;
		}
		throw new RecordNotFoundException();
	}
	/** 이름 검색, 제거 메소드 
	 * @throws RecordNotFoundException */
	public void delete(String name) throws RecordNotFoundException{
		Customer cu=search(name);
		list.remove(cu);
	}
	/** 단축키를 검색, 제거 메소드
	 * @throws RecordNotFoundException */
	public void delete(int hotKey) throws RecordNotFoundException{
		Customer cu=search(hotKey);
		list.remove(cu);
	}
	/** 이름 검색하여 번호를 수정한다. 동명이인 없음 가정
	 * @throws RecordNotFoundException */
	public void updateCust( String name,String phone,int hotKey) throws RecordNotFoundException{
		Customer cu=search(name);
		cu.setPhone(phone);
	}
}
