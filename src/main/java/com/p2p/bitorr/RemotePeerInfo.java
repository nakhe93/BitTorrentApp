package com.p2p.bitorr;

import java.util.Arrays;

/*
 *                     CEN5501C Project2
 * This is the program starting remote processes.
 * This program was only tested on CISE SunOS environment.
 * If you use another environment, for example, linux environment in CISE 
 * or other environments not in CISE, it is not guaranteed to work properly.
 * It is your responsibility to adapt this program to your running environment.
 */

public class RemotePeerInfo {
	public String peerId;
	public String peerAddress;
	public String peerPort;
	public boolean hasFile;
	public boolean[] pieceAvailable;
	public String getPeerId() {
		return peerId;
	}

	public void setPeerId(String peerId) {
		this.peerId = peerId;
	}

	public String getPeerAddress() {
		return peerAddress;
	}

	public void setPeerAddress(String peerAddress) {
		this.peerAddress = peerAddress;
	}

	public String getPeerPort() {
		return peerPort;
	}

	public void setPeerPort(String peerPort) {
		this.peerPort = peerPort;
	}

	public boolean isHasFile() {
		return hasFile;
	}

	public void setHasFile(boolean hasFile) {
		this.hasFile = hasFile;
	}

	
	
	public RemotePeerInfo(String pId, String pAddress, String pPort, String hasFile) {
		peerId = pId;
		peerAddress = pAddress;
		peerPort = pPort;
		
		if(Integer.parseInt(hasFile) == 1){
			this.hasFile = true;
			Arrays.fill(pieceAvailable, true);
		}
		   
		else{
			this.hasFile = false;
			Arrays.fill(pieceAvailable, false);
		}
			
	}

	public boolean[] getPieceAvailable() {
		return pieceAvailable;
	}

	public void setPieceAvailable(boolean[] pieceAvailable) {
		this.pieceAvailable = pieceAvailable;
	}
	
	public boolean ifPieceAvailable(int i){
		return pieceAvailable[i];
	}
	
	public void setPiece(int i, boolean value){
		pieceAvailable[i] = value;
	}
}
