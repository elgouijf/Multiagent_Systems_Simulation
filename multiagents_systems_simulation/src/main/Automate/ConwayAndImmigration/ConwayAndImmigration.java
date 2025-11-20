package main.Automate.ConwayAndImmigration;

public class ConwayAndImmigration {
    private int h , w , n ; /* longueur , largeur , number of states */
    private int[][] curgrid ; /* la grid courante */
    private int[][] initgrid ; /* la grid initiale  */

    public ConwayAndImmigration(int height , int width , int n, int[][] initg ){
    this.h = height ; 
    this.w = width ;
    this.n = n;
    this.initgrid = new int[height][width] ;
    this.curgrid = new int[height][width] ;
    for ( int i = 0 ; i < height ; i++ ){
        for ( int j = 0 ; j < width ; j++ ){
            this.initgrid[i][j] = initg[i][j] ;
            this.curgrid[i][j] = initg[i][j] ;
        }
    }
}

    public int[] nNeighbors  (int x , int y ){ /* renvoie le nombre de voisins pour chaque etat */
        int count[] = new int[n]  ; 
        
        for ( int i = -1 ; i<= 1 ; i++ ){
            for ( int j =-1 ; j<=1 ; j++ ){
                if ( j==0 && i==0 ) continue ; // on ne compte pas la cellule elle meme
                int k = curgrid[(x+i +h)%h][(y+j +w)%w];
                count[k] +=1;
            }
        }
        
        return count ;
    }

    public void updateGrid (){ // met a jour la grille selon les regles de conway ou de l'immigration
        int[][] newgrid = new int[h][w] ;
        for ( int i = 0 ; i < h ; i++ ){
            for ( int j = 0 ; j < w ; j++ ){
                int[] ng = nNeighbors(i,j) ;
                if (n==2){
                    if ( curgrid[i][j]==1 ){
                        if ( ng[1] == 2 || ng[1] == 3 ) newgrid[i][j] = 1 ;
                        else newgrid[i][j] = 0 ;
                    } else { // la cellule est morte
                        if ( ng[1] == 3 ) newgrid[i][j] = 1 ;
                        else newgrid[i][j] = 0 ;
                    }
                }
                else { // cas de l'immigration
                    int k = curgrid[i][j];
                    int next = (k + 1) % n;
                    if (ng[next] >= 3) newgrid[i][j] = next;
                    else newgrid[i][j] = k;
                }
                
            }
        }
        this.curgrid = newgrid ;
    }

    public int[][] getGrid (){
        return this.curgrid ;
    }
    
    public void reInit (){
        for ( int i = 0 ; i < h ; i++ ){
            for ( int j = 0 ; j < w ; j++ ){
                this.curgrid[i][j] = this.initgrid[i][j] ;
            }
        }
    }
}