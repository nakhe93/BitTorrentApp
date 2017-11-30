package com.p2p.bitorr;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.BitSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class PeerConnection implements Runnable{
	private Socket socket;
	private Socket connectionSocket;
	private ServerSocket serverSocket;
	private RemotePeerInfo currPeer;
	private ObjectInputStream inStream;
	private ObjectOutputStream outStream;
	private TorrentLogger logger;
	private BitSet pieceDetails;
	private AtomicBoolean termination;
	private static List<RemotePeerInfo> peers;
	private RemotePeerInfo remotePeer;
	private boolean isClient;
	ReentrantLock outStreamLock;
	
	public void run() {
		try {
			if(isClient){
				outStream.writeObject(HandShakeMaker.makeHandShake(Integer.parseInt(currPeer.getPeerId())));
				byte[] handShake = (byte[])inStream.readObject();
			}
			else{
				byte[] handShake = (byte[])inStream.readObject();
				outStream.writeObject(HandShakeMaker.makeHandShake(Integer.parseInt(currPeer.getPeerId())));
			}
			
			MessageHandler messageHandler = new MessageHandler();
			sendMessage(MessageMaker.makeMessage(MessageTypeNum.BITFIELD,new byte[(pieceDetails.length() + 7) / 8]));
					
			while (!termination.get()) {
				byte[] incomingMessage = (byte[])inStream.readObject();
				byte[] response = messageHandler.createMessage(incomingMessage,remotePeer);
				sendMessage(response);
			}
			
		}  catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	private void sendMessage(byte[] response) {
		try {
			outStreamLock.lock();
			outStream.writeObject(response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			outStreamLock.unlock();
		}
	}

	public PeerConnection(Socket socket,RemotePeerInfo currPeer, BitSet pieceDetails, List<RemotePeerInfo> peers, RemotePeerInfo remotePeer){
		this.socket = socket;
		this.currPeer = currPeer;
		this.pieceDetails = pieceDetails;
		this.peers = peers;
		this.remotePeer = remotePeer;
		this.logger = TorrentLogger.getInstance();
		this.termination = new AtomicBoolean(false);
		this.isClient = true;
		try {
			this.outStream = new ObjectOutputStream(socket.getOutputStream());
			this.outStream.flush();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		try {
			this.inStream = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		
	}
	
	public PeerConnection(ServerSocket socket,RemotePeerInfo currPeer, BitSet pieceDetails, List<RemotePeerInfo> peers, RemotePeerInfo remotePeer){
		this.serverSocket = socket;
		this.currPeer = currPeer;
		this.pieceDetails = pieceDetails;
		this.peers = peers;
		this.remotePeer = remotePeer;
		this.logger = TorrentLogger.getInstance();
		this.termination = new AtomicBoolean(false);
		this.isClient = false;
		try {
			this.connectionSocket = serverSocket.accept();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		try {
			this.outStream = new ObjectOutputStream(connectionSocket.getOutputStream());
			this.outStream.flush();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		try {
			this.inStream = new ObjectInputStream(connectionSocket.getInputStream());
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		
	}

}
