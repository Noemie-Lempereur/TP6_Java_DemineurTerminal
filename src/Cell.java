public class Cell {
    private int row;
    private int col;
    private boolean mine;
    private boolean visible;
    private boolean thinkMine;
    private boolean dontKnow;
    private int nbTouchingMines;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        mine=false;
        nbTouchingMines=0;
        visible=false;
        thinkMine=false;
        dontKnow=false;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isMine() {
        return mine;
    }

    public boolean isVisible() {
        return visible;
    }

    public int getNbTouchingMines() {
        return nbTouchingMines;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setNbTouchingMines(int nbTouchingMines) {
        this.nbTouchingMines = nbTouchingMines;
    }

    public boolean isThinkMine() {
        return thinkMine;
    }

    public boolean isDontKnow() {
        return dontKnow;
    }

    public void setThinkMine(boolean thinkMine) {
        this.thinkMine = thinkMine;
        if(isThinkMine()) {
            this.dontKnow = false;
        }
    }

    public void setDontKnow(boolean dontKnow) {
        this.dontKnow = dontKnow;
        if(isDontKnow()) {
            this.thinkMine = false;
        }
    }
}
