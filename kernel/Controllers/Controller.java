package kernel.Controllers;

import kernel.Vindow.View;
import kernel.Vindow.ViewSize;
import kernel.Vindow.PressNamePanel;

import java.util.Objects;

public class Controller {
    public int BOARD_SIZE;
    public String complexity;
    private View view;
    public boolean isAi = false;

    public Controller() {
        new ViewSize(this);
    }
    public void restartGame(){
        view.setVisible(false);
        new ViewSize(this);
    }

    public void CreatGame() {
        this.view = new View();
        view.add(new GameController(this));
        view.setVisible(true);
    }

    public void setSizeWorld(String complexity) {
        if (Objects.equals(complexity, "3x3")) {
            BOARD_SIZE = 3;
        } else if (Objects.equals(complexity, "5x5")) {
            BOARD_SIZE = 5;
        } else if (Objects.equals(complexity, "7x7")) {
            BOARD_SIZE = 7;
        }
        this.complexity = complexity;
    }

    public void setIe(boolean selected) {
        isAi = selected;
    }
}
