/* GridLayout*/

import java.awt.*;

class Grid extends Frame {
    public Grid() {
        this.setSize(200, 100);

        String items[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
        Button but[] = new Button[10];

        setLayout(new GridLayout(2, 5));

        for (int i = 0; i < items.length; i++) {
            add(new Button(items[i]));
        }

        this.show();
    }

    public static void main(String[] args) {
        Grid g = new Grid();
    }
}
/*
Result: В прикладном окне менеджер расстановки разместил 10 кнопок в 2 ряда с не
более 5 кнопок в ряду. См. рисунок ниже.
*/