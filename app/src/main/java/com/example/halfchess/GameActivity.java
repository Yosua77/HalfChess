package com.example.halfchess;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class GameActivity extends AppCompatActivity {
    private String diff = "none";
    private Papan[][] papan = new Papan[8][4];
    private boolean turn,checkmate = false;
    private int baris = -1,kolom = -1,gamestate = -1;//draw = 0, p1 win = 1 p2 win = 2
    TextView whiteTurn,blackTurn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        if(getIntent().hasExtra("diff"))
         diff = getIntent().getStringExtra("diff");
        papan[0][0] = new Papan(new Bidak(5,false),findViewById(R.id.board00), "#FFFFFF");
        papan[0][1] = new Papan(new Bidak(4,false),findViewById(R.id.board01), "#D9E1F6");
        papan[0][2] = new Papan(new Bidak(3,false),findViewById(R.id.board02), "#FFFFFF");
        papan[0][3] = new Papan(new Bidak(2,false),findViewById(R.id.board03), "#D9E1F6");

        papan[1][0] = new Papan(new Bidak(1,false),findViewById(R.id.board10), "#D9E1F6");
        papan[1][1] = new Papan(new Bidak(1,false),findViewById(R.id.board11), "#FFFFFF");
        papan[1][2] = new Papan(new Bidak(1,false),findViewById(R.id.board12), "#D9E1F6");
        papan[1][3] = new Papan(new Bidak(1,false),findViewById(R.id.board13), "#FFFFFF");

        papan[2][0] = new Papan(new Bidak(0,false),findViewById(R.id.board20), "#FFFFFF");
        papan[2][1] = new Papan(new Bidak(0,false),findViewById(R.id.board21), "#D9E1F6");
        papan[2][2] = new Papan(new Bidak(0,false),findViewById(R.id.board22), "#FFFFFF");
        papan[2][3] = new Papan(new Bidak(0,false),findViewById(R.id.board23), "#D9E1F6");

        papan[3][0] = new Papan(new Bidak(0,false),findViewById(R.id.board30), "#D9E1F6");
        papan[3][1] = new Papan(new Bidak(0,false),findViewById(R.id.board31), "#FFFFFF");
        papan[3][2] = new Papan(new Bidak(0,false),findViewById(R.id.board32), "#D9E1F6");
        papan[3][3] = new Papan(new Bidak(0,false),findViewById(R.id.board33), "#FFFFFF");

        papan[4][0] = new Papan(new Bidak(0,false),findViewById(R.id.board40), "#FFFFFF");
        papan[4][1] = new Papan(new Bidak(0,false),findViewById(R.id.board41), "#D9E1F6");
        papan[4][2] = new Papan(new Bidak(0,false),findViewById(R.id.board42), "#FFFFFF");
        papan[4][3] = new Papan(new Bidak(0,false),findViewById(R.id.board43), "#D9E1F6");

        papan[5][0] = new Papan(new Bidak(0,false),findViewById(R.id.board50), "#D9E1F6");
        papan[5][1] = new Papan(new Bidak(0,false),findViewById(R.id.board51), "#FFFFFF");
        papan[5][2] = new Papan(new Bidak(0,false),findViewById(R.id.board52), "#D9E1F6");
        papan[5][3] = new Papan(new Bidak(0,false),findViewById(R.id.board53), "#FFFFFF");

        papan[6][0] = new Papan(new Bidak(1,true),findViewById(R.id.board60), "#FFFFFF");
        papan[6][1] = new Papan(new Bidak(1,true),findViewById(R.id.board61), "#D9E1F6");
        papan[6][2] = new Papan(new Bidak(1,true),findViewById(R.id.board62), "#FFFFFF");
        papan[6][3] = new Papan(new Bidak(1,true),findViewById(R.id.board63), "#D9E1F6");

        papan[7][0] = new Papan(new Bidak(5,true),findViewById(R.id.board70), "#D9E1F6");
        papan[7][1] = new Papan(new Bidak(4,true),findViewById(R.id.board71), "#FFFFFF");
        papan[7][2] = new Papan(new Bidak(3,true),findViewById(R.id.board72), "#D9E1F6");
        papan[7][3] = new Papan(new Bidak(2,true),findViewById(R.id.board73), "#FFFFFF");
        turn = true;


        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 4; j++) {
                papan[i][j].letak.setOnClickListener(this::onClick);
            }
        }
        whiteTurn = findViewById(R.id.whiteText);
        blackTurn = findViewById(R.id.blackText);
        whiteTurn.setText("Your Turn");
        defaultState();
    }

    void onClick(View v){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 4; j++) {
                if(v.getId() == papan[i][j].letak.getId()){
                    //turn = white !turn = black
                    if(baris == -1 && kolom == -1) {
                        if (papan[i][j].getBidak().getValue() != 0) {
                            if ((turn && papan[i][j].getBidak().isWhite()) || (!turn && !papan[i][j].getBidak().isWhite())) {
                                papan[i][j].setPressed(true);
                                possibleMove(i, j);
                                this.baris = i;
                                this.kolom = j;
                                break;
                            } else if (papan[i][j].getBidak().isWhite()) {
                                Toast.makeText(GameActivity.this, "It is black's turn now", Toast.LENGTH_SHORT).show();
                                break;
                            } else if (!papan[i][j].getBidak().isWhite()) {
                                Toast.makeText(GameActivity.this, "It is white's turn now", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    }else{
                        if(papan[i][j].getStatus() != 0){
                            if(papan[i][j].getStatus() == 1){
                                papan[i][j].enPassant = papan[baris][kolom].enPassant;
                                papan[i][j].setBidak(new Bidak(papan[baris][kolom].getBidak().getValue(),papan[baris][kolom].getBidak().isWhite()));
                                if(papan[i][j].getBidak().getValue() == 1 && Math.abs(baris-i) == 2){
                                    papan[i][j].enPassant = true;
                                }
                                else {
                                    papan[i][j].enPassant = false;
                                    for (int k = 0; k < 8; k++) {
                                        for (int l = 0; l < 4; l++) {
                                            papan[k][l].enPassant = false;
                                        }
                                    }
                                }
                            }else{
                                papan[i][j].setBidak(new Bidak(papan[baris][kolom].getBidak().getValue(),papan[baris][kolom].getBidak().isWhite()));
                                if(turn){
                                    papan[i+1][j].setBidak(new Bidak(0,false));
                                    papan[i+1][j].untouched = false;
                                    papan[i+1][j].enPassant = false;
                                }
                                else {
                                    papan[i-1][j].setBidak(new Bidak(0,false));
                                    papan[i-1][j].untouched = false;
                                    papan[i-1][j].enPassant = false;
                                }

                            }
                            papan[baris][kolom].setBidak(new Bidak(0,false));
                            papan[i][j].untouched = false;
                            papan[baris][kolom].untouched = false;
                            baris=-1;
                            kolom=-1;
                            defaultState();
                            turn = !turn;
                            if(turn){
                                whiteTurn.setText("Your Turn");
                                blackTurn.setText("");
                            }else{
                                whiteTurn.setText("");
                                blackTurn.setText("Your Turn");
                            }
                        }else{
                            baris=-1;
                            kolom=-1;
                            defaultState();
                        }
                    }
                }
            }
        }
    }

    void defaultState(){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 4; j++) {
                if(papan[i][j].getBidak().getValue() != 0 && papan[i][j].getBidak().isWhite() == false){
                    papan[i][j].letak.setRotationX(180);
                    papan[i][j].letak.setRotationY(180);
                }else if(papan[i][j].getBidak().getValue() == 0 || papan[i][j].getBidak().isWhite() == true){
                    papan[i][j].letak.setRotationX(0);
                    papan[i][j].letak.setRotationY(0);
                }
                papan[i][j].setPressed(false);
                papan[i][j].setStatus(0);
                if(papan[i][j].getBidak().getValue() == 5){
                    if(turn && papan[i][j].getBidak().isWhite()) isCheckmate(i,j);
                    else if(!turn && !papan[i][j].getBidak().isWhite()) isCheckmate(i,j);
                }
            }
        }
    }

    void possibleMove(int baris, int kolom){
        if(papan[baris][kolom].getBidak().getValue() == 5){
            for (int i = baris-1; i < baris+2; i++) {
                for (int j = kolom-1; j < kolom+2; j++) {
                    if(i <= 7 && i >= 0 && j <= 3 && j >= 0 && (baris != i || kolom != j)){
                        if(papan[i][j].getBidak().getValue() == 0){
                            papan[i][j].setStatus(1);
                        }else if((!turn && papan[i][j].getBidak().isWhite()) || (turn && !papan[i][j].getBidak().isWhite())){
                            papan[i][j].setStatus(1);
                        }
                    }
                }
            }
        }else if(papan[baris][kolom].getBidak().getValue() == 4){
            boolean diagkiriatas = true, diagkananatas = true, diagkiribawah = true, diagkananbawah = true;
            int i = baris, j = kolom;
            while(diagkiriatas){
                if(i-1 >= 0 && j - 1 >= 0){
                    i--;
                    j--;
                    if(papan[i][j].getBidak().getValue() == 0){
                        papan[i][j].setStatus(1);
                    }else if((!turn && papan[i][j].getBidak().isWhite()) || (turn && !papan[i][j].getBidak().isWhite())){
                        papan[i][j].setStatus(1);
                        diagkiriatas = false;
                    }else{
                        diagkiriatas = false;
                    }
                }else diagkiriatas = false;
            }
            i = baris; j = kolom;
            while(diagkiribawah){
                if(i+1 <= 7 && j -1 >= 0){
                    i++;
                    j--;
                    if(papan[i][j].getBidak().getValue() == 0){
                        papan[i][j].setStatus(1);
                    }else if((!turn && papan[i][j].getBidak().isWhite()) || (turn && !papan[i][j].getBidak().isWhite())){
                        papan[i][j].setStatus(1);
                        diagkiribawah = false;
                    }else{
                        diagkiribawah = false;
                    }
                }else diagkiribawah = false;
            }i = baris; j = kolom;
            while(diagkananatas){
                if(i-1 >= 0 && j + 1 <= 3){
                    i--;
                    j++;
                    if(papan[i][j].getBidak().getValue() == 0){
                        papan[i][j].setStatus(1);
                    }else if((!turn && papan[i][j].getBidak().isWhite()) || (turn && !papan[i][j].getBidak().isWhite())){
                        papan[i][j].setStatus(1);
                        diagkananatas = false;
                    }else{
                        diagkananatas = false;
                    }
                }else diagkananatas = false;
            }i = baris; j = kolom;
            while(diagkananbawah){
                if(i + 1 <= 7 && j + 1 <= 3){
                    i++;
                    j++;
                    if(papan[i][j].getBidak().getValue() == 0){
                        papan[i][j].setStatus(1);
                    }else if((!turn && papan[i][j].getBidak().isWhite()) || (turn && !papan[i][j].getBidak().isWhite())){
                        papan[i][j].setStatus(1);
                        diagkananbawah = false;
                    }else{
                        diagkananbawah = false;
                    }
                }else diagkananbawah = false;
            }i = 0;
            boolean vertatas = true, vertbawah = true;
            while(vertatas || vertbawah){
                i++;
                if(baris - i >= 0){
                    if(vertatas){
                        if(papan[baris-i][kolom].getBidak().getValue() == 0){
                            papan[baris-i][kolom].setStatus(1);
                        }else if((!turn && papan[baris-i][kolom].getBidak().isWhite()) || (turn && !papan[baris-i][kolom].getBidak().isWhite())){
                            papan[baris-i][kolom].setStatus(1);
                            vertatas = false;
                        }else vertatas = false;
                    }
                }else{
                    vertatas = false;
                }
                if(baris + i <= 7){
                    if(vertbawah){
                        if(papan[baris+i][kolom].getBidak().getValue() == 0){
                            papan[baris+i][kolom].setStatus(1);
                        }else if((!turn && papan[baris+i][kolom].getBidak().isWhite()) || (turn && !papan[baris+i][kolom].getBidak().isWhite())){
                            papan[baris+i][kolom].setStatus(1);
                            vertbawah = false;
                        }else vertbawah = false;
                    }
                }else{
                    vertbawah = false;
                }
            }
            j = 0;
            boolean horzatas = true, horzbawah = true;
            while(horzatas || horzbawah){
                j++;
                if(kolom - j >= 0){
                    if(horzatas){
                        if(papan[baris][kolom-j].getBidak().getValue() == 0){
                            papan[baris][kolom-j].setStatus(1);
                        }else if((!turn && papan[baris][kolom-j].getBidak().isWhite()) || (turn && !papan[baris][kolom-j].getBidak().isWhite())){
                            papan[baris][kolom-j].setStatus(1);
                            horzatas = false;
                        }else horzatas = false;
                    }
                }else horzatas = false;
                if(kolom + j <= 3){
                    if(horzbawah){
                        if(papan[baris][kolom+j].getBidak().getValue() == 0){
                            papan[baris][kolom+j].setStatus(1);
                        }else if((!turn && papan[baris][kolom+j].getBidak().isWhite()) || (turn && !papan[baris][kolom+j].getBidak().isWhite())){
                            papan[baris][kolom+j].setStatus(1);
                            horzbawah = false;
                        }else horzbawah = false;
                    }
                }else horzbawah = false;
            }
        }else if(papan[baris][kolom].getBidak().getValue() == 3){
            if(baris - 2 >= 0 && kolom - 1  >= 0){
                if(papan[baris - 2][kolom - 1].getBidak().getValue() == 0){
                    papan[baris - 2][kolom - 1].setStatus(1);
                }else if((!turn && papan[baris - 2][kolom - 1].getBidak().isWhite()) || (turn && !papan[baris - 2][kolom - 1].getBidak().isWhite())){
                    papan[baris - 2][kolom - 1].setStatus(1);
                }
            }if(baris - 2 >= 0 && kolom + 1  <= 3){
                if(papan[baris - 2][kolom + 1].getBidak().getValue() == 0){
                    papan[baris - 2][kolom + 1].setStatus(1);
                }else if((!turn && papan[baris - 2][kolom + 1].getBidak().isWhite()) || (turn && !papan[baris - 2][kolom + 1].getBidak().isWhite())){
                    papan[baris - 2][kolom + 1].setStatus(1);
                }
            }if(baris + 2 <= 7 && kolom - 1  >= 0){
                if(papan[baris + 2][kolom - 1].getBidak().getValue() == 0){
                    papan[baris + 2][kolom - 1].setStatus(1);
                }else if((!turn && papan[baris + 2][kolom - 1].getBidak().isWhite()) || (turn && !papan[baris + 2][kolom - 1].getBidak().isWhite())){
                    papan[baris + 2][kolom - 1].setStatus(1);
                }
            }if(baris + 2 <= 7 && kolom + 1  <= 3){
                if(papan[baris + 2][kolom + 1].getBidak().getValue() == 0){
                    papan[baris + 2][kolom + 1].setStatus(1);
                }else if((!turn && papan[baris + 2][kolom + 1].getBidak().isWhite()) || (turn && !papan[baris + 2][kolom + 1].getBidak().isWhite())){
                    papan[baris + 2][kolom + 1].setStatus(1);
                }
            }if(baris - 1 >= 0 && kolom + 2  <= 3){
                if(papan[baris - 1][kolom + 2].getBidak().getValue() == 0){
                    papan[baris - 1][kolom + 2].setStatus(1);
                }else if((!turn && papan[baris - 1][kolom + 2].getBidak().isWhite()) || (turn && !papan[baris - 1][kolom + 2].getBidak().isWhite())){
                    papan[baris - 1][kolom + 2].setStatus(1);
                }
            }if(baris - 1 >= 0 && kolom - 2  >= 0){
                if(papan[baris - 1][kolom - 2].getBidak().getValue() == 0){
                    papan[baris - 1][kolom - 2].setStatus(1);
                }else if((!turn && papan[baris - 1][kolom - 2].getBidak().isWhite()) || (turn && !papan[baris - 1][kolom - 2].getBidak().isWhite())){
                    papan[baris - 1][kolom - 2].setStatus(1);
                }
            }if(baris + 1 <= 7 && kolom - 2  >= 0){
                if(papan[baris + 1][kolom - 2].getBidak().getValue() == 0){
                    papan[baris + 1][kolom - 2].setStatus(1);
                }else if((!turn && papan[baris + 1][kolom - 2].getBidak().isWhite()) || (turn && !papan[baris + 1][kolom - 2].getBidak().isWhite())){
                    papan[baris + 1][kolom - 2].setStatus(1);
                }
            }if(baris + 1 <= 7 && kolom + 2  <= 3){
                if(papan[baris + 1][kolom + 2].getBidak().getValue() == 0){
                    papan[baris + 1][kolom + 2].setStatus(1);
                }else if((!turn && papan[baris + 1][kolom + 2].getBidak().isWhite()) || (turn && !papan[baris + 1][kolom + 2].getBidak().isWhite())){
                    papan[baris + 1][kolom + 2].setStatus(1);
                }
            }
        }else if(papan[baris][kolom].getBidak().getValue() == 2){
            boolean diagkiriatas = true, diagkananatas = true, diagkiribawah = true, diagkananbawah = true;
            int i = baris, j = kolom;
            while(diagkiriatas){
                if(i-1 >= 0 && j - 1 >= 0){
                    i--;
                    j--;
                    if(papan[i][j].getBidak().getValue() == 0){
                        papan[i][j].setStatus(1);
                    }else if((!turn && papan[i][j].getBidak().isWhite()) || (turn && !papan[i][j].getBidak().isWhite())){
                        papan[i][j].setStatus(1);
                        diagkiriatas = false;
                    }else{
                        diagkiriatas = false;
                    }
                }else diagkiriatas = false;
            }
            i = baris; j = kolom;
            while(diagkiribawah){
                if(i+1 <= 7 && j -1 >= 0){
                    i++;
                    j--;
                    if(papan[i][j].getBidak().getValue() == 0){
                        papan[i][j].setStatus(1);
                    }else if((!turn && papan[i][j].getBidak().isWhite()) || (turn && !papan[i][j].getBidak().isWhite())){
                        papan[i][j].setStatus(1);
                        diagkiribawah = false;
                    }else{
                        diagkiribawah = false;
                    }
                }else diagkiribawah = false;
            }i = baris; j = kolom;
            while(diagkananatas){
                if(i-1 >= 0 && j + 1 <= 3){
                    i--;
                    j++;
                    if(papan[i][j].getBidak().getValue() == 0){
                        papan[i][j].setStatus(1);
                    }else if((!turn && papan[i][j].getBidak().isWhite()) || (turn && !papan[i][j].getBidak().isWhite())){
                        papan[i][j].setStatus(1);
                        diagkananatas = false;
                    }else{
                        diagkananatas = false;
                    }
                }else diagkananatas = false;
            }i = baris; j = kolom;
            while(diagkananbawah){
                if(i + 1 <= 7 && j + 1 <= 3){
                    i++;
                    j++;
                    if(papan[i][j].getBidak().getValue() == 0){
                        papan[i][j].setStatus(1);
                    }else if((!turn && papan[i][j].getBidak().isWhite()) || (turn && !papan[i][j].getBidak().isWhite())){
                        papan[i][j].setStatus(1);
                        diagkananbawah = false;
                    }else{
                        diagkananbawah = false;
                    }
                }else diagkananbawah = false;
            }
        }else if(papan[baris][kolom].getBidak().getValue() == 1){
            if(turn){
                if(papan[baris][kolom].untouched && baris - 2 >= 0 && papan[baris - 2][kolom].getBidak().getValue() == 0 && papan[baris - 1][kolom].getBidak().getValue() == 0){
                    papan[baris - 2][kolom].setStatus(1);
                    papan[baris - 1][kolom].setStatus(1);
                }else if(baris - 1 >= 0 && papan[baris - 1][kolom].getBidak().getValue() == 0){
                    papan[baris - 1][kolom].setStatus(1);
                }if(baris - 1 >= 0 && kolom + 1 <= 3 && papan[baris - 1][kolom + 1].getBidak().getValue() != 0 && ((!turn && papan[baris - 1][kolom+1].getBidak().isWhite()) || (turn && !papan[baris - 1][kolom+1].getBidak().isWhite()))){
                    papan[baris-1][kolom+1].setStatus(1);
                }if(baris - 1 >= 0 && kolom - 1 >= 0 && papan[baris - 1][kolom - 1].getBidak().getValue() != 0 && ((!turn && papan[baris - 1][kolom-1].getBidak().isWhite()) || (turn && !papan[baris - 1][kolom-1].getBidak().isWhite()))){
                    papan[baris-1][kolom-1].setStatus(1);
                }

                if(baris + 1 <= 7 && kolom + 1 <= 3 && papan[baris][kolom+1].enPassant){
                    papan[baris-1][kolom+1].setStatus(2);
                }
                if(baris + 1 <= 7 && kolom - 1 >= 0 && papan[baris][kolom-1].enPassant){
                    papan[baris-1][kolom-1].setStatus(2);
                }
            }else {
                if (papan[baris][kolom].untouched && baris + 2 <= 7 && papan[baris + 2][kolom].getBidak().getValue() == 0 && papan[baris + 1][kolom].getBidak().getValue() == 0) {
                    papan[baris + 2][kolom].setStatus(1);
                    papan[baris + 1][kolom].setStatus(1);
                } else if (baris + 1 <= 7 && papan[baris + 1][kolom].getBidak().getValue() == 0) {
                    papan[baris + 1][kolom].setStatus(1);
                }
                if (baris + 1 <= 7 && kolom + 1 <= 3 && papan[baris + 1][kolom + 1].getBidak().getValue() != 0 && ((!turn && papan[baris + 1][kolom + 1].getBidak().isWhite()) || (turn && !papan[baris + 1][kolom + 1].getBidak().isWhite()))) {
                    papan[baris + 1][kolom + 1].setStatus(1);
                }
                if (baris + 1 <= 7 && kolom - 1 >= 0 && papan[baris + 1][kolom - 1].getBidak().getValue() != 0 && ((!turn && papan[baris + 1][kolom - 1].getBidak().isWhite()) || (turn && !papan[baris + 1][kolom - 1].getBidak().isWhite()))) {
                    papan[baris + 1][kolom - 1].setStatus(1);
                }
                if(baris - 1 <= 7 && kolom + 1 <= 3 && papan[baris][kolom+1].enPassant){
                    papan[baris+1][kolom+1].setStatus(2);
                }
                if(baris - 1 <= 7 && kolom - 1 >= 0 && papan[baris][kolom-1].enPassant){
                    papan[baris+1][kolom-1].setStatus(2);
                }
            }
        }
    }

    void isCheckmate(int baris, int kolom){
        int[][] papanCheck = new int[baris+1][kolom+1];
        Arrays.fill(papan,0);
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                int b = baris+i,k = kolom+j;
                if(turn){
                    //pawn
                    if(b-1 >= 0 && k-1 >= 0 && papan[b-1][k-1].getBidak().getValue() == 1) papanCheck[b][k]++;
                    if(b-1 >= 0 && k+1 <= 3 && papan[b-1][k+1].getBidak().getValue() == 1) papanCheck[b][k]++;
                }else{
                    if(b+1 <= 7 && k-1 >= 0 && papan[b+1][k-1].getBidak().getValue() == 1) papanCheck[b][k]++;
                    if(b+1 <= 7 && k+1 <= 3 && papan[b+1][k+1].getBidak().getValue() == 1) papanCheck[b][k]++;
                }
                //knight
                if(b - 2 >= 0 && k - 1 >= 0 && papan[b - 2][k - 1].getBidak().getValue() == 3) papanCheck[b][k]++;
                if(b - 2 >= 0 && k + 1 <= 3 && papan[b - 2][k + 1].getBidak().getValue() == 3) papanCheck[b][k]++;
                if(b + 2 <= 7 && k - 1 >= 0 && papan[b + 2][k - 1].getBidak().getValue() == 3) papanCheck[b][k]++;
                if(b + 2 <= 7 && k + 1 <= 3 && papan[b + 2][k + 1].getBidak().getValue() == 3) papanCheck[b][k]++;
                if(k - 2 >= 0 && b - 1 >= 0 && papan[b - 1][k - 2].getBidak().getValue() == 3) papanCheck[b][k]++;
                if(k - 2 >= 0 && b + 1 <= 7 && papan[b + 1][k - 2].getBidak().getValue() == 3) papanCheck[b][k]++;
                if(k + 2 <= 3 && b - 1 >= 0 && papan[b - 1][k + 2].getBidak().getValue() == 3) papanCheck[b][k]++;
                if(k + 2 <= 3 && b + 1 <= 7 && papan[b + 1][k + 2].getBidak().getValue() == 3) papanCheck[b][k]++;
                //Bishop & Queen (Diagonal)
                for (int l = 0; l < 8; l++) {
                    for (int m = 0; m < 4; m++) {
                        if(Math.abs(l-m) == Math.abs(b-k) && (papan[l][m].getBidak().getValue() == 2 || papan[l][m].getBidak().getValue() == 4)){
                            papanCheck[b][k]++;
                        }
                        if(l+m == b+k && (papan[l][m].getBidak().getValue() == 2 || papan[l][m].getBidak().getValue() == 4)){
                            papanCheck[b][k]++;
                        }
                    }
                }
                //Queen
                for (int l = 0; l < 8; l++) {
                    if(papan[l][k].getBidak().getValue() == 4) papanCheck[b][k]++;
                }
                for (int l = 0; l < 4; l++) {
                    if(papan[b][l].getBidak().getValue() == 4) papanCheck[b][k]++;
                }
//                for (int l = -1; l < 2; l++) {
//                    for (int m = -1; m < 2; m++) {
//                        if(l !=0 || m != 0){
//                            if(turn){
//                                if(b+l <= 7 && b+l >= 0 && k + m <= 3 && k + m >= 0){
//                                    if(papan[b+l][k+m].getBidak().getValue() == 5 &&)
//                                }
//                            }
//                        }
//                    }
//                }
            }
        }
    }

//    boolean validateMove(int iawal, int jawal, int iakhir, int jakhir){
//        boolean done = false;
//        while(done){
//
//        }
//        return false;
//    }
}