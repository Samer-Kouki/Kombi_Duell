package service

import kotlin.test.*
import entity.*

/**
 * Tests für den GameService – Spielstart, Aktionen prüfen und Spielerwechsel.
 */
class GameServiceTest {

    private lateinit var rootService: RootService
    private lateinit var gameService: GameService

    @BeforeTest
    fun setup() {
        rootService = RootService()
        gameService = rootService.gameService
    }

    /**
     * Testet, ob Spiel mit zwei Spielern richtig gestartet wird
     */
    @Test
    fun testStartGameInitializesGame() {
        gameService.startGame("Samer", "Eni")

        val game = rootService.currentGame
        assertNotNull(game, "Spiel sollte nach Start nicht null sein.")

        // beide Namen müssen korrekt gesetzt sein
        val names = setOf(game.activePlayer.name, game.inactivePlayer.name)
        assertTrue(
            names.containsAll(setOf("Samer", "Eni")),
            "Beide Spielernamen sollten gesetzt sein."
        )
    }

    /**
     * Testet, ob Spieler noch was machen darf im Zug (max 2 Aktionen pro Zug)
     */
    @Test
    fun testCanDoAction() {
        gameService.startGame("A", "B")
        val game = rootService.currentGame!!

        // Am Anfang: darf Karte ziehen
        assertTrue(gameService.canDoAction(PlayerAction.DRAW_CARD))

        // eine Aktion gemacht dann darf noch eine
        game.performedActions.add(PlayerAction.DRAW_CARD)
        assertTrue(gameService.canDoAction(PlayerAction.SWAP_CARD))

        // zwei gemacht dann keine weitere erlaubt
        game.performedActions.add(PlayerAction.SWAP_CARD)
        assertFalse(gameService.canDoAction(PlayerAction.PLAY_COMBINATION))
    }

    /**
     * Testet, ob Spieler nach Zugende wechselt
     */
    @Test
    fun testEndTurnSwitchesPlayers() {
        gameService.startGame("Samer", "Eni")
        val game = rootService.currentGame!!

        val firstActiveName = game.activePlayer.name

        // Zug beenden
        gameService.endTurn()

        val newActiveName = game.activePlayer.name

        // Jetzt sollte der andere Spieler dran sein
        assertNotEquals(
            firstActiveName,
            newActiveName,
            "Der aktive Spieler sollte nach Zugende wechseln."
        )
    }
}
