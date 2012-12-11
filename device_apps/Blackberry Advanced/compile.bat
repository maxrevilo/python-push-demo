@echo off

:: Constants
set BBSDK="C:\Program Files\Research In Motion\BBSDK2\"
set JAVA_LOADER_DIR=%BBSDK%\bin\
set PROJ_DIR="%CD%"

:: Compiling
chdir /d %BBSDK%
.\bbwp.exe %PROJ_DIR%\blackberrytest.zip -g G1403Pos -o %PROJ_DIR%\bin\blackberrytest

:: Returning
chdir /d %PROJ_DIR%