package com.p2p.bitorr;

public enum MessageTypeNum {
	
	CHOKE((byte) 0),
	UNCHOKE((byte) 1),
	INTERESTED((byte) 2),
	NOTINTERESTED((byte) 3),
	HAVE((byte) 4),
	BITFIELD((byte) 5),
	REQUEST((byte) 6),
	PIECE((byte) 7);
	
	private byte msgNum;
	
	private MessageTypeNum(byte msgNum){
		this.msgNum = msgNum;
	};

	public byte getMsgNum() {
		return msgNum;
	}
	
	
	
}
