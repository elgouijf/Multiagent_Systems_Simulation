package tests;
import main.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import gui.GUISimulator;

public class TestBalls {
    public static void main(String[] args) {
        int n_points = (args.length > 0) ? Integer.parseInt(args[0]) : 8;
        int width    = (args.length > 1) ? Integer.parseInt(args[1]) : 500;
        int height   = (args.length > 2) ? Integer.parseInt(args[2]) : 500;

        GUISimulator gui = new GUISimulator (width , height , Color.BLACK ) ;
        
        Color colors[] = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.ORANGE};
        List<Point> initial_points = new ArrayList<>();
        List<Point> initial_velocities = new ArrayList<>();

        for (int i = 0; i < n_points; i++) {
            // Random initial velocity between -5 and +5
            initial_points.add(new Point((int)(Math.random() * width ), (int)(Math.random() * height )));
            initial_velocities.add(new Point((int)(Math.random() * 60 - 20), (int)(Math.random() * 60  - 20)));
        }




        Balls balls = new Balls(initial_points, initial_velocities);
        System.out.println("Positions initiales : " + balls);

        BallsSimulator simulator = new BallsSimulator(gui, balls, colors);


        gui.setSimulable (simulator);
    }
}
