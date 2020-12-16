package com.example.halfchess;

import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Random;

import javax.security.auth.login.LoginException;

public class Computer {
    private int depth = 1;
    private int numTurns, diff = 0;
    private ImageView biargkerror;


    public Computer(int diff, ImageView biargkerror) {
        this.diff = diff;
        if(diff== 1) depth = 0;
        numTurns = 0;
        this.biargkerror = biargkerror;
    }
    ArrayList<Papan[][]> possiblePapan = new ArrayList<>();
    ArrayList<int[]> possibleMoves = new ArrayList<>();

    int[] gerak(Papan[][] papan){
        ArrayList<int[]> bestMoves = new ArrayList<>();
        int bestScore;
        possibleMoves.clear();
        possiblePapan.clear();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 4; j++) {
                if(papan[i][j].getBidak().getValue() != 0){
                    if(!papan[i][j].getBidak().isWhite()){
                        for (int k = 0; k < 8; k++) {
                            for (int l = 0; l < 4; l++) {
                                if(isLegal(i,j,k,l,papan, false)){
                                    int[] posisi = new int[7];
                                    posisi[0] = papan[i][j].getBidak().getValue();
                                    posisi[1] = i;
                                    posisi[2] = j;
                                    posisi[3] = k;
                                    posisi[4] = l;
                                    posisi[5] = 0;
                                    posisi[6] = 0;
                                    possibleMoves.add(posisi);
                                    possiblePapan.add(getPapan(k,l,papan,i,j));
                                }
                            }
                        }
                    }
                }
            }
        }

        bestMoves.add(possibleMoves.get(0));
        bestScore = sbe(possiblePapan.get(0), Integer.MIN_VALUE,Integer.MAX_VALUE,depth,true);
        bestMoves.get(bestMoves.size()-1)[6] = bestScore;

        if(numTurns>0){
            for (int i = 1; i < possiblePapan.size(); i++) {
                int sbeScore = sbe(possiblePapan.get(i),Integer.MIN_VALUE,Integer.MAX_VALUE,depth,true);
                if(sbeScore > bestScore){
                    bestMoves.clear();
                    bestMoves.add(possibleMoves.get(i));
                    bestScore = sbeScore;
                    bestMoves.get(bestMoves.size()-1)[6] = bestScore;
                }else if(sbeScore == bestScore){
                    bestMoves.add(possibleMoves.get(i));
                    bestMoves.get(bestMoves.size()-1)[6] = bestScore;
                }
            }
        }else{
            bestMoves.clear();
            bestMoves.add(possibleMoves.get(new Random().nextInt(possibleMoves.size())));
        }
        numTurns++;
        Log.i("best",bestMoves.size()+"");
        for (int i = 0; i < bestMoves.size(); i++) {
            Log.i("bestMoves_"+i+"Bidak",bestMoves.get(i)[0]+"");
            Log.i("bestMoves_"+i+"iAwal",bestMoves.get(i)[1]+"");
            Log.i("bestMoves_"+i+"jAwal",bestMoves.get(i)[2]+"");
            Log.i("bestMoves_"+i+"iAkhir",bestMoves.get(i)[3]+"");
            Log.i("bestMoves_"+i+"jAkhir",bestMoves.get(i)[4]+"");
            Log.i("bestMoves_"+i+"Value",bestMoves.get(i)[6]+"");
        }
        int random = new Random().nextInt(bestMoves.size());
        int percent = new Random().nextInt(10);
        while (percent <= 3 && bestMoves.get(random)[0] == 5){
            random = new Random().nextInt(bestMoves.size());
            percent = new Random().nextInt(10);
        }
        Log.i("random",random+"");
        return bestMoves.get(random);
    }

    boolean isValid(int i, int j,Papan[][] papan, int baris, int kolom, boolean turn){
        Papan[][] tempPapan = new Papan[8][4];
        for (int k = 0; k < 8; k++) {
            for (int l = 0; l < 4; l++) {
                tempPapan[k][l] = new Papan(new Bidak(papan[k][l].getBidak().getValue(),papan[k][l].getBidak().isWhite()),biargkerror,"#FFFFFF");
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
        boolean nemu = false;
        for (int k = 0; k < 8; k++) {
            for (int l = 0; l < 4; l++) {
                if(tempPapan[k][l].getBidak().getValue() == 5 && tempPapan[k][l].getBidak().isWhite() == turn){
                    if(!isSave(k,l,tempPapan,turn)){
//                        Toast.makeText(this, "Invalid Move", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    nemu = true;
                    break;
                }
            }
            if(nemu) break;
        }

        return true;

    }

    boolean isSave(int baris, int kolom,Papan[][] papan,boolean turn){
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
            if((papan[i][j].getBidak().getValue() == 2 || papan[i][j].getBidak().getValue() == 4) && papan[i][j].getBidak().isWhite() != papan[baris][kolom].getBidak().isWhite()){
                return false;
            }else if(papan[i][j].getBidak().getValue() != 0){
                if(papan[i][j].getBidak().getValue() != 5)break;
            }
        }
        //kanan atas
        for (int i = baris - 1,j = kolom + 1; i >= 0 && j < 4; i--, j++) {
            if((papan[i][j].getBidak().getValue() == 2 || papan[i][j].getBidak().getValue() == 4) && papan[i][j].getBidak().isWhite() != turn){
                return false;
            }else if(papan[i][j].getBidak().getValue() != 0){
                if(papan[i][j].getBidak().getValue() != 5)break;
            }
        }
        //kiri atas
        for (int i = baris - 1,j = kolom - 1; i >= 0 && j >= 0; i--, j--) {
            if((papan[i][j].getBidak().getValue() == 2 || papan[i][j].getBidak().getValue() == 4) && papan[i][j].getBidak().isWhite() != turn){
                return false;
            }else if(papan[i][j].getBidak().getValue() != 0) {
                if(papan[i][j].getBidak().getValue() != 5)break;
            }
        }
        //kiri bawah
        for (int i = baris + 1,j = kolom - 1; i < 8 && j >= 0; i++, j--) {
            if((papan[i][j].getBidak().getValue() == 2 || papan[i][j].getBidak().getValue() == 4) && papan[i][j].getBidak().isWhite() != turn){
                return false;
            }else if(papan[i][j].getBidak().getValue() != 0) {
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
            else if(papan[i][kolom].getBidak().getValue() != 0) {
                if(papan[i][kolom].getBidak().getValue() != 5) break;
            }
        }
        for (int i = baris; i >= 0; i--) {
            if(papan[i][kolom].getBidak().getValue() == 4 && papan[i][kolom].getBidak().isWhite() != turn){
                return false;
            }
            else if(papan[i][kolom].getBidak().getValue() != 0 ) {
                if(papan[i][kolom].getBidak().getValue() != 5) break;
            }
        }
        for (int i = kolom; i < 4; i++) {
            if(papan[baris][i].getBidak().getValue() == 4 && papan[baris][i].getBidak().isWhite() != turn){
                return false;
            }
            else if(papan[baris][i].getBidak().getValue() != 0) {
                if(papan[baris][i].getBidak().getValue() != 5) break;
            }
        }

        for (int i = kolom; i >= 0; i--) {
            if(papan[baris][i].getBidak().getValue() == 4 && papan[baris][i].getBidak().isWhite() != turn){
                return false;
            }
            else if(papan[baris][i].getBidak().getValue() != 0) {
                if(papan[baris][i].getBidak().getValue() != 5) break;
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

    boolean isLegal(int iAsal, int jAsal, int iTujuan, int jTujuan, Papan[][] papan, boolean turn){
        if(papan[iAsal][jAsal].getBidak().getValue() == 5){
            //king
            if(Math.abs(iAsal-iTujuan) <=1 && Math.abs(jAsal-jTujuan) <= 1){
                if(papan[iTujuan][jTujuan].getBidak().getValue() == 0){
                    if(isValid(iTujuan,jTujuan,papan,iAsal,jAsal, turn)) return true;
                }
                else if(papan[iTujuan][jTujuan].getBidak().isWhite() != papan[iAsal][jAsal].getBidak().isWhite()){
                    if(isValid(iTujuan,jTujuan,papan,iAsal,jAsal, turn)) return true;
                }
            }
        }
        else if(papan[iAsal][jAsal].getBidak().getValue() == 4){
            //queen
            int iTemp = iAsal, jTemp = jAsal;
            if(Math.abs(iAsal-iTujuan) == Math.abs(jAsal - jTujuan)){
                if(!(iTemp == iTujuan && jTemp == jTujuan)){
                    if(iTemp>iTujuan){
                        iTemp--;
                    }else if(iTemp<iTujuan){
                        iTemp++;
                    }if(jTemp>jTujuan){
                        jTemp--;
                    }else if(jTemp<jTujuan){
                        jTemp++;
                    }
                }

                do{
                    if(papan[iTemp][jTemp].getBidak().getValue() != 0 && !(iTemp == iTujuan && jTemp == jTujuan)) return false;
                    if(!(iTemp == iTujuan && jTemp == jTujuan)){
                        if(iTemp>iTujuan){
                            iTemp--;
                        }else if(iTemp<iTujuan){
                            iTemp++;
                        }if(jTemp>jTujuan){
                            jTemp--;
                        }else if(jTemp<jTujuan){
                            jTemp++;
                        }
                    }
                }while(!(iTemp == iTujuan && jTemp == jTujuan));
                if(papan[iTujuan][jTujuan].getBidak().getValue() == 0){
                    if(isValid(iTujuan,jTujuan,papan,iAsal,jAsal, turn)) return true;
                }
                else if(papan[iTujuan][jTujuan].getBidak().isWhite() != papan[iAsal][jAsal].getBidak().isWhite()){
                    if(isValid(iTujuan,jTujuan,papan,iAsal,jAsal, turn)) return true;
                }
            }
            else if(iAsal == iTujuan || jAsal == jTujuan){
                if(!(iTemp == iTujuan && jTemp == jTujuan)){
                    if(iTemp>iTujuan){
                        iTemp--;
                    }else if(iTemp<iTujuan){
                        iTemp++;
                    }if(jTemp>jTujuan){
                        jTemp--;
                    }else if(jTemp<jTujuan){
                        jTemp++;
                    }
                }

                do{
                    if(papan[iTemp][jTemp].getBidak().getValue() != 0 && !(iTemp == iTujuan && jTemp == jTujuan)) return false;
                    if(!(iTemp == iTujuan && jTemp == jTujuan)){
                        if(iTemp>iTujuan){
                            iTemp--;
                        }else if(iTemp<iTujuan){
                            iTemp++;
                        }if(jTemp>jTujuan){
                            jTemp--;
                        }else if(jTemp<jTujuan){
                            jTemp++;
                        }
                    }

                }while(!(iTemp == iTujuan && jTemp == jTujuan));

                if(papan[iTujuan][jTujuan].getBidak().getValue() == 0){
                    if(isValid(iTujuan,jTujuan,papan,iAsal,jAsal, turn)) return true;
                }
                if(papan[iTujuan][jTujuan].getBidak().getValue() != 0 && papan[iTujuan][jTujuan].getBidak().isWhite() != papan[iAsal][jAsal].getBidak().isWhite()){
                    if(isValid(iTujuan,jTujuan,papan,iAsal,jAsal, turn)) return true;
                }
            }
        }
        else if(papan[iAsal][jAsal].getBidak().getValue() == 3){
            //knight
            if(Math.abs(iAsal-iTujuan) + Math.abs(jTujuan - jAsal) == 3 && iAsal != iTujuan && jAsal != jTujuan){
                if(papan[iTujuan][jTujuan].getBidak().getValue() == 0){
                    if(isValid(iTujuan,jTujuan,papan,iAsal,jAsal, turn)) return true;
                }
                else if(papan[iTujuan][jTujuan].getBidak().isWhite() != papan[iAsal][jAsal].getBidak().isWhite()){
                    if(isValid(iTujuan,jTujuan,papan,iAsal,jAsal, turn)) return true;
                }
            }
        }
        else if(papan[iAsal][jAsal].getBidak().getValue() == 2){
            int iTemp = iAsal, jTemp = jAsal;
            if(Math.abs(iAsal-iTujuan) == Math.abs(jAsal - jTujuan)){
                if(!(iTemp == iTujuan && jTemp == jTujuan)){
                    if(iTemp>iTujuan){
                        iTemp--;
                    }else if(iTemp<iTujuan){
                        iTemp++;
                    }if(jTemp>jTujuan){
                        jTemp--;
                    }else if(jTemp<jTujuan){
                        jTemp++;
                    }
                }

                do{
                    if(papan[iTemp][jTemp].getBidak().getValue() != 0 && !(iTemp == iTujuan && jTemp == jTujuan)) return false;
                    if(!(iTemp == iTujuan && jTemp == jTujuan)){
                        if(iTemp>iTujuan){
                            iTemp--;
                        }else if(iTemp<iTujuan){
                            iTemp++;
                        }if(jTemp>jTujuan){
                            jTemp--;
                        }else if(jTemp<jTujuan){
                            jTemp++;
                        }
                    }
                }while(!(iTemp == iTujuan && jTemp == jTujuan));
                if(papan[iTujuan][jTujuan].getBidak().getValue() == 0){
                    if(isValid(iTujuan,jTujuan,papan,iAsal,jAsal, turn)) return true;
                }
                else if(papan[iTujuan][jTujuan].getBidak().isWhite() != papan[iAsal][jAsal].getBidak().isWhite()){
                    if(isValid(iTujuan,jTujuan,papan,iAsal,jAsal, turn)) return true;
                }
            }
        }
        else if(papan[iAsal][jAsal].getBidak().getValue() == 1){
            if(!papan[iAsal][jAsal].getBidak().isWhite()){
                if(iAsal<iTujuan){
                    if(jAsal == jTujuan){
                        if(Math.abs(iAsal - iTujuan) == 1){
                            if(papan[iTujuan][jTujuan].getBidak().getValue() == 0) if(isValid(iTujuan,jTujuan,papan,iAsal,jAsal, turn)) return true;
                        }else if(Math.abs(iAsal - iTujuan) == 2 && papan[iAsal][jAsal].untouched){
                            if(papan[iTujuan][jTujuan].getBidak().getValue() == 0 && iTujuan <= 6 && papan[iTujuan+1][jTujuan].getBidak().getValue() == 0) if(isValid(iTujuan,jTujuan,papan,iAsal,jAsal, turn)) return true;
                        }
                    }else if(Math.abs(jAsal-jTujuan) == 1){
                        if(Math.abs(iAsal - iTujuan) == 1){
                            if(papan[iTujuan][jTujuan].getBidak().isWhite() != papan[iAsal][jAsal].getBidak().isWhite()) if(isValid(iTujuan,jTujuan,papan,iAsal,jAsal, turn)) return true;
                            if(papan[iTujuan-1][jTujuan].enPassant) return true;
                        }
                    }
                }
            }else{
                if(iAsal>iTujuan){
                    if(jAsal == jTujuan){
                        if(Math.abs(iAsal - iTujuan) == 1){
                            if(papan[iTujuan][jTujuan].getBidak().getValue() == 0) if(isValid(iTujuan,jTujuan,papan,iAsal,jAsal, turn)) return true;
                        }else if(Math.abs(iAsal - iTujuan) == 2 && papan[iAsal][jAsal].untouched){
                            if(papan[iTujuan][jTujuan].getBidak().getValue() == 0 && iTujuan >= 1 && papan[iTujuan-1][jTujuan].getBidak().getValue() == 0) if(isValid(iTujuan,jTujuan,papan,iAsal,jAsal, turn)) return true;
                        }
                    }else if(Math.abs(jAsal-jTujuan) == 1){
                        if(Math.abs(iAsal - iTujuan) == 1){
                            if(papan[iTujuan][jTujuan].getBidak().isWhite() != papan[iAsal][jAsal].getBidak().isWhite()) if(isValid(iTujuan,jTujuan,papan,iAsal,jAsal, turn)) return true;
                            if(papan[iTujuan+1][jTujuan].enPassant) return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    Papan[][] getPapan(int i, int j, Papan[][] papan, int baris, int kolom){
        Papan[][] tempPapan = new Papan[8][4];
        for (int k = 0; k < 8; k++) {
            for (int l = 0; l < 4; l++) {
                tempPapan[k][l] = new Papan(new Bidak(papan[k][l].getBidak().getValue(),papan[k][l].getBidak().isWhite()),biargkerror,"#FFFFFF");
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
        return tempPapan;
    }

    int sbe(Papan[][] papan,int alpha, int beta, int depth, boolean turn){
        if(depth == 0){
            int evaluation = eval(papan);
            return evaluation;
        }
        if(turn){
            ArrayList<int[]> moves = new ArrayList<>();

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 4; j++) {
                    if(papan[i][j].getBidak().getValue() != 0){
                        if(papan[i][j].getBidak().isWhite()){
                            for (int k = 0; k < 8; k++) {
                                for (int l = 0; l < 4; l++) {
////                                    cek apakah valid ke kotak yang dituju
                                    if(isLegal(i,j,k,l,papan,true)){
                                        int[] posisi = new int[6];
                                        //bidak,posawal,posakhir,isWhite
                                        posisi[0] = papan[i][j].getBidak().getValue();
                                        posisi[1] = i;
                                        posisi[2] = j;
                                        posisi[3] = k;
                                        posisi[4] = l;
                                        posisi[5] = 1;
                                        moves.add(posisi);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            int newBeta = beta;
            for (int[] item:moves) {
                Papan[][] successorPapan = getPapan(item[3],item[4],papan,item[1],item[2]);
                newBeta = Math.min(newBeta,sbe(successorPapan,alpha,beta,depth-1,!turn));
                if(newBeta <= alpha) break;
            }
            return newBeta;

        }else{
            ArrayList<int[]> moves = new ArrayList<>();

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 4; j++) {
                    if(papan[i][j].getBidak().getValue() != 0){
                        if(!papan[i][j].getBidak().isWhite()){
                            for (int k = 0; k < 8; k++) {
                                for (int l = 0; l < 4; l++) {
//                                    cek apakah valid ke kotak yang dituju
                                    if(isLegal(i,j,k,l,papan, false)){
                                        int[] posisi = new int[6];
                                        //bidak,posawal,posakhir,isWhite
                                        posisi[0] = papan[i][j].getBidak().getValue();
                                        posisi[1] = i;
                                        posisi[2] = j;
                                        posisi[3] = k;
                                        posisi[4] = l;
                                        posisi[5] = 0;
                                        moves.add(posisi);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            int newAlpha = alpha;
            for (int[] item:moves) {
                Papan[][] successorPapan = getPapan(item[3],item[4],papan,item[1],item[2]);
                newAlpha = Math.min(newAlpha,sbe(successorPapan,alpha,beta,depth-1,!turn));
                if(newAlpha >= beta) break;
            }
            return newAlpha;
        }
    }

    int eval(Papan[][] papan){
        int scoreWhite = 0, scoreBlack = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 4; j++) {
                if(papan[i][j].getBidak().getValue() != 0){
                    if(papan[i][j].getBidak().isWhite()){
                        if(papan[i][j].getBidak().getValue() == 5){
                            scoreWhite += 10000000;
                            if(diff == 3){
                                if(isSave(i,j,papan,true)) scoreWhite-= 20000000;
                            }
                        }else if(papan[i][j].getBidak().getValue() == 4){
                            if(diff == 1) scoreWhite+= 1;
                            else scoreWhite += 9;
                        }else if(papan[i][j].getBidak().getValue() == 3){
                            if(diff == 1) scoreWhite+= 1;
                            else scoreWhite += 3;
                        }else if(papan[i][j].getBidak().getValue() == 2){
                            if(diff == 1) scoreWhite+= 1;
                            else scoreWhite += 3;
                        }else if(papan[i][j].getBidak().getValue() == 1){
                            if(diff == 1) scoreWhite+= 1;
                            else scoreWhite += 1;
                        }
                    }else if(!papan[i][j].getBidak().isWhite()){
                        if(papan[i][j].getBidak().getValue() == 5){
                            scoreBlack += 10000000;
                            if(diff == 3){
                                if(isSave(i,j,papan,false)) scoreBlack-= 20000000;
                            }
                        }else if(papan[i][j].getBidak().getValue() == 4){
                            if(diff == 1) scoreBlack+= 1;
                            else scoreBlack += 9;
                        }else if(papan[i][j].getBidak().getValue() == 3){
                            if(diff == 1) scoreBlack+= 1;
                            else scoreBlack += 3;
                        }else if(papan[i][j].getBidak().getValue() == 2){
                            if(diff == 1) scoreBlack+= 1;
                            else scoreBlack += 3;
                        }else if(papan[i][j].getBidak().getValue() == 1){
                            if(diff == 1) scoreBlack+= 1;
                            else scoreBlack += 1;
                        }
                    }
                }
            }
        }
        return scoreBlack - scoreWhite;
    }

}
