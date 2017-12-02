package com.p2p.bitorr;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.PriorityQueue;
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
    private static int numPieces;
    private BitSet pieceDetails;
    private static PriorityQueue<RemotePeerInfo> downloadQueue;
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
        this.numPieces = cfr.getNumberOfPieces();
        this.pieceDetails = new BitSet(cfr.getNumberOfPieces());
        
        this.downloadQueue = new PriorityQueue<RemotePeerInfo>();
	}
	
	public void createPeerAndConnections(int peerId){
		int numPeers = peers.size();
		connections = new PeerConnection[numPeers];
		
		RemotePeerInfo currentPeer = null;
		int currentPeerIndex = 0;
		
		
		for(int i = 0; i < numPeers; i++){
			if(peerId == Integer.parseInt(peers.get(i).peerId)){
				currentPeer = peers.get(i);
				currentPeerIndex = i;
			}				
		}
		
		if(currentPeer.hasFile)
			pieceDetails.set(0,numPieces-1);
		else
			splitFileToPieces(currentPeer);
		
		//create socket with peers which have been started before(client)
		for(int i = 0; i < currentPeerIndex; i++){
			try {
				Socket socket = new Socket(peers.get(i).getPeerAddress(),Integer.parseInt(peers.get(i).getPeerPort()));
				PeerConnection connection = new PeerConnection(socket,currentPeer,pieceDetails,peers,peers.get(i));
				new Thread(connection).start();
				
			} catch (NumberFormatException e) {
				
				e.printStackTrace();
			} catch (UnknownHostException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
					
		}
		
		//create socket with peers which would start later(socket)
		for(int i = currentPeerIndex + 1; i < numPeers; i++){
			try {
				ServerSocket serverSocket = new ServerSocket(Integer.parseInt(currentPeer.getPeerPort()));
				PeerConnection connection = new PeerConnection(serverSocket,currentPeer,pieceDetails,peers,peers.get(i));
				new Thread(connection).start();
				
			} catch (NumberFormatException e) {
				
				e.printStackTrace();
			} catch (UnknownHostException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		
		Timer preferredNeighborTimer = new Timer();
        preferredNeighborTimer.schedule(new PreferredNeighborsManager(numberOfPreferredNeighbors,currentPeer),0,unchokingInterval*1000);		
	}

	private void splitFileToPieces(RemotePeerInfo currPeer) {
		Path filePath = FileSystems.getDefault().getPath("peer_" + currPeer.getPeerId());
        try {
			Files.createDirectories(filePath);
			
			for(int i = 0; i < numPieces; ++i){
	            int beginIdx = pieceSize;int lastIdx;
	            if(i != numPieces - 1)
	            	lastIdx = (i+1)*pieceSize - 1;
	            else
	            	lastIdx = fileSize - beginIdx;
	            
	            try {
					Files.write(filePath.resolve(String.valueOf(i)),
					Arrays.copyOfRange(Files.readAllBytes(filePath), beginIdx, lastIdx));
				} catch (IOException e) {
					
					e.printStackTrace();
				}
	        }
			
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}
	        
        
		
	}

	public PriorityQueue<RemotePeerInfo> getQueue() {
		return downloadQueue;
	}
	
}
