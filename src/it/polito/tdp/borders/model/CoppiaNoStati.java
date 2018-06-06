package it.polito.tdp.borders.model;

public class CoppiaNoStati {

	//javabean stupida che contiene due interi relativi agli stati
	
	private int state1no ;
	private int state2no ;
	
	public CoppiaNoStati(int state1no, int state2no) {
		super();
		this.state1no = state1no;
		this.state2no = state2no;
	}
	public int getState1no() {
		return state1no;
	}
	public void setState1no(int state1no) {
		this.state1no = state1no;
	}
	public int getState2no() {
		return state2no;
	}
	public void setState2no(int state2no) {
		this.state2no = state2no;
	}
	
	
}
