package com.p2p.bitorr;

import java.nio.ByteBuffer;

//This class contains method to construct the initial handshake message
public class HandShakeMaker {
	private int handShakeLength = 32;
	private HandShakeMaker(){}
	
	public byte[] makeHandShake(int peerId){
		ByteBuffer handshake = ByteBuffer.allocate(handShakeLength);
		handshake.put("P2PFILESHARINGPROJ".getBytes());
		handshake.putInt(18, 0);
		handshake.putInt(28,peerId);
		return handshake.array();
	}

}
