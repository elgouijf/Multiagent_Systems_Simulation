package tests;

import gui.GUISimulator;
import main.ImmigrationSimulator;

public class TestImmigration {
    public static void main(String[] args) {
        int height = 5, width = 5, n = 4, pix = 50;
        int[][] s = {
            {3, 0, 1, 1, 0},
            {3, 1, 1, 1, 2},
            {1, 1, 3, 2, 2},
            {0, 1, 2, 2, 2},
            {0, 3, 2, 2, 1}
        };

        GUISimulator gui = new GUISimulator(500, 500, java.awt.Color.WHITE);
        ImmigrationSimulator simulator = new ImmigrationSimulator(height, width, n, s, pix, gui);
        gui.setSimulable(simulator);
    }
}
