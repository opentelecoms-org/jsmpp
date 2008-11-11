@echo off
SETLOCAL

REM Set Application home directory
SET APP_HOME=%~dp0..

cd %APP_HOME%

SET LIB_DIR=lib\*.jar

@FOR %%V IN (%LIB_DIR%) DO call "stress\lcp.bat" %%~fV

@FOR %%V IN (*.jar) DO call "stress\lcp.bat" %%~fV

SET CLASSPATH=%LOCALCLASSPATH%
echo %CLASSPATH%

SET LOCALCLASSPATH=

"%JAVA_HOME%\bin\java.exe" %* org.jsmpp.examples.StressClient

ENDLOCAL