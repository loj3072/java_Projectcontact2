package kr.jaen.java;

import java.io.Serializable;

public class Customer implements Serializable{
	private String name;
	private String phone;
	private int hotKey=100;
	
	public Customer(String name, String phone, int hotKey) {
		this.name = name;
		this.phone = phone;
		this.hotKey = hotKey;
	}

	public Customer(String name, String phone) {
		this(name,phone,100);
	}

	public Customer() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getHotKey() {
		return hotKey;
	}

	public void setHotKey(int hotKey) {
		this.hotKey = hotKey;
	}
	@Override
	public String toString(){
		StringBuilder sb=new StringBuilder(name);
		sb.append("   ");
		sb.append(phone);
		sb.append("   ");
		if(hotKey!=100) sb.append(hotKey);
		return sb.toString();
	}

}
