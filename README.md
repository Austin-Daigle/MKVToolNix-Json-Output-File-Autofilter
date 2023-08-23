# MKVToolNix-Json-Output-File-Autofilter
This program removes directories and conflicting .json code (media directory data and "--output",) from the output.json file the MKVToolNix created. This is meant to assist in automating processing files through MKVToolNix with Windows Batch files by optimizing the output.json file for automation via batch script (The batch automation script in this project merges subtitles to its respective video file).

# How to Use:
* Ensure that [MKVToolNix](https://mkvtoolnix.download/downloads.html) is installed in your environment.
* Download the executable jar file or compile the source code for the MKVToolNix AutoFilter 2.0 Application.
* Ensure all media assets, subtitles, and automation script(s) are included in the same directory.
* Open MKVToolNix and load the media assets into the "source files" sub-window, then under the Multiplexer tab, select "create option file" to
make the options.json file.
* Start/Execute MKVToolNix Autofilter 2.0

* ![image](https://github.com/Austin-Daigle/MKVToolNix-Json-Output-File-Autofilter/assets/100094056/910a2a11-91ee-4c34-83be-deec86a991cb)
* Select the "options.json" file inside the file chooser and select Open.

![image](https://github.com/Austin-Daigle/MKVToolNix-Json-Output-File-Autofilter/assets/100094056/3d0fddba-1bf8-4e1c-b647-616bb5a62b95)
* The program will auto-filter and update the contents of the options.json file and open a tabbed terminal screen showing the console and
result log.

![image](https://github.com/Austin-Daigle/MKVToolNix-Json-Output-File-Autofilter/assets/100094056/acb5951a-9193-4031-b199-dd96f1e45ccd)
![image](https://github.com/Austin-Daigle/MKVToolNix-Json-Output-File-Autofilter/assets/100094056/fcca8638-ed8d-4539-8b36-4f479f066319)
* The user can copy the result to the clipboard, close the program, or return to the main menu.
* Finally, navigate to the directory containing the media assets, automation script, and options.json file and execute the batch script.
* If setup correctly, the media assets (.mkv files) should have their respective .srt subtitles files merged into them.

