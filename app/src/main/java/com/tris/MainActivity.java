package com.tris;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button[][] buttons = new Button[3][3];
    private boolean player1Turn = true;
    private int roundCount;
    private int player1Points;
    private int player2Points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inizializza i pulsanti nella matrice e imposta l'OnClickListener per ciascuno
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }

        // Imposta l'OnClickListener per il pulsante di reset
        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });
    }

    @Override
    public void onClick(View v) {
        // Ignora il click se il pulsante è già stato premuto
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }

        // Imposta il testo del pulsante in base al turno del giocatore
        if (player1Turn) {
            ((Button) v).setText("X");
        } else {
            ((Button) v).setText("O");
        }

        roundCount++;

        // Controlla se c'è una vittoria
        if (checkForWin()) {
            if (player1Turn) {
                player1Wins();
            } else {
                player2Wins();
            }
        } else if (roundCount == 9) {
            draw();
        } else {
            // Passa il turno al prossimo giocatore
            player1Turn = !player1Turn;
            if (!player1Turn) {
                makeComputerMove();
            }
        }
    }

    private boolean checkForWin() {
        // Ottiene lo stato corrente del campo di gioco
        String[][] field = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        // Controlla tutte le possibili combinazioni vincenti
        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2]) && !field[i][0].equals("")) {
                return true;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i]) && !field[0][i].equals("")) {
                return true;
            }
        }

        if (field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2]) && !field[0][0].equals("")) {
            return true;
        }

        if (field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0]) && !field[0][2].equals("")) {
            return true;
        }

        return false;
    }

    private void player1Wins() {
        // Aggiorna i punti del giocatore 1 e visualizza un messaggio di vittoria
        player1Points++;
        Toast.makeText(this, "Giocatore 1 vince!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
    }

    private void player2Wins() {
        // Aggiorna i punti del giocatore 2 (computer) e visualizza un messaggio di vittoria
        player2Points++;
        Toast.makeText(this, "Computer vince!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
    }

    private void draw() {
        // Visualizza un messaggio di pareggio
        Toast.makeText(this, "Pareggio!", Toast.LENGTH_SHORT).show();
        resetBoard();
    }

    private void updatePointsText() {
        // Aggiorna il testo delle TextView dei punteggi
        TextView textViewPlayer1 = findViewById(R.id.text_view_p1);
        TextView textViewPlayer2 = findViewById(R.id.text_view_p2);
        textViewPlayer1.setText("Giocatore 1: " + player1Points);
        textViewPlayer2.setText("Computer: " + player2Points);
    }

    private void resetBoard() {
        // Resetta il campo di gioco, ripristina il conteggio dei turni e passa il turno al giocatore 1
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
        roundCount = 0;
        player1Turn = true;
    }

    private void resetGame() {
        // Resetta il gioco, inclusi i punteggi
        player1Points = 0;
        player2Points = 0;
        updatePointsText();
        resetBoard();
    }

    private void makeComputerMove() {
        // Logica per la mossa del computer
        // Qui puoi implementare l'algoritmo dell'IA per determinare la mossa del computer
        // Per semplicità, facciamo una mossa casuale per ora

        int row, col;
        do {
            row = (int) (Math.random() * 3);
            col = (int) (Math.random() * 3);
        } while (!buttons[row][col].getText().toString().equals(""));

        buttons[row][col].setText("O");
        roundCount++;

        if (checkForWin()) {
            player2Wins();
        } else if (roundCount == 9) {
            draw();
        } else {
            player1Turn = true;
        }
    }
}
