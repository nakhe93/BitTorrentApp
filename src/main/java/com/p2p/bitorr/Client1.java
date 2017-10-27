package com.p2p.bitorr;

import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

//import com.network.utility.FileSplitterAndMerge;

public class Client1 {

	private static final int mPort = 8011;
	private static ArrayList<String> chunkIds = new ArrayList<String>();
	private static int ChunkNo;
	private static String fileName;

	//private static class Server extends Thread {
	private Socket requestSocket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private ServerSocket listener;
	private static int totalChunksReceived;


	//public Server() {}

	public void run() {
		try {
			// create a socket to connect to the server
			requestSocket = new Socket("localhost", 8008);
			System.out.println("Connected to localhost in port 8000");

			listener = new ServerSocket(mPort);

			// initialize inputStream and outputStream
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());

			// get Input from standard input
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

			System.out.print("Hello, enter the file name to download:");
			fileName = bufferedReader.readLine();
			out.writeObject("Please download the file:" + fileName);
			out.flush();
			if ("Ready to send".equals((String) in.readObject())) {
				out.writeObject("Okay");
				out.flush();
			}

			ChunkNo = (Integer) in.readObject();
			System.out.println("Number of chunks to be downloaded:" + ChunkNo);

			File file = new File(
					"D:/Projects/Network/Network Sample project/src/com/network/client/client1/summary_file/Summary.txt");

			//ChunkNo = Integer.parseInt(numberOfChunks);
			int Chunks=(Integer) in.readObject();
			System.out.println("Number of chunks to be received from server"+ Chunks);

			ByteArrayOutputStream baos = null;
			BufferedOutputStream bos = null;
			String chunkName = "";

			for (int i = 0; i < Chunks; i++) {
				chunkName = (String) in.readObject();
				System.out.println("chunkname:"+chunkName);

				// System.out.println("Downloading chunk:"+chunkName);
				byte[] aByte = new byte[1];
				baos = new ByteArrayOutputStream();
				//writeContentsToSummaryFile(file, chunkName);
				//createChuckIdListForExchange(chunkName);
				chunkIds.add(chunkName);
				System.out.println("chunkIds:"+chunkIds.get(i));

				bos = new BufferedOutputStream(new FileOutputStream(
						"D:/Projects/Network/Network Sample project/src/com/network/client/client1/chunks/"
								+ chunkName));
				int bytesRead = in.read(aByte, 0, aByte.length);

				do {
					baos.write(aByte);
					bytesRead = in.read(aByte);
				} while (bytesRead != -1);

				bos.write(baos.toByteArray());
				bos.flush();
				bos.close();

			}

			requestSocket.close();
			totalChunksReceived=chunkIds.size();

			//new UploadNeighbour().start();
			// while(true)

			//new DownloadNeighbour(listener.accept()).start();
			//}

		} catch (ConnectException e) {
			System.err.println("Connection refused. You need to initiate a server first.");
		} catch (ClassNotFoundException e) {
			System.err.println("Class not found");
		} catch (UnknownHostException unknownHost) {
			System.err.println("You are trying to connect to an unknown host!");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			// Close conns
			try {

				in.close();
				out.close();
				listener.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}
	//}

	/*private static void createChuckIdListForExchange(String chunkName) {
		chunkIds.add(chunkName);
	}*/

	static int index = 1;
	/*
	 * FileWriter fw=null; BufferedWriter bw=n
	 */;

		 private boolean checkIfPeerListIsModified(ArrayList<String> tempList) 
		 {
			 //ArrayList<String> peerList=new ArrayList<String>();

			 try {

				 out.writeObject("Share your chunks");
				 out.flush();
				 Object	peerChunkId = in.readObject();

				 //peerList=(ArrayList<String>) peerChunkId;
				 //System.out.println("current peer list:"+peerList);
				 System.out.println("previous peer list:"+tempList);
				 //System.out.println(peerList.containsAll(tempList));
				 

			 } catch (ClassNotFoundException e) {
				 // TODO Auto-generated catch block
				 e.printStackTrace();
			 } 
			 catch(EOFException e)
			 {
				 e.printStackTrace();
			 }catch(SocketException e)
			 {
				 e.printStackTrace();
			 } catch (IOException e) {
				 // TODO Auto-generated catch block
				 e.printStackTrace();
			 }
			 return true;
		 }	
	 

	 public static void main(String args[])
	 {
		 Client1 client = new Client1();
		 client.run();
	 }
}
