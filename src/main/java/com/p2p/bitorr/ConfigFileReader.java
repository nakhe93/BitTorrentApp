package com.p2p.bitorr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

//This class reads the provided commm.cfg file and sets the appropriate parameters
public class ConfigFileReader {
	
	private static int numberOfPreferredNeighbors;
	private static int unchokingInterval;
	private static int optimisticUnchokingInterval; 
	private static String fileName;
    private static int fileSize;
    private static int pieceSize;
    private static int numberOfPieces;
    
	public static int getNumberOfPreferredNeighbors() {
		return numberOfPreferredNeighbors;
	}

	public static int getUnchokingInterval() {
		return unchokingInterval;
	}

	public static int getOptimisticUnchokingInterval() {
		return optimisticUnchokingInterval;
	}

	public static String getFileName() {
		return fileName;
	}

	public static int getFileSize() {
		return fileSize;
	}

	public static int getPieceSize() {
		return pieceSize;
	}
	
	public static ConfigFileReader getInstance(){
		return new ConfigFileReader();
	}
    
    public static void parseConfigFile(Path filePath){
    	
    	try{
    	Scanner input = new Scanner(filePath);
    	while(input.hasNextLine()){
    		
    		String current = input.next();
    		
    		if(current == "NumberOfPreferredNeighbors")
    			numberOfPreferredNeighbors = input.nextInt();
    		
    		else if(current == "UnchokingInterval")
    			unchokingInterval = input.nextInt();
    		
    		else if(current == "OptimisticUnchokingInterval")
    			optimisticUnchokingInterval = input.nextInt();
    		
    		else if(current == "FileName")
    			fileName = input.next();
    		
    		else if(current == "FileSize")
    			fileSize = input.nextInt();
    		
    		else if(current == "PieceSize")
    			pieceSize = input.nextInt();
    		
    		numberOfPieces = (int)Math.ceil((double)fileSize/pieceSize);
    	}
    	input.close();
    		
    	}
    	catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
    	catch(IOException ex){
    		System.out.println(
                    "Error in reading file '" + 
                    fileName + "'");  
    	}
    	
    }

	public static int getNumberOfPieces() {
		return numberOfPieces;
	}

	public static void setNumberOfPieces(int numberOfPieces) {
		ConfigFileReader.numberOfPieces = numberOfPieces;
	}
    
}
