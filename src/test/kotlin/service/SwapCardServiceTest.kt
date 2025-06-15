package service

import entity.*
import kotlin.test.*

/**
 * Tests für das Tauschen mit SwapCardService.
 */
class SwapCardServiceTest {

    private lateinit var rootService: RootService
    private lateinit var swapService: SwapCardService

    /**
     * setup vorm jeden test. Spiel wird neu gemacht.
     */
    @BeforeTest
    fun setup() {
        rootService = RootService()
        swapService = rootService.swapService
        rootService.modelService.createGame("Samer", "Eni")

        val game = rootService.currentGame!!
        val player = game.activePlayer

        // eine Karte auf Hand, eine im Tauschstapel
        player.handCards.add(Card(CardValue.SEVEN, CardSuit.HEARTS))
        game.swapPile.add(Card(CardValue.EIGHT, CardSuit.HEARTS))
    }

    /**
     * normaler Tausch test. sollte klappen
     */
    @Test
    fun testValidSwap() {
        val game = rootService.currentGame!!
        val player = game.activePlayer

        val handCard = player.handCards.first()
        val swapCard = game.swapPile.first()

        swapService.execute(swapCard, handCard)

        // Karte aus Tausch ist jetzt auf Hand
        assertTrue(swapCard in player.handCards)

        // alte Handkarte ist jetzt im Tausch
        assertTrue(handCard in game.swapPile)

        // Aktion wurde gespeichert
        assertTrue(PlayerAction.SWAP_CARD in game.performedActions)
    }

    /**
     * falsche Karte aus Tauschbereich genommen
     */
    @Test
    fun testInvalidSwapCard() {
        val game = rootService.currentGame!!
        val player = game.activePlayer
        val handCard = player.handCards.first()

        // diese Karte ist nicht im Tauschbereich
        val invalidSwapCard = Card(CardValue.TEN, CardSuit.CLUBS)

        val exception = assertFailsWith<IllegalArgumentException> {
            swapService.validate(invalidSwapCard, handCard)
        }

        assertEquals(
            "Die angegebene Tauschkarte befindet sich nicht im Tauschbereich.",
            exception.message
        )
    }

    /**
     * Karte ist nicht in Hand, aber wird genutzt dann Fehler
     */
    @Test
    fun testInvalidHandCard() {
        val game = rootService.currentGame!!
        val swapCard = game.swapPile.first()

        // diese Karte ist nicht auf der Hand
        val invalidHandCard = Card(CardValue.KING, CardSuit.DIAMONDS)

        val exception = assertFailsWith<IllegalArgumentException> {
            swapService.validate(swapCard, invalidHandCard)
        }

        assertEquals(
            "Die angegebene Handkarte befindet sich nicht auf der Hand des Spielers.",
            exception.message
        )
    }

    /**
     * Spieler will 2 mal tauschen, geht nicht
     */
    @Test
    fun testSwapActionAlreadyDone() {
        val game = rootService.currentGame!!
        val player = game.activePlayer

        val handCard = player.handCards.first()
        val swapCard = game.swapPile.first()

        // schon getauscht vorher
        game.performedActions.add(PlayerAction.SWAP_CARD)

        val exception = assertFailsWith<IllegalStateException> {
            swapService.validate(swapCard, handCard)
        }

        assertEquals(
            "Diese Aktion wurde in diesem Zug bereits ausgeführt.",
            exception.message
        )
    }

    /**
     * alles korrekt dann sollte kein Fehler kommen
     */
    @Test
    fun testValidateWithoutException() {
        val game = rootService.currentGame!!
        val player = game.activePlayer

        val handCard = player.handCards.first()
        val swapCard = game.swapPile.first()

        // läuft ohne Probleme
        swapService.validate(swapCard, handCard)
    }
}
