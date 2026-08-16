package com.nqueens.ui;

public class Board {
    public static void printBoard(int[] board) {
        if (board == null) {
            System.out.println("Nenhuma solução para exibir (ou algoritmo não coleta solução).");
            return;
        }
        int n = board.length;
        System.out.println("\nExibindo uma solução válida:\n");
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                if (board[r] == c) {
                    System.out.print("👑 ");
                } else {
                    if ((r + c) % 2 == 0) System.out.print("⬜ ");
                    else System.out.print("⬛ "); //esses emojis funcionam no intellij, já no vscode fica "??" 
                                                     // todo: procurar uma solução
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}