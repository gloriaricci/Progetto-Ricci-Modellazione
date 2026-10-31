package it.unicam.cs.mpgc.rpg129873;

public class BaroneShimmer extends Enemy{

    private int counterTurni = 0;
    private int ricaricoEnergia = 20;

    public BaroneShimmer(){
        super("bossFinale", "Barone dello Shimmer", 300, 35, 1.5, 5, "distorsione temporale");
    }

    @Override
    public void attacca (Nox hero) {
        counterTurni++;

        hero.takeDamage(getAttackPower());

         if (counterTurni % 2 == 0){
             heal(ricaricoEnergia);
             System.out.println("Distorsione temporale!! il barone ricarica " + ricaricoEnergia + "punti vita.");
         }
    }
}

