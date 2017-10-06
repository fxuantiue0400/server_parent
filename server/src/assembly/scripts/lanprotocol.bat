@ECHO OFF
rem #
rem # Copyright (c) 2012-2015 Andrea Selva
rem #
                                                                      "
set "CURRENT_DIR=%cd%"
rem if not "%LAN_HOME%" == "" goto gotHome
set "LAN_HOME=%CURRENT_DIR%"
if exist "%LAN_HOME%\bin\lanprotocol.bat" goto okHome
cd ..
set "LAN_HOME=%cd%"
cd "%CURRENT_DIR%"
:gotHome
if exist "%LAN_HOME%\bin\lanprotocol.bat" goto okHome
    echo "%LAN_HOME%\bin\lanprotocol.bat"
    echo The LAN_HOME environment variable is not defined correctly
    echo This environment variable is needed to run this program
goto end
:okHome

rem Set JavaHome if it exists
if exist { "%JAVA_HOME%\bin\java" } (
    set "JAVA="%JAVA_HOME%\bin\java""
)

echo Using JAVA_HOME:       "%JAVA_HOME%"
echo Using LAN_HOME:   "%LAN_HOME%"

rem  set LOG_CONSOLE_LEVEL=info
rem  set LOG_FILE_LEVEL=fine
set JAVA_OPTS=
set JAVA_OPTS_SCRIPT=-Xms200m -Xmx200m -Xss256k -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=d:\javaHeapDump.hprof -Djava.awt.headless=true
set LAN_PATH=%LAN_HOME%
set LOG_FILE=%LAN_HOME%\config\logback.xml
%JAVA% -server %JAVA_OPTS% %JAVA_OPTS_SCRIPT% -Dname=lanprotocolServer -Dlogging.config=file:%LOG_FILE% -Dlan.path=%LAN_PATH% -cp %LAN_HOME%\lib\* xx.BootStrap
