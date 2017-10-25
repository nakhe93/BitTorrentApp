package com.p2p.bitorr;
import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

//class to log the desired messages in response to events
public class TorrentLogger { 	
	
    private static SimpleDateFormat dateI;
    private static TorrentLogger instance = null;
    
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
		String log = String.format("%s: Peer %s is connected from Peer %s.\n", 
			dateI.format(Calendar.getInstance().getTime()), peer1Id, peer2Id);
		writeLogToFile("log_peer_" + peer1Id + ".log", log);
	}
    
    public void TCPConnectToPeer(int peer1Id, int peer2Id) {
 		String log = String.format("%s: Peer %s makes a connection to Peer %s.\n", 
 			dateI.format(Calendar.getInstance().getTime()), peer1Id, peer2Id);
 		writeLogToFile("log_peer_" + peer1Id + ".log", log);
    }
    
		
	public void changeOfPrefNeighbors(String peerId, String[] neighborId) {
		String delimiter = "";
		StringBuilder neighborList = new StringBuilder();
		
		for(String s : neighborId) {
			neighborList = neighborList.append(delimiter + s);
			delimiter = ", ";
		}
		
		String log = String.format("%s: Peer %s has the preferred neighbors %s.\n", 
			dateI.format(Calendar.getInstance().getTime()), peerId, neighborList);
		
		writeLogToFile("log_peer_" + peerId + ".log", log);
	}
	
	public void changeOfOUNeighbor(int peerId, int neighborId) {
		String log = String.format("%s: Peer %s has the optimistically unchoked neighbor " 
			+ "%s.\n", 
			dateI.format(Calendar.getInstance().getTime()), peerId, neighborId);
		
		writeLogToFile("log_peer_" + peerId + ".log", log);
	}
	
	public void choke(int peer1Id, int peer2Id) {
		String log = String.format("%s: Peer %s is choked by %s.\n", 
			dateI.format(Calendar.getInstance().getTime()), peer1Id, peer2Id);
		
		writeLogToFile("log_peer_" + peer1Id + ".log", log);
	}
	
	public void unchoke(int peer1Id, int peer2Id) {
		String log = String.format("%s: Peer %s is unchoked by %s.\n", 
			dateI.format(Calendar.getInstance().getTime()), peer1Id, peer2Id);
		
		writeLogToFile("log_peer_" + peer1Id + ".log", log);
	}
	
	public void receiveHave(int peer1Id, int peer2Id, int piece) {
		String logString = String.format("%s: Peer %s received the 'have' message from %s for the " 
			+ "piece %s.\n", dateI.format(Calendar.getInstance().getTime()), peer1Id, peer2Id, piece);
		
		writeLogToFile("log_peer_" + peer1Id + ".log", logString);
	}
	
	public void receiveInterested(int peer1Id, int peer2Id) {
		String logString = String.format("%s: Peer %s received the 'interested' message from " 
			+ "%s.\n", dateI.format(Calendar.getInstance().getTime()), peer1Id, peer2Id);
		
		writeLogToFile("log_peer_" + peer1Id + ".log", logString);
	}
	
	public void receiveNotInterested(int peer1Id, int peer2Id) {
		String logString = String.format("%s: Peer %s received the 'not interested' message from "
			+ "%s.\n", dateI.format(Calendar.getInstance().getTime()), peer1Id, peer2Id);
		
		writeLogToFile("log_peer_" + peer1Id + ".log", logString);
	}
	
	public void downloadPiece(int peer1Id, int pieceIndex, int peer2Id, int numberPieces) {
		String logString = String.format("%s: Peer %s has downloaded the piece %s from %s. Now "  
			+ "the number of pieces it has is %s\n", dateI.format(Calendar.getInstance().getTime()), 
			peer1Id, pieceIndex, peer2Id, numberPieces);
		
		writeLogToFile("log_peer_" + peer1Id + ".log", logString);
	}
	
	public void downloadFile(int pid) {
		String logString = String.format("%s: Peer %s has has downloaded the complete file.\n", 
			dateI.format(Calendar.getInstance().getTime()), pid);
		
		writeLogToFile("log_peer_" + pid + ".log", logString);
	 }
}