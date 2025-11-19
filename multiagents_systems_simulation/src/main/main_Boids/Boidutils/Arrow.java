package main.main_Boids.Boidutils;

import gui.GraphicalElement;
import java.awt.Color;
import java.awt.Graphics2D;

public class Arrow implements GraphicalElement {
    private int x1, y1, x2, y2;
    private Color color;
    private int headSize;

    public Arrow(int x1, int y1, int x2, int y2, Color color, int headSize) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
        this.headSize = headSize;
    }

    @Override
    public void paint(Graphics2D g) {
        g.setColor(color);

        
        g.drawLine(x1, y1, x2, y2);

        // Calcul tête de flèche
        double angle = Math.atan2(y2 - y1, x2 - x1);

        int x3 = (int)(x2 - headSize * Math.cos(angle - Math.PI / 6));
        int y3 = (int)(y2 - headSize * Math.sin(angle - Math.PI / 6));

        int x4 = (int)(x2 - headSize * Math.cos(angle + Math.PI / 6));
        int y4 = (int)(y2 - headSize * Math.sin(angle + Math.PI / 6));

        // Deux lignes de la tête
        g.drawLine(x2, y2, x3, y3);
        g.drawLine(x2, y2, x4, y4);
    }
}
