import java.util.Random;

public class MinesweeperModel {
    private long startTime;
    private int bombs;
    private int flags = 0;
    private MineCell[][] field;
    private MinesweeperController controller;
    private int revealed = 0;

    MinesweeperModel(){};
    
    void setField(int rows, int cols, int bombs, MinesweeperController c) {
        controller = c;
        this.bombs = bombs;
        field = new MineCell[rows][cols];
        //create and randomize an array to determine which cells are bombs
        boolean[] bombSetup = new boolean[rows*cols];
        for (int i = 0; i < bombs; i++) {
            bombSetup[i] = true;
        }
        for (int i = bombs; i < bombSetup.length; i++) {
            bombSetup[i] = false;
        }
        Random rand = new Random();
		for (int i = 0; i < bombSetup.length; i++) {
			int randomIndexToSwap = rand.nextInt(bombSetup.length);
			boolean temp = bombSetup[randomIndexToSwap];
			bombSetup[randomIndexToSwap] = bombSetup[i];
			bombSetup[i] = temp;
		}
        for (int i = 0; i< field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                field[i][j] = new MineCell(bombSetup[i*field[0].length+j], i, j, field.length, field[0].length);
                int neighborBombs = 0;
                for (int[] pos : field[i][j].getNeighbors()) {
                    if (bombSetup[pos[0]*field[0].length+pos[1]]) {
                        neighborBombs++;
                    }
                }
                field[i][j].setbombs(neighborBombs);
            }
        }
        startTime = System.nanoTime();
    }

    void flagCell(int row, int col) {
        MineCell cell = field[row][col];
        if (cell.isRevealed()) {
            return;
        }
        cell.swapFlag();
        if (field[row][col].isFlagged()) {
            flags++;
        } else {
            flags--;
        }
    }

    int getFlagsRemaining() {
        return bombs-flags;
    }

    void reveal(int row, int column){
        MineCell cell = field[row][column];
        if (!cell.canReveal()) {
            return;
        }
        cell.reveal();
        revealed++;
        if (cell.isBomb()) {
            controller.gameOver(row, column);
        } else if (revealed+bombs == field.length*field[0].length) {
            controller.victory();
        }else if (cell.isZero()) {
                revealSurrounding(row, column);
            }
    }

    public boolean isBomb(int row, int column) {
        return field[row][column].isBomb();
    }

    public String getCell(int row, int column) {
        return field[row][column].displayImage();
    }
    public boolean isRevealed(int row, int column) {
        return field[row][column].isRevealed();
    }

    public int[][] getAllBombs() {
        int[][]result = new int[bombs][2];
        int bombsFound = 0;
        for (MineCell[] column: field) {
            for (MineCell cell: column) {
                if (cell.isBomb()) {
                    result[bombsFound] = cell.getlocation();
                    bombsFound++;
                }
            }
        }
        return result;
    }

    public void revealSurrounding(int row, int column) {
        MineCell cell = field[row][column];
        int flagCount = 0;
        for (int[] pos: cell.getNeighbors()) {
                if (field[pos[0]][pos[1]].isFlagged()) {
                    flagCount++;
                }
        }
        if (flagCount != cell.getnumber()) {
            return;
        }
        for (int[] pos: cell.getNeighbors()) {
                if (field[pos[0]][pos[1]].canReveal()) {
                    controller.revealCell(pos[0], pos[1]);
                }
        }
    }

    public int[] getTime() {
        int seconds = (int) (((System.nanoTime() - startTime)) / 1000000000);
        int[] time = {seconds/60, seconds%60};
        return time;

    }

    public int getFound() {
        return revealed;
    }

    public int getSafe() {
        return field.length*field[0].length - bombs;
    }
}
