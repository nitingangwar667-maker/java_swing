@echo off
echo ==========================================
echo   Online Course Registration & College Management System
echo ==========================================

if not exist bin mkdir bin

echo [1/2] Compiling Java source files...
javac -d bin -cp "lib/*" src/course/reg/*.java src/com/college/model/*.java src/com/college/util/*.java src/com/college/dao/*.java src/com/college/view/*.java src/com/college/*.java

if %errorlevel% neq 0 (
    echo Compilation failed! Please check if mysql-connector-j jar is present in the 'lib' folder.
    pause
    exit /b %errorlevel%
)

echo [2/2] Launching Application...
java -cp "bin;lib/*" com.college.Main
pause
