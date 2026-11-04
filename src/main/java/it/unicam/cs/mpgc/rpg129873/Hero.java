package it.unicam.cs.mpgc.rpg129873;

public class Hero extends Entity {
    private Weapon currentWeapon;
    private boolean ScattoRapidoUnlocked = false;
    private boolean CuraDeiTemplariUnlocked = false;
    private boolean FuriaShimmerUnlocked = false;

    private int countTurni = 0;
    private int shimmerCount = 0;
    public Hero(String id, String name, int maxHp,int attackPower){
        super(id, name, maxHp,attackPower);
    }

    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    public void setWeapon(Weapon weapon){
        this.currentWeapon = weapon;
    }

    public void unlockScattoRapido (){
        this.ScattoRapidoUnlocked = true;
    }
    public void unlockCuraDeiTemplari(){
        this.CuraDeiTemplariUnlocked = true;
    }
    public void unlockFuriaShimmer(){
        this.FuriaShimmerUnlocked = true;
    }

    public boolean hasFuriaShimmer(){
        return FuriaShimmerUnlocked;
    }

    public void onTurnStart(){
        countTurni++;

        if (CuraDeiTemplariUnlocked && countTurni % 3 == 0){
            heal(20);
            System.out.println("Hai rigenerato 20 punti vita.");
        }
    }

    @Override
    public int getAttackPower(){
        int basePower = super.getAttackPower();
        int totalDamage = basePower;
        if (currentWeapon != null){
            totalDamage+= currentWeapon.getDanno();
        }

        if (ScattoRapidoUnlocked && countTurni % 2 == 0){
            System.out.println("Usa scatto rapido (+5 danno)");
            totalDamage += 5;
        }

        return totalDamage;
    }

    public boolean executeFuriaShimmer(Enemy target){
        if (!FuriaShimmerUnlocked){
            System.out.println("La Furia Shimmer non è ancora sbloccata.");
            return false;
        }

        System.out.println("FURIA SHIMMER");
        target.takeDamage(getAttackPower());
        target.takeDamage(getAttackPower());
        shimmerCount++;

        if (shimmerCount % 3 == 0){
            takeDamage(5);
            System.out.println("L'uso della Furia Shimmer ti consuma la vita di 5 HP.");
        }
        return true;
    }

}

