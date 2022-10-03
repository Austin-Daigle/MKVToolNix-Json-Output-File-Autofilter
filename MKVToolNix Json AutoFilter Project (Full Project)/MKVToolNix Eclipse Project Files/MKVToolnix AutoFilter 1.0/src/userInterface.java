import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.*;
import java.awt.TrayIcon.MessageType;
import java.net.MalformedURLException;

public class userInterface {
	//filter class object
	public static jsonFilter filterClassObject = new jsonFilter();
	//processClipboardData class object
	public static processClipboardData clipboardClassObject = new processClipboardData();
	//processJsonFileData class object
	public static processJsonFileData fileClassObject = new processJsonFileData();
	
	//Main Method: call setDefaultGUIStyle() & displayMainMenuGUI();
	public static void main(String[] args) throws IOException
	{
		setDefaultGUIStyle();
		displayMainMenuGUI();
	}
	
	//Create default style for GUI
	public static void setDefaultGUIStyle()
	{
		 UIManager.put("OptionPane.messageFont", new FontUIResource(new Font(  
		          "Arial", Font.BOLD, 18)));       
		 UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font(  
		          "Arial", Font.PLAIN, 15)));
	}
	
	//Create Main Menu GUI
	public static void displayMainMenuGUI() throws IOException
	{
		//Custom button text
		Object[] mainMenuOptions = {"AutoFilter ClipBoard","AutoFilter .Json File","Read Me"};
		int userMainMenuSelection = JOptionPane.showOptionDialog(null,
		    "Please Select function below...",
		    "MKVToolNix Json AutoFilter",
		    JOptionPane.YES_NO_CANCEL_OPTION,
		    JOptionPane.QUESTION_MESSAGE,
		    null,
		    mainMenuOptions,
		    mainMenuOptions[2]);
		
		switch (userMainMenuSelection)
		{
		case -1: //User clicks "X" button
			System.exit(0);
		break;
		case 0: //AutoFilter Clipboard
			processClipboardData.scanAndFilterClipboardData();
		break;
		case 1: //AutoFilter File
		    processJsonFileData.fileAndSaveFile();
		break;
		case 2: //About Page
			displayAboutPageGUI();
		break;

		}		
	}

	//Display About information page
	public static void displayAboutPageGUI() throws IOException
	{
		JTextArea textArea = new JTextArea("This is a developement edition of this editor...The Read me has yet to be written...");
		JScrollPane scrollPane = new JScrollPane(textArea);  
		textArea.setLineWrap(true);  
		textArea.setWrapStyleWord(true); 
		scrollPane.setPreferredSize(new Dimension(325,325));
		
		Font font = new Font("Dialog", Font.PLAIN, 15);
        scrollPane.getViewport().getView().setFont(font);
		
		Object[] aboutPageOptions = {"Back"};
		int userAboutPageSelection = JOptionPane.showOptionDialog(null,
			scrollPane,
		    "About This Program",
		    JOptionPane.YES_NO_CANCEL_OPTION,
		    JOptionPane.QUESTION_MESSAGE,
		    null,
		    aboutPageOptions,
		    aboutPageOptions[0]);
		
		switch (userAboutPageSelection)
		{
		case -1:
			System.exit(0);
		break;
		case 0: //AutoFilter Clipboard
			displayMainMenuGUI();
		break;
		}
		
	}
	
	//Display an error message if the Clipboard is empty
	public static void displayClipboardErrorMsgGUI(String headingInput,String errorMessageInput) throws IOException

	{
		Object[] options = {"Back to Menu"," Rescan","Quit Program"};
		int userClipboardErrorMsgSelection = JOptionPane.showOptionDialog(null,
			errorMessageInput,
			headingInput,
			JOptionPane.YES_NO_CANCEL_OPTION,
			JOptionPane.ERROR_MESSAGE,
			null,
			options,
			options[1]);
		
		switch (userClipboardErrorMsgSelection)
		{
		case -1:
			System.exit(0);
		break;
		case 0:
			userInterface.displayMainMenuGUI();
		break;
		case 1:
			processClipboardData.scanAndFilterClipboardData();
		break;
		case 2:
			System.exit(0);
		break;
		}
	}

	//Display an error message if the directory is returned as null
	public static void displayDirectoryErrorMsgGUI(String headingInput,String errorMessageInput) throws IOException
	{
		Object[] userDirectoryErrorOptions = {"Back to Menu"," Choose Directory","Quit Program"};
		int userDirectoryErrorMsgSelection = JOptionPane.showOptionDialog(null,
			errorMessageInput,
			headingInput,
			JOptionPane.YES_NO_CANCEL_OPTION,
			JOptionPane.ERROR_MESSAGE,
			null,
			userDirectoryErrorOptions,
			userDirectoryErrorOptions[1]);
		
		switch (userDirectoryErrorMsgSelection)
		{
		case -1:
			System.exit(0);
		break;
		case 0:
			userInterface.displayMainMenuGUI();
		break;
		case 1:
			processJsonFileData.fileAndSaveFile();
		break;
		case 2:
			System.exit(0);
		break;
		}
	}
	
	//Display a notification GUI upon the completion of the filtering process
	public static boolean displayProcessNotificationGUI(String notificationMessage)
	{
		boolean goNext = false;
		//Custom button text
		Object[] userProcessNotificationOptions = {"Next","Quit"};
		int userProcessNotificationSelection = JOptionPane.showOptionDialog(null,
			notificationMessage,
		    "Notification",
		    JOptionPane.YES_NO_CANCEL_OPTION,
		    JOptionPane.PLAIN_MESSAGE,
		    null,
		    userProcessNotificationOptions,
		    userProcessNotificationOptions[1]);
		switch(userProcessNotificationSelection)
		{
		case -1:
			System.exit(0);
		break;
		case 0:
			goNext = true;
		break;
		case 1:
			System.exit(0);
		break;
		}
		return goNext;
	}

	//Display Console Log
	public static void displayConsoleLogGUI(ArrayList<String> getReturnData) throws IOException
	{
		JTextArea textArea = new JTextArea(getReturnData.get(0));
		JScrollPane scrollPane = new JScrollPane(textArea);  
		textArea.setLineWrap(true);  
		textArea.setWrapStyleWord(true); 
		scrollPane.setPreferredSize(new Dimension(425,300));
		scrollPane.getViewport().getView().setBackground(Color.BLACK);
		scrollPane.getViewport().getView().setForeground(Color.GREEN);

        Font font = new Font("Dialog", Font.BOLD + Font.ITALIC, 15);
        scrollPane.getViewport().getView().setFont(font);

		Object[] consoleLogOptions = {"Menu","Quit"};
		int userConsoleLogSelection = JOptionPane.showOptionDialog(null,
			scrollPane,
		    "Console Edit Log:",
		    JOptionPane.YES_NO_CANCEL_OPTION,
		    JOptionPane.PLAIN_MESSAGE,
		    null,
		    consoleLogOptions,
		    consoleLogOptions[1]);
		
		switch (userConsoleLogSelection)
		{
		case -1:
			System.exit(0);
		break;
		case 0:
			userInterface.displayMainMenuGUI();
		break;
		case 1:
			System.exit(0);
		break;
		}	
	}


}
