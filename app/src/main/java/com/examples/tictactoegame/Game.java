/*
 * Copyright 2019, Tic Tac Toe Game
 *
 * Written by Henrique Belotto
 *
 * Software distributed is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */


/*
  Model class for Game, which define the game's basic methods
 */

package com.examples.tictactoegame;

class Game {
    // array with empty positions on the board
    private int[] boardPositions;

    // possible combination of winning positions
    private final int[][] winningPositions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};

    // used to store the location of the winningPieces
    private int[] winningPieces;

    private int xScore;
    private int oScore;

    // activePlayer = 1 - X
    // activePlayer = 2 - circle
    private int activePlayer;

    // keep track of how many turns where played in the game
    private int turns;

    // game status - Running or over
    private boolean gameActive;

    // winner string
    private String winner = null;


    Game() {
        restartGameBoard();
        activePlayer = 1;
    }

    int getBoardPosition(int position) {
        return boardPositions[position];
    }

    void setBoardPosition(int position, int player) {
        boardPositions[position] = player;
    }

    int[] getBoardPositions() {
        return boardPositions;
    }

    int[] getWinningPieces() {
        return winningPieces;
    }

    void setWinningPieces(int[] winningPieces) {
        this.winningPieces = winningPieces;
    }

    // Check if someone has won. If true returns "won" as true, otherwise, return false
    boolean hasWon() {
        boolean won = false;
        // loop through the possible winning positions and check if they match with the boardPositions
        for (int[] winningPosition : winningPositions) {
            if (boardPositions[winningPosition[0]] == boardPositions[winningPosition[1]]
                    && boardPositions[winningPosition[1]] == boardPositions[winningPosition[2]]
                    && boardPositions[winningPosition[0]] != 0) {
                // Someone has won

                setWinningPieces(winningPosition);

                won = true;

                if (getActivePlayer() == 1) {
                    setWinner("X");
                } else {
                    setWinner("O");
                }

            }
        }

        return won;
    }

    int getXScore() {
        return xScore;
    }

    int getOScore() {
        return oScore;
    }

    void setXScore(int xScore) {
        this.xScore = xScore;
    }

    void setOScore(int oScore) {
        this.oScore = oScore;
    }


    void addXScore() {
        xScore++;
    }

    void addOScore() {
        oScore++;
    }

    void setBoardPositions(int[] boardPositions) {
        this.boardPositions = boardPositions;
    }

    void restartGameBoard() {
        boardPositions = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
        gameActive = true;
        winner = null;
        winningPieces = null;
    }


    int getActivePlayer() {
        return activePlayer;
    }

    void setActivePlayer(int activePlayer) {
        this.activePlayer = activePlayer;
    }

    void changeActivePlayer() {
        // rotate between players
        if (activePlayer == 1) {
            activePlayer = 2;
        } else {
            activePlayer = 1;
        }
    }

    int getTurns() {
        return turns;
    }

    void setTurns(int turns) {
        this.turns = turns;
    }

    void addTurn() {
        turns++;
    }

    void resetTurns() {
        turns = 0;
    }


    boolean isGameActive() {
        return gameActive;
    }

    void setGameActive(boolean gameActive) {
        this.gameActive = gameActive;
    }

    String getWinner() {
        return winner;
    }

    void setWinner(String winner) {
        this.winner = winner;
    }
}
