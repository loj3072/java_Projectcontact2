package kr.jaen.java;

public class DupException extends Exception {
	public DupException() {
		super("중복 오류~~~");//super의 파라미터에 에러에 대한 정보 메시지를 주면 저게 발생됨.
	}

}
