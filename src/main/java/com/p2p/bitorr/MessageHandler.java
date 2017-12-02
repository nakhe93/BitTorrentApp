package com.p2p.bitorr;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageHandler {
	RemotePeerInfo currPeer;
	RemotePeerInfo remotePeer;
	TorrentLogger logger;
	ArrayList<RemotePeerInfo> chokeList;  
	List<RemotePeerInfo> peers;
	
	public MessageHandler(RemotePeerInfo currPeer, RemotePeerInfo remotePeer, List<RemotePeerInfo> peers){
		this.currPeer = currPeer;
		this.remotePeer = remotePeer;
		this.logger = TorrentLogger.getInstance();
		this.chokeList = new ArrayList<RemotePeerInfo>();
		this.peers = peers;
	}
	
	public byte[] createMessage(byte[] incomingMessage, RemotePeerInfo remotePeer){
		int messageTypeNum = incomingMessage[4];
		switch(messageTypeNum){
		case 0:
			return actOnRecievedChoke();
		case 1:
			return actOnRecievedUnchoke();
		case 2:
			return ActOnRecievedInterested();
		case 3:
			return ActOnRecievedNotInterested();
		case 4:
			return ActOnRecievedHave();
		case 5:
			return ActOnRecievedBitfield();
		case 6:
			return ActOnRecievedRequest(incomingMessage);
		case 7:
			return ActOnRecievedPiece(incomingMessage);
		default:
			return null;
		}
	}

	private byte[] ActOnRecievedPiece(byte[] incomingMessage) {
		int pieceIndex = java.nio.ByteBuffer.wrap(Arrays.copyOfRange(incomingMessage, 0, 4)).getInt();
		boolean downloadComplete = false;
		
		Path filePath = FileSystems.getDefault().getPath("peer_" + currPeer.getPeerId() + "/" + pieceIndex);
		byte[] message = Arrays.copyOfRange(incomingMessage, 4, incomingMessage.length);
		try {
			Files.write(filePath, message);
			currPeer.setPiece(pieceIndex, true);
			int count = 0;
			
			for(int i = 0; i < currPeer.pieceAvailable.length; i++){
				if(currPeer.pieceAvailable[i] == true)
					count++;
			}
			logger.downloadPiece(Integer.parseInt(currPeer.getPeerId()), pieceIndex, Integer.parseInt(remotePeer.getPeerId()), count);
			
			for(int i = 0; i < currPeer.pieceAvailable.length; i++){
				if(currPeer.pieceAvailable[i] == false)
					return null;
			}
			
			logger.downloadFile(Integer.parseInt(currPeer.getPeerId()));
			
			for(int i = 0; i < peers.size(); i++){
				for(int j = 0; j < peers.get(i).pieceAvailable.length; j++)
					if(peers.get(i).pieceAvailable[i] == false)
						return null;
			}
			
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return null;
		
	}

	private byte[] ActOnRecievedRequest(byte[] incomingMessage) {
		 int pieceIndex = java.nio.ByteBuffer.wrap(Arrays.copyOfRange(incomingMessage, 0, 4)).getInt();
		 
		 Path filePath = FileSystems.getDefault().getPath("peer_" + currPeer.getPeerId() + "/" + pieceIndex);
	     try {
			byte[] pieceData = Files.readAllBytes(filePath);
			ByteBuffer data = ByteBuffer.allocate(4 + pieceData.length);
	        data.putInt(pieceIndex);
	        data.put(pieceData);
	        
	        return MessageMaker.makePayloadMessage(MessageTypeNum.PIECE, data.array());			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		 
		
	}

	private byte[] ActOnRecievedBitfield() {
		for(int i = 0; i < this.currPeer.getPieceAvailable().length; i++){
			if((this.currPeer.ifPieceAvailable(i) == false) && (this.remotePeer.ifPieceAvailable(i) == true))
				return MessageMaker.makeMessage(MessageTypeNum.INTERESTED);
		}
		return MessageMaker.makeMessage(MessageTypeNum.NOTINTERESTED);
	}

	private byte[] ActOnRecievedHave() {
		for(int i = 0; i < this.currPeer.getPieceAvailable().length; i++){
			if((this.currPeer.ifPieceAvailable(i) == false) && (this.remotePeer.ifPieceAvailable(i) == true)){
				logger.receiveHave(Integer.parseInt(this.currPeer.getPeerId()), Integer.parseInt(this.remotePeer.getPeerId()), i);
				return MessageMaker.makeMessage(MessageTypeNum.INTERESTED);
			}
				
		}
		return MessageMaker.makeMessage(MessageTypeNum.NOTINTERESTED);
	}

	private byte[] ActOnRecievedNotInterested() {
		logger.receiveNotInterested(Integer.parseInt(currPeer.getPeerId()), Integer.parseInt(remotePeer.getPeerId()));
		return null;
	}

	private byte[] ActOnRecievedInterested() {
		logger.receiveInterested(Integer.parseInt(currPeer.getPeerId()), Integer.parseInt(remotePeer.getPeerId()));
		
		return null;
		
	}

	private byte[] actOnRecievedUnchoke() {
		logger.unchoke(Integer.parseInt(currPeer.getPeerId()), Integer.parseInt(remotePeer.getPeerId()));
		chokeList.remove(remotePeer);
		
		return null;
	}

	private byte[] actOnRecievedChoke() {
		logger.choke(Integer.parseInt(currPeer.getPeerId()), Integer.parseInt(remotePeer.getPeerId()));
		chokeList.add(remotePeer);
		
		return null;
		
	}

}
