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
        this.nbCasesWithoutMines = this.nbCols * this.nbRows - nbMines;
        this.tour=0;
        this.lose=false;
        print();
        Scanner sc = new Scanner(System.in);
        System.out.println("Veuillez rentrer les coordonnées de la case que vous voulez dévoiler (exemple pour la case à la ligne 1 et à la colonne 3 : 1 3");
        String commande = sc.nextLine();
        int row = (int) commande.charAt(0)-48;
        int col = (int) commande.charAt(2)-48;
        putMines(nbMines,row,col);
        unveil(row, col);
    }

    // returns the String representation of a Cell, depending on its attributes and the debug flags
    public String getCellSymbol(Cell cell){

        String symbol = "#";        // default symbol = hidden cell

        if(cell.isThinkMine()){
            symbol="!";
        }

        if(cell.isDontKnow()){
            symbol="?";
        }

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

    public void putMines(int nbMines, int rowS, int colS) {
        Random random = new Random();
        if(nbRows*nbCols>nbMines){
            int nbMinesPosees = 0;
            while (nbMinesPosees!=nbMines) {
                int row = random.nextInt(nbRows); // ligne aléatoire entre 0 et nbRows
                int col = random.nextInt(nbCols); // colonne aléatoire entre 0 et nbCols
                if (!grid[row][col].isMine() && row != rowS && col != colS) {
                    grid[row][col].setMine(true);
                    nbMinesPosees++;
                    LinkedList<Cell> neighbors = new LinkedList<>();
                    neighbors = getNeighbors(grid[row][col]);
                    for (Cell neighbor : neighbors) {
                        neighbor.setNbTouchingMines(neighbor.getNbTouchingMines() + 1);
                    }
                }
            }
        }
    }

    public void unveil(int row, int col){
        if(grid[row][col].isMine()){
            grid[row][col].setVisible(true);
            System.out.println("Vous avez perdu");
            System.out.println("Voici l'emplacement des mines");
            showMines=true;
            print();
            lose=true;
        }else{
            if(grid[row][col].getNbTouchingMines()==0){
                LinkedList <Cell> cells = new LinkedList<>();
                cells.add(grid[row][col]);
                while (cells.size()!=0){
                    Cell test = cells.poll();
                    if(getCellSymbol(test).equals("#")||getCellSymbol(test).equals("?")||getCellSymbol(test).equals("!")) {
                        test.setVisible(true);
                        tour++;
                        if (test.getNbTouchingMines() == 0) {
                            LinkedList<Cell> neighbors = getNeighbors(test);
                            for (Cell neighbor : neighbors) {
                                cells.add(neighbor);
                            }
                        }
                    }
                }
            }else{
                grid[row][col].setVisible(true);
                tour++;
            }
        }
    }

    public void play(){
        while (!lose &&tour<nbCasesWithoutMines) {
            try {
                print();
                Scanner sc = new Scanner(System.in);
                System.out.println("Veuillez faire l'une des actions suivantes :");
                System.out.println("- Dévoiler une case (exemple pour la case à la ligne 1 et à la colonne 3 : \"d 1 3\"");
                System.out.println("- Marquer une case comme possedant une mine (exemple pour la case à la ligne 1 et à la colonne 3 : \"m 1 3 !\"");
                System.out.println("- Marquer une case comme inconnue (exemple pour la case à la ligne 1 et à la colonne 3 : \"m 1 3 ?\"");
                System.out.println("- Enlever les marquages (exemple pour la case à la ligne 1 et à la colonne 3 : \"m 1 3 #\"");
                System.out.println("- Quitter le jeu \"q\"");
                String commande = sc.nextLine();
                String[] arrOfCommand = commande.split(" ");
                if (arrOfCommand[0].equals("q")) {
                    return;
                }
                int row = Integer.parseInt(arrOfCommand[1]);
                int col = Integer.parseInt(arrOfCommand[2]);
                if (arrOfCommand[0].equals("d")) {
                    unveil(row, col);
                    if (tour == nbCasesWithoutMines && !lose) {
                        System.out.println("Vous avez gagné !");
                        print();
                    }
                }
                if (arrOfCommand[0].equals("m")) {
                    if (arrOfCommand[3].equals("?")) {
                        grid[row][col].setDontKnow(true);
                    }
                    if (arrOfCommand[3].equals("!")) {
                        grid[row][col].setThinkMine(true);
                    }
                    if (arrOfCommand[3].equals("#")) {
                        grid[row][col].setDontKnow(false);
                        grid[row][col].setThinkMine(false);
                    }
                }
            }catch (Exception e){
                System.out.println(e);
            }


            /*if(commande.charAt(0) == 'q'){
                return;
            }
            int row = (int) commande.charAt(2) - 48;
            int col = (int) commande.charAt(4) - 48;
            if (commande.charAt(0) == 'd') {
                unveil(row, col);
                if (tour == nbCasesWithoutMines && !lose) {
                    System.out.println("Vous avez gagné !");
                    print();
                }
            }
            if(commande.charAt(0) == 'm'){
                char symbole = commande.charAt(6);
                if(symbole=='?'){
                    grid[row][col].setDontKnow(true);
                }
                if(symbole=='!'){
                    grid[row][col].setThinkMine(true);
                }
                if(symbole=='#'){
                    grid[row][col].setDontKnow(false);
                    grid[row][col].setThinkMine(false);
                }
            }*/
        }
    }
}
