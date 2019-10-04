package training.dynamite;

import com.softwire.dynamite.bot.Bot;
import com.softwire.dynamite.game.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MyBot implements Bot {

    private Opponent opponent = new Opponent();
    private Strategy strategy = new Strategy(opponent);

    private List<Move> basicMoves = new ArrayList<>(Arrays.asList(Move.R, Move.P, Move.S));
    private int roundCounter = 0;
    private Move currentMove;
    private Move myLastMove;
    private boolean testingFinished = false;
    private boolean opponentReacted = false;

    public MyBot() {
        // Are you debugging?
        // Put a breakpoint on the line below to see when we start a new match
    } //CONSTRUCTOR

    @Override
    public Move makeMove(Gamestate gamestate) {
        // Are you debugging?
        // Put a breakpoint in this method to see when we make a move

        if(roundCounter >= 1) {
            strategy.updateStatistics(gamestate);
            if (didTestingFinished(gamestate)) {
                opponentReacted = true;
            }
        }
        currentMove = play(gamestate);
        roundCounter++;
        myLastMove = currentMove;


        if(currentMove == Move.D && strategy.getMyDynamites() == 0){
            currentMove = strategy.generateRandomBasicMove();
        }

        return currentMove;
    }


    //===========================================================================================

    private Move play(Gamestate gamestate) {
        if(strategy.isDraw()) {
            return currentMove = Move.D;
        }

        if(strategy.isDrawTwice()) {
            if(opponent.getLastMove() == Move.D) {
                return currentMove = Move.W;
            }
        }


        if(roundCounter == 1) {
            return currentMove = strategy.testOpponent();
        } else if(roundCounter > 1 && didIWon(gamestate) && !testingFinished) {
            return currentMove = strategy.generateWinningMove(myLastMove, opponent.getLastMove());
        } else if(roundCounter > 1 && !didIWon(gamestate) && !testingFinished) {
            return currentMove = strategy.testOpponent();
        } else if(roundCounter > 1 && opponentReacted) { //keep doing what I'm doing
            return currentMove = strategy.generateWinningMove(myLastMove, opponent.getLastMove());
        } else if(roundCounter <= 20 && !opponentReacted) {
            return currentMove = strategy.generateAnyMoveSafely();
        } else {
            return currentMove = strategy.generateAnyMoveSafely();
        }
    }


    private boolean didIWon(Gamestate gamestate) {
        return (strategy.isAWin(myLastMove, gamestate.getRounds().get(gamestate.getRounds().size()-1).getP2()));
    }

    private boolean didTestingFinished(Gamestate gamestate) {
        if(testingFinished == false && didIWon(gamestate)) {
            return true;
        }
        return false;
    }

}
