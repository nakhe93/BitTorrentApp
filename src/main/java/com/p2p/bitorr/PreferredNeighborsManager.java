package com.p2p.bitorr;

import java.util.*;

import java.io.IOException;
public class PreferredNeighborsManager extends TimerTask {
	
    private int noPrefNeighbors;
    private HashSet<RemotePeerInfo> presentPreferredNeighbors;
    private TorrentLogger logger;
    private RemotePeerInfo currPeer;
    
    public PreferredNeighborsManager(int noPrefNeighbors, RemotePeerInfo currPeer)
    {
        super();
        this.noPrefNeighbors = noPrefNeighbors;
        this.logger = TorrentLogger.getInstance();
        this.currPeer = currPeer;
    }
    
    @Override
    public void run(){
    	
        PeerManager peerManager = new PeerManager();
      
        HashSet<RemotePeerInfo> newPrefs = new HashSet<RemotePeerInfo>(noPrefNeighbors);     
        PriorityQueue<RemotePeerInfo> downloadQueue = peerManager.getQueue();
        
        int newIDPreferredNeighbor[] = new int[noPrefNeighbors];
        RemotePeerInfo prefNeighbor;
        for (int i = 0; i != noPrefNeighbors; ++i)
        {
        	prefNeighbor = downloadQueue.poll();
            newIDPreferredNeighbor[i] = Integer.parseInt(prefNeighbor.getPeerId());
            newPrefs.add(prefNeighbor);
            
        }
        logger.changeOfPrefNeighbors(Integer.parseInt(currPeer.getPeerId()), newIDPreferredNeighbor);
        for (RemotePeerInfo peer : newPrefs){        
            if (presentPreferredNeighbors.contains(peer))
            	presentPreferredNeighbors.remove(peer);
        }
        
        presentPreferredNeighbors = newPrefs;
    }
}