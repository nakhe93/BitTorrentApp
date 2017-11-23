package com.p2p.bitorr;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.BitSet;
import java.util.List;
import java.util.Timer;

public class PeerManager {
	
	private static PeerManager peerManager;
	private static Path peerPath;
	private static Path configPath;
	private static List<RemotePeerInfo> peers;
	private static int numberOfPreferredNeighbors;
	private static int optimisticUnchokingInterval;
	private static int unchokingInterval;
	private static String fileName;
    private static int fileSize;
    private static int pieceSize;
    private BitSet pieceDetails;
    private PeerConnection[] connections;
	
	public PeerManager(){		
		
		this.peerPath = FileSystems.getDefault().getPath("PeerInfo.cfg");
		this.configPath = FileSystems.getDefault().getPath("Common.cfg");
		
		ConfigFileReader cfr = ConfigFileReader.getInstance();
		PeerReader pReader =  PeerReader.getInstance();
		
		this.peers = pReader.getPeers(peerPath);
		cfr.parseConfigFile(configPath);
		
		this.numberOfPreferredNeighbors = cfr.getNumberOfPreferredNeighbors();
		this.optimisticUnchokingInterval = cfr.getOptimisticUnchokingInterval();
        this.unchokingInterval = cfr.getUnchokingInterval();
        this.fileSize = cfr.getFileSize();
        this.fileName = cfr.getFileName();
        this.pieceSize = cfr.getPieceSize();
        this.pieceDetails = new BitSet(cfr.getNumberOfPieces());
	}
	
	public void createPeerAndConnections(int peerId){
		int numPeers = peers.size();
		connections = new PeerConnection[numPeers];
		
		RemotePeerInfo currentPeer;
		int currentPeerIndex = 0;
		
		for(int i = 0; i < numPeers; i++){
			if(peerId == Integer.parseInt(peers.get(i).peerId)){
				currentPeer = peers.get(i);
				currentPeerIndex = i;
			}				
		}
		
		for(int i = 0; i < currentPeerIndex; i++){
			
		}
		
		for(int i = currentPeerIndex + 1; i < numPeers; i++){
			
		}
		
		Timer preferredNeighborTimer = new Timer();
        preferredNeighborTimer.schedule(new PreferredNeighborManager(numberOfPreferredNeighbors),0,unchokingInterval*1000);		
	}
	
}
