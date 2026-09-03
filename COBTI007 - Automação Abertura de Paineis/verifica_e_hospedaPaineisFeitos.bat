@echo off

REM Verifica se já existe algum mvnw rodando
tasklist /v | findstr /i "mvnw.cmd" > nul

IF %ERRORLEVEL% EQU 0 (
    echo Aplicacoes ja estao rodando.
    exit
)

REM Painel UZ
start "Painel UZ" cmd /k "cd /d "C:\MAG - TI\Status UZ - GS\Painel_BD" && mvnw.cmd spring-boot:run"

REM GEDI
start "GEDI" cmd /k "cd /d "C:\MAG - TI\GEDI - GS\painel_BD" && mvnw.cmd spring-boot:run"

REM Erros
start "Erros" cmd /k "cd /d "C:\MAG - TI\Erros\painel_BD" && mvnw.cmd spring-boot:run"

REM Consulta UZ
start "Consulta UZ" cmd /k "cd /d "C:\MAG - TI\Consulta UZ\painel_BD" && mvnw.cmd spring-boot:run"

REM Fluxo de Caixa
start "Fluxo de Caixa" cmd /k "cd /d "C:\MAG - TI\Painel_Com_BD\Novo\demo" && mvnw.cmd spring-boot:run"

REM JOB
start "JOB" cmd /k "cd /d "C:\MAG - TI\JOB\painel_BD" && mvnw.cmd spring-boot:run"

REM TESTE
start "TESTE" cmd /k "cd /d "C:\MAG - TI\Consulta UZ - Copy\painel_BD" && mvnw.cmd spring-boot:run"
