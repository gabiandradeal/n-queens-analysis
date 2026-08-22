# Reproduz o benchmark do estudo (Opção 2 do menu) sem interação via terminal.
# Por padrão, gera os CSVs de média em resultados_csv/testes/ (pasta de
# rascunho, ignorada pelo git) para não misturar execuções de teste com os
# dados oficiais em resultados_csv/maquinaX/. Padrão de N/rounds segue a
# metodologia usada para os dados já commitados (N = 4 a 14, 50 rodadas).
#
# Uso:
#   .\scripts\run-benchmark.ps1
#   .\scripts\run-benchmark.ps1 -MaxN 15 -Rounds 30
#   .\scripts\run-benchmark.ps1 -OutDir resultados_csv\maquina5
param(
    [int]$MinN = 4,
    [int]$MaxN = 14,
    [int]$Rounds = 50,
    [string]$OutDir = "resultados_csv/testes"
)

Set-Location (Join-Path $PSScriptRoot "..")

mvn -q clean compile exec:java `
    "-Dexec.mainClass=com.nqueens.Main" `
    "-Dexec.args=--minN $MinN --maxN $MaxN --rounds $Rounds --outDir $OutDir"
