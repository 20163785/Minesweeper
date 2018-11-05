package minesweeper;

//importai funkciju ir pelytes
import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

//jframe yra programos startavimo tipas
public class GUI extends JFrame {

    public boolean resetter = false;//restartas

    public boolean flagger = false;//veliava

    Date startDate = new Date();//laikas
    Date endDate;

    int spacing = 5;

    int neighs = 0;

    String vicMes = "Nothing yet!";

    public int mx = -100;//koordinates
    public int my = -100;

    public int smileyX = 605;//smailo vieta
    public int smileyY = 5;

    public int smileyCenterX = smileyX + 35; //smailo centras
    public int smileyCenterY = smileyY + 35;

    public int flaggerX = 445; //veliavos vieta
    public int flaggerY = 5;

    public int flaggerCenterX = flaggerX + 35; //veliavos centras
    public int flaggerCenterY = flaggerY + 35;

    public int timeX = 1130;//laikmacio vieta
    public int timeY = 5;

    public int vicMesX = 700;//uzraso vieta
    public int vicMesY = -50;

    public int sec = 0;

    public boolean happiness = true;

    public boolean victory = false;

    public boolean defeat = false;

    Random rand = new Random();

    int[][] mines = new int[16][9]; //minos
    int[][] neighbours = new int[16][9];//kaimyna
    boolean[][] revealed = new boolean[16][9];//tusti langeliai
    boolean[][] flagged = new boolean[16][9];//veliavos

    
    //gui konfiguracija
    public GUI() {
        this.setTitle("Minesweeper");
        this.setSize(1286, 829);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);

        //bombu ir kaimynu aprasymas
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++) {
                if (rand.nextInt(100) < 20) {
                    mines[i][j] = 1;
                } else {
                    mines[i][j] = 0;
                }
                revealed[i][j] = false;
            }
        }

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++) {
                neighs = 0;
                for (int m = 0; m < 16; m++) {
                    for (int n = 0; n < 9; n++) {
                        if (!(m == i && n == j)) {
                            if (isN(i, j, m, n) == true) {
                                neighs++;
                            }
                        }
                    }
                }
                neighbours[i][j] = neighs;
            }
        }

        Board board = new Board();
        this.setContentPane(board);

        Move move = new Move();
        this.addMouseMotionListener(move);

        Click click = new Click();
        this.addMouseListener(click);
    }

    public class Board extends JPanel {

        //spalvos langeliu, fono, minu, skaiciu ir tt
        @Override
        public void paint(Graphics g) {
            g.setColor(Color.lightGray);
            g.fillRect(0, 0, 1280, 800);
            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 9; j++) {
                    int spaccing = 0;
                    g.setColor(Color.gray);
                    if (revealed[i][j] == true) {
                        g.setColor(Color.white);
                        if (mines[i][j] == 1) {
                            g.setColor(Color.red);
                        }
                    }
                    if (mx >= spacing + i * 80 && mx < spacing + i * 80 + 80 - 2 * spacing && my >= spaccing + j * 80 + 80 + 26 && my < spacing + j * 80 + 80 + 80 - 2 * spacing) {
                        g.setColor(Color.DARK_GRAY);
                    }
                    g.fillRect(spacing + i * 80, spacing + j * 80 + 80, 80 - 2 * spacing, 80 - 2 * spacing);
                    if (revealed[i][j] == true) {
                        g.setColor(Color.black);
                        if (mines[i][j] == 0 && neighbours[i][j] != 0) {
                            if (neighbours[i][j] == 1) {
                                g.setColor(Color.blue);
                            } else if (neighbours[i][j] == 2) {
                                g.setColor(Color.green);
                            } else if (neighbours[i][j] == 3) {
                                g.setColor(Color.red);
                            } else if (neighbours[i][j] == 4) {
                                g.setColor(new Color(0, 0, 128));
                            } else if (neighbours[i][j] == 5) {
                                g.setColor(new Color(178, 34, 34));
                            } else if (neighbours[i][j] == 6) {
                                g.setColor(new Color(72, 209, 204));
                            } else if (neighbours[i][j] == 7) {
                                g.setColor(Color.black);
                            } else if (neighbours[i][j] == 8) {
                                g.setColor(Color.darkGray);
                            }
                            g.setFont(new Font("Tahoma", Font.BOLD, 40));
                            g.drawString(Integer.toString(neighbours[i][j]), i * 80 + 27, j * 80 + 80 + 55);
                        } else if (mines[i][j] == 1) {
                            g.fillRect(i * 80 + 10 + 20, j * 80 + 80 + 20, 20, 40);
                            g.fillRect(i * 80 + 20, j * 80 + 80 + 10 + 20, 40, 20);
                            g.fillRect(i * 80 + 5 + 20, j * 80 + 80 + 5 + 20, 30, 30);
                            g.fillRect(i * 80 + 38, j * 80 + 80 + 15, 4, 50);
                            g.fillRect(i * 80 + 15, j * 80 + 80 + 38, 50, 4);
                        }
                    }

                    //flagerio paveiksliukas ant kvadrateliu
                    if (flagged[i][j] == true) {
                        g.setColor(Color.black);
                        g.fillRect(i*80+32+5, j*80+80+15, 7, 40);
                        g.fillRect(i*80+20+5, j*80+80+50, 30, 10);
                        g.setColor(Color.red);
                        g.fillRect(i*80+16+5, j*80+80+15, 20, 15);
                        g.setColor(Color.black);
                        g.drawRect(i*80+16+5, j*80+80+15, 20, 15);
                        g.drawRect(i*80+17+5, j*80+80+16, 18, 13);
                        g.drawRect(i*80+18+5, j*80+80+17, 16, 11);
                    }

                }
            }

            //smiley paveikslas
            g.setColor(Color.yellow);
            g.fillOval(smileyX, smileyY, 70, 70);
            g.setColor(Color.black);
            g.fillRect(smileyX + 15, smileyY + 20, 10, 10);
            g.fillRect(smileyX + 45, smileyY + 20, 10, 10);
            if (happiness == true) {
                g.fillRect(smileyX + 20, smileyY + 50, 30, 5);
                g.fillRect(smileyX + 15, smileyY + 45, 5, 5);
                g.fillRect(smileyX + 50, smileyY + 45, 5, 5);
            } else {
                g.fillRect(smileyX + 20, smileyY + 45, 30, 5);
                g.fillRect(smileyX + 15, smileyY + 50, 5, 5);
                g.fillRect(smileyX + 50, smileyY + 50, 5, 5);
            }

            //flagger paveikslas
            g.setColor(Color.lightGray);
            g.fillOval(flaggerX, flaggerY, 70, 70);
            g.setColor(Color.black);
            g.fillRect(flaggerX + 32, flaggerY + 10, 7, 40);
            g.fillRect(flaggerX + 20, flaggerY + 50, 30, 10);
            g.setColor(Color.red);
            g.fillRect(flaggerX + 16, flaggerY + 15, 20, 15);
            g.setColor(Color.black);
            g.drawRect(flaggerX + 16, flaggerY + 15, 20, 15);
            g.drawRect(flaggerX + 17, flaggerY + 16, 18, 13);
            g.drawRect(flaggerX + 18, flaggerY + 17, 16, 11);

            if (flagger == true) {
                g.setColor(Color.red);
            }

            g.drawOval(flaggerX, flaggerY, 70, 70);
            g.drawOval(flaggerX + 1, flaggerY + 1, 68, 68);
            g.drawOval(flaggerX + 2, flaggerY + 2, 66, 66);

            //taimeris
            g.setColor(Color.black);
            g.fillRect(timeX, timeY, 140, 70);
            if (defeat == false && victory == false) {
                sec = (int) ((new Date().getTime() - startDate.getTime()) / 1000); //dalinam is 1000 nes pateikia laika milisekundems nuo zaidimo pradzios
            }
            if (sec > 999) {
                sec = 999;
            }
            g.setColor(Color.WHITE);
            if (victory == true) {
                g.setColor(Color.green);
            } else if (defeat == true) {
                g.setColor(Color.red);
            }
            g.setFont(new Font("Tahoma", Font.PLAIN, 80));
            if (sec < 10) {
                g.drawString("00" + Integer.toString(sec), timeX, timeY + 65);
            } else if (sec < 100) {
                g.drawString("0" + Integer.toString(sec), timeX, timeY + 65);
            } else {
                g.drawString(Integer.toString(sec), timeX, timeY + 65);
            }

            //pergales paveiksliukas
            if (victory == true) {
                g.setColor(Color.green);
                vicMes = "PERGALĖ!";
            } else if (defeat == true) {
                g.setColor(Color.red);
                vicMes = "PRALAIMĖJAI";
            }

            if (victory == true || defeat == true) {//ciuozimas ir sustojimas uzraso
                vicMesY = -50 + (int) (new Date().getTime() - endDate.getTime()) / 10;
                if (vicMesY > 67) {
                    vicMesY = 67;
                }
                g.setFont(new Font("Tahoma", Font.PLAIN, 70));
                g.drawString(vicMes, vicMesX, vicMesY);
            }

        }
    }

    public class Move implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {

        }

        @Override
        public void mouseMoved(MouseEvent e) {
            mx = e.getX();
            my = e.getY();
            /*
            System.out.println("The mouse was moved!");
            System.out.println("X: " + mx + ", Y: " +my);
             */
        }

    }

    public class Click implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {

            mx = e.getX();//kad neglicintu smailikas, kai greitai nori paresetint zaidima
            my = e.getY();

            if (inBoxX() != -1 && inBoxY() != -1) {
                System.out.println("The mouse is in the [" + inBoxX() + ", " + inBoxY() + "], Number of neighs: " + neighbours[inBoxX()][inBoxY()]);
                if (flagger == true && revealed[inBoxX()][inBoxY()] == false) {
                    if (flagged[inBoxX()][inBoxY()] == false) {
                        flagged[inBoxX()][inBoxY()] = true;
                    } else {
                        flagged[inBoxX()][inBoxY()] = false;
                    }
                } else {
                    if (flagged[inBoxX()][inBoxY()] == false){
                    revealed[inBoxX()][inBoxY()] = true;
                    }
                }
            } else {
                System.out.println("The pointer is not inside of any box!");
            }

            if (inSmiley() == true) {
                System.out.println("The pointer is inside the smiley!");
            } else {
                System.out.println("The pointer is not inside the smiley!");
            }

            if (inSmiley() == true) {
                resetAll();
            }

            if (inFlagger() == true) {
                if (flagger == false) {
                    flagger = true;
                    System.out.println("In flagger = true!");
                } else {
                    flagger = false;
                    System.out.println("In flagger = flase!");
                }
            }

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

    }

    public void checkVictoryStatus() {

        if (defeat == false) {
            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 9; j++) {
                    if (revealed[i][j] == true && mines[i][j] == 1) {
                        defeat = true;
                        happiness = false;
                        endDate = new Date();
                    }
                }
            }
        }
        if (totalBoxesRevealed() >= 144 - totalMines() && victory == false) {
            victory = true;
            endDate = new Date();
        }
    }

    // tikrina visu minu skaiciu ir grazina rezultata
    public int totalMines() {
        int total = 0;
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++) {
                if (mines[i][j] == 1) {
                    total++;
                }
            }
        }
        return total;
    }

    //skaiciuoja atskleistas dezutes
    public int totalBoxesRevealed() {
        int total = 0;
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++) {
                if (revealed[i][j] == true) {
                    total++;
                }
            }
        }
        return total;
    }

    public void resetAll() {

        resetter = true;

        flagger = false;

        startDate = new Date();

        vicMesY = -50;

        vicMes = "Nothing yet!";

        happiness = true;
        victory = false;
        defeat = false;

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++) {
                if (rand.nextInt(100) < 20) {
                    mines[i][j] = 1;
                } else {
                    mines[i][j] = 0;
                }
                revealed[i][j] = false;
                flagged[i][j] = false;
            }
        }

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++) {
                neighs = 0;
                for (int m = 0; m < 16; m++) {
                    for (int n = 0; n < 9; n++) {
                        if (!(m == i && n == j)) {
                            if (isN(i, j, m, n) == true) {
                                neighs++;
                            }
                        }
                    }
                }
                neighbours[i][j] = neighs;
            }
        }

        resetter = false;

    }

    //aprasoma, kad zaidimo restartavimo mygtukas bus smailikas 
    public boolean inSmiley() {
        int dif = (int) Math.sqrt(Math.abs(mx - smileyCenterX) * Math.abs(mx - smileyCenterX) + Math.abs(my - smileyCenterY) * Math.abs(my - smileyCenterY));
        if (dif < 35) {
            return true;
        }
        return false;
    }

    public boolean inFlagger() {
        int dif = (int) Math.sqrt(Math.abs(mx - flaggerCenterX) * Math.abs(mx - flaggerCenterX) + Math.abs(my - flaggerCenterY) * Math.abs(my - flaggerCenterY));
        if (dif < 35) {
            return true;
        }
        return false;
    }

    //dezuciu koordinaciu aprasymas
    public int inBoxX() {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++) {
                int spaccing = 0;
                if (mx >= spacing + i * 80 && mx < spacing + i * 80 + 80 - 2 * spacing && my >= spaccing + j * 80 + 80 + 26 && my < spacing + j * 80 + 80 + 80 - 2 * spacing) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int inBoxY() {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++) {
                int spaccing = 0;
                if (mx >= spacing + i * 80 && mx < spacing + i * 80 + 80 - 2 * spacing && my >= spaccing + j * 80 + 80 + 26 && my < spacing + j * 80 + 80 + 80 - 2 * spacing) {
                    return j;
                }
            }
        }
        return -1;
    }

    public boolean isN(int mX, int mY, int cX, int cY) {
        if (mX - cX < 2 && mX - cX > - 2 && mY - cY < 2 && mY - cY > - 2 && mines[cX][cY] == 1) {
            return true;
        }
        return false;
    }

}
