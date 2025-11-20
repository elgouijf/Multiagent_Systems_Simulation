package tests.tests_on_automates;
import main.Automate.ConwayAndImmigration.*;


import java.awt.Color;
import gui.GUISimulator;


public class TestConway {
    public static void main(String[] args) {
        
    
    int height = 15;
    int width = 10;
    int pix = 50;
    int n=2;
    int[][] s = new int[20][20];
        for ( int i = 0 ; i < 20 ; i++ ) {
            for ( int j = 0 ; j < 20 ; j++ ) {
                s[i][j] = (int)(Math.random() * 2);
            }
        }
    GUISimulator guiS = new GUISimulator (width*pix , height*pix , Color.WHITE ) ;
    ConwaySimulator conwaySim = new ConwaySimulator ( height , width , s , pix , guiS ) ;
    guiS.setSimulable (conwaySim) ; 

}
}
