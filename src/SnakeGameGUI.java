
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SnakeGameGUI extends JFrame implements ActionListener {
    private SnakeGame snakeGame;
    private Timer timer;
    

    public SnakeGameGUI() {
        snakeGame = new SnakeGame();
        timer = new Timer(100, this); // 100 milisegundos

        setTitle("Snake Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        add(snakeGame);
        pack();

        addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e.getKeyCode());
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        setFocusable(true);
        requestFocusInWindow(); // Obtener enfoque del panel

        setLocationRelativeTo(null);
        setVisible(true);

        timer.start();
    }

    private void handleKeyPress(int keyCode) {

        if (keyCode == KeyEvent.VK_SPACE) {
        
        } else {
            snakeGame.handleKeyPress(keyCode);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        snakeGame.update();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SnakeGameGUI::new);
    }
}

class SnakeGame extends JPanel {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static final int UNIT_SIZE = 20;

    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    private int appleX;
    private int appleY;
    private int[] snakeX;
    private int[] snakeY;
    private int snakeLength;
    private Direction direction;
    private boolean gameOver;
    private boolean autoPlay;

    public SnakeGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        requestFocusInWindow(); // Obtener enfoque del panel

        initGame();
    }

    private void initGame() {
        snakeX = new int[WIDTH * HEIGHT / UNIT_SIZE];
        snakeY = new int[WIDTH * HEIGHT / UNIT_SIZE];
        snakeLength = 1;
        direction = Direction.RIGHT;
        gameOver = false;

        generateApple();

        snakeX[0] = WIDTH / 2;
        snakeY[0] = HEIGHT / 2;
    }

    private void generateApple() {
        appleX = (int) (Math.random() * (WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = (int) (Math.random() * (HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void handleKeyPress(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
                if (direction != Direction.DOWN)
                    direction = Direction.UP;
                break;
            case KeyEvent.VK_DOWN:
                if (direction != Direction.UP)
                    direction = Direction.DOWN;
                break;
            case KeyEvent.VK_LEFT:
                if (direction != Direction.RIGHT)
                    direction = Direction.LEFT;
                break;
            case KeyEvent.VK_RIGHT:
                if (direction != Direction.LEFT)
                    direction = Direction.RIGHT;
                break;
        }
    }

   public void update() {
    if (autoPlay) {
        updateAutoPlayDirection();
    }
    
    moveSnake();
    checkCollision();
    checkAppleCollision();
}

private void updateAutoPlayDirection() {
    // Lógica para actualizar la dirección de la serpiente en modo de juego automático
    // Puedes implementar tu propia lógica aquí para determinar la dirección automáticamente
    // Por ejemplo, puedes usar un algoritmo para mover la serpiente hacia la manzana.
    // En este caso, implementaremos un movimiento aleatorio de la serpiente.

    Direction[] directions = Direction.values();
    int randomIndex = (int) (Math.random() * directions.length);
    direction = directions[randomIndex];
}

    private void moveSnake() {
        for (int i = snakeLength - 1; i > 0; i--) {
            snakeX[i] = snakeX[i - 1];
            snakeY[i] = snakeY[i - 1];
        }

        switch (direction) {
            case UP:
                snakeY[0] -= UNIT_SIZE;
                break;
            case DOWN:
                snakeY[0] += UNIT_SIZE;
                break;
            case LEFT:
                snakeX[0] -= UNIT_SIZE;
                break;
            case RIGHT:
                snakeX[0] += UNIT_SIZE;
                break;
        }
    }

    private void checkCollision() {
        
        // // Comprobar colisión con los bordes del juego y el propio cuerpo de la
        // serpiente
        // if (snakeX[0] < 0 || snakeY[0] < 0 || snakeX[0] >= WIDTH || snakeY[0] >=
        // HEIGHT) {
        // gameOver = true;
        // }

        // for (int i = 1; i < snakeLength; i++) {
        // if (snakeX[i] == snakeX[0] && snakeY[i] == snakeY[0]) {
        // gameOver = true;
        // break;
        // }
        // }

        // Comprobar colisión con los bordes del juego y actualizar la posición de la
        // serpiente
        if (snakeX[0] < 0)
            snakeX[0] = WIDTH - UNIT_SIZE;
        else if (snakeX[0] >= WIDTH)
            snakeX[0] = 0;

        if (snakeY[0] < 0)
            snakeY[0] = HEIGHT - UNIT_SIZE;
        else if (snakeY[0] >= HEIGHT)
            snakeY[0] = 0;

        // Comprobar colisión con el propio cuerpo de la serpiente
        for (int i = 1; i < snakeLength; i++) {
            if (snakeX[i] == snakeX[0] && snakeY[i] == snakeY[0]) {
                gameOver = true;
                break;
            }
        }
    }

    private void checkAppleCollision() {
        // Comprobar colisión con la comida (manzana) y aumentar la longitud de la
        // serpiente
        if (snakeX[0] == appleX && snakeY[0] == appleY) {
            snakeLength++;
            generateApple();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over", WIDTH / 2 - 100, HEIGHT / 2);
            return;
        }

        g.setColor(Color.GREEN);
        for (int i = 0; i < snakeLength; i++) {
            g.fillRect(snakeX[i], snakeY[i], UNIT_SIZE, UNIT_SIZE);
        }

        g.setColor(Color.RED);
        g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
    }
}

