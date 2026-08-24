@echo off
setlocal

set "BASE_DIR=%~dp0"
set "MAVEN_VERSION=3.9.15"
set "MAVEN_HOME=%BASE_DIR%.maven\apache-maven-%MAVEN_VERSION%"
set "MAVEN_ZIP=%BASE_DIR%.maven\apache-maven-%MAVEN_VERSION%-bin.zip"

if exist "%MAVEN_HOME%\bin\mvn.cmd" goto run_maven

if not exist "%BASE_DIR%.maven" mkdir "%BASE_DIR%.maven"

if not exist "%MAVEN_ZIP%" (
    echo Downloading Apache Maven %MAVEN_VERSION%...
    powershell -NoProfile -ExecutionPolicy Bypass -Command ^
      "$ProgressPreference='SilentlyContinue'; Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/%MAVEN_VERSION%/apache-maven-%MAVEN_VERSION%-bin.zip' -OutFile '%MAVEN_ZIP%'"
    if errorlevel 1 (
        echo Failed to download Maven. Check your internet connection.
        exit /b 1
    )
)

echo Extracting Apache Maven...
powershell -NoProfile -ExecutionPolicy Bypass -Command ^
  "Expand-Archive -Force '%MAVEN_ZIP%' '%BASE_DIR%.maven'"
if errorlevel 1 (
    echo Failed to extract Maven.
    exit /b 1
)

:run_maven
call "%MAVEN_HOME%\bin\mvn.cmd" %*
exit /b %ERRORLEVEL%
