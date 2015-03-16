package com.example.admin.models;

import java.io.Serializable;

/**
 * Created by Kyle on 2/23/15.
 */
public class Card implements Serializable {

    String frontStr;
    String backStr;
    int deckID;
    boolean isRight;
    String parseID;

    public Card() {
    }

    public Card(String frontStr, String backStr, int deckID) {
        this.frontStr = frontStr;
        this.backStr = backStr;
        this.deckID = deckID;
        isRight = false;
    }

    public int getDeckID()
    {
        return this.deckID;
    }

    public void setDeckID(int deckID)
    {
        this.deckID = deckID;
    }

    public String getFrontStr()
    {
        return this.frontStr;
    }

    public void setFrontStr(String frontStr)
    {
        this.frontStr = frontStr;
    }

    public String getBackStr()
    {
        return this.backStr;
    }

    public void setBackStr(String backStr)
    {
        this.backStr = backStr;
    }

    public String getParseID() { return this.parseID; }

    public void setParseID(String parseID) { this.parseID = parseID; }

    public boolean getIsRight()
    {
        return this.isRight;
    }

    public void setIsRight(boolean flag)
    {
        this.isRight = flag;
    }

    public String toString()
    {
        return this.frontStr + "   -   " + this.backStr;
    }

}
