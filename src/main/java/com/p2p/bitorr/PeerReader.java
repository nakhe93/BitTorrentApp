package com.p2p.bitorr;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class PeerReader {
	
	private PeerReader(){}
	
	public static PeerReader getInstance(){
		return new PeerReader();
	}
	
	public ArrayList<RemotePeerInfo> getPeers(Path path){
		ArrayList<RemotePeerInfo> peers = new ArrayList<RemotePeerInfo>();
		try
		{
			Scanner in = new Scanner(path);
			while (in.hasNextLine()){
				
				String peerID = in.next();
				String peerAddress = in.next();
				String peerPort = in.next();
				String peerHasFile = in.next();

				RemotePeerInfo rpi = new RemotePeerInfo(peerID, peerAddress, peerPort, peerHasFile);
				peers.add(rpi);
			}
			in.close();
		}catch (Exception e){
			e.printStackTrace();
		}
		return peers;
	}

}
