//Hungry Snake Game developed using Java
//Author - Anush Raghavender
//RollNumber - CED16I042
//Course - Data Structures & Algorithms

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {
    private final int WIDTH = 500;
    private final int HEIGHT = 500;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 900;
    private final int RAND_POS = 30;
    private final int DELAY = 140;

    private final int[] x = new int[ALL_DOTS];
    private final int[] y = new int[ALL_DOTS];

    private int dots;
    private int apple_x;
    private int apple_y;

    private int score = 0;

    private int bonusApple_x;
    private int bonusApple_y;
    private boolean bonusAppleVisible = false;
    private boolean bonusActive = false;
    private int bonusTimer = 0;

    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    private boolean inGame = true;

    private Timer timer;
    private Image body;
    private Image apple;
    private Image bonusApple;
    private Image head;

    public SnakeGame() {
        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        loadImages();
        initGame();
    }

    private void loadImages() {
        ImageIcon iid = new ImageIcon("resources/dot.png");
        body = iid.getImage();

        ImageIcon iia = new ImageIcon("resources/apple.png");
        apple = iia.getImage();

        ImageIcon iih = new ImageIcon("resources/head.png");
        head = iih.getImage();

        ImageIcon iib = new ImageIcon("resources/bonus_apple.png");
        bonusApple = iib.getImage();
    }

    //Start game
    private void initGame() {
        dots = 3;

        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }

        spawnApple();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    //Draw the apple, bonus apple, snake head, snake body & score
    private void doDrawing(Graphics g) {
        if (inGame) {
            if (bonusAppleVisible) {
                g.drawImage(bonusApple, bonusApple_x, bonusApple_y, this);
            }
            else {
                g.drawImage(apple, apple_x, apple_y, this);
            }            

            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.drawImage(head, x[z], y[z], this);
                } else {
                    g.drawImage(body, x[z], y[z], this);
                }
            }

            g.setColor(Color.white);
            g.drawString("Score: " + score, 5, 15);
            Toolkit.getDefaultToolkit().sync();

        } else {
            gameOver(g);
        }
    }

    //Display game over text
    private void gameOver(Graphics g) {
        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (WIDTH - metr.stringWidth(msg)) / 2, HEIGHT / 2);
        g.drawString("Score: " + score, (WIDTH - 50) / 2, HEIGHT / 2 + 20);
    }

    //Check if snake ate apple, increase score by 1, increase length of snake by 1, spawn normal apple, if score is divisible by 5 spawn bonus apple
    private void checkApple() {
        if (!bonusAppleVisible && (x[0] == apple_x) && (y[0] == apple_y)) {
            dots++;
            score++;
            if (score % 5 == 0) {
                bonusActive = true;
                bonusAppleVisible = true;
                bonusTimer = 50;
                spawnBonusApple();
            } else {
                spawnApple();
            }
        }
    }

    //Check if snake ate bonus apple, increase score by 2, increase length of snake by 2, spawn normal apple
    private void checkBonusApple() {
        if (bonusActive) {
            if (bonusTimer == 0) {
                bonusActive = false;
                bonusAppleVisible = false;
                spawnApple();
            } else {
                bonusTimer--;
            }
        }
        if (bonusAppleVisible && (x[0] == bonusApple_x) && (y[0] == bonusApple_y)) {
            score+=2;
            for (int i = 0; i < 2; i++) {
                dots++;
                x[dots - 1] = x[dots - 2];
                y[dots - 1] = y[dots - 2];
            }
            bonusActive = false;
            bonusAppleVisible = false;
            spawnApple();
        }
    }

    //Move snake in direction of arrow key pressed
    private void move() {
        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (left) {
            x[0] -= DOT_SIZE;
        }
        if (right) {
            x[0] += DOT_SIZE;
        }
        if (up) {
            y[0] -= DOT_SIZE;
        }
        if (down) {
            y[0] += DOT_SIZE;
        }
    }

    //Check collision between game window and snake body
    private void checkCollision() {
        for (int z = dots; z > 0; z--) {
            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }

        if (y[0] >= HEIGHT) {
            inGame = false;
        }
        if (y[0] < 0) {
            inGame = false;
        }
        if (x[0] >= WIDTH) {
            inGame = false;
        }
        if (x[0] < 0) {
            inGame = false;
        }
        if (!inGame) {
            timer.stop();
        }
    }

    //Randomly spawn apple in board
    private void spawnApple() {
        Random r = new Random();
        int rX = r.nextInt(RAND_POS);
        int rY = r.nextInt(RAND_POS);
        apple_x = ((rX * DOT_SIZE));
        apple_y = ((rY * DOT_SIZE));
    }

    //Randomly spawn bonus apple in board
    private void spawnBonusApple() {
        Random r = new Random();
        int rX = r.nextInt(RAND_POS);
        int rY = r.nextInt(RAND_POS);
        bonusApple_x = ((rX * DOT_SIZE));
        bonusApple_y = ((rY * DOT_SIZE));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkApple();
            checkBonusApple();
            checkCollision();
            move();
        }

        repaint();
    }

    //Handle user inputs to change direction of snake
    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!right)) {
                left = true;
                up = false;
                down = false;
            }
            if ((key == KeyEvent.VK_RIGHT) && (!left)) {
                right = true;
                up = false;
                down = false;
            }
            if ((key == KeyEvent.VK_UP) && (!down)) {
                up = true;
                right = false;
                left = false;
            }
            if ((key == KeyEvent.VK_DOWN) && (!up)) {
                down = true;
                right = false;
                left = false;
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new SnakeGame());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
