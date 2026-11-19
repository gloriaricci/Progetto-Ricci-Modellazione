package it.unicam.cs.mpgc.rpg129873.model;

/**
 * Base comune di chiunque combatta (eroe e nemici): identificatore, nome, punti vita
 * e potenza d'attacco di base. Gestisce i punti vita in modo che restino sempre tra
 * 0 e il massimo. Le sottoclassi aggiungono le regole specifiche (arma e abilità per
 * l'eroe, attacco e effetti speciali per i nemici).
 * <p>
 * L'id identifica l'entità anche per la vista, che lo usa per scegliere lo sprite:
 * i valori come {@code "boss1"} o {@code "nox"} devono quindi coincidere con le
 * chiavi di {@code sprites.json}.
 */

public abstract class Entity {
    private final String id;
    private final String name;
    private int maxHp;
    private int currentHp;
    private final int attackPower;

    public Entity(String id, String name, int maxHp, int attackPower) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("The entity id cannot be null or blank");
        }

        if (maxHp < 1) {
            throw new IllegalArgumentException("The max hp must be at least 1");
        }

        this.id = id;
        this.name = name != null ? name : "Unknown entity";
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.attackPower = attackPower;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMaxHp() {
        return maxHp;
    }

    /**
     * Cambia la vita massima senza curare: la vita attuale viene solo ridotta se supera
     * il nuovo massimo. Chi vuole la vita piena (per esempio al cambio di livello)
     * deve chiamare anche {@link #setCurrentHp}.
     */
    public void setMaxHp(int maxHp) {
        if (maxHp < 1) {
            throw new IllegalArgumentException("The max hp must be at least 1");
        }
        this.maxHp = maxHp;
        this.currentHp = Math.min(this.currentHp, maxHp);
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public void setCurrentHp(int currentHp) {
        this.currentHp = Math.max(0, Math.min(currentHp, this.maxHp));
    }

    public int getAttackPower() {
        return attackPower;
    }

    public void takeDamage(int damage) {
        if (damage < 0) {
            return;
        }
        this.currentHp = Math.max(0, this.currentHp - damage);
    }

    public void heal(int amount) {
        if (amount < 0) {
            return;
        }
        this.currentHp = Math.min(this.maxHp, this.currentHp + amount);
    }

    public boolean isAlive() {
        return this.currentHp > 0;
    }

}

