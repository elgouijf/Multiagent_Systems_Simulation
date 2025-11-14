package main.Boidutils;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Polygon;
import gui.GraphicalElement;


public class PolygonGraphics implements GraphicalElement {
    private int[] xPoints;
    private int[] yPoints;
    private int nPoints;
    private Color color;

    public PolygonGraphics(int[] xPoints, int[] yPoints, int nPoints, Color color) {
        this.xPoints = xPoints;
        this.yPoints = yPoints;
        this.nPoints = nPoints;
        this.color = color;
    }

    @Override
    public void paint(Graphics2D graphic) {
        Polygon polygon = new Polygon(xPoints, yPoints, nPoints);
        graphic.setColor(color);
        graphic.fillPolygon(polygon);
        graphic.drawPolygon(polygon);
    }
}