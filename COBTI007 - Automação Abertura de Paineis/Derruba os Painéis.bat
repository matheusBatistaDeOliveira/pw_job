REM DERRUBAR

for /f "tokens=5" %%A in ('netstat -ano ^| findstr ":1812 .*LISTENING"') do taskkill /F /T /PID %%A
for /f "tokens=5" %%A in ('netstat -ano ^| findstr ":1911 .*LISTENING"') do taskkill /F /T /PID %%A
for /f "tokens=5" %%A in ('netstat -ano ^| findstr ":6842 .*LISTENING"') do taskkill /F /T /PID %%A
for /f "tokens=5" %%A in ('netstat -ano ^| findstr ":9473 .*LISTENING"') do taskkill /F /T /PID %%A
for /f "tokens=5" %%A in ('netstat -ano ^| findstr ":6961 .*LISTENING"') do taskkill /F /T /PID %%A
for /f "tokens=5" %%A in ('netstat -ano ^| findstr ":5856 .*LISTENING"') do taskkill /F /T /PID %%A
for /f "tokens=5" %%A in ('netstat -ano ^| findstr ":8088 .*LISTENING"') do taskkill /F /T /PID %%A
netstat -ano | findstr ":1812 :1911 :6842 :9473 :6961 :5856"
pause