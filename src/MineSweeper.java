import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class MineSweeper {

    // debug flags
    private boolean showMines = false;
    private boolean showNbTouchingMines = false;

    // grid properties
    private int nbRows;

    private int nbCols;
    private Cell[][] grid;  //premier crochet correspond à la ligne et le second à la colonne

    private int nbCasesWithoutMines;
    private int tour;
    private boolean lose;

    public MineSweeper(int nbRows, int nbCols, int nbMines) {
        this.nbRows = nbRows;
        this.nbCols = nbCols;
        this.grid = new Cell[nbRows][nbCols];
        for(int i = 0; i<nbRows;i++){
            for(int j=0; j<nbCols;j++){
                grid[i][j]=new Cell(i,j);
            }
        }
        putMines(nbMines);
        this.nbCasesWithoutMines = this.nbCols * this.nbRows - nbMines;
        this.tour=0;
        this.lose=false;
    }

    // returns the String representation of a Cell, depending on its attributes and the debug flags
    public String getCellSymbol(Cell cell){

        String symbol = "#";        // default symbol = hidden cell

        // shows the mine in the cell if the cell is visible or if the showMines flag is on
        if((cell.isVisible() || this.showMines) && cell.isMine()){
            symbol = "*";
        }
        // shows the number of touching mines if the cell is visible or if the showNbTouchingMines flag is on
        else if( cell.isVisible() || this.showNbTouchingMines ){

            // special case of a visible cell : " " is displayed instead of "0"
            if( cell.isVisible() && cell.getNbTouchingMines() == 0){
                symbol = " ";
            }
            else{
                symbol = Integer.toString(cell.getNbTouchingMines());
            }

        }

        return symbol;
    }

    // prints the game grid
    public void print(){

        int firstColumnWidth = (int)Math.ceil(Math.log10(this.nbRows));     // first column width = number of digits in nbRows
        int otherColumnsWidth = (int)Math.ceil(Math.log10(this.nbCols));    // other columns width = number of digits in nbCols

        // first line = column numbers
        System.out.printf("%" + firstColumnWidth + "s ", "");
        for(int j = 0; j < this.nbCols; j++){
            System.out.printf("%" + otherColumnsWidth + "s ", j);
        }
        System.out.println();

        for(int i = 0; i < this.nbRows; i++){

            // first column = row numbers
            System.out.printf("%" + firstColumnWidth + "s ", i);
            for(int j= 0; j < this.nbCols; j++){

                Cell cell = this.grid[i][j];
                String cellSymbol = getCellSymbol(cell);
                System.out.printf("%" + otherColumnsWidth + "s ", cellSymbol);
            }
            System.out.println();
        }
    }

    // returns the neighbors of a Cell at the specified row and col in the grid
    public LinkedList<Cell> getNeighbors(Cell cell){

        LinkedList<Cell> neighbors = new LinkedList<>();
        int row = cell.getRow();
        int col = cell.getCol();

        if(row - 1 >= 0){
            neighbors.add( this.grid[row - 1][col] );

            if(col + 1 < this.nbCols){
                neighbors.add( this.grid[row - 1][col + 1] );
            }

            if(col - 1 >= 0){
                neighbors.add( this.grid[row - 1][col - 1] );
            }
        }

        if(col + 1 < this.nbCols){
            neighbors.add(  this.grid[row][col + 1] );
        }

        if(col - 1 >= 0){
            neighbors.add(  this.grid[row][col - 1] );
        }

        if(row + 1 < this.nbRows){
            neighbors.add( this.grid[row + 1][col] );

            if(col + 1 < this.nbCols){
                neighbors.add(  this.grid[row + 1][col + 1] );
            }

            if(col - 1 >= 0){
                neighbors.add( this.grid[row + 1][col - 1] );
            }
        }

        return neighbors;
    }

    public void putMines(int nbMines) {
        Random random = new Random();
        int k=0;
        for (int i = 0; i < nbMines; i++) {
            int row = random.nextInt(nbRows); // ligne aléatoire entre 0 et nbRows
            int col = random.nextInt(nbCols); // colonne aléatoire entre 0 et nbCols
            if(!grid[row][col].isMine()) {
                grid[row][col].setMine(true);
                k=0;
                LinkedList<Cell> neighbors = new LinkedList<>();
                neighbors=getNeighbors(grid[row][col]);
                for (Cell neighbor:neighbors) {
                    neighbor.setNbTouchingMines(neighbor.getNbTouchingMines()+1);
                }
            }else{
                if(k<500) {
                    k++;
                    i--;
                }
            }
        }
    }

    public void unveil(int row, int col){
        grid[row][col].setVisible(true);
        if(grid[row][col].isMine()==true){
            System.out.println("Vous avez perdu");
            System.out.println("Voici l'emplacement des mines");
            showMines=true;
            print();
            lose=true;
        }
    }

    public void play(){
        while (!lose &&tour<nbCasesWithoutMines) {
            print();
            Scanner sc = new Scanner(System.in);
            System.out.println("Veuillez rentrer les coordonnées de la case que vous voulez dévoiler (exemple pour la case à la ligne 1 et à la colonne 3 : 1 3");
            String commande = sc.nextLine();
            int row = (int) commande.charAt(0)-48;
            int col = (int) commande.charAt(2)-48;
            unveil(row, col);
            tour++;
            if(tour==nbCasesWithoutMines){
                System.out.println("Vous avez gagné !");
            }
        }
    }
}
