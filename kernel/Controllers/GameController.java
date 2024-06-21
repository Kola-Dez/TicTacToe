package kernel.Controllers;

import kernel.GameInfo.Definitions;
import kernel.Vindow.GameOverPanel;
import kernel.Vindow.PressNamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameController extends JPanel {
    private final int BOARD_SIZE;
    private final char[][] board;
    private final JButton[][] buttons; // Массив кнопок для отображения игрового поля
    public String complexity;
    private final Controller controller;
    private String namePlayer;
    private long startTime;
    private boolean playerHod;
    private final boolean isAi;
    private boolean gameOver;

    /**
     ** Конструктор класса GameController, инициализирует поле и кнопки
     */
    public GameController(Controller controller) {
        this.controller = controller;
        this.isAi = controller.isAi;
        this.BOARD_SIZE = this.controller.BOARD_SIZE;
        this.gameOver = false;
        this.complexity = controller.complexity;
        board = new char[BOARD_SIZE][BOARD_SIZE]; // Инициализация игрового поля
        buttons = new JButton[BOARD_SIZE][BOARD_SIZE]; // Инициализация массива кнопок
        playerHod = true;
        setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        initializeButtons();
        startGame(); // Запуск игры
    }

    /**
     ** Запускает игру: очищает и выводит игровое поле
     */
    private void startGame() {
        clearBoard();
        timerStart();
    }

    /**
     ** Инициализирует кнопки и добавляет их на панель
     */
    private void initializeButtons() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                buttons[i][j] = new JButton("");
                int fontSize = Math.max(10, 40 - BOARD_SIZE); // Устанавливаем размер шрифта в зависимости от размера доски
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, fontSize));
                buttons[i][j].setPreferredSize(new Dimension(100, 100));
                int finalI = i;
                int finalJ = j;

                buttons[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Обработчик нажатия на кнопку
                        if (!gameOver && isCellValidField(finalI, finalJ)) { // Проверяем, можно ли поставить X или O

                            if (playerHod) {
                                board[finalI][finalJ] = Definitions.X_FIELD;
                                buttons[finalI][finalJ].setText("X");
                                playerHod = false;
                                gameOver = isGameOver("Player \"X\" wins!");
                                if (isAi && !gameOver) {
                                    aiTurn();
                                    gameOver = isGameOver("AI wins!");
                                    playerHod = true;
                                }
                            } else {
                                board[finalI][finalJ] = Definitions.O_FIELD;
                                buttons[finalI][finalJ].setText("O");
                                playerHod = true;
                                gameOver = isGameOver("Player \"O\" wins!");
                            }
                        }
                    }
                });
                add(buttons[i][j]); // Добавляем кнопку на панель
            }
        }
    }

    /**
     ** Проверяет, завершена ли игра
     * @param text Сообщение для отображения
     * @return true если игра завершена, иначе false
     */
    private boolean isGameOver(String text) {
        if (isWin(Definitions.X_FIELD)) {
            JOptionPane.showMessageDialog(this, text);
            if (isAi) {
                pressName();
            }else {
                exitGame();
            }
            return true;
        } else if (isWin(Definitions.O_FIELD)) { // Проверяем победу AI
            JOptionPane.showMessageDialog(this, text);
            exitGame();
            return true;
        } else if (isDraft()) { // Проверяем ничью
            JOptionPane.showMessageDialog(this, "Draw!");
            exitGame();
            return true;
        }
        return false; // Игра продолжается
    }

    /**
     ** Проверяет, является ли игра ничьей
     * @return true если ничья, иначе false
     */
    private boolean isDraft() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == Definitions.EMPTY_FIELD) {
                    return false;
                }
            }
        }
        return true; // Нет пустых клеток, ничья
    }

    /**
     ** Проверяет, выиграл ли указанный игрок
     * @param playerField Символ игрока ('X' или 'O')
     * @return true если игрок выиграл, иначе false
     */
    private boolean isWin(char playerField) {
        // Проверка горизонталей и вертикалей
        for (int i = 0; i < BOARD_SIZE; i++) {
            boolean horizontalWin = true;
            boolean verticalWin = true;
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] != playerField) {
                    horizontalWin = false;
                }
                if (board[j][i] != playerField) {
                    verticalWin = false;
                }
            }
            if (horizontalWin || verticalWin) {
                return true;
            }
        }

        // Проверка диагоналей
        boolean diagonal1Win = true;
        boolean diagonal2Win = true;
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (board[i][i] != playerField) {
                diagonal1Win = false;
            }
            if (board[i][BOARD_SIZE - 1 - i] != playerField) {
                diagonal2Win = false;
            }
        }
        return diagonal1Win || diagonal2Win;
    }

    private void pressName() {
        PressNamePanel pressNamePanel = new PressNamePanel(this);
        getParent().add(pressNamePanel);
        setVisible(false);
    }

    /**
     ** Ход AI (ставит 'O' в случайную пустую клетку)
     */
    private void aiTurn() {
        int x, y;
        do {
            x = (int) (Math.random() * BOARD_SIZE);
            y = (int) (Math.random() * BOARD_SIZE);
        } while (!isCellValidField(x, y)); // Проверка валидности клетки
        board[x][y] = Definitions.O_FIELD;
        buttons[x][y].setText("O"); // Обновляем текст кнопки
    }

    /**
     ** Проверяет, является ли клетка валидной для хода
     * @param x координата X
     * @param y координата Y
     * @return true если клетка валидная, иначе false
     */
    private boolean isCellValidField(int x, int y) {
        if (x < 0 || y < 0 || x >= BOARD_SIZE || y >= BOARD_SIZE) {
            return false;
        }
        return board[x][y] == Definitions.EMPTY_FIELD;
    }

    /**
     ** Очищает игровое поле
     */
    private void clearBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = Definitions.EMPTY_FIELD;
                buttons[i][j].setText("");
            }
        }
    }

    public void setNamePlayer(String namePlayer) {
        this.namePlayer = namePlayer;
    }

    public void viewGameOverPanel() {
        GameOverPanel gameOverPanel = new GameOverPanel(this);
        getParent().add(gameOverPanel);
        gameOverPanel.setVisible(true);
    }

    /**
     * Получение имени игрока.
     * @return Имя игрока.
     */
    public String getNamePlayer() {
        return this.namePlayer;
    }

    /**
     * Запуск таймера.
     */
    private void timerStart() {
        this.startTime = System.nanoTime();
    }

    /**
     * Получение текущего времени.
     * @return Текущее время в секундах.
     */
    public int getTime() {
        long endTime = System.nanoTime();
        double durationSeconds = (endTime - startTime) / 1e9;
        return (int) durationSeconds;
    }

    public void exitGame() {
        System.exit(1);
    }

    public void restartGame() {
        controller.restartGame();
    }
}
