package it.unicam.cs.mpgc.rpg129873.game;

import it.unicam.cs.mpgc.rpg129873.game.GameEvent.*;
import it.unicam.cs.mpgc.rpg129873.level.Level;
import it.unicam.cs.mpgc.rpg129873.level.LevelProvider;
import it.unicam.cs.mpgc.rpg129873.model.Enemy;
import it.unicam.cs.mpgc.rpg129873.model.Hero;
import it.unicam.cs.mpgc.rpg129873.model.PotionHolder;

import java.util.Objects;

/**
 * Regole di una partita a turni di Nox: tiene l'eroe, il livello corrente e
 * a chi tocca. Il turno si svolge in due tempi: il giocatore agisce
 * ({@link #playerAttack}, {@link #playerUsePotion}, {@link #playerUseActiveAbility}),
 * poi il nemico risponde quando il chiamante invoca {@link #enemyTurn()}.
 * La separazione permette alla vista di inserire pause e animazioni tra le due mosse.
 * <p>
 * Non conosce grafica né salvataggi: comunica solo con eventi
 * ({@link GameEventListener}) e con fotografie dello stato ({@link #status()},
 * {@link #snapshot()}). Non è thread-safe.
 */

public class Game {

    private final Hero player;
    private final PotionHolder potions;
    private final LevelProvider levelProvider;
    private final HealthProgression progression;
    private final GameEventListener listener;
    private Level level;
    private int currentLevelNumber;
    // true dopo un'azione del giocatore che non ha chiuso lo scontro:
    // finché è true il giocatore non può agire e il nemico deve rispondere.
    private boolean enemyTurnPending = false;

    /**
     * Il tipo intersezione richiede un eroe che sia anche portatore di pozioni:
     * lo stesso oggetto viene tenuto come {@code player} (combattimento) e come
     * {@code potions} (pozioni). Il costruttore non emette eventi: vedi {@link #start()}.
     */
    public <T extends Hero & PotionHolder> Game(T player,
                                                LevelProvider levelProvider,
                                                HealthProgression progression,
                                                int startingLevel,
                                                GameEventListener listener) {
        this.player = Objects.requireNonNull(player);
        this.potions = player;
        this.levelProvider = Objects.requireNonNull(levelProvider);
        this.progression = Objects.requireNonNull(progression);
        this.listener = Objects.requireNonNull(listener);
        this.currentLevelNumber = startingLevel;
        this.level = levelProvider.createLevel(startingLevel);
    }

    /**
     * Annuncia il livello corrente. Non è chiamato dal costruttore, così la vista
     * può collegare il listener prima di ricevere il primo evento (anche dopo un restore).
     */
    public void start() {
        announceLevel();
    }

    /**
     * Attacco base. Ignorato se la partita è finita o il nemico deve ancora rispondere.
     * Il danno nell'evento è quello realmente tolto, che può essere minore di quello
     * nominale se al nemico restava poca vita.
     */
    public void playerAttack() {
        if (!canPlayerAct()) {
            return;
        }
        beginPlayerTurn();
        Enemy enemy = getCurrentEnemy();
        int enemyHpBefore = enemy.getCurrentHp();
        player.attack(enemy);
        listener.onEvent(new HeroAttacked(player.getName(), enemy.getName(),
                enemyHpBefore - enemy.getCurrentHp()));
        afterPlayerAction();
    }

    /**
     * Beve una pozione. Se la vita è già piena (controllato per primo) o le pozioni
     * sono finite, emette {@link ActionRefused} e non consuma il turno: il nemico non risponde.
     * È l'unica azione che non chiama {@code beginPlayerTurn}, quindi non fa avanzare
     * il contatore dei turni delle abilità passive.
     */
    public void playerUsePotion() {
        if (!canPlayerAct()) {
            return;
        }
        if (player.getCurrentHp() == player.getMaxHp()) {
            listener.onEvent(new ActionRefused(RefusalReason.FULL_HP));
            return;
        }
        if (!potions.usePotion()) {
            listener.onEvent(new ActionRefused(RefusalReason.NO_POTIONS));
            return;
        }
        listener.onEvent(new PotionUsed(potions.getPotionsCount()));
        afterPlayerAction();
    }

    /**
     * Usa l'abilità attiva. Se non è ancora sbloccata non fa nulla: nessun evento
     * e nessun turno consumato. Il contraccolpo (RecoilTaken) è ricavato dalla
     * differenza di vita dell'eroe prima e dopo l'uso, così {@code Game} non deve
     * conoscere le regole delle singole abilità.
     */
    public void playerUseActiveAbility() {
        if (!canPlayerAct()) {
            return;
        }
        if (!player.hasActiveAbility()) {
            return;
        }
        beginPlayerTurn();
        Enemy enemy = getCurrentEnemy();
        int enemyHpBefore = enemy.getCurrentHp();
        int heroHpBefore = player.getCurrentHp();
        player.useActiveAbility(enemy);
        listener.onEvent(new AbilityUsed(player.getActiveAbility().getName(), enemy.getName(),
                enemyHpBefore - enemy.getCurrentHp()));
        int recoil = heroHpBefore - player.getCurrentHp();
        if (recoil > 0) {
            listener.onEvent(new RecoilTaken(recoil));
        }
        afterPlayerAction();
    }

    /**
     * Risposta del nemico. Agisce solo se un'azione del giocatore l'ha lasciata in
     * sospeso, quindi chiamarla più volte è innocuo. Dopo l'attacco legge l'eventuale
     * effetto speciale del nemico ({@code consumeEvent}) e lo notifica come {@link EnemySpecial}.
     */
    public void enemyTurn() {
        if (!enemyTurnPending) {
            return;
        }
        enemyTurnPending = false;
        Enemy enemy = getCurrentEnemy();
        int heroHpBefore = player.getCurrentHp();
        enemy.attack(player);
        listener.onEvent(new EnemyAttacked(enemy.getName(), heroHpBefore - player.getCurrentHp()));
        enemy.consumeEvent().ifPresent(effect ->
                listener.onEvent(new EnemySpecial(enemy.getName(), effect)));
        checkGameState();
    }

    public boolean isEnemyTurnPending() {
        return enemyTurnPending;
    }

    /**
     * Partita finita se l'eroe è morto o se è morto l'ultimo nemico dell'ultimo livello.
     * Negli altri casi {@code checkGameState} ha già sostituito il nemico sconfitto
     * (secondo gemello o livello successivo), quindi il nemico corrente è sempre vivo.
     */
    public boolean isFinished() {
        return !player.isAlive() || !getCurrentEnemy().isAlive();
    }

    public boolean isWon() {
        return isFinished() && player.isAlive();
    }

    public Enemy getCurrentEnemy() {
        return level.getCurrentEnemy();
    }

    public int getCurrentLevelNumber() {
        return currentLevelNumber;
    }

    /** Stato minimo da salvare: vedi {@link GameState} per cosa non include. */
    public GameState snapshot() {
        return new GameState(currentLevelNumber, player.getCurrentHp(), potions.getPotionsCount());
    }

    /**
     * Fotografia di sola lettura per la vista, con valori copiati: la UI legge solo
     * questa e non accede mai direttamente a eroe e nemici.
     */
    public GameStatus status() {
        Enemy enemy = getCurrentEnemy();
        return new GameStatus(
                currentLevelNumber,
                player.getId(),
                player.getName(),
                player.getCurrentHp(),
                player.getMaxHp(),
                potions.getPotionsCount(),
                potions.getMaxPotions(),
                player.hasActiveAbility(),
                enemy.getId(),
                enemy.getName(),
                enemy.getCurrentHp(),
                enemy.getMaxHp(),
                isFinished(),
                isWon());
    }

    private boolean canPlayerAct() {
        return !isFinished() && !enemyTurnPending;
    }

    private void beginPlayerTurn() {
        int hpBefore = player.getCurrentHp();
        player.onTurnStart();
        int healed = player.getCurrentHp() - hpBefore;
        if (healed > 0) {
            listener.onEvent(new HeroHealed(healed));
        }
    }

    private void afterPlayerAction() {
        if (!player.isAlive() || !getCurrentEnemy().isAlive()) {
            checkGameState();
        } else {
            enemyTurnPending = true;
        }
    }

    /**
     * Valuta l'esito dopo ogni mossa e fa avanzare la partita:
     * <ol>
     * <li>eroe morto: {@link HeroDefeated}, anche se nello stesso momento è morto
     * il nemico (per esempio per il contraccolpo di Furia Shimmer);</li>
     * <li>nemico ancora vivo: non succede nulla;</li>
     * <li>nemico morto con altri nemici nel livello (i gemelli): entra il successivo,
     * senza ricompensa e senza recuperare vita;</li>
     * <li>ultimo nemico del livello: si applica la ricompensa, poi si passa al livello
     * successivo (vita massima aumentata e ripristinata) oppure, se era l'ultimo,
     * scatta {@link GameWon}.</li>
     * </ol>
     */
    private void checkGameState() {
        if (!player.isAlive()) {
            listener.onEvent(new HeroDefeated());
            return;
        }
        if (getCurrentEnemy().isAlive()) {
            return;
        }

        listener.onEvent(new EnemyDefeated(getCurrentEnemy().getName()));

        if (level.hasNextEnemy()) {
            level.nextEnemy();
            listener.onEvent(new EnemyArrived(getCurrentEnemy().getName()));
            return;
        }

        applyReward();

        if (currentLevelNumber < levelProvider.getTotalLevels()) {
            currentLevelNumber++;
            level = levelProvider.createLevel(currentLevelNumber);
            applyProgression();
            announceLevel();
        } else {
            listener.onEvent(new GameWon());
        }
    }

    private void applyReward() {
        // Prima e dopo la ricompensa: la differenza diventa gli eventi di ricompensa.
        Loadout before = Loadout.of(player);
        level.getReward().apply(player);
        Loadout.of(player).gainedSince(before).forEach(listener::onEvent);
    }

    private void applyProgression() {
        player.setMaxHp(progression.maxHpForLevel(currentLevelNumber));
        player.setCurrentHp(player.getMaxHp());
    }

    private void announceLevel() {
        listener.onEvent(new LevelStarted(currentLevelNumber, getCurrentEnemy().getName()));
    }
}