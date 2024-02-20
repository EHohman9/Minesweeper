import javax.swing.JButton;

public class MyButton extends JButton{
    private int[] location = new int[2];
    MyButton(int row, int column) {
        location[0] = row;
        location[1] = column;
    }

    int[] getData() {
        return location;
    }
}
