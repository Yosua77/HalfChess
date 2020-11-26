package com.example.halfchess;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

public class Papan {
    private Bidak bidak;
    ImageView letak;
    private int status;
    boolean untouched;
    boolean enPassant;
    private boolean pressed;
    private String color;

    public Papan( Bidak bidak,ImageView letak, String color) {
        this.bidak = bidak;
        this.letak = letak;
        pressed = false;
        status = 0;
        enPassant = false;
        untouched = true;
        this.color = color;
        letak.setBackgroundColor(Color.parseColor(this.color));
        updatePapan();
    }

    public Bidak getBidak() {
        return bidak;
    }

    public void setBidak(Bidak bidak) {
        this.bidak = bidak;
        this.status = 0;
        this.pressed = false;
        updatePapan();
    }

    public void updatePapan(){
        if(!pressed){
            if(bidak.getValue() == 1) {
                if(bidak.isWhite()) letak.setImageResource(R.drawable.ic_white_pawn);
                else letak.setImageResource(R.drawable.ic_black_pawn);
            }else if(bidak.getValue() == 2) {
                if(bidak.isWhite()) letak.setImageResource(R.drawable.ic_white_bishop);
                else letak.setImageResource(R.drawable.ic_black_bishop);
            }else if(bidak.getValue() == 3) {
                if(bidak.isWhite()) letak.setImageResource(R.drawable.ic_white_knight);
                else letak.setImageResource(R.drawable.ic_black_knight);
            }else if(bidak.getValue() == 4) {
                if(bidak.isWhite()) letak.setImageResource(R.drawable.ic_white_queen);
                else letak.setImageResource(R.drawable.ic_black_queen);
            }else if(bidak.getValue() == 5) {
                if(bidak.isWhite()) letak.setImageResource(R.drawable.ic_white_king);
                else letak.setImageResource(R.drawable.ic_black_king);
            }else{
                letak.setImageResource(0);
            }
        }else{
            if(bidak.getValue() == 1) {
                if(bidak.isWhite()) letak.setImageResource(R.drawable.ic_white_pawn_selected);
                else letak.setImageResource(R.drawable.ic_black_pawn_selected);
            }else if(bidak.getValue() == 2) {
                if(bidak.isWhite()) letak.setImageResource(R.drawable.ic_white_bishop_selected);
                else letak.setImageResource(R.drawable.ic_black_bishop_selected);
            }else if(bidak.getValue() == 3) {
                if(bidak.isWhite()) letak.setImageResource(R.drawable.ic_white_knight_selected);
                else letak.setImageResource(R.drawable.ic_black_knight_selected);
            }else if(bidak.getValue() == 4) {
                if(bidak.isWhite()) letak.setImageResource(R.drawable.ic_white_queen_selected);
                else letak.setImageResource(R.drawable.ic_black_queen_selected);
            }else if(bidak.getValue() == 5) {
                if(bidak.isWhite()) letak.setImageResource(R.drawable.ic_white_king_selected);
                else letak.setImageResource(R.drawable.ic_black_king_selected);
            }else{
                letak.setImageResource(0);
            }
        }
        if(status != 0){
            if(color.equals("#D9E1F6"))
                letak.setBackgroundColor(Color.parseColor("#5ca08e"));
            else
                letak.setBackgroundColor(Color.parseColor("#8abaae"));
        }else{
            letak.setBackgroundColor(Color.parseColor(color));
        }

    }

    public void pressed(){
        if(!pressed) pressed = true;
        else pressed = false;
        updatePapan();
    }

    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
        updatePapan();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
        updatePapan();
    }
}
