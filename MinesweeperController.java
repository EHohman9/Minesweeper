public class MinesweeperController{
    MinesweeperView view;
    MinesweeperModel model;

    public MinesweeperController(MinesweeperView view, MinesweeperModel model) {
        this.view = view;
        this.model = model;
        view.registerController(this);
    }

    public void startGame(int rows, int columns, int bombs) {
        model.setField(rows, columns, bombs, this);
        view.setGameWindow(rows, columns, bombs);
    }

    public void revealCell(int i, int j) {
        model.reveal(i,j);
        view.updateCell(i, j, model.getCell(i,j), model.getFlagsRemaining());
    }

    public void flagCell(int i, int j) {
        model.flagCell(i, j);
        view.updateCell(i, j, model.getCell(i,j), model.getFlagsRemaining());
    }

    public void gameOver(int row, int column) {
        for (int[] location: model.getAllBombs()) {
            view.updateCell(location[0], location[1], "bomb", model.getFlagsRemaining());
        }
        view.gameOver(row,column, model.getTime(), model.getFound(), model.getSafe());
    }

    public void victory() {
        view.victory(model.getTime());
    }

    public boolean isRevealed(int row, int column) {
        return model.isRevealed(row, column);
    }

    public void revealSurrounding(int i, int j) {
        model.revealSurrounding(i,j);
    }
}