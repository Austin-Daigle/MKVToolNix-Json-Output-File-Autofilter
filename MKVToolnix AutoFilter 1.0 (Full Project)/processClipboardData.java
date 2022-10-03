import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.*;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.Color;

public class processClipboardData{
	//create object for main interface class
	public static userInterface userInterfaceClassObject = new userInterface();
	//create object for filter class
	public static jsonFilter filterClassObject = new jsonFilter();
	

	public static void scanAndFilterClipboardData() throws IOException 
	{
		String clipboardText = "";

		//Create Clipboard object and check for input data errors
	    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	    DataFlavor dataFlavor = DataFlavor.stringFlavor;
	    if (clipboard.isDataFlavorAvailable(dataFlavor)) 
	    {
	    	try 
	    	{
	    		clipboardText = (String) clipboard.getData(dataFlavor);
	    	} 
	    	catch (HeadlessException e)
	    	{
	    		userInterface.displayClipboardErrorMsgGUI("Java Clipboard Error:","Incorrect Data from Clipboard Detected:\n"
	    				+ "HeadlessException Thrown.");
	    	}
	    	catch(UnsupportedFlavorException e)
	    	{
	    		userInterface.displayClipboardErrorMsgGUI("Java Clipboard Error:","Incorrect Data from Clipboard Detected:\n"
	    				+ "UnsupportedFlavorException Thrown.");
	    	}
	    	catch(IOException e)
	    	{
	    		userInterface.displayClipboardErrorMsgGUI("Java Clipboard Error:","Incorrect Data from Clipboard Detected:\n"
	    				+ "IOException Thrown.");
	    	}
	    }
	    
	    //Check if clipboard is empty
	    if(clipboardText.isEmpty())
	    {
	    	userInterface.displayClipboardErrorMsgGUI("Java Input Error:","No data in Clipboard.");
	    }
	    else
	    {
	    	saveClipboardData(clipboardText);
	    }

	}

	public static void saveClipboardData(String clipboardText) throws IOException
	{
		//pass clipboard data into jsonFilter.getGUIResult and get getReturnData arraylist for console data and final result
		ArrayList<String> getReturnData = new ArrayList<String>(jsonFilter.getFilteredResult(clipboardText,false));	
	
		//Create clipboard object and save result to clipboard
		StringSelection saveStringSelection = new StringSelection(getReturnData.get(1));
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(saveStringSelection, null);
	
		//Display Success Notification
		if(userInterface.displayProcessNotificationGUI("Clipboard Data Processed and Saved Successfully.\n"
				+ "Click \"Next\" to view console log or Quit. "))
		{
			//Display displayConsoleLogGUI()
			userInterface.displayConsoleLogGUI(getReturnData);
		}


	}

	public static void deleteClipboardData()
	{
		//delete clipboard data
		StringSelection deletetionStringSelection = new StringSelection("");
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(deletetionStringSelection, null);
		
	}
	

	

}
