import java.awt.Color;
import java.util.*;

public class Schelling {
    private int[][] grid;           // Grille d'habitation
    private int size;               // Taille de la grille
    private int K;                  // Seuil de tolérance
    private List<int[]> emptyCells; // Liste des maisons vides
    private int nbColors;           // Nombre de couleurs (familles)

    public Schelling(int size, int nbColors, int K) {
        this.size = size;
        this.K = K;
        this.nbColors = nbColors;
        grid = new int[size][size];
        emptyCells = new ArrayList<>();
        initGrid();
    }

    /** Initialise la grille avec des familles et des maisons vides */
    private void initGrid() {
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int val = rand.nextInt(nbColors + 1); // 0 = vide, sinon couleur
                grid[i][j] = val;
                if (val == 0) emptyCells.add(new int[]{i, j});
            }
        }
    }

    /** Compte combien de voisins sont différents */
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

    /** Applique une étape de la simulation */
    public void next() {
        List<int[]> unhappy = new ArrayList<>();

        // 1. Trouver les familles mécontentes
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid[i][j] != 0 && countDifferentNeighbors(i, j) > K) {
                    unhappy.add(new int[]{i, j});
                }
            }
        }

        // 2. Déménager les familles mécontentes
        Collections.shuffle(emptyCells);
        Random rand = new Random();
        for (int[] p : unhappy) {
            if (emptyCells.isEmpty()) break;
            int[] newHome = emptyCells.remove(0);
            int color = grid[p[0]][p[1]];
            grid[p[0]][p[1]] = 0; // devient vide
            emptyCells.add(p);   // ancienne maison devient libre
            grid[newHome[0]][newHome[1]] = color;
        }
    }

    public int[][] getGrid() {
        return grid;
    }

    public void reInit() {
        emptyCells.clear();
        initGrid();
    }
}
