@echo off

:: Constants
set BBSDK="C:\Program Files\Research In Motion\BBSDK2\"
set JAVA_LOADER_DIR=%BBSDK%\bin\
set PROJ_DIR="%CD%"

:: Installing on device
chdir /d %JAVA_LOADER_DIR%
.\JavaLoader.exe load %PROJ_DIR%\bin\blackberrytest\StandardInstall\blackberrytest.cod

:: Returning
chdir /d %PROJ_DIR%