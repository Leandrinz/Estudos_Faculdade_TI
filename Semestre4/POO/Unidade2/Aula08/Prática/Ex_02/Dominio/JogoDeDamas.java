package Semestre4.POO.Unidade2.Aula08.Prática.Ex_02.Dominio;

public class JogoDeDamas {

    private static final int linhas = 8;
    private static final int colunas = 8;

    private char[][] tabuleiro;

    public JogoDeDamas() {
        tabuleiro = new char[linhas][colunas];

        // Preenche o tabuleiro vazio
        for (int l = 0; l < linhas; l++) {
            for (int c = 0; c < colunas; c++) {
                tabuleiro[l][c] = '.';
            }
        }

        // Peças do jogador X
        for (int l = 0; l < 3; l++) {
            for (int c = 0; c < colunas; c++) {
                if ((l + c) % 2 != 0) {
                    tabuleiro[l][c] = 'X';
                }
            }
        }

        // Peças do jogador O
        for (int l = 5; l < 8; l++) {
            for (int c = 0; c < colunas; c++) {
                if ((l + c) % 2 != 0) {
                    tabuleiro[l][c] = 'O';
                }
            }
        }
    }
    public String toString() {
        String res = "";

        for (int l = 0; l < linhas; l++) {
            for (int c = 0; c < colunas; c++) {
                res += tabuleiro[l][c];
            }

            res += "\n";
        }

        return res;
    }
}