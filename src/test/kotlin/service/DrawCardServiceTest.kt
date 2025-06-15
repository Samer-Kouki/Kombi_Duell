package service

import entity.*
import kotlin.test.*

/**
 * Testklasse für das Kartenziehen – also DrawCardService.
 */
class DrawCardServiceTest {

    private lateinit var rootService: RootService
    private lateinit var drawService: DrawCardService

    /**
     * Vor jedem Test: neues Spiel starten und 1 Karte in Nachziehstapel legen
     */
    @BeforeTest
    fun setup() {
        rootService = RootService()
        drawService = rootService.drawService
        rootService.modelService.createGame("Samer", "Eni")

        val game = rootService.currentGame!!
        game.drawPile.add(Card(CardValue.FIVE, CardSuit.CLUBS))
    }
/**
    /**
     * Testet, ob Karte korrekt gezogen wird:
     * - landet auf Hand
     * - Aktion wird gespeichert
     */
    @Test
    fun testValidDrawCard() {
        val game = rootService.currentGame!!
        val player = game.activePlayer

        drawService.execute()

        // jetzt sollte eine Karte auf der Hand liegen
        assertEquals(1, player.handCards.size)

        // es sollte die richtige Karte sein (Fünf Kreuz)
        assertEquals(CardValue.FIVE, player.handCards.first().value)

        // Aktion "Ziehen" sollte gespeichert sein
        assertTrue(PlayerAction.DRAW_CARD in game.performedActions)
    }
*/
    /**
     * Spieler hat schon 10 Karten  dann , darf nix mehr ziehen
     */
    @Test
    fun testDrawWithFullHand() {
        val player = rootService.currentGame!!.activePlayer

        // Hand voll machen
        repeat(10) {
            player.handCards.add(Card(CardValue.FIVE, CardSuit.HEARTS))
        }

        // Versuch zu ziehen → muss Fehler werfen
        val exception = assertFailsWith<IllegalStateException> {
            drawService.validate()
        }

        assertEquals("Spieler darf nicht mehr als 10 Handkarten haben.", exception.message)
    }

    /**
     * Nachziehstapel ist leer dann , darf nicht ziehen
     */
    @Test
    fun testDrawWithEmptyPile() {
        val game = rootService.currentGame!!

        // Stapel leeren
        game.drawPile.clear()

        val exception = assertFailsWith<IllegalStateException> {
            drawService.validate()
        }

        assertEquals("Nachziehstapel ist leer.", exception.message)
    }

    /**
     * Spieler hat in diesem Zug schon gezogen dann , nochmal geht nicht
     */
    @Test
    fun testDrawActionAlreadyDone() {
        val game = rootService.currentGame!!

        // Aktion wurde schon gemacht
        game.performedActions.add(PlayerAction.DRAW_CARD)

        val exception = assertFailsWith<IllegalStateException> {
            drawService.validate()
        }

        assertEquals("Ziehen wurde bereits in diesem Zug ausgeführt.", exception.message)
    }
}
