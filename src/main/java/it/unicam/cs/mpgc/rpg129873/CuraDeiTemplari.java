package it.unicam.cs.mpgc.rpg129873;

public class CuraDeiTemplari implements PassiveAbility{
    @Override
    public String getName(){
        return "Cura dei Templari";
    }

    @Override
    public void onTurnStart(Hero owner, int turn){
        if (turn % 3 == 0){
            owner.heal(15);
        }
    }
}
