import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.io.*;

/**
 * @author Austin
 * 
 *<h2>jsonFilter.class</h2>
 *
 * This constructor class filters and removes all of the defined conflicts and resource directory
 * paths in the user-provided .json code. The user-provided .json code is actually data from a 
 * file that is automatically generated as "options.json" by the program <b>MKVToolnix GUI</b>, this 
 * file is automatically generated to store media resource attributes/metadata and location
 * directories. 
 * 
 * In order to automatically batch-edit media files using batch scripts with <b>MKVToolnix GUI</b> all 
 * directory data relating to the location of resources (media location on a drive) must be identified
 * and removed, and any conflicting .json code attributes such as "--output," must also be removed in 
 * order to ensure a successful automation operation. 
 *  
 */

public class jsonFilter {
    /*
     * This area is reserved for the initialization and declaration of all of the 
     * variables and objects that will be used in the following methods. 
     */

    //this is a string variable to store the user-provided string of the .json code input.
    private String jsonInput = "";

    //This is a string variable to store the output of the filtered json code.
    private String jsonOutput = "";

    //This is a string variable to store the output of the filtering process console log.
    private String consoleLog = "";

    //this 1D array provides a list of supported file extensions for the filter() method. 
    private String[] allSupportedFileExtensions = {
        //All Supported Video Formats
        ".mkv",
        ".webm",
        ".mp4",
        ".m4p",
        ".3gp",
        ".3g2",
        ".m4b",
        ".flv",
        ".mpg",
        ".mp2",
        ".mpeg",
        ".mpv",
        ".m2v",
        ".vob",
        ".ogv",
        ".ogg",
        ".rm",
        ".rmvb",
        ".ogx",
        ".avi",
        //All Supported Audio Formats
        ".mp3",
        ".mp2",
        ".aac",
        ".flac",
        ".ac3",
        ".dts",
        ".dtshd",
        ".ogm",
        ".spx",
        ".opus",
        ".spx",
        ".opus",
        //All Supported Subtitles 
        ".srt",
        ".pgs",
        ".sup",
        ".idx",
        ".sub",
        ".ass",
        ".ssa",
        //All Supported Picture Formats
        ".jpeg",
        ".png",
        ".gif",
        ".bmp"
    };

    //Create a String ArrayList to store all of the directory root paths 
    ArrayList < String > allSystemRootPaths = new ArrayList < String > ();
    File[] rootPaths = File.listRoots();

    //This is a database that stores the indexes and string info of all root path and file extensions in order.
    private TreeMap < Integer, String > rootPathsAndFileExtensions = new TreeMap < Integer, String > ();
    //This is a database that stores the indexes and string info of all root paths in order.
    private TreeMap < Integer, String > rootPathDatabase = new TreeMap < Integer, String > ();
    //This is a database that stores the indexes and string info of all file extensions in order
    private TreeMap < Integer, String > fileExtensionDatabase = new TreeMap < Integer, String > ();
    //This is a database that stores the indexes and string info of all file extension/root path indices and the condition of being a root path or file extension
    private TreeMap < Integer, String > entryTypeDatabase = new TreeMap < Integer, String > ();
    //This boolean variable is used to indicate an error in the .json input
    private boolean isErrorPresent = false;
    //This ArrayList has all of the values from the Values of entryTypeDatabase to be processed to identify input errors.
    private ArrayList < String > entryTypeErrorCheck = new ArrayList < String > ();
    //This ArrayList has all of the filtered strings that will be removed from jsonInput
    private ArrayList < String > flaggedJsonStrings = new ArrayList < String > ();
    //This ArrayList has all of the predefined json strings that will be removed
    //The removed strings can appear multiple times or a single instance but they do not need a screening process. 
    private ArrayList < String > jsonCodeBlackList = new ArrayList < String > ();


    //Default constructor method
    public jsonFilter() {
        //Clear all variables/objects
        jsonInput = "";
        jsonOutput = "";
        consoleLog = "";
        rootPathsAndFileExtensions.clear();
        rootPathDatabase.clear();
        fileExtensionDatabase.clear();
        entryTypeDatabase.clear();
        isErrorPresent = false;
        entryTypeErrorCheck.clear();
        flaggedJsonStrings.clear();
        jsonCodeBlackList.clear();
        //add basic json filters to jsonCodeFilter
        jsonCodeBlackList.add("\"(\",");
        jsonCodeBlackList.add("\")\",");
        jsonCodeBlackList.add("\"--output\",");
        jsonCodeBlackList.add("\n");

    }

    //setter for jsonInput
    public void setJsonInput(String jsonInput) {
        this.jsonInput = jsonInput;

        for (int i = 0; i < rootPaths.length; i++) {
            System.out.println(rootPaths[i].toString() + " Has been detected and added to index");
            allSystemRootPaths.add(rootPaths[i].toString());
        }
        //run the filter process.
        filter();
    }

    //getter for jsonInput
    public String getJsonInput() {
        return jsonInput;
    }

    //getter for jsonOutput 
    public String getJsonOutput() {
        return jsonOutput;
    }

    //getter for consoleLog
    public String getConsoleLog() {
        return consoleLog;
    }

    public Boolean getErrorStatus() {
        return isErrorPresent;
    }


    /**
     * This method is the primary function that filters all of the json code data.
     */
    public void filter() {
        //remove all spaces in the variable jsonInput
        jsonInput = jsonInput.replaceAll(" ", "");
        consoleLog = consoleLog + "=============================================================";
        consoleLog = consoleLog + "\n" + jsonInput + "\n";
        consoleLog = consoleLog + "=============================================================";
        //Execute primary filter method
        consoleLog = consoleLog + "\n	>>>> SEARCHING FOR ALL SYSTEM ROOT PATHS <<<<";

        //Scan For all System Root Paths
        for (int i = 0; i < allSystemRootPaths.size(); i++) {
            consoleLog = consoleLog + "\n" +
                "[Process] Searching for all instances for " + allSystemRootPaths.get(i);
            stringFinder(jsonInput, allSystemRootPaths.get(i), true);
        }
        consoleLog = consoleLog + "\n	>>>> SEARCHING FOR ALL SUPPORTED FILE EXTENSIONS <<<<";
        //Scan for all Supported File extensions
        for (int i = 0; i < allSupportedFileExtensions.length; i++) {
            consoleLog = consoleLog + "\n[Process] Searching for all instances of " +
                allSupportedFileExtensions[i];
            stringFinder(jsonInput, allSupportedFileExtensions[i], false);
        }

        /*
         * this method checks the inputs for discrepancies/mismatched/missing file
         * extensions/root paths.
         */
        inputErrorCheck();
        if (!isErrorPresent) {
            //This method find the strings to remove from the console output.
            findStringsToRemove();
            //set the value of the jsonOutput to the value of jsonInput
            jsonOutput = jsonInput;
            consoleLog = consoleLog + "\n[Alart] The following Strings have been marked for deletion:";
            consoleLog = consoleLog +
                "\n-------------------------------------------------------------" +
                "---------------------------------------------";
            //This for loop delete all of the detected directory paths marked for deletion
            for (int i = 0; i < flaggedJsonStrings.size(); i++) {
                consoleLog = consoleLog + "\n" + flaggedJsonStrings.get(i);
                deleteAllInstancesOfString(flaggedJsonStrings.get(i));
            }
            consoleLog = consoleLog +
                "\n-------------------------------------------------------------" +
                "---------------------------------------------";

            //Delete all data from the output from the entries on the jsonCodeBlacklist
            for (int i = 0; i < jsonCodeBlackList.size(); i++) {
                //delete all instances of the string from the output.
                deleteAllInstancesOfString(jsonCodeBlackList.get(i));
            }
            //This calls the auto-format method to "clean-up" the output.
            autoFormat(jsonOutput);
            consoleLog = consoleLog + "\n[Alart] The filtering process has " +
                "been executed successfully. Click on the \"Result\"" +
                " tab to see the filtered .json code.";
        }
    }

    /*
     * This method deletes all instances of a string within the jsonOutput string.
     */
    private void deleteAllInstancesOfString(String markedToDelete) {
        //replace all instances of the markedToDelete string to "".
        jsonOutput = jsonOutput.replace(markedToDelete, "");
    }

    /*
     * This method automatically add new line between 
     * each of the statements and brackets in jsonOutput.
     */

    private void autoFormat(String jsonOutput) {
        consoleLog = consoleLog + "\n[Process] Auto-Formating the .json code output.";
        //add a newline after each comma.
        this.jsonOutput = jsonOutput.replace(",", ",\n");
        //If there is a opening bracket at the beginning of the string then add a space.
        if (jsonOutput.charAt(0) == '[') {
            jsonOutput = jsonOutput.substring(0, 1) + "\n" +
                jsonOutput.substring(1);
        }
        //If there is a closing bracket then add a newline before the closing bracket.
        if (jsonOutput.charAt(jsonOutput.length() - 1) == ']') {
            jsonOutput = jsonOutput + "\n";
        }
    }

    /**
     * This method takes all of of the indexes of rootPathsAndFileExtensionsIndices to add to list of string to remove.
     * Basically this method removes directories
     */
    private void findStringsToRemove() {
        //create a local ArrayList to store all of the indices of directory root paths and file extensions
        ArrayList < Integer > rootPathsAndFileExtensionsIndices = new ArrayList < Integer > ();
        //for every object in the entryType database.
        for (Entry < Integer, String > i: entryTypeDatabase.entrySet()) {
            rootPathsAndFileExtensionsIndices.add(i.getKey());
        }
        //for every entry in rootPathsAndFileExtensionsIndices
        for (int i = 0; i < rootPathsAndFileExtensionsIndices.size(); i = i + 2) {
            flaggedJsonStrings.add(jsonInput.substring(rootPathsAndFileExtensionsIndices.get(i), rootPathsAndFileExtensionsIndices.get(i + 1)));
        }
    }

    /*
     * This method printout all of the indices of the found string "searchFor" from the input string "searchFrom"
     * searchFrom: the string to search for 
     * searchFor: the string to search from
     * IsRootPath: if the searched word is a rootpath then input that into the function
     * Create a variable to save the last index of the string searchFor from searchFrom.
     */
    private void stringFinder(String searchFrom, String searchFor, Boolean isRootPath) {
        int lastIndexOfString = 0;
        /*while the value of lastIndexOfString is not -1 (meaning that there is at least one 
         * more searchFor string inside of searchFrom). 
         * A value of -1 from the method indexOf() indicated that the string instance does not occur. 
         */
        while (!(lastIndexOfString == -1)) {
            //find the index of searchFor from searchFrom and return the value of the closest instance of that string. 
            lastIndexOfString = searchFrom.indexOf(searchFor, lastIndexOfString);
            //If the current value of LastIndexOfString is not equal to -1 then...
            if (!(lastIndexOfString == -1)) {
                //Add the length of searchFor.length to the current value of lastIndexOfString
                lastIndexOfString = lastIndexOfString + searchFor.length();
                //Printout result (realistically add a database entry here...)
                consoleLog = consoleLog + "\n    --> Entry Found at index: " + lastIndexOfString;

                //Add entry into directoryPathsAndFileExtensions, the index value is the value of the found string and the string is the string to search for.
                rootPathsAndFileExtensions.put(lastIndexOfString, searchFor);

                //If rootPaths are being searched for.
                if (isRootPath) {
                    //a offset of searchFor.length() has been added to the rootPath data to include the length of the root path
                    //an additional offset has been added to account for the quotation mark before the statement.
                    //If searchFinder is true then add entry into rootPathDatabase 
                    rootPathDatabase.put(lastIndexOfString - (searchFor.length() + 1), searchFor);
                    //Add a string value and an index value for the errorCheckDatabase for later analysis
                    entryTypeDatabase.put(lastIndexOfString - (searchFor.length() + 1), "rootPath: " + searchFor);
                } else {
                    //an additional offset has been added to account for the quotation marks/commas for the end of a statement. 
                    //If searchFinder is false then add entry into fileExtensionDatabase
                    fileExtensionDatabase.put(lastIndexOfString + 2, searchFor);
                    //Add a string value and an index value for the errorCheckDatabase for later analysis
                    entryTypeDatabase.put(lastIndexOfString + 2, "fileExtension: " + searchFor);
                }
            }
        }
    }

    /*
     * This method scans the input to detect missing file extensions or 
     * directory roots. If an error is detected then the rest of the 
     * process will be interrupted and a error message will be displayed.
     */
    private void inputErrorCheck() {
        //for every Value in entryTypeDatebase and add that to entryTypeErrorCheck
        for (Entry < Integer, String > x: entryTypeDatabase.entrySet()) {
            entryTypeErrorCheck.add(x.getValue());
        }

        consoleLog = consoleLog + "\n[Alart] Scanning for discrepancies...";

        //if there is an odd amount of entries in entryTypeErrorCheck then add a "missing statement" placeholder for further analysis
        if (!(entryTypeErrorCheck.size() % 2 == 0)) {
            consoleLog = consoleLog +
                "\n[Error!] Missing file extension/directory root path Detected. " +
                "Processing placeholder has been added entry added...";
            entryTypeErrorCheck.add("Missing Statement");
        }
        consoleLog = consoleLog + "\n	>>> VERIFYING DIRECTORIES TO REMOVE <<<";
        consoleLog = consoleLog +
            "\n-------------------------------------------------------------" +
            "---------------------------------------------";
        //Scan data from entryTypeErrorCheck and then save all errors to errorAnalysisLog
        for (int i = 0; i < entryTypeErrorCheck.size(); i = i + 2) {
            //If state for a valid directory path is not found then go through error-check states.
            if (entryTypeErrorCheck.get(i).contains("rootPath") && entryTypeErrorCheck.get(i + 1).contains("fileExtension")) {
                consoleLog = consoleLog + "\n	[Status] current directory validated.";
            } else if (entryTypeErrorCheck.get(i).contains("fileExtension") && entryTypeErrorCheck.get(i + 1).contains("fileExtension")) {
                consoleLog = consoleLog + "\n	[Error!] Discrepancy detected, Error code: 00A.";
                isErrorPresent = true;
            } else if (entryTypeErrorCheck.get(i).contains("rootPath") && entryTypeErrorCheck.get(i + 1).contains("rootPath")) {
                consoleLog = consoleLog + "\n	[Error!] Discrepancy detected, Error code: 00B.";
                isErrorPresent = true;
            } else if (entryTypeErrorCheck.get(i).contains("fileExtension") && entryTypeErrorCheck.get(i + 1).contains("rootPath")) {
                consoleLog = consoleLog + "\n	[Error!] Discrepancy detected, Error code: 00C.";
                isErrorPresent = true;
            } else if (entryTypeErrorCheck.get(i).contains("rootPath") && entryTypeErrorCheck.get(i + 1).contains("Missing Statement")) {
                consoleLog = consoleLog + "\n	[Error!] Discrepancy detected, Error code: 00D.";
                isErrorPresent = true;
            } else if (entryTypeErrorCheck.get(i).contains("Missing Statement") && entryTypeErrorCheck.get(i + 1).contains("rootPath")) {
                consoleLog = consoleLog + "\n	[Error!] Discrepancy detected, Error code: 00E.";
                isErrorPresent = true;
            }
        }
        consoleLog = consoleLog +
            "\n-------------------------------------------------------------" +
            "---------------------------------------------";


        //Printout if an error statement has been detected...
        if (isErrorPresent) {
            consoleLog = consoleLog + "	>>> INDEX DATABASE AND ANALYSIS DATA <<<";
            consoleLog = consoleLog +
                "\n-------------------------------------------------------------" +
                "---------------------------------------------";
            consoleLog = consoleLog + "\nrootPathDatabase: " + rootPathDatabase;
            consoleLog = consoleLog + "\n\nAmount of Entries in rootPathDatabase: " + rootPathDatabase.size();
            consoleLog = consoleLog + "\n\nfileExtensionDatabase: " + fileExtensionDatabase;
            consoleLog = consoleLog + "\n\nAmount of Entries in fileExtensionDatabase: " + fileExtensionDatabase.size();
            consoleLog = consoleLog + "\n\nrootPathsAndFileExtensions: " + rootPathsAndFileExtensions;
            consoleLog = consoleLog + "\n\nAmount of Entries in rootPathsAndFileExtensions: " + rootPathsAndFileExtensions.size();
            consoleLog = consoleLog + "\n\nentryTypeDatabase: " + entryTypeDatabase;
            consoleLog = consoleLog +
                "\n-------------------------------------------------------------" +
                "---------------------------------------------";

            consoleLog = consoleLog + "\n[Alart] missing/mismatch/unrecognized statements found!" +
                " Please check all directory and code statements to resolve any and all" +
                " error(s) that have been detected. Use the \"Read Me\" page to find the" +
                "error code key and trouble shooting guide.";
        } else {
            consoleLog = consoleLog + "\n[Success] No discrepancies found.";
        }

    }

}