package minesweeper;

/**
 * Gytis Jankauskas
 * PRif-16/1
 * 20163785
 */
public class Minesweeper implements Runnable {

    GUI gui = new GUI();

    public static void main(String[] args) {
        new Thread(new Minesweeper()).start();
    }

    @Override
    public void run() {
       while(true){
           gui.repaint();
           if (gui.resetter == false){
           gui.checkVictoryStatus();
           //System.out.println("Victory: " + gui.victory + ", Defeat: " + gui.defeat);
           }
       }
    }
    
}
