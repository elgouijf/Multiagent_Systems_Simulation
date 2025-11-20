package tests.tests_on_automates;

import gui.GUISimulator;
import main.Automate.ConwayAndImmigration.*;;

public class TestImmigration {
    public static void main(String[] args) {
        int height = 10, width = 10, n = 4, pix = 50;
        int[][] s = {
            {3, 3, 0, 0, 1, 1, 1, 1, 0, 0},
            {3, 3, 0, 0, 1, 1, 1, 1, 0, 0},
            {3, 3, 1, 1, 1, 1, 1, 1, 2, 2},
            {3, 3, 1, 1, 1, 1, 1, 1, 2, 2},
            {1, 1, 1, 1, 3, 3, 2, 2, 2, 2},
            {1, 1, 1, 1, 3, 3, 2, 2, 2, 2},
            {0, 0, 1, 1, 2, 2, 2, 2, 2, 2},
            {0, 0, 1, 1, 2, 2, 2, 2, 2, 2},
            {0, 0, 3, 3, 2, 2, 2, 2, 1, 1},
            {0, 0, 3, 3, 2, 2, 2, 2, 1, 1}
        };

        GUISimulator gui = new GUISimulator(500, 500, java.awt.Color.WHITE);
        ImmigrationSimulator simulator = new ImmigrationSimulator(height, width, n, s, pix, gui);
        gui.setSimulable(simulator);
    }
}
