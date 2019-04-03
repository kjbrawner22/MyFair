package com.example.myfair.db;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * NOT CURRENTLY IN USE ANYWHERE IN CODE
 * DELETE IN FUTURE
 */
public class CardList {
    private static final String TAG = "CardListLog";
    private ArrayList<Card> list;

    public CardList(){
        list = new ArrayList<>();
    }

    public CardList(ArrayList<Card> cardList){
        list = cardList;
    }

    public void add(Card c){
        list.add(c);
    }

    public int size(){
        return list.size();
    }

    public Card getByIndex(int index){
        if(list.isEmpty())
            return null;
        else if(index < 0 || index >= list.size())
            return null;
        return list.get(index);
    }

    public Card getById(String id){
        for (Card c: list){
            if(c.getId().equals(id)) return c;
        }
        return null;
    }

    public Card setById(String id, HashMap<String, Object> map){
        for (Card c: list){
            if(c.getId().equals(id)){
                c.setMap(map);
                return c;
            }
        }
        return null;
    }

    public void displayIDs(){
        StringBuilder stringBuilder = new StringBuilder();
        for (Card c: list){
            stringBuilder.append(c.getId()).append(" ");
        }
        Log.d(TAG, stringBuilder.toString());
    }

    public void displayWithContents(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n");
        for (Card c: list){
            stringBuilder.append(c.getId()).append(" => ").append(c.display()).append("\n");
        }
        Log.d(TAG, stringBuilder.toString());
    }

}
