package tests;
import main.*;


import java.awt.Color;
import gui.GUISimulator;


public class TestConway {
    public static void main(String[] args) {
        
    
    int height = 50;
    int width = 50;
    int pix = 100;
    boolean[][] s = new boolean[height][width];
    s[1][1] = s[1][2] = s[2][1] = s[3][2] = s[4][4] =true;
    boolean[][] grid = {
        {false, false, false, false, false, false, false, false, false, true }
        , {  true , false, false, false, false, false, false, false, true , true }
        , { false, true , false, false, false, false, false, false, true , false}
        , { false, false, true , false, false, false, false, false, true , false}
        , { false, false, false, true , true , false, false, false, false, false}
        , { false, false, false, false, false, true , false, false, false, false}
        , { false, false, false, false, false, false, true , false, false, false}
        , { false, false, false, false, false, false, false, true , false, false}
        , { false, false, false, false, false, false, false, false, true , false}
        , { false, false, false, false, false, false, false, false, false, false}
    };
    GUISimulator guiS = new GUISimulator (width*pix , height*pix , Color.WHITE ) ;
    ConwaySimulator conwaySim = new ConwaySimulator ( height , width , s , pix , guiS ) ;
    guiS.setSimulable (conwaySim) ;

}
}
