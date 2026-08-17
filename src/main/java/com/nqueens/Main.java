package com.nqueens;

import com.nqueens.benchmark.*;
import com.nqueens.ui.*;
import com.nqueens.algorithms.*;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/**
 * Gerencia as interações no terminal através do loop do menu principal.
 *
 * @author georis
 * @version 1.1
 * @since 17/08/2026
 */
public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("<----- \uD83D\uDC51 PROJETO N-QUEENS \uD83D\uDC51 ----->");
                System.out.println("1. Executar um algoritmo (ver tabuleiro e métricas)");
                System.out.println("2. Executar Benchmark (Dashboard) e Exportar CSVs de Média");
                System.out.println("0. Sair");
                System.out.print("Escolha: ");
                int op = sc.nextInt();

                if (op == 0) break;

                if (op == 1) {
                    System.out.print("Digite N (tamanho do tabuleiro - Máximo 16 recomendado): ");
                    int n = sc.nextInt();

                    if (n >= 32) {
                        System.out.println("\n❌ ERRO: O algoritmo Bitmask suporta no máximo N=31 devido ao limite de 32 bits da variável 'int' no Java.");
                        System.out.println("Além disso, qualquer N acima de 16 vai demorar dias para rodar. Tente um número menor.\n");
                        continue;
                    }
                    if (n <= 0) {
                        System.out.println("\n❌ ERRO: N deve ser maior que zero.\n");
                        continue;
                    }

                    System.out.println("Qual algoritmo? (1-Backtracking, 2-BranchBound, 3-Bitmask)");
                    int algOp = sc.nextInt();

                    if (algOp < 1 || algOp > 3) {
                        System.out.println("\nERRO >> Opção de algoritmo inválida.\n");
                        continue;
                    }

                    var alg = algOp == 1 ? new Backtracking() : (algOp == 2 ? new BranchAndBound() : new Bitmask());
                    System.out.println("\nProcessando... (Isso pode demorar dependendo do N)");
                    var m = alg.solve(n);

                    System.out.println("\n✅ Execução Concluída!");
                    System.out.println("Algoritmo: " + m.algorithmName);
                    System.out.println("Soluções encontradas: " + m.solutionsFound);
                    System.out.println("Tempo de execução: " + (m.timeNano / 1_000_000.0) + " ms");
                    System.out.println("Memória utilizada: " + (m.memoryUsedBytes / 1024.0) + " KB");
                    System.out.println("Chamadas Recursivas (Estados): " + m.recursiveCalls);
                    System.out.println("Podas Realizadas: " + m.podas);

                    Board.printBoard(m.firstSolution);

                } else if (op == 2) {
                    System.out.print("Executar benchmark do N = 4 até N = (Máximo recomendado = 15): ");
                    int maxN = sc.nextInt();

                    if (maxN >= 32) {
                        System.out.println("\nERRO: Para o benchmark, o N máximo absoluto é 31, mas recomendamos no máximo 15 para não travar sua máquina.\n");
                        continue;
                    }

                    // Tratamento para impedir valores menores que 4 (já que não são úteis)
                    if (maxN < 4) {
                        System.out.println("\n❌ ERRO: O benchmark começa a partir de N=4. Por favor, digite um valor maior ou igual a 4.\n");
                        continue;
                    }

                    System.out.print("Quantas rodadas de teste deseja executar para tirar a média?  ");
                    int rounds = sc.nextInt();

                    if (rounds <= 0) {
                        System.out.println("\nERRO: O número de rodadas deve ser maior que zero.\n");
                        continue;
                    }

                    // Lista mestra que vai guardar os resultados de todas as rodadas
                    List<List<com.nqueens.core.Metrics>> allRounds = new ArrayList<>();

                    for (int r = 1; r <= rounds; r++) {
                        System.out.println("\n========================================================");
                        System.out.println("⏳ EXECUTANDO RODADA " + r + " DE " + rounds + "...");
                        System.out.println("========================================================");

                        // O BenchmarkRunner automaticamente imprime o dashboard da rodada atual
                        var results = BenchmarkRunner.run(4, maxN); // usando var para o próprio java saber o tipo da variável
                        allRounds.add(results);
                    }

                    // Envia todo o histórico para o CsvExporter processar as médias
                    CsvExporter.exportAverages(allRounds, 4, maxN);

                } else {
                    System.out.println("\nERRO: Opção inválida.\n");
                }

                // O tratamento para Strings (letras e símbolos) ocorre automaticamente aqui
            } catch (InputMismatchException e) {
                System.out.println("\n❌ ERRO: Entrada inválida. Por favor, digite apenas números inteiros.\n");
                sc.nextLine(); // Limpa o buffer do teclado com as letras digitadas
            } catch (Exception e) {
                System.out.println("\n❌ ERRO INESPERADO: " + e.getMessage() + "\n");
                sc.nextLine();
            }
        }
        sc.close();
    }
}