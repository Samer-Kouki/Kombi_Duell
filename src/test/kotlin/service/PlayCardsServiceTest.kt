package service

import entity.*
import kotlin.test.*

/**
 * Tests für das Ausspielen von Kartenkombis (PlayCardsService)
 */
class PlayCardsServiceTest {

    private lateinit var rootService: RootService
    private lateinit var playService: PlayCardsService

    /**
     * Vor jedem Test neues Spiel starten
     */
    @BeforeTest
    fun setup() {
        rootService = RootService()
        playService = rootService.playCardsService
        rootService.modelService.createGame("Samer", "Eni")
    }

    /**
     * Spieler spielt 3 gleiche Karten (Drilling) dann sollte klappen
     */
    @Test
    fun `valid triplet combination`() {
        val player = rootService.currentGame!!.activePlayer
        val cards = listOf(
            Card(CardValue.EIGHT, CardSuit.HEARTS),
            Card(CardValue.EIGHT, CardSuit.SPADES),
            Card(CardValue.EIGHT, CardSuit.CLUBS)
        )
        player.handCards.addAll(cards)

        // Kombination ausspielen
        playService.execute(cards)

        // Punktzahl prüfen (10 Punkte für Drilling)
        assertEquals(10, player.score)

        // Karten müssen im Ablagestapel liegen
        assertTrue(cards.all { it in player.discardPile })
    }

    /**
     * Spieler spielt 4 gleiche Karten (Vierling) dann , auch gültig
     */
    @Test
    fun `valid quadruple combination`() {
        val player = rootService.currentGame!!.activePlayer
        val cards = listOf(
            Card(CardValue.TEN, CardSuit.HEARTS),
            Card(CardValue.TEN, CardSuit.SPADES),
            Card(CardValue.TEN, CardSuit.CLUBS),
            Card(CardValue.TEN, CardSuit.DIAMONDS)
        )
        player.handCards.addAll(cards)

        playService.execute(cards)

        // Vierling gibt 15 Punkte
        assertEquals(15, player.score)
        assertTrue(cards.all { it in player.discardPile })
    }

    /**
     * Gültige Reihenfolge (5-6-7 in gleicher Farbe) dann  erlaubt
     */
    @Test
    fun `valid sequence combination`() {
        val player = rootService.currentGame!!.activePlayer
        val cards = listOf(
            Card(CardValue.FIVE, CardSuit.HEARTS),
            Card(CardValue.SIX, CardSuit.HEARTS),
            Card(CardValue.SEVEN, CardSuit.HEARTS)
        )
        player.handCards.addAll(cards)

        playService.execute(cards)

        // 3 Karten in Reihe geben 6 Punkte (3 * 2)
        assertEquals(6, player.score)
        assertTrue(cards.all { it in player.discardPile })
    }

    /**
     * Reihenfolge mit falscher Farbe dann  nicht erlaubt
     */
    @Test
    fun `sequence with wrong color should fail`() {
        val player = rootService.currentGame!!.activePlayer
        val cards = listOf(
            Card(CardValue.FIVE, CardSuit.HEARTS),
            Card(CardValue.SIX, CardSuit.HEARTS),
            Card(CardValue.SEVEN, CardSuit.SPADES)
        )
        player.handCards.addAll(cards)

        val exception = assertFailsWith<IllegalArgumentException> {
            playService.execute(cards)
        }

        // muss sagen, dass keine gültige Kombi
        assertTrue(exception.message?.contains("gültige Kombination") == true)
    }

    /**
     * Zu wenige Karten (nur 2) dann ungültig
     */
    @Test
    fun `less than three cards should fail`() {
        val player = rootService.currentGame!!.activePlayer
        val cards = listOf(
            Card(CardValue.FIVE, CardSuit.HEARTS),
            Card(CardValue.SIX, CardSuit.HEARTS)
        )
        player.handCards.addAll(cards)

        val exception = assertFailsWith<IllegalArgumentException> {
            playService.execute(cards)
        }

        assertTrue(exception.message?.contains("gültige Kombination") == true)
    }

    /**
     * Karten mit gleichem Symbol, aber keine Reihe dann ungültig
     */
    @Test
    fun `mixed values should fail`() {
        val player = rootService.currentGame!!.activePlayer
        val cards = listOf(
            Card(CardValue.FIVE, CardSuit.CLUBS),
            Card(CardValue.SEVEN, CardSuit.CLUBS),
            Card(CardValue.EIGHT, CardSuit.CLUBS)
        )
        player.handCards.addAll(cards)

        val exception = assertFailsWith<IllegalArgumentException> {
            playService.execute(cards)
        }

        assertTrue(exception.message?.contains("gültige Kombination") == true)
    }
}
