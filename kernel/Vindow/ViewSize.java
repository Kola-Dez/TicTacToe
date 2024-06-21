package kernel.Vindow;

import kernel.Controllers.Controller;
import kernel.GameInfo.Definitions;

import javax.swing.*;
import java.awt.*;

public class ViewSize extends JFrame {

    private JRadioButton playerButton;
    private JRadioButton aiButton;
    private Controller controller;

    public ViewSize(Controller c) {
        this.controller = c;
        setTitle(Definitions.gameName);
        setSize(Definitions.widthWindow, Definitions.heightWindow);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JLabel enterComplexityLabel = new JLabel("Введите размеры игры: 3x3, 5x5, 7x7");
        enterComplexityLabel.setFont(new Font("Arial", Font.BOLD, 20));
        enterComplexityLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(enterComplexityLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1));
        centerPanel.setBackground(Color.BLACK);

        JPanel radioButtonPanel = new JPanel(new FlowLayout());
        radioButtonPanel.setBackground(Color.BLACK);
        ButtonGroup group = new ButtonGroup();
        playerButton = new JRadioButton(" 1 vs 1 ");
        playerButton.setFont(new Font("Arial", Font.PLAIN, 18));
        playerButton.setBackground(Color.BLACK);
        playerButton.setForeground(Color.WHITE);
        aiButton = new JRadioButton("Player vs AI");
        aiButton.setFont(new Font("Arial", Font.PLAIN, 18));
        aiButton.setSelected(true);
        aiButton.setBackground(Color.BLACK);
        aiButton.setForeground(Color.WHITE);
        group.add(playerButton);
        group.add(aiButton);
        radioButtonPanel.add(playerButton);
        radioButtonPanel.add(aiButton);
        centerPanel.add(radioButtonPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.BLACK);
        JButton playButtonLite = createButton("3x3", c);
        JButton playButtonNormal = createButton("5x5", c);
        JButton playButtonHard = createButton("7x7", c);

        buttonPanel.add(playButtonLite);
        buttonPanel.add(playButtonNormal);
        buttonPanel.add(playButtonHard);

        centerPanel.add(buttonPanel);
        add(centerPanel, BorderLayout.CENTER);

        pack(); // Подгоняем размеры окна
        setVisible(true); // Показываем окно
    }

    private JButton createButton(String label, Controller c) {
        JButton button = new JButton(label);
        button.addActionListener(e -> {
            c.setSizeWorld(label.toLowerCase());
            c.setIe(aiButton.isSelected()); // Устанавливаем значение isIe в зависимости от выбранной радио-кнопки
            setVisible(false);
            c.CreatGame();
            dispose();
        });
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setHorizontalAlignment(SwingConstants.CENTER);
        return button;
    }
}
