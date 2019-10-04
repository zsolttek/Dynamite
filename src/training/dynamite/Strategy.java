package training.dynamite;

import com.softwire.dynamite.game.Gamestate;
import com.softwire.dynamite.game.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Strategy {

    private Opponent opponent;

    private List<Move> basicMoves = new ArrayList<>(Arrays.asList(Move.R, Move.P, Move.S));
    private List<Move> allMoves = new ArrayList<>(Arrays.asList(Move.R, Move.P, Move.S, Move.D, Move.W));
    private int roundCounter = 1;

    public int getMyDynamites() {
        return myDynamites;
    }

    private int myDynamites = 100;

    private boolean draw = false;

    public boolean isDrawTwice() {
        return drawTwice;
    }

    private boolean drawTwice = false;


    public boolean isDraw() {
        return draw;
    }



    public Strategy(Opponent opponent) {
        this.opponent = opponent;
    }

    //=============================================================

    public int generateRandomNumber(int upperLimit) {
        Random random = new Random();
        return random.nextInt(upperLimit) + 1; //from 1 to upper limit (including)
    }

    public Move generateRandomBasicMove() {
        int idx = generateRandomNumber(3);
        return basicMoves.get(idx - 1);
    }

    private Move generateAnyMove() {
        int idx = generateRandomNumber(5);
        return allMoves.get(idx - 1);
    }

    public Move testOpponent() {
        return generateRandomBasicMove();
    }

    public void updateStatistics(Gamestate gamestate) {
        int lastIdx = gamestate.getRounds().size() - 1;
        opponent.setLastMove(gamestate.getRounds().get(lastIdx).getP2());

        if (opponent.getLastMove() == Move.D) {
            opponent.setDynamite(opponent.getDynamite() - 1);
        }
        if (gamestate.getRounds().get(lastIdx).getP1() == Move.D) {
            myDynamites -= 1;
        }

        draw = (gamestate.getRounds().get(lastIdx).getP1() == opponent.getLastMove());

        if(gamestate.getRounds().size() >= 2) {
            drawTwice = (gamestate.getRounds().get(lastIdx).getP1() == opponent.getLastMove()
                    && gamestate.getRounds().get(lastIdx - 1).getP1() == gamestate.getRounds().get(lastIdx - 1).getP2());
        }

    }


    public boolean isAWin(Move x, Move y) {
        if (x != Move.R || y != Move.S && y != Move.W) {
            if (x != Move.P || y != Move.R && y != Move.W) {
                if (x == Move.S && (y == Move.P || y == Move.W)) {
                    return true;
                } else if (x != Move.D || y != Move.R && y != Move.P && y != Move.S) {
                    return x == Move.W && y == Move.D;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public Move generateWinningMove(Move myLastMove, Move opponentMove) {

        if (myLastMove == Move.R && opponentMove == Move.D) {
            return generateRandomBasicMove();
        } else if (myLastMove == Move.R && opponentMove != Move.P) {
            return Move.S;
        } else if (myLastMove == Move.P && opponentMove == Move.D) {
            return generateRandomBasicMove();
        } else if (myLastMove == Move.P && opponentMove != Move.S) {
            return Move.R;
        } else if (myLastMove == Move.S && opponentMove == Move.D) {
            return generateRandomBasicMove();
        } else if (myLastMove == Move.S && opponentMove != Move.R) {
            return Move.P;
        } else if (myLastMove == Move.W && opponentMove == Move.D) {
            return Move.W;
        } else if (myLastMove == Move.D && opponentMove != Move.W) {
            return generateRandomBasicMove();
        } else {
            if (myDynamites > 0) {
                return generateAnyMove();
            } else {
                return generateRandomBasicMove();
            }
        }
    }

    //    private List<Move> allMoves = new ArrayList<>(Arrays.asList(Move.R, Move.P, Move.S, Move.D, Move.W));
    public Move generateAnyMoveSafely() {
        Move move;
        Move move2;
        int random;
        boolean flag = true;
        boolean flag2 = true;

        if (myDynamites > 0) {
            random = generateRandomNumber(20);
            if ((random >= 10 && random <= 13) && opponent.getDynamite() > 0) {
                return allMoves.get(allMoves.size() - 1); // return all
            } else if(opponent.getDynamite() <= 0) {
                return allMoves.get(allMoves.size() - 2); // exclude water
            }
        } else {  // no dynamite left for me
            if (opponent.getDynamite() > 0) { //include water only if opponent has dynamite
                move = allMoves.get(allMoves.size() - 1);
                while (flag) {
                    move = allMoves.get(allMoves.size() - 1);
                    if (move != Move.D) {
                        flag = false;
                    }
                }
                if(move == Move.W) {
                    if(generateRandomNumber(10) != 1 || generateRandomNumber(10) != 2) {
                        return generateRandomBasicMove();
                    }
                } else {
                    return move;
                }
            } else {
                return generateRandomBasicMove();
            }
        }

        int newRandom = generateRandomNumber(10);
        if(newRandom == 1 || newRandom == 2) {
            return generateAnyMove();
        } else {
            while(flag2) {
                move2 =  generateAnyMove();
                if(move2 != Move.W) {
                    flag2 = false;
                    return  move2;
                }
            }
        }

        return generateAnyMove();
    }
}
