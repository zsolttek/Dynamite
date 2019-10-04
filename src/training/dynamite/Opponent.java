package training.dynamite;

import com.softwire.dynamite.game.Move;

public class Opponent {


    private boolean reacted;
    private int dynamite = 100;

    public int getDynamite() {
        return dynamite;
    }

    public void setDynamite(int dynamite) {
        this.dynamite = dynamite;
    }

    public Move getLastMove() {
        return lastMove;
    }

    public void setLastMove(Move lastMove) {
        this.lastMove = lastMove;
    }

    public boolean isInitialTestReacton() {
        return initialTestReacton;
    }

    public void setInitialTestReacton(boolean initialTestReacton) {
        this.initialTestReacton = initialTestReacton;
    }

    private Move lastMove = null;
    private boolean initialTestReacton = false;



}
