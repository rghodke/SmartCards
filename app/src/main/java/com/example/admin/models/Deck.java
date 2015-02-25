package com.example.admin.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Kyle on 2/23/15.
 */
public class Deck implements Serializable {

    String course;
    String deckName;
    ArrayList<Card> cardArray = new ArrayList();

    public Deck()
    {
    }

    public Deck(String course, String deckName)
    {
        this.course = course;
        this.deckName = deckName;
    }

    public String getCourse()
    {
        return this.course;
    }

    public void setCourse(String course)
    {
        this.course = course;
    }

    public String getDeckName()
    {
        return this.deckName;
    }

    public void setDeckName(String deckName)
    {
        this.deckName = deckName;
    }

    public void addCard(Card card)
    {
        this.cardArray.add(card);
    }

    public int getArraySize()
    {
        return this.cardArray.size();
    }

    public ArrayList<Card> getCardArray()
    {
        return this.cardArray;
    }

    public void setCardArray(ArrayList<Card> cardList) { this.cardArray = cardList; }
}
