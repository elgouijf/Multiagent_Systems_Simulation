package main.Automate.ConwayAndImmigration;
import gui.GUISimulator;
import gui.Rectangle;
import gui.Simulable;

import java.awt.Color;

public class ConwaySimulator implements Simulable  {
    int h , w ; // longueur et largeur de la grille
    ConwayAndImmigration conway ; /* l'instance de Conway elle-même */
    int pix ; /* taille de chaque cellule en pixels */
    GUISimulator guiS ; /* le simulateur GUI */

    public ConwaySimulator ( int height , int width , int[][] grid , int pixelsize, GUISimulator guiS ){
        this.h = height ; 
        this.w = width ; 
        this.conway = new ConwayAndImmigration(height, width, 2, grid) ; 
        this.pix = pixelsize ;
        this.guiS = guiS ;
        this.reDisplay() ;
    }
    
    public void reDisplay (){
        this.guiS.reset(); 
        int[][] grid = this.conway.getGrid() ;
        for ( int i = 0 ; i < h ; i++ ){
            for ( int j = 0 ; j < w ; j++ ){
                if ( grid[i][j]==1 ){ // la cellule est vivante
                    this.guiS.addGraphicalElement( new Rectangle(i * pix + pix / 2, j * pix + pix / 2, Color.decode("#1f77b4"), Color.decode("#1f77b4"), pix) );
                }
                else {
                    this.guiS.addGraphicalElement( new Rectangle(i * pix + pix / 2, j * pix + pix / 2, Color.WHITE, Color.WHITE, pix) );
                }
            }
        }
    }
    @Override
    public void next (){
        this.conway.updateGrid() ;
        this.reDisplay() ;
    }
    @Override
    public void restart (){
        this.conway.reInit() ;
        this.reDisplay() ;
    }
    
}
