import java.awt.EventQueue;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.FontUIResource;
import javax.swing.JButton;
import javax.swing.JFileChooser;

public class mainInterface {

    jsonFilter jsonInputObject = new jsonFilter();

    /*
     * This creates the application
     */
    public static void main(String[] args) {

        //create and try the runnable interface.
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    @SuppressWarnings("unused")
                    mainInterface window = new mainInterface();
                    //window.mainMenu.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * run the primary interface data and objects.
     */
    public mainInterface() {
        initialize();
    }

    /**
     * This method reads the contents of a file from a path
     * @param path this is the string of the path to the given file
     * @return return the contents of the file as a string.
     */
    public static String readContentsOfFile(String path) {
        String fileContents = "";
        try {
            fileContents = new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContents;
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {

        /*
         * CREATE UI OBJECTS FOR MENU GUI
         */

        //Create the JFrame for the main menu GUI
        //NOTE: JFrames are named and referenced by their interface name 
        JFrame mainMenu = new JFrame("MKVToolNix AutoFilter 2.0");
        mainMenu.setBounds(100, 100, 407, 245);
        mainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenu.getContentPane().setLayout(null);
        mainMenu.setLocationRelativeTo(null);
        mainMenu.setResizable(false);

        //Create the panel for the mainMenu frame
        JPanel mainMenuPanel = new JPanel();
        mainMenuPanel.setBounds(0, 0, 389, 307);
        mainMenu.getContentPane().add(mainMenuPanel);
        mainMenuPanel.setLayout(null);

        //Create the JButton for 'Filter File'
        JButton mainMenufilterFileButton = new JButton("Filter File");
        mainMenufilterFileButton.setBounds(12, 24, 366, 44);
        mainMenuPanel.add(mainMenufilterFileButton);

        //Create the JButton for 'Filter text'
        JButton mainMenuFilterTextButton = new JButton("Filter Text");
        mainMenuFilterTextButton.setBounds(12, 81, 366, 44);
        mainMenuPanel.add(mainMenuFilterTextButton);

        //Create the JButton for 'Read Me'
        JButton mainMenuReadMeButton = new JButton("Read Me");
        mainMenuReadMeButton.setBounds(12, 138, 366, 44);
        mainMenuPanel.add(mainMenuReadMeButton);
        mainMenu.setVisible(true);

        /*
         * 	CREATE UI OBJECTS FOR TEXT INPUT GUI
         */
        JFrame textInput = new JFrame("Text Input");
        textInput.setResizable(false);
        textInput.setSize(500, 440);

        //Create the text input panel object
        JPanel textInputPanel = new JPanel();
        textInputPanel.setBorder(new TitledBorder(new EtchedBorder(), "Paste/Input text below"));

        //create text box object
        JTextArea textInputArea = new JTextArea(14, 40);
        textInputArea.setLineWrap(true);
        textInputArea.setWrapStyleWord(true);
        textInputArea.setText("");

        //Create scroll object
        JScrollPane textInputAreaScroll = new JScrollPane(textInputArea);
        textInputAreaScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        textInputPanel.add(textInputAreaScroll);

        //Create return to menu button
        //NOTE: all "back" or "main Menu" buttons will be referenced as "returnButton"
        JButton textInputReturnButton = new JButton("Back");
        textInputReturnButton.setBounds(15, 265, 100, 50);
        textInput.add(textInputReturnButton);

        //Create the paste from text button
        JButton textInputPasteButton = new JButton("Paste From Clipboard");
        textInputPasteButton.setBounds(125, 265, 185, 50);
        textInput.add(textInputPasteButton);

        //Create the delete all text button
        JButton textInputClearAllButton = new JButton("Clear All [X]");
        textInputClearAllButton.setBounds(320, 265, 145, 50);
        textInput.add(textInputClearAllButton);

        //Create the submit button 
        JButton textInputSubmitTextButton = new JButton("Submit");
        textInputSubmitTextButton.setBounds(15, 325, 450, 50);
        textInputSubmitTextButton.setEnabled(false);
        textInput.add(textInputSubmitTextButton);
        textInput.add(textInputPanel);
        textInput.setLocationRelativeTo(null);
        textInput.setVisible(false);

        /*
         * CREATE UI OBJECTS FOR README PAGE
         */

        JFrame readMe = new JFrame("Read Me");
        readMe.setSize(500, 380);
        readMe.setLocationRelativeTo(null);
        readMe.setResizable(false);

        JPanel readMePanel = new JPanel();
        readMePanel.setBorder(new TitledBorder(new EtchedBorder(), "MKVToolNix AutoFilter 2.0 Read Me"));

        //create text box object
        JTextArea readMeTextArea = new JTextArea(14, 40);
        readMeTextArea.setLineWrap(true);
        readMeTextArea.setWrapStyleWord(true);
        readMeTextArea.setText("		General Information:"
        		+ "\n---------------------------------------------------------------------" 
        		+ "------------------------------"
        		+ "\nThis program is a third-party "
        		+ "automation tool design to assist the "
        		+ "bulk-editing of media content for the media metadata/attribute editing "
        		+ "program MKVToolNix. This program is not affiliated with MKVToolNix. "
        		+ "\n"
        		+ "This program is designed to remove directories and code conflicts for "
        		+ "the auto-generated option .json configuration file from MKVToolNix. This "
        		+ "program functions on the premise of identifying directories by searching"
        		+ " for a root directory (like C:\\ or D:\\ for Windows) and the file extension"
        		+ " to find and remove directories from the code. If a file extension or"
        		+ " directory root is missing from a directory, then that directory is invalid,"
        		+ " resulting in an error. Once each directory has been identified, that directory"
        		+ " will be removed, and all other flagged code statements to be removed"
        		+ " (such as –output,).\n"
        		+ "\n---------------------------------------------------------------------"
        		+ "------------------------------"
        		+ "\n		General Troubleshooting"
        		+ "\n---------------------------------------------------------------------"
        		+ "------------------------------"
        		+ "\nSuppose an error has been found from the console log. In that case, this"
        		+ " is likely due to either a missing file extension/directory root or an "
        		+ "incorrect order of an extension/directory root (for example, a file "
        		+ "extension coming before the directory root, etc.)."
        		+ "Errors can be corrected by checking to see if every directory has the"
        		+ " correct directory structure, i.e., having the drive path first and the"
        		+ " file extension last. The console log will show the order of the directories"
        		+ " as they are scanned and the status of the directory's integrity. If there"
        		+ " is an error with the directory, an error code will be given in the status"
        		+ " update. To fix a directory error, look for the missing/incorrect root path"
        		+ " or file extension and introduce the correct root path/file extension. \r\n"
        		+ "\n---------------------------------------------------------------------"
        		+ "------------------------------"
        		+ "\n		Error Codes:"
        		+ "\n---------------------------------------------------------------------"
        		+ "------------------------------"
        		+ "\n Error Code:		Explanation:"
        		+ "\n 00A	Two file extensions have been detected for the position of "
        		+ "		the root path And the file extension."
        		+ "\n 00B	Two directory roots have been detected for the position of"
        		+ "		the root path and the file extension." 
        		+ "\n 00C	The directory root is in the position of the file extension, "
        		+ "		and the file extension is in the position of the directory root."
        		+ "\n 00D	Missing file extension."
        		+ "\n 00E	Missing directory path.");
        readMeTextArea.setEditable(false);

        //Create scroll object
        JScrollPane readMeTextScroll = new JScrollPane(readMeTextArea);
        readMeTextScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        readMePanel.add(readMeTextScroll);
        JButton readMeReturnButton = new JButton("Back");
        readMeReturnButton.setBounds(155, 265, 170, 50);
        readMe.add(readMeReturnButton);
        readMe.add(readMePanel);

        readMe.setVisible(false);

        /*
         * CREATE UI OBJECTS FOR OUTPUT CONSOLE
         */
        JFrame consoleOutput = new JFrame("Result");
        consoleOutput.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        consoleOutput.setBounds(100, 100, 502, 380);
        consoleOutput.getContentPane().setLayout(null);
        consoleOutput.setLocationRelativeTo(null);
        consoleOutput.setResizable(false);

        JTabbedPane resultTabs = new JTabbedPane(JTabbedPane.TOP);
        resultTabs.setBounds(12, 13, 462, 252);
        consoleOutput.getContentPane().add(resultTabs);

        TextArea systemLogTextArea = new TextArea("", 10, 10, TextArea.SCROLLBARS_VERTICAL_ONLY);
        resultTabs.addTab("Console Log", null, systemLogTextArea, null);

        TextArea consoleOutputTextArea = new TextArea("", 10, 10, TextArea.SCROLLBARS_VERTICAL_ONLY);
        resultTabs.addTab("Result", null, consoleOutputTextArea, null);

        JButton consoleOutputClipboardCopyButton = new JButton("Copy Result To Clipboard");
        consoleOutputClipboardCopyButton.setBounds(12, 278, 178, 40);
        consoleOutput.getContentPane().add(consoleOutputClipboardCopyButton);

        JButton consoleOutputReturnButton = new JButton("Main Menu");
        consoleOutputReturnButton.setBounds(202, 278, 129, 40);
        consoleOutput.getContentPane().add(consoleOutputReturnButton);

        JButton consoleOutputCloseProgram = new JButton("Close Program");
        consoleOutputCloseProgram.setBounds(343, 278, 129, 40);
        consoleOutput.getContentPane().add(consoleOutputCloseProgram);
        consoleOutput.setVisible(true);
        consoleOutput.setVisible(false);


        /*
         * 
         * ACTION LISTENERS
         * 
         */

        /*
         * Action Listeners for Main Menu UI
         */
        mainMenufilterFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainMenu.setVisible(false);


                JFileChooser fileSelector = new JFileChooser("c:");
                FileNameExtensionFilter acceptedFileTypes = new FileNameExtensionFilter("*.json", "json");
                fileSelector.addChoosableFileFilter(new FileNameExtensionFilter("*.txt", "txt"));
                fileSelector.setFileFilter(acceptedFileTypes);
                fileSelector.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileSelector.setAcceptAllFileFilterUsed(true);

                if (!(fileSelector.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)) {
                    System.out.println("Error No File Choosen!");
                } else {
                    jsonInputObject.setJsonInput(readContentsOfFile(fileSelector.getSelectedFile() + ""));
                    consoleOutputTextArea.setText(jsonInputObject.getJsonOutput());
                    systemLogTextArea.setText("All Data Read Successfully from file..."
                        +
                        "\nSelected File Path: " + fileSelector.getSelectedFile() +
                        "\nContents From: " + fileSelector.getName(fileSelector.getSelectedFile())
                        +
                        "\n" + jsonInputObject.getConsoleLog());
                    consoleOutput.setVisible(true);

                    //If there are no errors found
                    if (!jsonInputObject.getErrorStatus()) {
                        //write all filtered data to file
                        try {
                            FileWriter myWriter = new FileWriter(fileSelector.getSelectedFile());
                            myWriter.write(jsonInputObject.getJsonOutput());
                            myWriter.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }


                }

            }
        });

        mainMenuFilterTextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainMenu.setVisible(false);
                textInputArea.setText("");
                textInputSubmitTextButton.setEnabled(false);
                textInput.setVisible(true);
            }
        });

        mainMenuReadMeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainMenu.setVisible(false);
                readMe.setVisible(true);
            }
        });

        /*
         * Action Listeners for Text Input UI
         */
        textInputReturnButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textInput.setVisible(false);
                mainMenu.setVisible(true);
            }
        });

        textInputPasteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Toolkit toolkitObject = Toolkit.getDefaultToolkit();
                Clipboard clipboardObject = toolkitObject.getSystemClipboard();
                String clipboardText;
                try {
                    clipboardText = (String) clipboardObject.getData(DataFlavor.stringFlavor);
                    textInputArea.setText(textInputArea.getText() + clipboardText);
                } catch (UnsupportedFlavorException | IOException e1) {
                    e1.printStackTrace();
                }


                if ((readMeTextArea.getText().length() <= 0)) {
                    textInputSubmitTextButton.setEnabled(false);
                } else {
                    textInputSubmitTextButton.setEnabled(true);
                }
            }
        });

        textInputClearAllButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textInputArea.setText("");
                textInputSubmitTextButton.setEnabled(false);
            }
        });

        textInputArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent arg0) {
                if ((textInputArea.getText().length() <= 0)) {
                    textInputSubmitTextButton.setEnabled(false);
                } else {
                    textInputSubmitTextButton.setEnabled(true);
                }
            }
        });

        textInputSubmitTextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jsonInputObject.setJsonInput(textInputArea.getText());
                textInput.setVisible(false);
                consoleOutputTextArea.setText(jsonInputObject.getJsonOutput());
                systemLogTextArea.setText("All Data Read Successfully from text input...\n"+
                jsonInputObject.getConsoleLog());
                consoleOutput.setVisible(true);
            }
        });


        /*
         * Action Listeners for Read Me UI
         */
        readMeReturnButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                readMe.setVisible(false);
                mainMenu.setVisible(true);
            }
        });


        /*
         * Action Listeners for Console Output UI
         */
        consoleOutputClipboardCopyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                StringSelection selection = new StringSelection(jsonInputObject.getJsonOutput());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, selection);
                consoleOutputClipboardCopyButton.setText("Copied!");
            }
        });

        consoleOutputReturnButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                consoleOutput.setVisible(false);
                mainMenu.setVisible(true);
            }
        });

        consoleOutputCloseProgram.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

    }



}