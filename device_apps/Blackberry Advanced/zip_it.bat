:: NEEDS http://stahlforce.com/dev/index.php?tool=zipunzip
@echo off

:: Constants
set PROJ_DIR="%CD%"

chdir /d ./blackberrytest
zip -r ../blackberrytest .

chdir /d ..