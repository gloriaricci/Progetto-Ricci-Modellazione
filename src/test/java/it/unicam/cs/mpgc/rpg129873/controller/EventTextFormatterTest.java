package it.unicam.cs.mpgc.rpg129873.controller;

import it.unicam.cs.mpgc.rpg129873.game.GameEvent.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventTextFormatterTest {

    private final EventTextFormatter formatter = new EventTextFormatter();

    @Test
    void attackIsDescribedWithNamesAndDamage() {
        assertEquals("Nox attacca Goblin e infligge 15 danni.",
                formatter.format(new HeroAttacked("Nox", "Goblin", 15)));
    }

    @Test
    void rewardsAreDescribedByKind() {
        assertEquals("Hai ottenuto l'arma: Spada!",
                formatter.format(new RewardGained(RewardKind.WEAPON, "Spada")));
        assertEquals("Hai ottenuto l'abilità attiva: Furia Shimmer!",
                formatter.format(new RewardGained(RewardKind.ACTIVE, "Furia Shimmer")));
    }

    @Test
    void refusalsHaveTheirOwnText() {
        assertEquals("La tua vita è già al massimo.",
                formatter.format(new ActionRefused(RefusalReason.FULL_HP)));
        assertEquals("Non hai più pozioni di cura.",
                formatter.format(new ActionRefused(RefusalReason.NO_POTIONS)));
    }

    @Test
    void levelStartRewardAndArrivalAreAnnouncements() {
        assertTrue(formatter.isAnnouncement(new LevelStarted(1, "Boss")));
        assertTrue(formatter.isAnnouncement(new RewardGained(RewardKind.WEAPON, "Spada")));
        assertTrue(formatter.isAnnouncement(new EnemyArrived("Gemello 2")));
    }

    @Test
    void allOtherEventsStayInTheLogOnly() {
        assertFalse(formatter.isAnnouncement(new EnemyDefeated("Boss")));
        assertFalse(formatter.isAnnouncement(new HeroAttacked("Nox", "Boss", 15)));
        assertFalse(formatter.isAnnouncement(new AbilityUsed("Furia Shimmer", "Boss", 30)));
        assertFalse(formatter.isAnnouncement(new RecoilTaken(5)));
        assertFalse(formatter.isAnnouncement(new HeroHealed(15)));
        assertFalse(formatter.isAnnouncement(new PotionUsed(1)));
        assertFalse(formatter.isAnnouncement(new ActionRefused(RefusalReason.NO_ABILITY)));
        assertFalse(formatter.isAnnouncement(new EnemyAttacked("Boss", 15)));
        assertFalse(formatter.isAnnouncement(new EnemySpecial("Qualcosa")));
        assertFalse(formatter.isAnnouncement(new HeroDefeated()));
        assertFalse(formatter.isAnnouncement(new GameWon()));
    }

    @Test
    void levelStartAnnouncementHasTheLevelAsTitleAndTheBossAsSubtitle() {
        assertEquals(new Announcement("Livello 2", "Madre Brassmonkey"),
                formatter.toAnnouncement(new LevelStarted(2, "Madre Brassmonkey")));
    }

    @Test
    void rewardAndArrivalAnnouncementsHaveNoSubtitle() {
        assertEquals(new Announcement("Hai ottenuto l'arma: Spada!", ""),
                formatter.toAnnouncement(new RewardGained(RewardKind.WEAPON, "Spada")));
        assertEquals(new Announcement("Arriva Gemello 2!", ""),
                formatter.toAnnouncement(new EnemyArrived("Gemello 2")));
    }

    @Test
    void toAnnouncementRejectsEventsThatAreNotAnnouncements() {
        assertThrows(IllegalArgumentException.class,
                () -> formatter.toAnnouncement(new PotionUsed(1)));
    }
}
