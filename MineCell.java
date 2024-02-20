import java.util.LinkedList;
import java.util.List;

public class MineCell {
    private boolean revealed = false;
    private boolean isBomb;
    private boolean flagged = false;
    private List<int[]> neighbors;
    private int neighborBombs;
    private int row;
    private int column;

    MineCell(boolean bomb, int row, int column, int maxRow, int maxColumn) {
        isBomb = bomb;
        this.row = row;
        this.column = column;
        neighbors = new LinkedList<int[]>();
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = column - 1; j <= column + 1; j++) {
                if (!(i == row && j == column) && i >= 0 && i < maxRow && j >= 0 && j < maxColumn) {
                    neighbors.add(new int[] {i, j});
                }
            }
        }
    }

    List<int[]> getNeighbors() {
        return neighbors;
    }

    int[] getlocation() {
        return new int[] {row, column};
    }

    void setbombs(int neighbors) {
        neighborBombs = neighbors;
    }

    void reveal() {
        revealed = true;
    }

    void swapFlag() {
        flagged = !flagged;
    }

    int getnumber() {
        return neighborBombs;
    }

    boolean isBomb() {
        return isBomb;
    }

    boolean isFlagged() {
        return flagged;
    }

    boolean isRevealed() {
        return revealed;
    }

    boolean canReveal() {
        return !flagged && !revealed;
    }

    boolean isZero() {
        return neighborBombs == 0;
    }

    String displayImage() {
        if (revealed) {
            if (isBomb) {return "M";}
            switch (neighborBombs) {
                case 0:
                    return "0";
                case 1:
                    return "1";
                case 2:
                    return "2";
                case 3:
                    return "3";
                case 4:
                    return "4";
                case 5:
                    return "5";
                case 6:
                    return "6";
                case 7:
                    return "7";
                case 8:
                    return "8";
                default:
                    //error message
                    break;
            }
        }
        if (flagged) {
            return "F";
        }
        return "U";
    }
}