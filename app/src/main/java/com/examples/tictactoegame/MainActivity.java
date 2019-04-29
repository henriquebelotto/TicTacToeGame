/*
 * Copyright 2019, Tic Tac Toe Game
 *
 * Written by Henrique Belotto
 *
 * Software distributed is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */


/*
  MainActivity class - Defines the UI and interact with the model
 */

package com.examples.tictactoegame;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.GridLayout;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    // Creating & Initializing the variables
    private Game game;
    private ImageView restartGame;
    private TextView winnerText;
    private TextView xScore;
    private TextView oScore;


    private GridLayout gridLayout;
    private ImageView piece;

    private static final String STATE_BOARD_POSITION = "boardPositions";
    private static final String STATE_GAME_ACTIVE = "gameActive";
    private static final String STATE_X_SCORE = "xScore";
    private static final String STATE_O_SCORE = "oScore";
    private static final String STATE_TURNS = "turns";
    private static final String STATE_ACTIVE_PLAYER = "activePlayer";
    private static final String STATE_WINNER = "winner";
    private static final String STATE_WINNING_PIECES = "winningPieces";

    // onCreate method is called whenever the app is started or when there is a configuration
    // change, such as rotate the screen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        game = new Game();

        // linking the View objects and variables
        restartGame = findViewById(R.id.restartGameBtn);
        restartGame.setImageResource(R.drawable.refresh);
        winnerText = findViewById(R.id.winnerText);
        gridLayout = findViewById(R.id.gridLayout);
        xScore = findViewById(R.id.xScore);
        oScore = findViewById(R.id.oScore);

        // The game was previously saved - Restore it to how it was
        if (savedInstanceState != null) {
            game.setBoardPositions(savedInstanceState.getIntArray(STATE_BOARD_POSITION));
            game.setGameActive(savedInstanceState.getBoolean(STATE_GAME_ACTIVE));
            game.setXScore(savedInstanceState.getInt(STATE_X_SCORE));
            game.setOScore(savedInstanceState.getInt(STATE_O_SCORE));
            game.setTurns(savedInstanceState.getInt(STATE_TURNS));
            game.setActivePlayer(savedInstanceState.getInt(STATE_ACTIVE_PLAYER));
            game.setWinner(savedInstanceState.getString(STATE_WINNER));
            game.setWinningPieces(savedInstanceState.getIntArray(STATE_WINNING_PIECES));
            restoreGame();
        }
    }

    // onClick method for each position on the board
    public void showPieceOnClick(View view) {
        piece = (ImageView) view;

        // stores the information about where the player has clicked
        int tappedCounter = Integer.parseInt(piece.getTag().toString());

        // Check if the position has already been clicked
        // AND that the game is Active (nobody has won)
        if (game.getBoardPosition(tappedCounter) == 0 && game.isGameActive()) {

            // keep track of how many times the players have already played
            game.addTurn();

            // use the method from the Game class to fill the array with the gameStatus
            game.setBoardPosition(tappedCounter, game.getActivePlayer());

            // Check who is playing, player 1 or 2
            if (game.getActivePlayer() == 1) {
                // player 1 uses X
                piece.setImageResource(R.drawable.x_piece);
            } else {
                // player 2 uses circle
                piece.setImageResource(R.drawable.circle);
            }

            // animating the piece
            piece.setAlpha(0f);
            piece.animate().alpha(1f).rotation(1440).setDuration(500);

            // only check if someone has won after 5 turns have already been played
            // it's impossible to win before 5 turns have been played
            if (game.getTurns() >= 5) {

                // check if someone has won
                if (game.hasWon()) {

                    game.setGameActive(false);

                    // Setting the visibility to the restart button and winner text
                    winnerText.setText(getString(R.string.winner_text,game.getWinner()));

                    restartGame.setVisibility(View.VISIBLE);
                    winnerText.setVisibility(View.VISIBLE);

                    if (game.getActivePlayer() == 1) {
                        // X has won
                        game.addXScore();

                        xScore.setText(String.format(Locale.getDefault(),"%d", game.getXScore()));
                    } else {
                        // O has won
                        game.addOScore();
                        oScore.setText(String.format(Locale.getDefault(),"%d",game.getOScore()));
                    }

                    setBackgroundColorWinningPieces();

                } else if (game.getTurns() >= 9) {
                    game.setGameActive(false);

                    // Setting the visibility to the restart button and winner text
                    winnerText.setText(getString(R.string.draw));

                    restartGame.setVisibility(View.VISIBLE);
                    winnerText.setVisibility(View.VISIBLE);
                }
            }

            // change to the next player
            game.changeActivePlayer();
        }
    }

    public void restartGameOnClick(View view) {
        // Setting the visibility to the restart button and winner text
        restartGame.setVisibility(View.INVISIBLE);
        winnerText.setVisibility(View.INVISIBLE);

        game.resetTurns();

        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            piece = (ImageView) gridLayout.getChildAt(i);
            piece.setImageDrawable(null);


            piece.setBackgroundColor(getResources().getColor(R.color.backgroundPiece));

            // Return the TranslationZ for the pieces that were elevated (winning pieces)
            // This feature is only available if SDK >= 21
            if (Build.VERSION.SDK_INT >= 21 && game.getWinningPieces() != null) {
                for (int position : game.getWinningPieces()) {
                    gridLayout.getChildAt(position).animate().translationZ(-1000f).setDuration(1000);
                }
            }
        }

        game.restartGameBoard();
        game.setGameActive(true);
    }

    // Restore the board accordingly with the boardPositions
    private void restoreGame() {

        // loop through all the positions
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            piece = (ImageView) gridLayout.getChildAt(i);

            // Check if X or O or no one has placed a piece at this position
            switch (game.getBoardPosition(i)) {
                case 1:
                    // X has placed a piece at this position
                    piece.setImageResource(R.drawable.x_piece);
                    break;

                case 2:
                    // O has placed a piece at this position
                    piece.setImageResource(R.drawable.circle);
                    break;

                default:
                    // no one has placed a piece there
                    piece.setImageDrawable(null);
            }
        }

        // game was over - Resetting the restart button and winner/draw text
        if (!game.isGameActive()) {
            // Draw
            if (game.getWinner() == null) {
                // Setting the visibility to the restart button and winner text
                winnerText.setText(getString(R.string.draw));
            } else {
                // Setting the visibility to the restart button and winner text
                winnerText.setText(getString(R.string.winner_text,game.getWinner()));
                setBackgroundColorWinningPieces();
            }

            restartGame.setVisibility(View.VISIBLE);
            winnerText.setVisibility(View.VISIBLE);
        }

        xScore.setText(String.format(Locale.getDefault(),"%d", game.getXScore()));
//        xScore.setText(Integer.toString(game.getXScore()));
        oScore.setText(String.format(Locale.getDefault(),"%d",game.getOScore()));
//        oScore.setText(Integer.toString(game.getOScore()));
    }

    // Method to store the current status
    // This information will be used to restore the game if needed
    @Override
    public void onSaveInstanceState(Bundle savedInstance) {

        savedInstance.putIntArray(STATE_BOARD_POSITION, game.getBoardPositions());
        savedInstance.putInt(STATE_X_SCORE, game.getXScore());
        savedInstance.putInt(STATE_O_SCORE, game.getOScore());
        savedInstance.putBoolean(STATE_GAME_ACTIVE, game.isGameActive());
        savedInstance.putInt(STATE_TURNS, game.getTurns());
        savedInstance.putInt(STATE_ACTIVE_PLAYER, game.getActivePlayer());
        savedInstance.putString(STATE_WINNER, game.getWinner());
        savedInstance.putIntArray(STATE_WINNING_PIECES, game.getWinningPieces());

        super.onSaveInstanceState(savedInstance);

    }

    // Change the background color of the winning pieces to clearly indicate to the player
    // who and how someone has won
    private void setBackgroundColorWinningPieces() {

        for (int position : game.getWinningPieces()) {
            gridLayout.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.colorWin));

            // This feature is only available if SDK >= 21
            // TranslateZ for the winning pieces
            if (Build.VERSION.SDK_INT >= 21) {
                gridLayout.getChildAt(position).animate().translationZ(1000f).setDuration(1000);
            }

        }

    }

    // shutdownOnClick the app
    public void shutdownOnClick(View view) {
        finish();
    }
}
