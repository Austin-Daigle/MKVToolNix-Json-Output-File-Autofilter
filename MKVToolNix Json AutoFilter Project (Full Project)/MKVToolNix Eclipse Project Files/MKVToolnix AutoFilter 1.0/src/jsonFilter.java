import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.html.HTMLDocument.Iterator;

public class jsonFilter 
	{
	//create object for main interface class
	public static userInterface userInterfaceClassObject = new userInterface();
	//processClipboardData class object
	public static processClipboardData clipboardClassObject = new processClipboardData();
	
	static ArrayList<Integer> drivePathIndex = new ArrayList<Integer>(); // raw unordered input of opening statements
	static ArrayList<Integer> fileExtensionIndex = new ArrayList<Integer>(); // raw unordered input of closing statements	
	static ArrayList<String> drivePathDatabase = new ArrayList<String>(); //store the type of directory 	
	static ArrayList<String> fileExtensionDatabase = new ArrayList<String>(); //store the type of directory 			
	static ArrayList<String> consoleLog = new ArrayList<String>();
	public static ArrayList<String> returnConsoleAndOutput = new ArrayList<String>();
	
	static TreeMap<Integer, String> drivePathMap = new TreeMap<>();
	static TreeMap<Integer, String> fileExtensionMap = new TreeMap<>();

	static String drivePath = "";
	static String fileExtension = "";
	static String filteredJsonScriptOutput = "";
	static String consoleLogText = "";
	public static String consoleBorder = "=============================================";
	
	static String[] allSupportedFileExtensions = 
		{
			//All Supported Video Formats
			".mkv",".webm",".mp4",".m4p",".3gp",".3g2",
			".m4b",".flv",".mpg",".mp2",".mpeg",".mpv",
			".m2v",".vob",".ogv",".ogg",".rm",".rmvb",
			".ogx",
			//All Supported Audio Formats
			".mp3",".mp2",".aac",".flac",".ac3",".dts",
			".dtshd",".ogm",".spx",".opus",
			".spx",".opus",
			//All Supported Subtitles 
			".srt",".pgs",".sup",".idx",".sub",".ass",
			".ssa",
			//All Supported Picture Formats
			".jpeg",".png",".gif",".bmp"
		};
	
	static String[] allSupportedLinuxDirectoryPaths = 
		{
		//Supported Linux Root Path 	
			"/bin/","/boot/","/dev/","/etc/","/home/","/lib/","/media/",
			"/mnt/","/opt/","/sbin/","/srv/","/tmp/","/usr/","/var/",
			"/root/","/proc/",
		};	
	
	static String[] windowsDirectoryLetters = 
		{
		//Supported Windows Drive Path Letters
			"A:/", "B:/", "C:/", "D:/", "E:/", "F:/",
			"G:/", "H:/", "I:/", "J:/", "K:/", "L:/",
			"M:/", "N:/", "O:/", "P:/", "Q:/", "R:/",
			"S:/", "T:/", "U:/", "V:/", "W:/", "X:/",
			"Y:/", "Z:/",	
		};
	
	//This is the method for this class to process and return all primary data. 
	public static ArrayList<String> getFilteredResult(String givenInput,boolean isInputFromFile)
	{
		deleteVariableAndObjectData();
		cleanJsonScript(givenInput,isInputFromFile);
		returnConsoleAndOutput.add(consoleLogText);
		returnConsoleAndOutput.add(filteredJsonScriptOutput);
		return returnConsoleAndOutput;
	}

	//Delete all data from variables and objects
	public static void deleteVariableAndObjectData()
	{
		returnConsoleAndOutput.clear();
		drivePathIndex.clear();
		fileExtensionIndex.clear();
		drivePathDatabase.clear();
		fileExtensionDatabase.clear();
		consoleLog.clear();
		drivePathMap.clear();
		fileExtensionMap.clear();
		drivePath = "";
		fileExtension = "";
		filteredJsonScriptOutput = "";
		consoleLogText = "";
	}
	

	//This is the method that filters the .json script that the user enters. 
	public static void cleanJsonScript(String givenInput,boolean isInputFromFile)
	{
		//add border to console log arraylist
		consoleLog.add(consoleBorder);
		//add the correct header to the console log arraylist according to which operation call this function
		if(isInputFromFile == true)
			{
				consoleLog.add("File Input:");
			}
		else
			{
				consoleLog.add("Paste Input:");
			}
		//add a border (predefined string) to console log arraylist		
		consoleLog.add(consoleBorder);
		//add the givenInput (from the fileReader or Clipboard reader) to the console log arraylist.
		consoleLog.add(givenInput);
		//add a border (predefined string) to console log arraylist	
		consoleLog.add(consoleBorder);
		consoleLog.add(">>> Replacing all instance of backslashes to forword slashes "
				+ "to resolve Java Errors <<<");
		givenInput = givenInput.replaceAll("\\\\", "/");
		//Change all double backslashes to a single forward slash to prevent java errors
		// Inverse of the statement above: 		givenInput = givenInput.replaceAll("//", "\\\\");
		consoleLog.add(">>> Purging spaces from input <<<");
		//remove all spaces from the .json input
		givenInput = givenInput.replaceAll("\\s", "");
		consoleLog.add(">>> Scanning for all MicroSoft Windows drive letters <<<");	
		//Create all Windows OS Drive Letter and pass into verifyAndIndexDrivePaths();
		for(int i = 0; i < windowsDirectoryLetters.length; i++)
		{
			drivePath = windowsDirectoryLetters[i];
			consoleLog.add("Windows Path: "+drivePath+"");
			verifyAndIndexDrivePaths(givenInput,drivePath,drivePathIndex,drivePathDatabase,drivePathMap);
		}
		consoleLog.add(">>> Scanning for all possible Linux Directory Paths <<<");	
		//Create all Linux Drive Directories and pass into verifyAndIndexDrivePaths();
		for(int i = 0; i <allSupportedLinuxDirectoryPaths.length; i++)
		{
			drivePath = allSupportedLinuxDirectoryPaths[i];
			consoleLog.add("Linux Path: "+drivePath+"");
			verifyAndIndexDrivePaths(givenInput,drivePath,drivePathIndex,drivePathDatabase,drivePathMap);
		}
		//AutoSort drive path index
		Collections.sort(drivePathIndex);
		//Print all drive path letters and their indices
		//Create File Extension and pass to method
		consoleLog.add(">>> Scanning for all supported File Extensions <<<");
		for(int i =0; i < allSupportedFileExtensions.length; i++)
		{
			fileExtension = allSupportedFileExtensions[i]+"\",";
			consoleLog.add("Scanning for file extension: "+allSupportedFileExtensions[i]+"");
			verifyAndIndexFileExtensions(givenInput,fileExtension,fileExtensionIndex,fileExtensionDatabase,fileExtensionMap);
		}
		Collections.sort(fileExtensionIndex);
		//Print all file extension indices
		//ArrayList DataBase check
		resultErrorCheck(drivePathIndex,fileExtensionIndex);
		convertMapstoDatabases(drivePathMap,fileExtensionMap,drivePathDatabase,fileExtensionDatabase);
		
		try 
		{
			filterJsonScript(givenInput,drivePathIndex,drivePathDatabase,fileExtensionIndex,fileExtensionDatabase);
		}
		catch(Exception e)
		{

		}		
	}
	
	//This method converts the drivePath and fileExtension paths maps into Arraylists 
	@SuppressWarnings("rawtypes")
	public static void convertMapstoDatabases(TreeMap<Integer, String> drivePathMap,TreeMap<Integer, String> fileExtensionMap,
			ArrayList<String> drivePathDatabase, ArrayList<String> fileExtensionDatabase)
	{
	      for(Map.Entry x:drivePathMap.entrySet())  
	      {  
	          drivePathDatabase.add((String) x.getValue());
	      }
	      
	      for(Map.Entry x:fileExtensionMap.entrySet())  
	      {  
	    	  fileExtensionDatabase.add((String) x.getValue());
	      }
	}

	//Index all instances all drive path according the drive path array and store the char position as an index into an arraylist.
	public static void verifyAndIndexDrivePaths(String givenInput,String drivePath,ArrayList<Integer> drivePathIndex,
			ArrayList<String> drivePathDatabase,TreeMap<Integer, String> drivePathMap)
	{
		//Index all instances of possible Windows Drive Path Letters
		int lastIndexOfDrivePath = 0;		
		while(!(lastIndexOfDrivePath == -1))
		{
			lastIndexOfDrivePath = givenInput.indexOf(drivePath,lastIndexOfDrivePath);	    
		    if(!(lastIndexOfDrivePath == -1))
		    {
		    	lastIndexOfDrivePath = lastIndexOfDrivePath + drivePath.length();
		    	drivePathIndex.add(lastIndexOfDrivePath);
		    	drivePathMap.put(lastIndexOfDrivePath, drivePath);
		    }
		}
	}
	
	//Index all instances all file extension according the file extensions and store the char position as an index into an arraylist.
	public static void verifyAndIndexFileExtensions(String givenInput,String fileExtension,ArrayList<Integer> fileExtensionIndex,
			ArrayList<String> fileExtensionDatabase,TreeMap<Integer, String> fileExtensionMap)
	{
		//verifyAndIndexFileExtensions(givenInput,fileExtension,fileExtensionIndex,fileExtensionDatabase,fileExtensionMap);
		//Index all instances of supported File Extensions
		int lastIndexOfFileExt = 0;		
		while(!(lastIndexOfFileExt == -1))
		{
			lastIndexOfFileExt = givenInput.indexOf(fileExtension,lastIndexOfFileExt);	    
		    if(!(lastIndexOfFileExt == -1))
		    {
		    	lastIndexOfFileExt = lastIndexOfFileExt + fileExtension.length();
		    	fileExtensionIndex.add(lastIndexOfFileExt);
		    	fileExtensionMap.put(lastIndexOfFileExt, fileExtension);
		    }
		}
	}
	
	//store install entry from the drive path index to the console log.
	public static void printdrivePathIndex(String givenInput,String drivePath,ArrayList<Integer> drivePathIndex,
			ArrayList<String> drivePathDatabase)
	{
		//Print Status of Drive Path Index
		consoleLog.add("");
		consoleLog.add(">>> Scanning Indexes of Drive Path Letters <<<");
		for(int i = 0; i < drivePathIndex.size(); i++) 
		{
			//System.out.println(givenInput.substring(drivePathIndex.get(i)-drivePath.length(),drivePathIndex.get(i)));
			consoleLog.add("Entry: "+i+" | Index: "+drivePathIndex.get(i)+" | Drive Path: "
					+givenInput.substring(drivePathIndex.get(i)-drivePath.length(),drivePathIndex.get(i)));
		}	
		consoleLog.add("");
	}
	

	//store install entry from the file extensions index to the console log.	
	public static void printFileExtIndex(String givenInput,String fileExtension,ArrayList<Integer> fileExtensionIndex,
			ArrayList<String> drivePathDatabase)
	{
		//Print Status of Drive Path Index
		consoleLog.add("");
		consoleLog.add(">>> Scanning Indexes of File Extensions <<<");
		for(int i = 0; i < fileExtensionIndex.size(); i++) 
		{
			consoleLog.add("");
			consoleLog.add("Entry: "+i+" | Index: "+fileExtensionIndex.get(i)+" | File Extension: "
					+givenInput.substring(fileExtensionIndex.get(i)-fileExtension.length(),fileExtensionIndex.get(i)));
		}
	}
	
	//Technically a non-essential function
	public static void resultErrorCheck(ArrayList<Integer> drivePathIndex,ArrayList<Integer> fileExtensionIndex)
	{
		//Database Inequality Error Check
		if(drivePathIndex.size()>fileExtensionIndex.size())
		{
			//
			//
			//
			//
			//Create an arrayList for storing error information
			ArrayList<String> errorCheckLog = new ArrayList<String>();
			
			for(int i = 0; i < errorCheckLog.size();i++)
			{
				consoleLogText = consoleLogText + errorCheckLog.get(i)+"\n";
			}
			
			
			consoleLogText = "[ERROR]\n MISSING/RECOGNIZED OPENING STATMENT OR DRIVE PATH. "
					+ "PLEASE CHECK AND CORRECT OPENING STATMENT OR RESOURCE DIRECTORY PATH(S)."
					+ "\nDrive Path Index:\n"+drivePathIndex
					+ "\nFile Extension Index:\n"+fileExtensionIndex;
		
		}
		if(drivePathIndex.size()<fileExtensionIndex.size())
		{
			//
			//
			//
			//
			//Create an arrayList for storing error information
			ArrayList<String> errorCheckLog = new ArrayList<String>();
			
			for(int i = 0; i < errorCheckLog.size();i++)
			{
				consoleLogText = consoleLogText + errorCheckLog.get(i)+"\n";
			}
			
			consoleLogText = "[ERROR] MISSING/INVALID CLOSING STATMENT OR FILE EXTENSION. "
					+ "PLEASE CHECK AND CORRECT ENDING STATEMENT OR FILE EXTENSION."
					+ "\nDrive Path Index:\n"+drivePathIndex
					+ "\nFile Extension Index:\n"+fileExtensionIndex;

		}
	}

	public static void filterJsonScript(String givenInput,ArrayList<Integer> drivePathIndex,ArrayList<String> drivePathDatabase,
			ArrayList<Integer> fileExtensionIndex,ArrayList<String> fileExtensionDatabase)
	{
		filteredJsonScriptOutput = givenInput;
		String beginingQuote = "\"";	
		consoleLog.add("");
		consoleLog.add(">>> Printing all Ordered Drive Paths <<<");
		for(int i = 0; i < drivePathIndex.size(); i++)
		{
			consoleLog.add("Entry: "+i+" | Index: "+drivePathIndex.get(i)+" | Drive Path: "+drivePathDatabase.get(i));

		}
		consoleLog.add("");
		consoleLog.add(">>> Printing all Ordered File Extensions <<<");

		for(int i = 0; i < drivePathIndex.size(); i++)
		{
			consoleLog.add("Entry: "+i+" | Index: "+fileExtensionIndex.get(i)+" | File Extension: "+fileExtensionDatabase.get(i));
		}
		consoleLog.add("");
		consoleLog.add(">>> Removing all of the following directory path(s) from givenInput... <<<");
		consoleLog.add("---------------------------------------------------------------------------------");
		for(int i = 0; i < drivePathIndex.size(); i++)
		{
			String replaceString = "";
			consoleLog.add(
					givenInput.substring(drivePathIndex.get(i)-((drivePathDatabase.get(i).length())+(beginingQuote.length())),
					fileExtensionIndex.get(i)));
			replaceString = givenInput.substring(drivePathIndex.get(i)-((drivePathDatabase.get(i).length())+(beginingQuote.length())),
					fileExtensionIndex.get(i));
			//look here
			filteredJsonScriptOutput = filteredJsonScriptOutput.replace(replaceString, "");
		}
		consoleLog.add("---------------------------------------------------------------------------------");
		consoleLog.add(">>> Removing all instanced of \"--output\", from the input <<<");
		filteredJsonScriptOutput = filteredJsonScriptOutput.replace("\"--output\",", "");
		consoleLog.add(">>> Removing all instanced of \"(\",\")\", from the input <<<");
		filteredJsonScriptOutput = filteredJsonScriptOutput.replace("\"(\",\")\",", "");
		consoleLog.add(">>> Reverting all forword slashes to backslashes"
				+ "to maintain input origin accuracy <<<");
		filteredJsonScriptOutput = filteredJsonScriptOutput.replaceAll("//", "\\\\");

		if(filteredJsonScriptOutput.charAt(0)=='[')
		{
			filteredJsonScriptOutput = filteredJsonScriptOutput.substring(0,1)+"\n"
	    			+filteredJsonScriptOutput.substring(1);
		}
		
		
		if(filteredJsonScriptOutput.charAt(filteredJsonScriptOutput.length()-1)==']')
		{
	    	filteredJsonScriptOutput = filteredJsonScriptOutput.substring(0,filteredJsonScriptOutput.length()-1)
	    			+"\n"
	    			+filteredJsonScriptOutput.substring(filteredJsonScriptOutput.length()-1);
		}	
		int lastIndexOfComma = 0;		
		while(!(lastIndexOfComma == -1))
		{
			lastIndexOfComma = filteredJsonScriptOutput.indexOf("\",",lastIndexOfComma);	    
		    if(!(lastIndexOfComma == -1))
		    {
		    	lastIndexOfComma = lastIndexOfComma + ("\",").length();
		    	filteredJsonScriptOutput = filteredJsonScriptOutput.substring(0,lastIndexOfComma)+"\n"
		    			+filteredJsonScriptOutput.substring(lastIndexOfComma);
		    }
		}
		consoleLog.add(consoleBorder);			
		consoleLog.add("Output:");
		consoleLog.add(consoleBorder);	
		consoleLog.add(filteredJsonScriptOutput);
		for(int i = 0; i < consoleLog.size();i++)
		{
			consoleLogText = consoleLogText + consoleLog.get(i)+"\n";
		}
		
	}
	

}

