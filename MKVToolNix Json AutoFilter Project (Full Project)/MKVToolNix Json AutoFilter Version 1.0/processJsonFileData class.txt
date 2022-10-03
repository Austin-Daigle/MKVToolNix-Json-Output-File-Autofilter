import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.*;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

public class processJsonFileData 
	{
	//create object for main interface class
	public static userInterface userInterfaceClassObject = new userInterface();
	//create object for filter class
	public static jsonFilter filterClassObject = new jsonFilter();

	public static void fileAndSaveFile() throws IOException
	{
	    UIManager.put("FileChooser.listFont",new javax.swing.plaf.FontUIResource("Arial", 15, 15));
		String fileStreamOutputStream = "";	
	    JFileChooser userFileChooser = new JFileChooser();
	    FileNameExtensionFilter acceptedFileTypes = new FileNameExtensionFilter("*.json", "Json"); 
	    userFileChooser.addChoosableFileFilter(new FileNameExtensionFilter("*.txt", "txt"));
	    userFileChooser.setFileFilter(acceptedFileTypes);
	    userFileChooser.setDialogTitle("Please Choose the Options.Json File to Filter");
	    userFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	    userFileChooser.setAcceptAllFileFilterUsed(true);

	    if(!(userFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION))
	    {
	    	userInterface.displayDirectoryErrorMsgGUI("File Chooser Error","No File has been selected.");
	    }  
	    else 
	    {

	    }	

	    
		File file = userFileChooser.getSelectedFile();
		try (FileInputStream fisObject = new FileInputStream(file)) 
		{
 			int content;
			while ((content = fisObject.read()) != -1) 
			{
				//convert to char and store it to a string
				fileStreamOutputStream = fileStreamOutputStream + ((char) content);
			} 
		}
		catch (IOException e) 
		{
			userInterface.displayDirectoryErrorMsgGUI("Error:","File Not Found");
		}

		ArrayList<String> getReturnData = new ArrayList<String>(jsonFilter.getFilteredResult(fileStreamOutputStream,true));
	    
	    FileWriter fileWriter = new FileWriter(userFileChooser.getSelectedFile());
	    PrintWriter printWriter = new PrintWriter(fileWriter);
	    printWriter.print(getReturnData.get(1));
	    printWriter.close();  


		//Display displayConsoleLogGUI()
		if(userInterface.displayProcessNotificationGUI(userFileChooser.getSelectedFile()
				+ " has been successfully filtered and all edits have been saved.\n"
				+ "Click \"Next\" to view the edit log or \"Quit\" to close the program."))
		{
			//Display displayConsoleLogGUI()
			userInterface.displayConsoleLogGUI(getReturnData);
		}


	}
	
}
