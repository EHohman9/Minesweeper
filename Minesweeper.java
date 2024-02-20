public class Minesweeper {

    public static void main(String[] args) {
        MinesweeperView view = new MinesweeperView();
        MinesweeperModel model = new MinesweeperModel();
        new MinesweeperController(view, model);
    }
}