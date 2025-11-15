import gui.GUISimulator;
import gui.Oval;
import gui.Simulable;
import java.awt.Color;

public class SchellingSimulator implements Simulable {
    private GUISimulator gui;
    private Schelling model;
    private int cellSize = 10;

    public SchellingSimulator(GUISimulator gui, Schelling model) {
        this.gui = gui;
        this.model = model;

        // on dit à la GUI : "c'est moi le Simulable"
        gui.setSimulable(this);

        // première fois qu'on affiche la grille
        draw();
    }

    /** Dessine toute la grille du modèle dans la fenêtre */
    private void draw() {

        // On efface tout ce qui était dessiné avant
        gui.reset();

        // On récupère la grille du modèle
        int[][] grid = model.getGrid();

        // On parcourt toute la grille
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {

                int valeur = grid[i][j];

                // Si la cellule n'est pas vide
                if (valeur != 0) {

                    // Couleur pour dessin :
                    Color couleur;

                    if (valeur == 1) {
                        couleur = Color.BLUE;
                    } else {
                        couleur = Color.RED;
                    }

                    // On dessine un petit cercle
                    gui.addGraphicalElement(
                        new Oval(
                            j * cellSize,  // position horizontale
                            i * cellSize,  // position verticale
                            couleur,       // couleur du bord
                            couleur,       // couleur du remplissage
                            cellSize       // taille
                        )
                    );
                }
            }
        }
    }

    /** Avance d'une étape dans la simulation */
    @Override
    public void next() {
        model.next(); // on met à jour la grille
        draw();       // on redessine
    }

    /** Redémarre la simulation */
    @Override
    public void restart() {
        model.reInit(); // on remet la grille initiale
        draw();         // on redessine
    }
}

