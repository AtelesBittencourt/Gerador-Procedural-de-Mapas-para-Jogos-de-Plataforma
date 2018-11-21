 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import ai.core.AI;
import ai.*;
import ai.abstraction.WorkerRush;
import ai.abstraction.pathfinding.BFSPathFinding;
import ai.asymmetric.PGS.PGSSCriptChoiceRandom;
import ai.asymmetric.PGS.PGSmRTS;
import ai.asymmetric.PGS.POEmRTS;
import ai.asymmetric.SSS.SSSmRTS;
import ai.mcts.naivemcts.NaiveMCTS;
import ai.scv.SCV;
import gui.PhysicalGameStatePanel;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import javax.swing.JFrame;
import rts.GameState;
import rts.PhysicalGameState;
import rts.PlayerAction;
import rts.units.UnitTypeTable;
import util.XMLWriter;

/**
 *
 * @author santi
 */
public class GameVisualSimulationTestLote {
    public static void main(String args[]) throws Exception {
        String arq = "C:\\Users\\Ateles Junior\\Documents\\2018-2\\TCC\\MicroRTS\\TesteLongo.txt";
        for(int vez = 0; vez < 100; vez++){
            UnitTypeTable utt = new UnitTypeTable();
            PhysicalGameState pgs = PhysicalGameState.load("maps/16x16/basesWorkers16x16.xml", utt);
            //PhysicalGameState pgs = PhysicalGameState.load("maps/16x16/melee16x16Mixed8.xml", utt);
            //PhysicalGameState pgs = PhysicalGameState.load("maps/32x32/basesWorkersBarracks32x32.xml", utt);


            //PhysicalGameState pgs = PhysicalGameState.load("maps/BroodWar/(4)Fortress.scxA.xml", utt);
    //        PhysicalGameState pgs = MapGenerator.basesWorkers8x8Obstacle();

            GameState gs = new GameState(pgs, utt);
            int MAXCYCLES = 5000;
            int PERIOD = 20;
            boolean gameover = false;

            //AI ai1 = new PGSmRTS(utt);
            AI ai1 = new POEmRTS(utt);
            //AI ai2 = new SSSmRTS(utt);
            AI ai2 = new PGSmRTS(utt);
            //AI ai2 = new PGSSCriptChoiceRandom(utt);

            //JFrame w = PhysicalGameStatePanel.newVisualizer(gs,640,640,false,PhysicalGameStatePanel.COLORSCHEME_BLACK);
    //        JFrame w = PhysicalGameStatePanel.newVisualizer(gs,640,640,false,PhysicalGameStatePanel.COLORSCHEME_WHITE);

            long nextTimeToUpdate = System.currentTimeMillis() + PERIOD;
            do{
                if (System.currentTimeMillis()>=nextTimeToUpdate) {

                    PlayerAction pa1 = ai1.getAction(0, gs);
                    PlayerAction pa2 = ai2.getAction(1, gs);
                    gs.issueSafe(pa1);
                    gs.issueSafe(pa2);

                    // simulate:
                    gameover = gs.cycle();
                    //w.repaint();
                    nextTimeToUpdate+=PERIOD;
                } else {
                    try {
                        Thread.sleep(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }while(!gameover && gs.getTime()<MAXCYCLES);
            ai1.gameOver(gs.winner());
            ai2.gameOver(gs.winner());
            //System.out.println("Ganhador: " + gs.winner());
            //System.out.println("Game Over");
            try{
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(arq, true)));
                out.println("Ganhador: " + gs.winner());
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }    
}
