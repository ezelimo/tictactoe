package ezel.tictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    static final String PLAYER_1 = "X";
    static final String PLAYER_2 = "O";

    boolean player1Turn = true;
    byte[][] board = new byte[3][3];

    int moveCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TableLayout tableLayout = findViewById(R.id.board);
        for (int i = 0; i < 3; i++) {
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            for (int j = 0; j < 3; j++) {
                Button btn = (Button) row.getChildAt(j);
                btn.setOnClickListener(new CellListener(i, j));
            }
        }

    }


    class CellListener implements View.OnClickListener {
        int row, col;

        public CellListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void onClick(View view) {
            if (isCellOcuppied(row, col)) {
                Toast.makeText(MainActivity.this, "Cell is already occupied.", Toast.LENGTH_LONG).show();
                return;
            }
            if (player1Turn) {
                ((Button) view).setText(PLAYER_1);
                board[row][col] = 1;
                moveCount++;
            } else {
                ((Button) view).setText(PLAYER_2);
                board[row][col] = 2;
                moveCount++;
            }

            int gameState= gameEnded(row,col);
            if (gameState == -1) {
                player1Turn = !player1Turn;
            } else if (gameEnded(row, col) == 0) {
                Toast.makeText(MainActivity.this, "This is a draw.", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(MainActivity.this, "Player "+ gameState +" WINS", Toast.LENGTH_LONG).show();
            }
        }

    }

    public boolean isCellOcuppied(int row,int col){
        return (board[row][col] != 0);
    }

    public int gameEnded(int row, int col ){
        int player = board[row][col];
        boolean win =true;
        for (int i = 0; i <3 ; i++) {
            if (board[i][col] != player){
                win=false;
                break;
            }
        }

        if(win){
            return player;
        }


        win =true;
        for (int i = 0; i <3 ; i++) {
            if (board[row][i] != player){
                win=false;
                break;
            }
        }

        if(win){
            return player;
        }


        if (moveCount==9){
            return 0;
        }

        return -1;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("player1Turn",player1Turn);
        outState.putInt("moveCount",moveCount);
        byte[] boardOneDimension = new byte[9];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boardOneDimension[3*i+j] = board[i][j];
            }
        }
        outState.putByteArray("board",boardOneDimension);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        player1Turn = savedInstanceState.getBoolean("player1Turn");
        moveCount = savedInstanceState.getInt("moveCount");
        byte[] boardOneDimension = savedInstanceState.getByteArray("board");
        for (int i = 0; i < 9; i++) {
            board[i/3][i%3] = boardOneDimension[i];
        }
        TableLayout tableLayout = findViewById(R.id.board);
        for (int i = 0; i < 3; i++) {
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            for (int j = 0; j < 3; j++) {
                Button btn = (Button) row.getChildAt(j);
                if(board[i][j] == 1){
                    btn.setText(PLAYER_1);
                }else if (board[i][j] == 2){
                    btn.setText(PLAYER_2);
                }else{
                    btn.setText("");
                }

            }
        }

    }
}