package it.unicam.cs.mpgc.rpg129873.game;

/**
 * Decide i punti vita massimi dell'eroe a un dato livello.
 * {@link Game} la usa a ogni cambio di livello (la vita massima sale e
 * viene ripristinata del tutto), {@link GameFactory} per creare o ripristinare
 * l'eroe. Per cambiare il bilanciamento basta fornire un'altra implementazione.
 * Il valore restituito deve essere almeno 1; per livelli minori di 1 ci si
 * aspetta una {@link IllegalArgumentException}.
 */

public interface HealthProgression {
    int maxHpForLevel(int level);
}
