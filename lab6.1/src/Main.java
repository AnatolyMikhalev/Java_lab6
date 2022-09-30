import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

public class Main {
    static int count;

    public static void main(String[] args) {
        count = 0;
        Figures balls = new Figures();
    }
}

class Figures extends Frame implements Observer, ActionListener, ItemListener {
    private LinkedList LL = new LinkedList();
    private Color col;
    private int type;
    private int speed;
    private Frame f;  //Управляющее окно
    private Button b, bChange;
    private Choice c, cFigures, cSpeed;
    private TextField tfNumber, tfSpeed;

    Figures() {
        this.addWindowListener(new WindowAdapter2());
        f = new Frame();
        f.setSize(new Dimension(230, 300));
        f.setTitle("Контроль");
        f.setLayout(new GridLayout(0,1));  //Менеджер расположения
        f.addWindowListener(new WindowAdapter2());

        c = new Choice();         //выпадающий список названий
        c.addItem("Круг");
        c.addItem("Овал");
        c.addItem("Квадрат");
        c.addItemListener(this);
        f.add(c, new Point(60, 20));

        cSpeed = new Choice();         //выпадающий список названий
        cSpeed.addItem("1");
        cSpeed.addItem("2");
        cSpeed.addItem("3");
        cSpeed.addItem("4");
        cSpeed.addItem("5");
        cSpeed.addItemListener(this);
        f.add(cSpeed, new Point(60, 20));

        cFigures = new Choice();         //выпадающий список названий
        f.add(cFigures, new Point(60, 20));

        JLabel labelSpeed = new JLabel("Введите номер фигуры:");
        f.add(labelSpeed);

        tfNumber = new TextField();
        f.add(tfNumber);

        JLabel labelspeed = new JLabel("Введите скорость фигуры:");
        f.add(labelspeed);

        tfSpeed = new TextField();
        f.add(tfSpeed);

        b = new Button("Пуск");
        b.setSize(new Dimension(10, 40));
        b.setActionCommand("OK");
        b.addActionListener(this);
        f.add(b, new Point(20, 20));

        bChange = new Button("Применить изменения");
        bChange.setSize(new Dimension(10, 40));
        bChange.setActionCommand("Change");
        bChange.addActionListener(this);
        f.add(bChange, new Point(20, 20));

        f.setVisible(true);

        this.setSize(500, 200);  // Демонстрационное окно
        this.setVisible(true);
        this.setLocation(100, 150);
    }

    public void update(Observable o, Object arg) {
        Figure figure = (Figure) arg;
        System.out.println(figure.thr.getName() + ":   x= " + figure.x+ "y= " + figure.y);
        repaint();
    }

    public void paint(Graphics g) {
        if (!LL.isEmpty()) {
            for (Object LL1 : LL) {
                Figure figure = (Figure) LL1;
                g.setColor(figure.col);

                switch (figure.type) {
                    case 0:
                        g.drawOval(figure.x, figure.y, 20, 20);
                        g.drawString(figure.thr.getName(), figure.x, figure.y);
                        break;
                    case 1:
                        g.drawOval(figure.x, figure.y, 20, 40);
                        g.drawString(figure.thr.getName(), figure.x, figure.y);
                        break;
                    case 2:
                        g.drawRect(figure.x, figure.y, 40, 40);
                        g.drawString(figure.thr.getName(), figure.x, figure.y);
                        break;
                }
            }
        }
    }

    public void itemStateChanged(ItemEvent iE) {
    }

    public void actionPerformed(ActionEvent aE) {
        boolean exist = false;

        if (aE.getActionCommand() == "OK") {
            if (!LL.isEmpty()) {
                for (Object LL1 : LL) {
                    Figure figure = (Figure) LL1;
                    if (this.tfNumber.getText().equals(figure.thr.getName())) {
                        exist = true;
                        break;
                    }
                }
            }
            if (!exist) {
                if (Main.count < 9) {
                    Color initialcolor = Color.RED;
                    col = JColorChooser.showDialog(this, "Select a color", initialcolor);
                    type = c.getSelectedIndex();
                    speed = Integer.parseInt(this.tfSpeed.getText());
                    cFigures.add(this.tfNumber.getText());

                    Figure figure = new Figure(col, this.tfNumber.getText(), type, speed);

                    LL.add(figure);
                    figure.addObserver(this);
                } else {
                    System.out.println("Количество фигур слишком велико");
                }
            }
        } else if (aE.getActionCommand() == "Change") {
            switch (cSpeed.getSelectedIndex()) {
                case 0 -> speed = 1;
                case 1 -> speed = 2;
                case 2 -> speed = 3;
                case 3 -> speed = 4;
                case 4 -> speed = 5;
            }

            String figureName = cFigures.getSelectedItem();

            for (Object LL1 : LL) {
                Figure figure = (Figure) LL1;

                if (figureName.equals(figure.thr.getName())) {
                    Color initialcolor = figure.col;
                    figure.col = JColorChooser.showDialog(this, "Select a color", initialcolor);
                    figure.speed = speed;
                    break;
                }
            }
        }
        repaint();
    }
}

class Figure extends Observable implements Runnable {
    Thread thr;
    private boolean xplus;
    private boolean yplus;
    int x;
    int y;
    int type;
    int speed;
    int directionX;
    int directionY;
    Color col;

    public Figure(Color col, String text, int type, int speed) {
        xplus = true;
        yplus = true;
        x = 0;
        y = 29;
        directionX = (int) (Math.random() * 3 + 1);
        directionY = (int) (Math.random() * 3 + 1);

        this.col = col;
        this.type = type;
        this.speed = speed;
        Main.count++;
        //thr = new Thread(this, Main.count + ":" + text + ":");
        thr = new Thread(this, text);
        thr.start();
    }

    public void run() {
        while (true) {
            if (x >= 475) xplus = false;
            if (x <= -1) xplus = true;
            if (y >= 175) yplus = false;
            if (y <= 29) yplus = true;

            if (xplus) x += directionX * speed;
            else x -= directionX * speed;

            if (yplus) y += directionY * speed;
            else y -= directionY * speed;

            setChanged();
            notifyObservers(this);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }
        }
    }
}

class WindowAdapter2 extends WindowAdapter {
    public void windowClosing(WindowEvent wE) {
        System.exit(0);
    }
}