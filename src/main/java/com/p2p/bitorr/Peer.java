package com.p2p.bitorr;

public class Peer {
	
	private int peerId;
	private String hostName;
	private int port;
	private boolean hasFile;
	
	public Peer(int peerId, String hostName, int port, boolean hasFile){
		this.peerId = peerId;
		this.hostName = hostName;
		this.port = port;
		this.hasFile = hasFile;
	}

	public int getPeerId() {
		return peerId;
	}

	public void setPeerId(int peerId) {
		this.peerId = peerId;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isHasFile() {
		return hasFile;
	}

	public void setHasFile(boolean hasFile) {
		this.hasFile = hasFile;
	}

}
