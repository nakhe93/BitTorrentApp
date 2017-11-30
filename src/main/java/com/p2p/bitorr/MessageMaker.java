package com.p2p.bitorr;

import java.nio.ByteBuffer;

//This class contains methods to construct messages 
public class MessageMaker {
	
	public static byte[] makeNoPayloadMessage(MessageTypeNum type){
		return makeMessage(type);
	}
	
	public static byte[] makePayloadMessage(MessageTypeNum type, byte[] payload){
		return makeMessage(type,payload);
	}
	
	//returns an byte array of messages which don't have payload
	public static byte[] makeMessage(MessageTypeNum type){
		ByteBuffer message = ByteBuffer.allocate(5);
		message.putInt(0);
		message.put(type.getMsgNum());
		return message.array();
	}
	
	//returns an byte array of messages with payload
	public static byte[] makeMessage(MessageTypeNum type, byte[] payload){
		ByteBuffer message = ByteBuffer.allocate(5 + payload.length);
		message.putInt(0);
		message.put(type.getMsgNum());
		message.put(payload);
		return message.array();
	}

}
