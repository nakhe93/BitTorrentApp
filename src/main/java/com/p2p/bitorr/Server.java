package com.p2p.bitorr;

import java.nio.channels.*;
import java.util.*;
import java.net.*;
import java.io.*;
import java.nio.*;
//import com.network.utility.FileSplitterAndMerge;
public class Server {

	private static final int mPort = 8008;   //The server will be listening on this port number
	private static ArrayList<Integer> list1=new ArrayList<Integer>();
	private static ArrayList<Integer> list2=new ArrayList<Integer>();
	private static ArrayList<Integer> list3=new ArrayList<Integer>();
	private static ArrayList<Integer> list4=new ArrayList<Integer>();
	private static ArrayList<Integer> list5=new ArrayList<Integer>();
	private static String fName="";

	/**
	 * @param args
	 * @throws Exception
	 */	public static void main(String[] args) throws Exception {
		System.out.println("Server running..."); 
		//readConfigFile();
    	ServerSocket listener = new ServerSocket(mPort);

		System.out.println("Please enter the File Name:");
		 BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		    fName = br.readLine();
		    System.out.println("File name:"+fName);
		      
		    String fPath="D:/Projects/Network/Network Sample project/src/com/network/server/"+fName;
	        //FileSplitterAndMerge.split(fPath,fName);
	        
	        //allocateChunks(FileSplitterAndMerge.ChunkNo);
	        
        	
		int clientNo = 1;
		/*System.out.println(clientNo);*/
		try{
	       		while(clientNo<6) {
                		new Handler(listener.accept(),clientNo).start();
				//System.out.println("Client "  + clientNo + " is connected!");
				clientNo++;
                
            	}
        	} finally {
            		listener.close();
        	} 
 
    	}
		
		
		
}

	/**
     	* A handler thread class.  Handlers are spawned from the listening
     	* loop and are responsible for dealing with a single client's requests.
     	*/
    	class Handler extends Thread {
        	private String msg;    //msg received from the client
		
        	private ObjectInputStream in;	//stream read from the socket
        	private ObjectOutputStream out;    //stream write to the socket
			private String MESSAGE;    //uppercase msg send to the client
		private Socket conn;
		private int no;		//The index number of the client
		File filedownload=null;
		BufferedInputStream readFile=null;

        	public Handler(Socket conn, int no) {
            		this.conn = conn;
	    		this.no = no;
        	}

        public void run() {
 		try{
			//initialize Input and Output streams
			out = new ObjectOutputStream(conn.getOutputStream());
			out.flush();
			in = new ObjectInputStream(conn.getInputStream());
			try{
				//while(true)
					//receive the msg sent from the client
					msg = (String)in.readObject();
					//show the msg to the user
					System.out.println("Receive msg: " + msg + " to client " + no);
					
					//sendMessage("Ready to send");
					/*if("Okay".equalsIgnoreCase((String)in.readObject()))
						
					{	
						int totalNumberOfChunks=FileSplitterAndMerge.ChunkNo;
						out.writeObject(totalNumberOfChunks);
						out.flush();
						
						if(no==1)
						{downloadChunksToClients(list1);}
						
						else if(no==2)
						{downloadChunksToClients(list2);}
						
						else if(no==3)
						{downloadChunksToClients(list3);}
						
						else if(no==4)
						{downloadChunksToClients(list4);}
						
						else if(no==5)
						{downloadChunksToClients(list5);}
						
						
					
					
					}*/
			}
			catch(ClassNotFoundException classnot){
					System.err.println("Data received in unknown format");
				}
		}
		catch(IOException ioException){
			ioException.printStackTrace();
			System.out.println("Disconnect with Client " + no);
		}
		finally{
			//Close conns
			try{
				in.close();
				out.close();
				conn.close();
			}
			catch(IOException ioException){
				System.out.println("Disconnect with Client " + no);
			}
		}
	}

		private void downloadChunksToClients(ArrayList<Integer> list) throws IOException, FileNotFoundException {
			out.writeObject(list.size());
			out.flush();
			for(int i=0;i<list.size();i++)
			{
			System.out.println("list 1 elements:"+list.get(i));
            }
		}
	}

         