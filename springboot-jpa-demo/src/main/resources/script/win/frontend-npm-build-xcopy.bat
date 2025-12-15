set "CURRENT_DIR=%cd%"

cd src\main\webapp
call npm run build

copy /Y %CURRENT_DIR%\src\main\webapp\errorpage.html %CURRENT_DIR%\src\main\resources\webapp\

cd %CURRENT_DIR%\src\main\admin_webapp
call npm run build

cd %CURRENT_DIR%
md src\main\resources\webapp\admin
xcopy /Y /S /E /H /R /C src\main\admin_webapp\build\*.* src\main\resources\webapp\admin

