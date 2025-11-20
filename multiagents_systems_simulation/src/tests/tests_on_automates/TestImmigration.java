package tests.tests_on_automates;
import java.math.*;
import gui.GUISimulator;
import main.Automate.ConwayAndImmigration.*;;

public class TestImmigration {
    public static void main(String[] args) {
        int height = 30, width = 30, n = 4, pix = 50;
        int[][] s = new int[30][30];
        for ( int i = 0 ; i < 30 ; i++ ) {
            for ( int j = 0 ; j < 30 ; j++ ) {
                s[i][j] = (int)(Math.random() * 4);
            }
        }

        GUISimulator gui = new GUISimulator(500, 500, java.awt.Color.WHITE);
        ImmigrationSimulator simulator = new ImmigrationSimulator(height, width, n, s, pix, gui);
        gui.setSimulable(simulator);
    }
}
