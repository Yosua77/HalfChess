package com.example.halfchess;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class GameActivity extends AppCompatActivity {
    private String diff = "none";
    private Papan[][] papan = new Papan[8][4];
    private boolean turn;
    private boolean[] check = new boolean[2];
    private ImageView virtualIV;
    private int baris = -1,kolom = -1,gamestate = -1;//draw = 0, white win = 1, black win = 2
    TextView whiteTurn,blackTurn;

    //tingkat kesulitan di variable diff, buat komputer jalan pake function move. untuk easy,medium,hard tolong kasih function sendiri"
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        virtualIV = findViewById(R.id.virtualIV);
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
        check[0] = false;
        check[1] = false;

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
        if(gamestate == -1) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 4; j++) {
                    if (v.getId() == papan[i][j].letak.getId()) {
                        //turn = white !turn = black
                        if (baris == -1 && kolom == -1) {
                            if (papan[i][j].getBidak().getValue() != 0) {
                                if ((turn && papan[i][j].getBidak().isWhite()) || (!turn && !papan[i][j].getBidak().isWhite())) {
                                    papan[i][j].setPressed(true);
                                    this.baris = i;
                                    this.kolom = j;
                                    possibleMove(i, j);
                                    break;
                                } else if (papan[i][j].getBidak().isWhite()) {
                                    Toast.makeText(GameActivity.this, "It is black's turn now", Toast.LENGTH_SHORT).show();
                                    break;
                                } else if (!papan[i][j].getBidak().isWhite()) {
                                    Toast.makeText(GameActivity.this, "It is white's turn now", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                        } else {
                            if (papan[i][j].getStatus() != 0) {
                                if (papan[i][j].getStatus() == 1) {
                                    papan[i][j].enPassant = papan[baris][kolom].enPassant;
                                    papan[i][j].setBidak(new Bidak(papan[baris][kolom].getBidak().getValue(), papan[baris][kolom].getBidak().isWhite()));
                                    if (papan[i][j].getBidak().getValue() == 1 && Math.abs(baris - i) == 2) {
                                        papan[i][j].enPassant = true;
                                    } else {
                                        papan[i][j].enPassant = false;
                                        for (int k = 0; k < 8; k++) {
                                            for (int l = 0; l < 4; l++) {
                                                papan[k][l].enPassant = false;
                                            }
                                        }
                                    }
                                } else {
                                    papan[i][j].setBidak(new Bidak(papan[baris][kolom].getBidak().getValue(), papan[baris][kolom].getBidak().isWhite()));
                                    if (turn) {
                                        papan[i + 1][j].setBidak(new Bidak(0, false));
                                        papan[i + 1][j].untouched = false;
                                        papan[i + 1][j].enPassant = false;
                                    } else {
                                        papan[i - 1][j].setBidak(new Bidak(0, false));
                                        papan[i - 1][j].untouched = false;
                                        papan[i - 1][j].enPassant = false;
                                    }

                                }
                                papan[baris][kolom].setBidak(new Bidak(0, false));
                                papan[i][j].untouched = false;
                                papan[baris][kolom].untouched = false;
                                defaultState();
                                turn = !turn;
                                isWinning();
                                check[0] = false;
                                check[1] = false;
                                baris = -1;
                                kolom = -1;
                                if(gamestate == -1) {
                                    if (turn) {
                                        for (int k = 0; k < 8; k++) {
                                            for (int l = 0; l < 4; l++) {
                                                if (papan[k][l].getBidak().getValue() == 5 && papan[k][l].getBidak().isWhite()) {
                                                    if (isSave(k, l, papan))
                                                        whiteTurn.setText("Your Turn");
                                                    else {
                                                        check[0] = true;
                                                        whiteTurn.setText("check!");
                                                    }
                                                    break;
                                                }
                                            }
                                        }
                                        blackTurn.setText("");
                                    }else {
                                        for (int k = 0; k < 8; k++) {
                                            for (int l = 0; l < 4; l++) {
                                                if (papan[k][l].getBidak().getValue() == 5 && !papan[k][l].getBidak().isWhite()) {
                                                    if (isSave(k, l, papan))
                                                        blackTurn.setText("Your Turn");
                                                    else {
                                                        check[1] = true;
                                                        blackTurn.setText("check!");
                                                    }
                                                    break;
                                                }
                                            }
                                        }
                                        whiteTurn.setText("");
                                    }
                                }
                            } else {
                                baris = -1;
                                kolom = -1;
                                defaultState();
                            }
                        }
                    }
                }
            }
        }
    }

    //defaultState reset status papan
    void defaultState(){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 4; j++) {
                if(papan[i][j].getBidak().getValue() != 0 && !papan[i][j].getBidak().isWhite()){
                    papan[i][j].letak.setRotationX(180);
                    papan[i][j].letak.setRotationY(180);
                }else if(papan[i][j].getBidak().getValue() == 0 || papan[i][j].getBidak().isWhite()){
                    papan[i][j].letak.setRotationX(0);
                    papan[i][j].letak.setRotationY(0);
                }
                papan[i][j].setPressed(false);
                papan[i][j].setStatus(0);
                if(i == 0 && papan[i][j].getBidak().getValue() == 1 && papan[i][j].getBidak().isWhite()) papan[i][j].getBidak().setValue(4);
                if(i == 7 && papan[i][j].getBidak().getValue() == 1 && !papan[i][j].getBidak().isWhite()) papan[i][j].getBidak().setValue(4);
            }
        }
    }

    //cek menang
    void isWinning(){
        int availableMoveWhite = 0, availableMoveBlack = 0;
        boolean checkWhite = false,checkBlack=false;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 4; j++) {
                if(papan[i][j].getBidak().getValue() != 0 && papan[i][j].getBidak().isWhite()){
                    int jumlah = isAnyMove(i,j);
                    availableMoveWhite+=jumlah;
                    if(papan[i][j].getBidak().getValue() == 1) Log.i("pionW",jumlah+"");
                    if(papan[i][j].getBidak().getValue() == 2) Log.i("bishopW",jumlah+"");
                    if(papan[i][j].getBidak().getValue() == 3) Log.i("knightW",jumlah+"");
                    if(papan[i][j].getBidak().getValue() == 4) Log.i("queenW",jumlah+"");
                    if(papan[i][j].getBidak().getValue() == 5) Log.i("kingW",jumlah+"");
                    if(papan[i][j].getBidak().getValue() == 5 && !isSave(i,j,papan)) checkWhite = true;
                }else if(papan[i][j].getBidak().getValue() != 0 && !papan[i][j].getBidak().isWhite()){
                    int jumlah = isAnyMove(i,j);
                    availableMoveBlack+= jumlah;
                    if(papan[i][j].getBidak().getValue() == 1) Log.i("pion",jumlah+"");
                    if(papan[i][j].getBidak().getValue() == 2) Log.i("bishop",jumlah+"");
                    if(papan[i][j].getBidak().getValue() == 3) Log.i("knight",jumlah+"");
                    if(papan[i][j].getBidak().getValue() == 4) Log.i("queen",jumlah+"");
                    if(papan[i][j].getBidak().getValue() == 5) Log.i("king",jumlah+"");
                    if(papan[i][j].getBidak().getValue() == 5 && !isSave(i,j,papan)) checkBlack = true;
                }
            }
        }
        if(availableMoveBlack == 0 && checkBlack){
            gamestate = 1;
            blackTurn.setText("Checkmate!");
            whiteTurn.setText("You win!");
        }else if(availableMoveBlack == 0 && !checkBlack){
            gamestate = 0;
            whiteTurn.setText("Draw : Stalemate");
            blackTurn.setText("Draw : Stalemate");
        }
        if(availableMoveWhite == 0 && checkWhite){
            gamestate = 2;
            blackTurn.setText("You win!");
            whiteTurn.setText("Checkmate!");
        }else if(availableMoveWhite == 0 && !checkWhite){
            gamestate = 0;
            whiteTurn.setText("Draw : Stalemate");
            blackTurn.setText("Draw : Stalemate");
        }
    }

    //apa ada langkah? return jumlah langkah yg tersedia buat bidak yang dipilih(i sama j nya)
    int isAnyMove(int baris,int kolom){
        int jumlah = 0;
        if(papan[baris][kolom].getBidak().getValue() == 5){
            for (int i = baris-1; i < baris+2; i++) {
                for (int j = kolom-1; j < kolom+2; j++) {
                    if(i <= 7 && i >= 0 && j <= 3 && j >= 0 && (baris != i || kolom != j)){
                        if(papan[i][j].getBidak().getValue() == 0){
                            if(isValid(i,j,papan,baris,kolom)){
                                jumlah++;
                            }
                        }else if((!turn && papan[i][j].getBidak().isWhite()) || (turn && !papan[i][j].getBidak().isWhite())){
                            if(isValid(i,j,papan,baris,kolom)){
                                jumlah++;
                            }
                        }
                    }
                }
            }
        }else if(papan[baris][kolom].getBidak().getValue() == 4){
            //queen
            boolean diagkiriatas = true, diagkananatas = true, diagkiribawah = true, diagkananbawah = true;
            int i = baris, j = kolom;
            while(diagkiriatas){
                if(i-1 >= 0 && j - 1 >= 0){
                    i--;
                    j--;
                    if(papan[i][j].getBidak().getValue() == 0){
                        if(isValid(i,j,papan,baris,kolom)){
                            jumlah++;
                        }
                    }else if((!turn && papan[i][j].getBidak().isWhite()) || (turn && !papan[i][j].getBidak().isWhite())){
                        if(isValid(i,j,papan,baris,kolom)){
                            jumlah++;
                        }
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
                    if(papan[i][j].getBidak().getValue() == 0 ){
                        if(isValid(i,j,papan,baris,kolom)){
                            jumlah++;
                        }
                    }else if((!turn && papan[i][j].getBidak().isWhite()) || (turn && !papan[i][j].getBidak().isWhite())){
                        if(isValid(i,j,papan,baris,kolom)){
                            jumlah++;
                        }
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
                        if(isValid(i,j,papan,baris,kolom)){
                            jumlah++;
                        }
                    }else if((!turn && papan[i][j].getBidak().isWhite()) || (turn && !papan[i][j].getBidak().isWhite())){
                        if(isValid(i,j,papan,baris,kolom)){
                            jumlah++;
                        }
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
                        if(isValid(i,j,papan,baris,kolom)){
                            jumlah++;
                        }
                    }else if((!turn && papan[i][j].getBidak().isWhite()) || (turn && !papan[i][j].getBidak().isWhite())){
                        if(isValid(i,j,papan,baris,kolom)){
                            jumlah++;
                        }
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
                            if(isValid(baris - i,kolom,papan,baris,kolom)){
                                jumlah++;
                            }
                        }else if(((!turn && papan[baris-i][kolom].getBidak().isWhite()) || (turn && !papan[baris-i][kolom].getBidak().isWhite()))&& isValid(i,j,papan,baris,kolom)){
                            if(isValid(baris - i,kolom,papan,baris,kolom)){
                                jumlah++;
                            }
                            vertatas = false;
                        }else vertatas = false;
                    }
                }else{
                    vertatas = false;
                }
                if(baris + i <= 7){
                    if(vertbawah){
                        if(papan[baris+i][kolom].getBidak().getValue() == 0){
                            if(isValid(baris + i, kolom,papan,baris,kolom)){
                                jumlah++;
                            }
                        }else if((!turn && papan[baris+i][kolom].getBidak().isWhite()) || (turn && !papan[baris+i][kolom].getBidak().isWhite())){
                            if(isValid(baris + i,kolom,papan,baris,kolom)){
                                jumlah++;
                            }
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
                            if(isValid(baris, kolom-j,papan,baris,kolom)){
                                jumlah++;
                            }
                        }else if((!turn && papan[baris][kolom-j].getBidak().isWhite()) || (turn && !papan[baris][kolom-j].getBidak().isWhite())){
                            if(isValid(baris,kolom-j,papan,baris,kolom)) jumlah++;
                            horzatas = false;
                        }else horzatas = false;
                    }
                }else horzatas = false;
                if(kolom + j <= 3){
                    if(horzbawah){
                        if(papan[baris][kolom+j].getBidak().getValue() == 0){
                            if(isValid(baris,kolom+j,papan,baris,kolom)) jumlah++;
                        }else if((!turn && papan[baris][kolom+j].getBidak().isWhite()) || (turn && !papan[baris][kolom+j].getBidak().isWhite())){
                            if(isValid(baris,kolom+j,papan,baris,kolom)) jumlah++;
                            horzbawah = false;
                        }else horzbawah = false;
                    }
                }else horzbawah = false;
            }
        }else if(papan[baris][kolom].getBidak().getValue() == 3){
            //knight
            if(baris - 2 >= 0 && kolom - 1  >= 0){
                if(papan[baris - 2][kolom - 1].getBidak().getValue() == 0){
                    if(isValid(baris-2,kolom-1,papan,baris,kolom)) jumlah++;
                }else if((!turn && papan[baris - 2][kolom - 1].getBidak().isWhite()) || (turn && !papan[baris - 2][kolom - 1].getBidak().isWhite())){
                    if(isValid(baris-2,kolom-1,papan,baris,kolom)) jumlah++;
                }
            }if(baris - 2 >= 0 && kolom + 1  <= 3){
                if(papan[baris - 2][kolom + 1].getBidak().getValue() == 0){
                    if(isValid(baris-2,kolom+1,papan,baris,kolom)) jumlah++;
                }else if((!turn && papan[baris - 2][kolom + 1].getBidak().isWhite()) || (turn && !papan[baris - 2][kolom + 1].getBidak().isWhite())){
                    if(isValid(baris-2,kolom+1,papan,baris,kolom)) jumlah++;
                }
            }if(baris + 2 <= 7 && kolom - 1  >= 0){
                if(papan[baris + 2][kolom - 1].getBidak().getValue() == 0){
                    if(isValid(baris+2,kolom-1,papan,baris,kolom)) jumlah++;
                }else if((!turn && papan[baris + 2][kolom - 1].getBidak().isWhite()) || (turn && !papan[baris + 2][kolom - 1].getBidak().isWhite())){
                    if(isValid(baris+2,kolom-1,papan,baris,kolom)) jumlah++;
                }
            }if(baris + 2 <= 7 && kolom + 1  <= 3){
                if(papan[baris + 2][kolom + 1].getBidak().getValue() == 0){
                    if(isValid(baris+2,kolom+1,papan,baris,kolom)) jumlah++;
                }else if((!turn && papan[baris + 2][kolom + 1].getBidak().isWhite()) || (turn && !papan[baris + 2][kolom + 1].getBidak().isWhite())){
                    if(isValid(baris+2,kolom+1,papan,baris,kolom)) jumlah++;
                }
            }if(baris - 1 >= 0 && kolom + 2  <= 3){
                if(papan[baris - 1][kolom + 2].getBidak().getValue() == 0){
                    if(isValid(baris-1,kolom+2,papan,baris,kolom)) jumlah++;
                }else if((!turn && papan[baris - 1][kolom + 2].getBidak().isWhite()) || (turn && !papan[baris - 1][kolom + 2].getBidak().isWhite())){
                    if(isValid(baris-1,kolom+2,papan,baris,kolom)) jumlah++;
                }
            }if(baris - 1 >= 0 && kolom - 2  >= 0){
                if(papan[baris - 1][kolom - 2].getBidak().getValue() == 0){
                    if(isValid(baris-1,kolom-2,papan,baris,kolom)) jumlah++;
                }else if((!turn && papan[baris - 1][kolom - 2].getBidak().isWhite()) || (turn && !papan[baris - 1][kolom - 2].getBidak().isWhite())){
                    if(isValid(baris-1,kolom-2,papan,baris,kolom)) jumlah++;
                }
            }if(baris + 1 <= 7 && kolom - 2  >= 0){
                if(papan[baris + 1][kolom - 2].getBidak().getValue() == 0){
                    if(isValid(baris+1,kolom-2,papan,baris,kolom)) jumlah++;
                }else if((!turn && papan[baris + 1][kolom - 2].getBidak().isWhite()) || (turn && !papan[baris + 1][kolom - 2].getBidak().isWhite())){
                    if(isValid(baris+1,kolom-2,papan,baris,kolom)) jumlah++;
                }
            }if(baris + 1 <= 7 && kolom + 2  <= 3){
                if(papan[baris + 1][kolom + 2].getBidak().getValue() == 0 && isValid(baris + 1,kolom + 2,papan,baris,kolom)){
                    if(isValid(baris+1,kolom+2,papan,baris,kolom)) jumlah++;
                }else if(((!turn && papan[baris + 1][kolom + 2].getBidak().isWhite()) || (turn && !papan[baris + 1][kolom + 2].getBidak().isWhite())) && isValid(baris + 1,kolom + 2,papan,baris,kolom)){
                    if(isValid(baris+1,kolom+2,papan,baris,kolom)) jumlah++;
                }
            }
        }else if(papan[baris][kolom].getBidak().getValue() == 2){
            //bishop
            boolean diagkiriatas = true, diagkananatas = true, diagkiribawah = true, diagkananbawah = true;
            int i = baris, j = kolom;
            while(diagkiriatas){
                if(i-1 >= 0 && j - 1 >= 0){
                    i--;
                    j--;
                    if(papan[i][j].getBidak().getValue() == 0){
                        if(isValid(i,j,papan,baris,kolom)) jumlah++;
                    }else if((!turn && papan[i][j].getBidak().isWhite()) || (turn && !papan[i][j].getBidak().isWhite())){
                        if(isValid(i,j,papan,baris,kolom)) jumlah++;
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
                        if(isValid(i,j,papan,baris,kolom)) jumlah++;
                    }else if((!turn && papan[i][j].getBidak().isWhite()) || (turn && !papan[i][j].getBidak().isWhite())){
                        if(isValid(i,j,papan,baris,kolom)) jumlah++;
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
                        if(isValid(i,j,papan,baris,kolom)) jumlah++;
                    }else if((!turn && papan[i][j].getBidak().isWhite()) || (turn && !papan[i][j].getBidak().isWhite())){
                        if(isValid(i,j,papan,baris,kolom)) jumlah++;
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
                        if(isValid(i,j,papan,baris,kolom)) jumlah++;
                    }else if((!turn && papan[i][j].getBidak().isWhite()) || (turn && !papan[i][j].getBidak().isWhite())){
                        if(isValid(i,j,papan,baris,kolom)) jumlah++;
                        diagkananbawah = false;
                    }else{
                        diagkananbawah = false;
                    }
                }else diagkananbawah = false;
            }
        }else if(papan[baris][kolom].getBidak().getValue() == 1){
            //pawn
            if(turn){
                if(papan[baris][kolom].untouched && baris - 2 >= 0 && papan[baris - 2][kolom].getBidak().getValue() == 0 && papan[baris - 1][kolom].getBidak().getValue() == 0 && isValid(baris -2,kolom,papan,baris,kolom)){
                    jumlah+=2;
                }else if(baris - 1 >= 0 && papan[baris - 1][kolom].getBidak().getValue() == 0 && isValid(baris -1,kolom,papan,baris,kolom)){
                    jumlah++;
                }if(baris - 1 >= 0 && kolom + 1 <= 3 && papan[baris - 1][kolom + 1].getBidak().getValue() != 0 && ((!turn && papan[baris - 1][kolom+1].getBidak().isWhite()) || (turn && !papan[baris - 1][kolom+1].getBidak().isWhite())) && isValid(baris - 1,kolom + 1,papan,baris,kolom)){
                    jumlah++;
                }if(baris - 1 >= 0 && kolom - 1 >= 0 && papan[baris - 1][kolom - 1].getBidak().getValue() != 0 && ((!turn && papan[baris - 1][kolom-1].getBidak().isWhite()) || (turn && !papan[baris - 1][kolom-1].getBidak().isWhite())) && isValid(baris - 1,kolom - 1,papan,baris,kolom)){
                    jumlah++;
                }

                if(baris + 1 <= 7 && kolom + 1 <= 3 && papan[baris][kolom+1].enPassant && isValid(baris + 1,kolom + 1,papan,baris,kolom)){
                    jumlah++;
                }
                if(baris + 1 <= 7 && kolom - 1 >= 0 && papan[baris][kolom-1].enPassant && isValid(baris + 1,kolom - 1,papan,baris,kolom)){
                    jumlah++;
                }
            }else {
                if (papan[baris][kolom].untouched && baris + 2 <= 7 && papan[baris + 2][kolom].getBidak().getValue() == 0 && papan[baris + 1][kolom].getBidak().getValue() == 0 && isValid(baris + 2,kolom,papan,baris,kolom)) {
                    jumlah+=2;
                } else if (baris + 1 <= 7 && papan[baris + 1][kolom].getBidak().getValue() == 0 && isValid(baris + 1,kolom,papan,baris,kolom)) {
                    jumlah++;
                }
                if (baris + 1 <= 7 && kolom + 1 <= 3 && papan[baris + 1][kolom + 1].getBidak().getValue() != 0 && ((!turn && papan[baris + 1][kolom + 1].getBidak().isWhite()) || (turn && !papan[baris + 1][kolom + 1].getBidak().isWhite())) && isValid(baris + 1,kolom + 1,papan,baris,kolom)) {
                    jumlah++;
                }
                if (baris + 1 <= 7 && kolom - 1 >= 0 && papan[baris + 1][kolom - 1].getBidak().getValue() != 0 && ((!turn && papan[baris + 1][kolom - 1].getBidak().isWhite()) || (turn && !papan[baris + 1][kolom - 1].getBidak().isWhite())) && isValid(baris + 1,kolom - 1,papan,baris,kolom)) {
                    jumlah++;
                }
                if(baris - 1 <= 7 && kolom + 1 <= 3 && papan[baris][kolom+1].enPassant && isValid(baris - 1,kolom + 1,papan,baris,kolom)){
                    jumlah++;
                }
                if(baris - 1 <= 7 && kolom - 1 >= 0 && papan[baris][kolom-1].enPassant && isValid(baris - 1,kolom - 1,papan,baris,kolom)){
                    jumlah++;
                }
            }
        }
        return jumlah;
    }

    //possibleMove nandai tempat yang boleh dilewati. kalo papan statusnya di set 1 brrti bisa diclick(dilewati)
    void possibleMove(int baris, int kolom){
        //king
        if(papan[baris][kolom].getBidak().getValue() == 5){
            for (int i = baris-1; i < baris+2; i++) {
                for (int j = kolom-1; j < kolom+2; j++) {
                    if(i <= 7 && i >= 0 && j <= 3 && j >= 0 && (baris != i || kolom != j)){
                        if(papan[i][j].getBidak().getValue() == 0){
                            if(isValid(i,j,papan,baris,kolom)){
                                papan[i][j].setStatus(1);
                            }
                        }else if((!turn && papan[i][j].getBidak().isWhite()) || (turn && !papan[i][j].getBidak().isWhite())){
                            if(isValid(i,j,papan,baris,kolom)){
                                papan[i][j].setStatus(1);
                            }
                        }
                    }
                }
            }
        }else if(papan[baris][kolom].getBidak().getValue() == 4){
            //queen
            boolean diagkiriatas = true, diagkananatas = true, diagkiribawah = true, diagkananbawah = true;
            int i = baris, j = kolom;
            while(diagkiriatas){
                if(i-1 >= 0 && j - 1 >= 0){
                    i--;
                    j--;
                    if(papan[i][j].getBidak().getValue() == 0){
                        if(isValid(i,j,papan,baris,kolom)){
                            papan[i][j].setStatus(1);
                        }
                    }else if((!turn && papan[i][j].getBidak().isWhite()) || (turn && !papan[i][j].getBidak().isWhite())){
                        if(isValid(i,j,papan,baris,kolom)){
                            papan[i][j].setStatus(1);
                        }
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
                    if(papan[i][j].getBidak().getValue() == 0 ){
                        if(isValid(i,j,papan,baris,kolom)){
                            papan[i][j].setStatus(1);
                        }
                    }else if((!turn && papan[i][j].getBidak().isWhite()) || (turn && !papan[i][j].getBidak().isWhite())){
                        if(isValid(i,j,papan,baris,kolom)){
                            papan[i][j].setStatus(1);
                        }
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
                        if(isValid(i,j,papan,baris,kolom)){
                            papan[i][j].setStatus(1);
                        }
                    }else if((!turn && papan[i][j].getBidak().isWhite()) || (turn && !papan[i][j].getBidak().isWhite())){
                        if(isValid(i,j,papan,baris,kolom)){
                            papan[i][j].setStatus(1);
                        }
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
                        if(isValid(i,j,papan,baris,kolom)){
                            papan[i][j].setStatus(1);
                        }
                    }else if((!turn && papan[i][j].getBidak().isWhite()) || (turn && !papan[i][j].getBidak().isWhite())){
                        if(isValid(i,j,papan,baris,kolom)){
                            papan[i][j].setStatus(1);
                        }
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
                            if(isValid(baris - i,kolom,papan,baris,kolom)){
                                papan[baris-i][kolom].setStatus(1);
                            }
                        }else if(((!turn && papan[baris-i][kolom].getBidak().isWhite()) || (turn && !papan[baris-i][kolom].getBidak().isWhite()))&& isValid(i,j,papan,baris,kolom)){
                            if(isValid(baris - i,kolom,papan,baris,kolom)){
                                papan[baris-i][kolom].setStatus(1);
                            }
                            vertatas = false;
                        }else vertatas = false;
                    }
                }else{
                    vertatas = false;
                }
                if(baris + i <= 7){
                    if(vertbawah){
                        if(papan[baris+i][kolom].getBidak().getValue() == 0){
                            if(isValid(baris + i, kolom,papan,baris,kolom)){
                                papan[baris+i][kolom].setStatus(1);
                            }
                        }else if((!turn && papan[baris+i][kolom].getBidak().isWhite()) || (turn && !papan[baris+i][kolom].getBidak().isWhite())){
                            if(isValid(baris + i,kolom,papan,baris,kolom)){
                                papan[baris+i][kolom].setStatus(1);
                            }
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
                            if(isValid(baris,kolom-j,papan,baris,kolom))papan[baris][kolom-j].setStatus(1);
                        }else if((!turn && papan[baris][kolom-j].getBidak().isWhite()) || (turn && !papan[baris][kolom-j].getBidak().isWhite())){
                            if(isValid(baris,kolom-j,papan,baris,kolom))papan[baris][kolom-j].setStatus(1);
                            horzatas = false;
                        }else horzatas = false;
                    }
                }else horzatas = false;
                if(kolom + j <= 3){
                    if(horzbawah){
                        if(papan[baris][kolom+j].getBidak().getValue() == 0){
                            if(isValid(baris,kolom+j,papan,baris,kolom))papan[baris][kolom+j].setStatus(1);
                        }else if((!turn && papan[baris][kolom+j].getBidak().isWhite()) || (turn && !papan[baris][kolom+j].getBidak().isWhite())){
                            if(isValid(baris,kolom+j,papan,baris,kolom))papan[baris][kolom+j].setStatus(1);
                            horzbawah = false;
                        }else horzbawah = false;
                    }
                }else horzbawah = false;
            }
        }else if(papan[baris][kolom].getBidak().getValue() == 3){
            //knight
            if(baris - 2 >= 0 && kolom - 1  >= 0){
                if(papan[baris - 2][kolom - 1].getBidak().getValue() == 0){
                    if(isValid(baris-2,kolom-1,papan,baris,kolom)) papan[baris - 2][kolom - 1].setStatus(1);
                }else if((!turn && papan[baris - 2][kolom - 1].getBidak().isWhite()) || (turn && !papan[baris - 2][kolom - 1].getBidak().isWhite())){
                    if(isValid(baris-2,kolom-1,papan,baris,kolom))papan[baris - 2][kolom - 1].setStatus(1);
                }
            }if(baris - 2 >= 0 && kolom + 1  <= 3){
                if(papan[baris - 2][kolom + 1].getBidak().getValue() == 0){
                    if(isValid(baris-2,kolom+1,papan,baris,kolom))papan[baris - 2][kolom + 1].setStatus(1);
                }else if((!turn && papan[baris - 2][kolom + 1].getBidak().isWhite()) || (turn && !papan[baris - 2][kolom + 1].getBidak().isWhite())){
                    if(isValid(baris-2,kolom+1,papan,baris,kolom))papan[baris - 2][kolom + 1].setStatus(1);
                }
            }if(baris + 2 <= 7 && kolom - 1  >= 0){
                if(papan[baris + 2][kolom - 1].getBidak().getValue() == 0){
                    if(isValid(baris+2,kolom-1,papan,baris,kolom))papan[baris + 2][kolom - 1].setStatus(1);
                }else if((!turn && papan[baris + 2][kolom - 1].getBidak().isWhite()) || (turn && !papan[baris + 2][kolom - 1].getBidak().isWhite())){
                    if(isValid(baris+2,kolom-1,papan,baris,kolom))papan[baris + 2][kolom - 1].setStatus(1);
                }
            }if(baris + 2 <= 7 && kolom + 1  <= 3){
                if(papan[baris + 2][kolom + 1].getBidak().getValue() == 0){
                    if(isValid(baris+2,kolom+1,papan,baris,kolom))papan[baris + 2][kolom + 1].setStatus(1);
                }else if((!turn && papan[baris + 2][kolom + 1].getBidak().isWhite()) || (turn && !papan[baris + 2][kolom + 1].getBidak().isWhite())){
                    if(isValid(baris+2,kolom+1,papan,baris,kolom))papan[baris + 2][kolom + 1].setStatus(1);
                }
            }if(baris - 1 >= 0 && kolom + 2  <= 3){
                if(papan[baris - 1][kolom + 2].getBidak().getValue() == 0){
                    if(isValid(baris-1,kolom+2,papan,baris,kolom))papan[baris - 1][kolom + 2].setStatus(1);
                }else if((!turn && papan[baris - 1][kolom + 2].getBidak().isWhite()) || (turn && !papan[baris - 1][kolom + 2].getBidak().isWhite())){
                    if(isValid(baris-1,kolom+2,papan,baris,kolom))papan[baris - 1][kolom + 2].setStatus(1);
                }
            }if(baris - 1 >= 0 && kolom - 2  >= 0){
                if(papan[baris - 1][kolom - 2].getBidak().getValue() == 0){
                    if(isValid(baris-1,kolom-2,papan,baris,kolom))papan[baris - 1][kolom - 2].setStatus(1);
                }else if((!turn && papan[baris - 1][kolom - 2].getBidak().isWhite()) || (turn && !papan[baris - 1][kolom - 2].getBidak().isWhite())){
                    if(isValid(baris-1,kolom-2,papan,baris,kolom))papan[baris - 1][kolom - 2].setStatus(1);
                }
            }if(baris + 1 <= 7 && kolom - 2  >= 0){
                if(papan[baris + 1][kolom - 2].getBidak().getValue() == 0){
                    if(isValid(baris+1,kolom-2,papan,baris,kolom))papan[baris + 1][kolom - 2].setStatus(1);
                }else if((!turn && papan[baris + 1][kolom - 2].getBidak().isWhite()) || (turn && !papan[baris + 1][kolom - 2].getBidak().isWhite())){
                    if(isValid(baris+1,kolom-2,papan,baris,kolom))papan[baris + 1][kolom - 2].setStatus(1);
                }
            }if(baris + 1 <= 7 && kolom + 2  <= 3){
                if(papan[baris + 1][kolom + 2].getBidak().getValue() == 0 && isValid(baris + 1,kolom + 2,papan,baris,kolom)){
                    if(isValid(baris+1,kolom+2,papan,baris,kolom))papan[baris + 1][kolom + 2].setStatus(1);
                }else if(((!turn && papan[baris + 1][kolom + 2].getBidak().isWhite()) || (turn && !papan[baris + 1][kolom + 2].getBidak().isWhite())) && isValid(baris + 1,kolom + 2,papan,baris,kolom)){
                    if(isValid(baris+1,kolom+2,papan,baris,kolom))papan[baris + 1][kolom + 2].setStatus(1);
                }
            }
        }else if(papan[baris][kolom].getBidak().getValue() == 2){
            //bishop
            boolean diagkiriatas = true, diagkananatas = true, diagkiribawah = true, diagkananbawah = true;
            int i = baris, j = kolom;
            while(diagkiriatas){
                if(i-1 >= 0 && j - 1 >= 0){
                    i--;
                    j--;
                    if(papan[i][j].getBidak().getValue() == 0){
                        if(isValid(i,j,papan,baris,kolom))papan[i][j].setStatus(1);
                    }else if((!turn && papan[i][j].getBidak().isWhite()) || (turn && !papan[i][j].getBidak().isWhite())){
                        if(isValid(i,j,papan,baris,kolom))papan[i][j].setStatus(1);
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
                        if(isValid(i,j,papan,baris,kolom))papan[i][j].setStatus(1);
                    }else if((!turn && papan[i][j].getBidak().isWhite()) || (turn && !papan[i][j].getBidak().isWhite())){
                        if(isValid(i,j,papan,baris,kolom))papan[i][j].setStatus(1);
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
                        if(isValid(i,j,papan,baris,kolom))papan[i][j].setStatus(1);
                    }else if((!turn && papan[i][j].getBidak().isWhite()) || (turn && !papan[i][j].getBidak().isWhite())){
                        if(isValid(i,j,papan,baris,kolom))papan[i][j].setStatus(1);
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
                        if(isValid(i,j,papan,baris,kolom))papan[i][j].setStatus(1);
                    }else if((!turn && papan[i][j].getBidak().isWhite()) || (turn && !papan[i][j].getBidak().isWhite())){
                        if(isValid(i,j,papan,baris,kolom))papan[i][j].setStatus(1);
                        diagkananbawah = false;
                    }else{
                        diagkananbawah = false;
                    }
                }else diagkananbawah = false;
            }
        }else if(papan[baris][kolom].getBidak().getValue() == 1){
            //pawn
            if(turn){
                if(papan[baris][kolom].untouched && baris - 2 >= 0 && papan[baris - 2][kolom].getBidak().getValue() == 0 && papan[baris - 1][kolom].getBidak().getValue() == 0 && isValid(baris -2,kolom,papan,baris,kolom)){
                    papan[baris - 2][kolom].setStatus(1);
                    papan[baris - 1][kolom].setStatus(1);
                }else if(baris - 1 >= 0 && papan[baris - 1][kolom].getBidak().getValue() == 0 && isValid(baris -1,kolom,papan,baris,kolom)){
                    papan[baris - 1][kolom].setStatus(1);
                }if(baris - 1 >= 0 && kolom + 1 <= 3 && papan[baris - 1][kolom + 1].getBidak().getValue() != 0 && ((!turn && papan[baris - 1][kolom+1].getBidak().isWhite()) || (turn && !papan[baris - 1][kolom+1].getBidak().isWhite())) && isValid(baris - 1,kolom + 1,papan,baris,kolom)){
                    papan[baris-1][kolom+1].setStatus(1);
                }if(baris - 1 >= 0 && kolom - 1 >= 0 && papan[baris - 1][kolom - 1].getBidak().getValue() != 0 && ((!turn && papan[baris - 1][kolom-1].getBidak().isWhite()) || (turn && !papan[baris - 1][kolom-1].getBidak().isWhite())) && isValid(baris - 1,kolom - 1,papan,baris,kolom)){
                    papan[baris-1][kolom-1].setStatus(1);
                }

                if(baris + 1 <= 7 && kolom + 1 <= 3 && papan[baris][kolom+1].enPassant && isValid(baris + 1,kolom + 1,papan,baris,kolom)){
                    papan[baris-1][kolom+1].setStatus(2);
                }
                if(baris + 1 <= 7 && kolom - 1 >= 0 && papan[baris][kolom-1].enPassant && isValid(baris + 1,kolom - 1,papan,baris,kolom)){
                    papan[baris-1][kolom-1].setStatus(2);
                }
            }else {
                if (papan[baris][kolom].untouched && baris + 2 <= 7 && papan[baris + 2][kolom].getBidak().getValue() == 0 && papan[baris + 1][kolom].getBidak().getValue() == 0 && isValid(baris + 2,kolom,papan,baris,kolom)) {
                    papan[baris + 2][kolom].setStatus(1);
                    papan[baris + 1][kolom].setStatus(1);
                } else if (baris + 1 <= 7 && papan[baris + 1][kolom].getBidak().getValue() == 0 && isValid(baris + 1,kolom,papan,baris,kolom)) {
                    papan[baris + 1][kolom].setStatus(1);
                }
                if (baris + 1 <= 7 && kolom + 1 <= 3 && papan[baris + 1][kolom + 1].getBidak().getValue() != 0 && ((!turn && papan[baris + 1][kolom + 1].getBidak().isWhite()) || (turn && !papan[baris + 1][kolom + 1].getBidak().isWhite())) && isValid(baris + 1,kolom + 1,papan,baris,kolom)) {
                    papan[baris + 1][kolom + 1].setStatus(1);
                }
                if (baris + 1 <= 7 && kolom - 1 >= 0 && papan[baris + 1][kolom - 1].getBidak().getValue() != 0 && ((!turn && papan[baris + 1][kolom - 1].getBidak().isWhite()) || (turn && !papan[baris + 1][kolom - 1].getBidak().isWhite())) && isValid(baris + 1,kolom - 1,papan,baris,kolom)) {
                    papan[baris + 1][kolom - 1].setStatus(1);
                }
                if(baris - 1 <= 7 && kolom + 1 <= 3 && papan[baris][kolom+1].enPassant && isValid(baris - 1,kolom + 1,papan,baris,kolom)){
                    papan[baris+1][kolom+1].setStatus(2);
                }
                if(baris - 1 <= 7 && kolom - 1 >= 0 && papan[baris][kolom-1].enPassant && isValid(baris - 1,kolom - 1,papan,baris,kolom)){
                    papan[baris+1][kolom-1].setStatus(2);
                }
            }
        }
    }

    //Cek valid langkah contoh membuka jalan musuh memakan king
    boolean isValid(int i, int j,Papan[][] papan, int baris, int kolom){
        Papan[][] tempPapan = new Papan[8][4];
        for (int k = 0; k < 8; k++) {
            for (int l = 0; l < 4; l++) {
                tempPapan[k][l] = new Papan(new Bidak(papan[k][l].getBidak().getValue(),papan[k][l].getBidak().isWhite()),virtualIV,"#FFFFFF");
            }
        }
        tempPapan[i][j].enPassant = tempPapan[baris][kolom].enPassant;
        tempPapan[i][j].setBidak(new Bidak(tempPapan[baris][kolom].getBidak().getValue(),tempPapan[baris][kolom].getBidak().isWhite()));
        if(tempPapan[i][j].getBidak().getValue() == 1 && Math.abs(baris-i) == 2){
            tempPapan[i][j].enPassant = true;
        }
        else {
            tempPapan[i][j].enPassant = false;
            for (int k = 0; k < 8; k++) {
                for (int l = 0; l < 4; l++) {
                    tempPapan[k][l].enPassant = false;
                }
            }
        }
        tempPapan[baris][kolom].setBidak(new Bidak(0,false));
        tempPapan[i][j].untouched = false;
        tempPapan[baris][kolom].untouched = false;
        for (int k = 0; k < 8; k++) {
            for (int l = 0; l < 4; l++) {
                if(tempPapan[k][l].getBidak().getValue() == 5 && tempPapan[k][l].getBidak().isWhite() == turn){
                    if(!isSave(k,l,tempPapan)){
//                        Toast.makeText(this, "Invalid Move", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    break;
                }
            }
        }
        return true;

    }

    //isSave buat ngecek apa ada yg nyerang di kotak itu
    boolean isSave(int baris, int kolom,Papan[][] papan){
        if(turn){
            //pawn
            if(baris - 1 >= 0 && kolom-1 >= 0 && papan[baris-1][kolom-1].getBidak().getValue() == 1 && papan[baris-1][kolom-1].getBidak().isWhite() != turn){
                return false;
            }
            if(baris - 1 >= 0 && kolom+1 <= 3 && papan[baris-1][kolom+1].getBidak().getValue() == 1 && papan[baris-1][kolom+1].getBidak().isWhite() != turn){
                return false;
            }
        }else{
            if(baris + 1 <= 7 && kolom-1 >= 0 && papan[baris + 1][kolom - 1].getBidak().getValue() == 1 && papan[baris+1][kolom-1].getBidak().isWhite() != turn){
                return false;
            }
            if(baris + 1 <= 7 && kolom+1 <= 3 && papan[baris + 1][kolom + 1].getBidak().getValue() == 1 && papan[baris+1][kolom+1].getBidak().isWhite() != turn){
                return false;
            }
        }//bishop & queen(diagonal)
        //kanan bawah
        for (int i = baris + 1,j = kolom + 1; i < 8 && j < 4; i++, j++) {
            if((papan[i][j].getBidak().getValue() == 2 || papan[i][j].getBidak().getValue() == 4) && papan[i][j].getBidak().isWhite() != turn){
                return false;
            }else if(papan[i][j].getBidak().getValue() != 0 && papan[i][j].getBidak().isWhite() == turn){
                if(papan[i][j].getBidak().getValue() != 5)break;
            }
        }
        //kanan atas
        for (int i = baris - 1,j = kolom + 1; i >= 0 && j < 4; i--, j++) {
            if((papan[i][j].getBidak().getValue() == 2 || papan[i][j].getBidak().getValue() == 4) && papan[i][j].getBidak().isWhite() != turn){
                return false;
            }else if(papan[i][j].getBidak().getValue() != 0 && papan[i][j].getBidak().isWhite() == turn){
                if(papan[i][j].getBidak().getValue() != 5)break;
            }
        }
        //kiri atas
        for (int i = baris - 1,j = kolom - 1; i >= 0 && j >= 0; i--, j--) {
            if((papan[i][j].getBidak().getValue() == 2 || papan[i][j].getBidak().getValue() == 4) && papan[i][j].getBidak().isWhite() != turn){
                return false;
            }else if(papan[i][j].getBidak().getValue() != 0 && papan[i][j].getBidak().isWhite() == turn) {
                if(papan[i][j].getBidak().getValue() != 5)break;
            }
        }
        //kiri bawah
        for (int i = baris + 1,j = kolom - 1; i < 8 && j >= 0; i++, j--) {
            if((papan[i][j].getBidak().getValue() == 2 || papan[i][j].getBidak().getValue() == 4) && papan[i][j].getBidak().isWhite() != turn){
                return false;
            }else if(papan[i][j].getBidak().getValue() != 0 && papan[i][j].getBidak().isWhite() == turn) {
                if(papan[i][j].getBidak().getValue() != 5)break;
            }
        }
        //knight
        if(baris - 2 >= 0 && kolom - 1  >= 0){
            if(papan[baris - 2][kolom - 1].getBidak().getValue() == 3 && papan[baris - 2][kolom - 1].getBidak().isWhite() != turn){
                return false;
            }
        }if(baris - 2 >= 0 && kolom + 1  <= 3){
            if(papan[baris - 2][kolom + 1].getBidak().getValue() == 3 && papan[baris - 2][kolom + 1].getBidak().isWhite() != turn){
                return false;
            }
        }if(baris + 2 <= 7 && kolom - 1  >= 0){
            if(papan[baris + 2][kolom - 1].getBidak().getValue() == 3 && papan[baris + 2][kolom - 1].getBidak().isWhite() != turn){
                return false;
            }
        }if(baris + 2 <= 7 && kolom + 1  <= 3){
            if(papan[baris + 2][kolom + 1].getBidak().getValue() == 3 && papan[baris + 2][kolom + 1].getBidak().isWhite() != turn){
                return false;
            }
        }if(baris - 1 >= 0 && kolom + 2  <= 3){
            if(papan[baris - 1][kolom + 2].getBidak().getValue() == 3 && papan[baris - 1][kolom + 2].getBidak().isWhite() != turn){
                return false;
            }
        }if(baris - 1 >= 0 && kolom - 2  >= 0){
            if(papan[baris - 1][kolom - 2].getBidak().getValue() == 3 && papan[baris - 1][kolom - 2].getBidak().isWhite() != turn){
                return false;
            }
        }if(baris + 1 <= 7 && kolom - 2  >= 0){
            if(papan[baris + 1][kolom - 2].getBidak().getValue() == 3 && papan[baris + 1][kolom - 2].getBidak().isWhite() != turn){
                return false;
            }
        }if(baris + 1 <= 7 && kolom + 2  <= 3){
            if(papan[baris + 1][kolom + 2].getBidak().getValue() == 3 && papan[baris + 1][kolom + 2].getBidak().isWhite() != turn){
                return false;
            }
        }
        //queen
        for (int i = baris; i < 8; i++) {
            if(papan[i][kolom].getBidak().getValue() == 4 && papan[i][kolom].getBidak().isWhite() != turn){
                return false;
            }
            else if(papan[i][kolom].getBidak().getValue() != 0 && papan[i][kolom].getBidak().isWhite() == turn) {
                if(papan[i][kolom].getBidak().getValue() != 5)break;
            }
        }
        for (int i = baris; i >= 0; i--) {
            if(papan[i][kolom].getBidak().getValue() == 4 && papan[i][kolom].getBidak().isWhite() != turn){
                return false;
            }
            else if(papan[i][kolom].getBidak().getValue() != 0 && papan[i][kolom].getBidak().isWhite() == turn) {
                if(papan[i][kolom].getBidak().getValue() != 5)break;
            }
        }
        for (int i = kolom; i < 4; i++) {
            if(papan[baris][i].getBidak().getValue() == 4 && papan[baris][i].getBidak().isWhite() != turn){
                return false;
            }
            else if(papan[baris][i].getBidak().getValue() != 0 && papan[baris][i].getBidak().isWhite() == turn) {
                if(papan[baris][i].getBidak().getValue() != 5)break;
            }
        }

        for (int i = kolom; i >= 0; i--) {
            if(papan[baris][i].getBidak().getValue() == 4 && papan[baris][i].getBidak().isWhite() != turn){
                return false;
            }
            else if(papan[baris][i].getBidak().getValue() != 0 && papan[baris][i].getBidak().isWhite() == turn) {
                if(papan[baris][i].getBidak().getValue() != 5)break;
            }
        }

        //king
        for (int i = baris-1; i < baris+2; i++) {
            for (int j = kolom-1; j < kolom+2; j++) {
                if(i <= 7 && i >= 0 && j <= 3 && j >= 0 && (baris != i || kolom != j)){
                    if(papan[i][j].getBidak().getValue() == 5 && papan[i][j].getBidak().isWhite() != turn){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    //move untuk computer gerak
    void move(int iawal, int jawal, int iakhir, int jakhir){
        papan[iawal][jawal].letak.performClick();
        papan[iakhir][jakhir].letak.performClick();
    }
}