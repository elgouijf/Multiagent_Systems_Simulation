package main.main_Boids.Boidutils;

import gui.GUISimulator;
import java.awt.Color;

public class FlowFieldDraw {

    public static void draw(GUISimulator gui, FlowField field) {
        int res = field.getResolution();
        Vector_2D[][] grid = field.getFieldArray();

        for (int row = 0; row < field.getRows(); row++) {
            for (int col = 0; col < field.getColumns(); col++) {
                Vector_2D v = grid[row][col];

                int x1 = col * res + res / 2;
                int y1 = row * res + res / 2;
                int x2 = (int)(x1 + v.getX() * res * 0.5);
                int y2 = (int)(y1 + v.getY() * res * 0.5);

                gui.addGraphicalElement(new Arrow(x1, y1, x2, y2, Color.CYAN, 6));
            }
        }
    }
}
