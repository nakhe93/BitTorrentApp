package com.p2p.bitorr;

public class peerProcess {
	
	public static void main(String args[]){
		
		int peerId = Integer.parseInt(args[0]);
		TorrentLogger.getInstance().setPeerId(peerId);
		
		PeerManager peerManager = new PeerManager();
		peerManager.createPeerAndConnections(peerId);
		
	}

}
