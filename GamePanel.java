import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_HEIGHT = 600;
    static final int SCREEN_WIDTH = 600;
    static final int UNIT_SIZE = 30;
    static final int GAME_UNITS = (SCREEN_HEIGHT * SCREEN_WIDTH) / UNIT_SIZE;
    static final int DELAY = 75;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    boolean running = false;
    int bodyParts = 2;
    int score = 0;
    int appleX;
    int appleY;
    char direction = 'D';
    Random random;
    Timer timer;

    GamePanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
       }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g) {
        if (running) {
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            g.setFont(new Font("monospace", Font.PLAIN, 30));
            g.drawString("Score: " + score, (SCREEN_WIDTH / 2) - 50, g.getFont().getSize());
    
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(new Color(0xfcca46));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(0, 230, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
        } else {
            gameOver(g);
        }
    }
    public void newApple() {
        random = new Random();
        appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }
    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }
    public void gameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("monospace", Font.ITALIC, 75));
        g.drawString("GAME OVER", (SCREEN_WIDTH / 4) - g.getFont().getSize(), SCREEN_HEIGHT / 2);
        g.setFont(new Font("monospace", Font.PLAIN, 30));
        g.drawString("Score: " + score, (SCREEN_WIDTH / 2) - 50, g.getFont().getSize());
    
    }
    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            score++;
            newApple();
        }
    }
    public void checkCollision() {
        // collide with bodyparts
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        // collide with edges
        if (x[0] + UNIT_SIZE == SCREEN_WIDTH || x[0] < 0
                || y[0] + UNIT_SIZE == SCREEN_HEIGHT || y[0] - UNIT_SIZE < 0) {
            running = false;
        }

        if (x[0] == 0 && direction == 'L')
            running = false;

        if (!running)
            timer.stop();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollision();
        }
        repaint();
    }
    public class MyKeyAdapter extends KeyAdapter {
        
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case 37:
                    if (direction != 'R')
                        direction = 'L';
                    break;
                case 38:
                    if (direction != 'D')
                        direction = 'U';
                    break;
                case 39:
                    if (direction != 'L')
                        direction = 'R';
                    break;
                case 40:
                    if (direction != 'U')
                        direction = 'D';
                    break;
		case 32:
            if (!running) {
                for (int i = bodyParts; i >= 0; i--)
                {
                    x[i] = 0;
                    y[i] = 0;
                }
                bodyParts = 2;
                score = 0;
                direction = 'D';
                startGame();
            }
		    break;
            }
        }
        
    }

}
