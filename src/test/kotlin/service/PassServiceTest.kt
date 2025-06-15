package service

import entity.*
import kotlin.test.*

/**
 * Tests für das Passen (Spielzug aussetzen).
 */
class PassServiceTest {

    private lateinit var rootService: RootService
    private lateinit var passService: PassService

    /**
     * Vor jedem Test neues Spiel starten
     */
    @BeforeTest
    fun setup() {
        rootService = RootService()
        passService = rootService.passService
        rootService.modelService.createGame("Samer", "Eni")
    }
/**
    /**
     * Testet, ob der Spieler korrekt passt
     */
    @Test
    fun testPassAction() {
        val game = rootService.currentGame!!
        val oldActive = game.activePlayer

        // Spieler setzt aus
        passService.execute()

        // jetzt sollte der andere Spieler dran sein
        assertNotEquals(oldActive, game.activePlayer)

        // der alte Spieler ist jetzt inaktiv und hat "PASS" als Aktion gespeichert
        val nowInactive = game.inactivePlayer
        assertEquals(oldActive, nowInactive)
        assertTrue(PlayerAction.PASS in nowInactive.performedActions)
    }
    */
}
