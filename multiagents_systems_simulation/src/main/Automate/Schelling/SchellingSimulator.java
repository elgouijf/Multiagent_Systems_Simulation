package main.Automate.Schelling;
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

        gui.setSimulable(this);
        draw();
    }

    private Color getColor(int id) {
        switch(id) {
            case 1: return Color.BLUE;
            case 2: return Color.RED;
            case 3: return Color.GREEN;
            case 4: return Color.MAGENTA;
            case 5: return Color.ORANGE;
            default: return Color.GRAY;
        }
    }

    private void draw() {
        gui.reset();
        int[][] grid = model.getGrid();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {

                int val = grid[i][j];
                if (val != 0) {
                    Color c = getColor(val);
                    gui.addGraphicalElement(
                        new Oval(j * cellSize, i * cellSize, c, c, cellSize));
                }
            }
        }
    }

    @Override
    public void next() {
        model.next();
        draw();

        double seg = model.segregationLevel() * 100;
        System.out.println("Ségrégation : " + (int)seg + "%");
    }

    @Override
    public void restart() {
        model.reInit();
        draw();
        System.out.println("Grille réinitialisée !");
    }
}
