package kr.jaen.java;

public class RecordNotFoundException extends Exception {
	public RecordNotFoundException() {
		super("데이터가 없습니다");
	}

}
