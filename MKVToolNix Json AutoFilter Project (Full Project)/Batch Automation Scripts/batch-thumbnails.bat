@echo off
FOR %%f IN (*.mkv) DO (
"C:\Program Files\MKVToolNix\mkvpropedit.exe" "%%~nf.mkv" --attachment-name "cover" --attachment-mime-type "image/jpeg" --add-attachment "%%~nf.jpg"
)
pause