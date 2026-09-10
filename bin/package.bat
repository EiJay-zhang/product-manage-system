@echo off
echo.
echo [信息] 打包Web工程，生成 ruoyi-admin/target/pms.jar。
echo.

%~d0
cd %~dp0

cd ..
call mvn clean package -Dmaven.test.skip=true

pause