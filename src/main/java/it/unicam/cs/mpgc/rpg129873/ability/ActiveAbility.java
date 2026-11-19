package it.unicam.cs.mpgc.rpg129873.ability;

import it.unicam.cs.mpgc.rpg129873.model.Entity;
import it.unicam.cs.mpgc.rpg129873.model.Hero;

/**
 * Abilità che il giocatore attiva premendo il pulsante dedicato. Usarla consuma il turno
 * come un attacco. L'eroe ne ha una sola alla volta.
 */

public interface ActiveAbility {
    String getName();

    /**
     * Applica l'effetto dell'abilità. {@code Game} ricava danno e contraccolpo confrontando
     * la vita di bersaglio e proprietario prima e dopo la chiamata, quindi l'effetto deve
     * passare da {@code takeDamage} e {@code heal} e non modificare la vita in altro modo.
     * Le implementazioni possono tenere contatori propri; non vengono salvati.
     */
    void use(Hero owner, Entity target);
}
