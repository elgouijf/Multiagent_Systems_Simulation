package tests;
import main.Automate.Schelling.*;
import java.awt.Color;
import java.util.Scanner;
import gui.GUISimulator;

public class TestSchelling {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("===== Simulation de Schelling =====");

        System.out.print("Taille de la grille (ex 50) : ");
        int size = sc.nextInt();

        System.out.print("Nombre de couleurs (max 5) : ");
        int nbColors = sc.nextInt();
        if (nbColors > 5) nbColors = 5;

        System.out.print("Valeur de K (tolérance) : ");
        int K = sc.nextInt();

        printExpectedThreshold(nbColors);

        // Création modèle + interface graphique
        Schelling model = new Schelling(size, nbColors, K);
        GUISimulator gui = new GUISimulator(600, 600, Color.BLACK);
        new SchellingSimulator(gui, model);
    }

    private static void printExpectedThreshold(int nbColors) {
        System.out.println("\n>> Seuil théorique de ségrégation :");

        switch(nbColors) {
            case 1: System.out.println("• triviale, une seule couleur"); break;
            case 2: System.out.println("• Ségrégation forte si K ≤ 3"); break;
            case 3: System.out.println("• Ségrégation forte si K ≤ 4"); break;
            case 4: System.out.println("• Ségrégation forte si K ≤ 5"); break;
            case 5: System.out.println("• Ségrégation forte si K ≤ 6"); break;
        }
        System.out.println();
    }
}
