package com.p2p.bitorr;
import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

//class to log the desired messages in response to events
public class TorrentLogger { 	
	
    private static SimpleDateFormat dateI;
    private static TorrentLogger instance = null;
    private int peerId;
    
    public int getPeerId() {
		return peerId;
	}

	public void setPeerId(int peerId) {
		this.peerId = peerId;
	}

	private TorrentLogger() {}
    
    private synchronized void writeLogToFile(String filename, String logString) {
    	try {
    	File log = new File(filename);
    	
    	//create a new log file if it doesn't exist
    	if(!log.exists()) 
    		log.createNewFile();
    	
    		//appends to existing file
	    	FileOutputStream appendLog = new FileOutputStream(log, true); 
	    	appendLog.write(logString.getBytes());
	    	appendLog.flush();
	    	appendLog.close();
	    	
    	} catch(IOException ie) {
    		System.err.println("Failed to write to log file.");
    	}
    }
    public static synchronized TorrentLogger getInstance() {
    	if(instance == null) {
    		instance = new TorrentLogger();
    		dateI = new SimpleDateFormat("HH:mm:ss"); 
    	}
    	return instance;
    }
    
    public void TCPConnectFromPeer(int peer1Id, int peer2Id) {
		String log = String.format("%s: Peer %d is connected from Peer %d.\n", 
			dateI.format(Calendar.getInstance().getTime()), peer1Id, peer2Id);
		writeLogToFile("log_peer_" + peer1Id + ".log", log);
	}
    
    public void TCPConnectToPeer(int peer1Id, int peer2Id) {
 		String log = String.format("%s: Peer %d makes a connection to Peer %d.\n", 
 			dateI.format(Calendar.getInstance().getTime()), peer1Id, peer2Id);
 		writeLogToFile("log_peer_" + peer1Id + ".log", log);
    }
    
		
	public void changeOfPrefNeighbors(int peerId, int[] neighborId) {
		String delimiter = "";
		StringBuilder neighborList = new StringBuilder();
		
		for(int s : neighborId) {
			neighborList = neighborList.append(delimiter + s);
			delimiter = ", ";
		}
		
		String log = String.format("%s: Peer %d has the preferred neighbors %d.\n", 
			dateI.format(Calendar.getInstance().getTime()), peerId, neighborList);
		
		writeLogToFile("log_peer_" + peerId + ".log", log);
	}
	
	public void changeOfOUNeighbor(int peerId, int neighborId) {
		String log = String.format("%s: Peer %d has the optimistically unchoked neighbor " 
			+ "%d.\n", 
			dateI.format(Calendar.getInstance().getTime()), peerId, neighborId);
		
		writeLogToFile("log_peer_" + peerId + ".log", log);
	}
	
	public void choke(int peer1Id, int peer2Id) {
		String log = String.format("%s: Peer %d is choked by %d.\n", 
			dateI.format(Calendar.getInstance().getTime()), peer1Id, peer2Id);
		
		writeLogToFile("log_peer_" + peer1Id + ".log", log);
	}
	
	public void unchoke(int peer1Id, int peer2Id) {
		String log = String.format("%s: Peer %d is unchoked by %d.\n", 
			dateI.format(Calendar.getInstance().getTime()), peer1Id, peer2Id);
		
		writeLogToFile("log_peer_" + peer1Id + ".log", log);
	}
	
	public void receiveHave(int peer1Id, int peer2Id, int piece) {
		String logString = String.format("%s: Peer %d received the 'have' message from %d for the " 
			+ "piece %d.\n", dateI.format(Calendar.getInstance().getTime()), peer1Id, peer2Id, piece);
		
		writeLogToFile("log_peer_" + peer1Id + ".log", logString);
	}
	
	public void receiveInterested(int peer1Id, int peer2Id) {
		String logString = String.format("%s: Peer %s received the 'interested' message from " 
			+ "%s.\n", dateI.format(Calendar.getInstance().getTime()), peer1Id, peer2Id);
		
		writeLogToFile("log_peer_" + peer1Id + ".log", logString);
	}
	
	public void receiveNotInterested(int peer1Id, int peer2Id) {
		String logString = String.format("%s: Peer %d received the 'not interested' message from "
			+ "%d.\n", dateI.format(Calendar.getInstance().getTime()), peer1Id, peer2Id);
		
		writeLogToFile("log_peer_" + peer1Id + ".log", logString);
	}
	
	public void downloadPiece(int peer1Id, int pieceIndex, int peer2Id, int numberPieces) {
		String logString = String.format("%s: Peer %s has downloaded the piece %d from %d. Now "  
			+ "the number of pieces it has is %d\n", dateI.format(Calendar.getInstance().getTime()), 
			peer1Id, pieceIndex, peer2Id, numberPieces);
		
		writeLogToFile("log_peer_" + peer1Id + ".log", logString);
	}
	
	public void downloadFile(int peerId) {
		String logString = String.format("%s: Peer %d has has downloaded the complete file.\n", 
			dateI.format(Calendar.getInstance().getTime()), peerId);
		
		writeLogToFile("log_peer_" + peerId + ".log", logString);
	 }
}