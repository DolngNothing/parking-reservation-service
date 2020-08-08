for /f "tokens=5 delims= " %%a in ('netstat -ano ^| findstr "8090" ^| findstr "LISTENING"') do set pid=%%a
if not "%pid%" == "" (
  taskkill /f /PID %pid%
) else (
  rem echo Server is not running.
)

java -jar d:\deploy\parking-reservation-service-0.0.1-SNAPSHOT.jar
