#!/usr/bin/env bash
# Reproduz o benchmark do estudo (Opção 2 do menu) sem interação via terminal.
# Por padrão, gera os CSVs de média em resultados_csv/testes/ (pasta de
# rascunho, ignorada pelo git) para não misturar execuções de teste com os
# dados oficiais em resultados_csv/maquinaX/. Padrão de N/rounds segue a
# metodologia usada para os dados já commitados (N = 4 a 14, 50 rodadas).
#
# Uso:
#   scripts/run-benchmark.sh
#   scripts/run-benchmark.sh --maxN 15 --rounds 30
#   scripts/run-benchmark.sh --outDir resultados_csv/maquina5
set -euo pipefail

MIN_N=4
MAX_N=14
ROUNDS=50
OUT_DIR="resultados_csv/testes"

while [[ $# -gt 0 ]]; do
  case "$1" in
    --minN) MIN_N="$2"; shift 2 ;;
    --maxN) MAX_N="$2"; shift 2 ;;
    --rounds) ROUNDS="$2"; shift 2 ;;
    --outDir) OUT_DIR="$2"; shift 2 ;;
    *) echo "Argumento desconhecido: $1" >&2; exit 1 ;;
  esac
done

cd "$(dirname "$0")/.."

mvn -q clean compile exec:java \
  -Dexec.mainClass="com.nqueens.Main" \
  -Dexec.args="--minN ${MIN_N} --maxN ${MAX_N} --rounds ${ROUNDS} --outDir ${OUT_DIR}"
