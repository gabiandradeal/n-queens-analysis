package com.nqueens.benchmark;

import com.nqueens.core.Metrics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class CsvExporter {

    public static void exportAverages(List<List<Metrics>> allRounds, int minN, int maxN) {
        String[] algos = {"Backtracking Classico", "Branch and Bound", "Bitmask"};

        // Pega a quantidade exata de rodadas (tentativas) executadas
        int tentativas = allRounds.size();

        String diretorio = "resultados_csv/maquinaX";

        // Cria a pasta no caso ela ainda não exista
        File pasta = new File(diretorio);
        if (!pasta.exists()) {
            pasta.mkdirs();
        }

        // Adiciona o número de tentativas dinamicamente no nome do arquivo
        String[] fileNames = {
                diretorio + "/medias_Backtracking_" + tentativas + "_tentativas.csv",
                diretorio + "/medias_BranchAndBound_" + tentativas + "_tentativas.csv",
                diretorio + "/medias_Bitmask_" + tentativas + "_tentativas.csv"
        };

        System.out.println("\n📊 Calculando médias das " + tentativas + " rodadas e gerando CSVs...");

        for (int i = 0; i < algos.length; i++) {
            String targetAlg = algos[i];
            String filename = fileNames[i];

            try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
                // Cabeçalho padronizado para as planilhas
                writer.println("N,Algoritmo,TempoMedio_ms,MemoriaMedia_KB,Chamadas_Recursivas,Podas,Solucoes");

                for (int n = minN; n <= maxN; n++) {
                    double totalTime = 0;
                    double totalMem = 0;
                    long chamadas = 0;
                    long solucoes = 0;
                    int count = 0;
                    long podas = 0;

                    // Varre todas as rodadas coletando os dados específicos deste N e deste Algoritmo
                    for (List<Metrics> round : allRounds) {
                        for (Metrics m : round) {
                            if (m.n == n && m.algorithmName.equals(targetAlg)) {
                                totalTime += (m.timeNano / 1_000_000.0);
                                totalMem += (m.memoryUsedBytes / 1024.0);
                                chamadas = m.recursiveCalls;
                                solucoes = m.solutionsFound;
                                podas = m.podas; // <-- Atualizado para usar a variável 'podas'
                                count++;
                            }
                        }
                    }

                    // Calcula e escreve a média
                    if (count > 0) {
                        double avgTime = totalTime / count;
                        double avgMem = totalMem / count;
                        // não tem média das podas, chamadas e soluções, pois são constantes a cada execução
                        writer.printf(java.util.Locale.US, "%d,%s,%.2f,%.2f,%d,%d,%d\n",
                                n, targetAlg, avgTime, avgMem, chamadas, podas, solucoes);
                    }
                }
                System.out.println("Arquivo exportado com sucesso: " + filename);
            } catch (IOException e) {
                System.err.println("Erro ao exportar " + filename + ": " + e.getMessage());
            }
        }
    }
}