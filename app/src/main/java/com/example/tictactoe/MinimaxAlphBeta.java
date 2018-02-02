package com.example.tictactoe;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by prajvalb on 11/24/17.
 */

public class MinimaxAlphBeta {
    /**
     *
     * This will recursively call minimax depending on the current player,
     * if the current player is X, the algorithm will find the max available board and if
     * the current player is O, the algorithm will find the min avaiable board
     * The terminal state check is done at the start before recursively calling minimax,
     * the terminal checks are checkWinner for player X(AI) and O(User) and if the board state is
     * full, if either of the conditions gets satisfied then it will return the value as decided
     * if winner is AI(X), assign +1000-depth, if winner is User(O) assign -1000-depth and if the state is
     * draw assign 0 and return.
     * This also implemets Alpha Beta pruinig, so that the whole tree is not necessarily explored
     * every time and only the necessary ones are explored, this will speed up the algorithm
     * and return the result quickly.
     *
     * @param  board  board for which the minimax will be called recursively
     * @param player  player for which the board state should be generated
     * @return boolean true/false
     **/
    public static int miniMax(Board board, int depth, int alpha, int beta, String player){
        BoardActivity.totalCount++;
        if (board.checkWinner(board.getBoardState(), "X")) {
            return 1000-depth;
        } else if (board.checkWinner(board.getBoardState(), "O")) {
            return -1000-depth;
        } else if (board.boardFullCheck(board.getBoardState())){
            return 0;
        } else {
            int value = (int)Double.NEGATIVE_INFINITY;
            if (player.equals("X")) {
                ArrayList<Board> someStates = board.generateSuccessors(board, "X");
                for (Board sBoard : someStates) {
                    board.printBoardStateMax(sBoard.getBoardState());
                    value = Math.max(value, miniMax(sBoard,depth+1, alpha, beta, "O"));
                    alpha = Math.max(alpha, value);
                    if (beta <= alpha)
                        break;
                }
                return value;
            }

            else  {
                int best = (int)Double.POSITIVE_INFINITY;
                ArrayList<Board> otherStates = board.generateSuccessors(board, "O");
                for (Board oBoard : otherStates) {
                    board.printBoardStateMin(oBoard.getBoardState());
                    best = Math.min(best, miniMax(oBoard, depth+1, alpha, beta,"X"));
                    beta = Math.min(beta, best);
                    if (beta <= alpha)
                        break;
                }
                return best;
            }

        }

    }
}
