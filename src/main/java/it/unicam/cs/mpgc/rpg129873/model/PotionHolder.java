package it.unicam.cs.mpgc.rpg129873.model;

/**
 * Contratto di chi porta pozioni di cura. È separata da {@link Hero} perché non tutti
 * gli eroi devono averle: {@code Game} richiede un eroe che sia anche {@code PotionHolder}.
 */

public interface PotionHolder {

    /**
     * @return {@code true} se la pozione è stata bevuta; {@code false} se non ce ne sono
     * più o la vita è già piena, e in quel caso non viene consumata
     */
    boolean usePotion();

    int getPotionsCount();

    int getMaxPotions();
}
