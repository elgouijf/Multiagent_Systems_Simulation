package tests;
import main.*;

import gui.GUISimulator;
import java.awt.Color;

public class TestSchelling {
    public static void main(String[] args) {
        GUISimulator gui = new GUISimulator(600, 600, Color.BLACK);
        Schelling model = new Schelling(50, 2, 3); // grille 50x50, 2 couleurs, seuil K=3
        new SchellingSimulator(gui, model);
    }
}

