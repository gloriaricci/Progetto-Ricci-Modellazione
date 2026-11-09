package it.unicam.cs.mpgc.rpg129873.controller;

public class ConsoleEventListener implements GameEventListener{
    @Override
    public void onMessage(String message){
        System.out.println(message);
    }
}
