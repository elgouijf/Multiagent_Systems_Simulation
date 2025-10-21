package main;
import gui.Simulable;
import gui.GUISimulator;
import java.awt.Point;
import java.util.List;
import java.awt.Color;


public class BoidsSimulator implements Simulable {
    private GUISimulator guis;
    private Balls balls;
    private Color[] colors;
    private int width;
    private int height;
    int radius = 7;


    // Constructor
    public BoidsSimulator(GUISimulator guis, Balls balls, Color[] colors){
        this.balls = balls;
        this.guis = guis;
        this.width = guis.getWidth();
        this.height = guis.getHeight();
        this.colors = colors;
        
        reDisplay();
    }
    
    @Override
    public void next(){
        List<Point> points = balls.getBalls();
        List<Point> velocities = balls.getVelocities();
        // Update dimensions in case of window resize
        this.width = guis.getWidth();
        this.height = guis.getHeight();

        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            Point v = velocities.get(i);

            // Move
            p.translate(v.x, v.y); // time step = 1

            // Bounce horizontally
            if (p.x < 0 || p.x + 2*this.radius > width) {
                v.x = -v.x;
                // Keep inside window
                p.x = Math.max(0, Math.min(p.x, width - 2*this.radius));
            }

            // Bounce vertically
            if (p.y < 0 || p.y + 2*this.radius > height) {
                v.y = -v.y;
                p.y = Math.max(0, Math.min(p.y, height - 2*this.radius));
            }
        }
        reDisplay();
    }

    @Override
    public void restart(){
        balls.reInit();
        reDisplay();
    }

    public void reDisplay(){
        this.guis.reset();
        List<Point> points = this.balls.getBalls();
        Color[] colors = this.colors;
        for (int i = 0; i < points.size(); i++){
            Color color = colors[i % colors.length];
            Point p = points.get(i);
            gui.Oval ballg = new gui.Oval(p.x, p.y, color, color, 2*this.radius, 2*this.radius);
            guis.addGraphicalElement(ballg);
        }

    }
}

