package main;
<<<<<<< HEAD
=======
import java.awt.Color;
>>>>>>> 32629e9d047148e8a238c5fc3387abf6973efd2d
import java.util.*;

public class Schelling {
    private int[][] grid;
    private int size;
    private int K;
    private int nbColors;

    private List<int[]> emptyCells;

    public Schelling(int size, int nbColors, int K) {
        this.size = size;
        this.nbColors = nbColors;
        this.K = K;

        grid = new int[size][size];
        emptyCells = new ArrayList<>();

        initGrid();
    }

    /** Initialise la grille aléatoirement */
    private void initGrid() {
        Random rand = new Random();
        emptyCells.clear();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int val = rand.nextInt(nbColors + 1); // 0 = vide
                grid[i][j] = val;
                if (val == 0) emptyCells.add(new int[]{i, j});
            }
        }
    }

    /** Compte le nombre de voisins différents */
    private int countDifferentNeighbors(int x, int y) {
        int color = grid[x][y];
        int diff = 0;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;

                int nx = (x + dx + size) % size;
                int ny = (y + dy + size) % size;

                int neighbor = grid[nx][ny];

                if (neighbor != 0 && neighbor != color)
                    diff++;
            }
        }
        return diff;
    }

    /** Effectue une étape : trouve les mécontents et les déplace. */
    public void next() {

        List<int[]> unhappy = new ArrayList<>();

        // Trouver les familles mécontentes
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                if (grid[i][j] != 0 && countDifferentNeighbors(i, j) > K) {
                    unhappy.add(new int[]{i, j});
                }
            }
        }

        // Déplacement des familles
        Collections.shuffle(emptyCells);
        for (int[] cell : unhappy) {
            if (emptyCells.isEmpty()) break;

            int[] newHome = emptyCells.remove(0);
            int color = grid[cell[0]][cell[1]];

            grid[cell[0]][cell[1]] = 0;
            emptyCells.add(cell);

            grid[newHome[0]][newHome[1]] = color;
        }
    }

    /** Niveau de ségrégation mesuré */
    public double segregationLevel() {
        int total = 0;
        int same = 0;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                if (grid[i][j] != 0) {

                    int color = grid[i][j];

                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            if (dx == 0 && dy == 0) continue;

                            int nx = (i + dx + size) % size;
                            int ny = (j + dy + size) % size;

                            if (grid[nx][ny] != 0) {
                                total++;
                                if (grid[nx][ny] == color)
                                    same++;
                            }
                        }
                    }
                }
            }
        }
        return (total == 0) ? 0 : (double) same / total;
    }

    public int[][] getGrid() {
        return grid;
    }

    public void reInit() {
        initGrid();
    }
}
