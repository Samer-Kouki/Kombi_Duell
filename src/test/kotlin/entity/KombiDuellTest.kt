package entity

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Test cases for [KombiDuell]
 */
class KombiDuellTest {

    /**
     * Ensure KombiDuell initializes players and piles correctly.
     */
    @Test
    fun testKombiDuellInitialization() {
        val player1 = Player("Samer")
        val player2 = Player("Eni")

        val game = KombiDuell(player1, player2)

        // Prüfen, ob die Spieler korrekt gesetzt sind
        assertEquals(player1, game.activePlayer)
        assertEquals(player2, game.inactivePlayer)

        // Prüfen, ob die Stapel und Auswahlbereich leer sind
        assertTrue(game.drawPile.isEmpty())
        assertTrue(game.swapPile.isEmpty())
    }
}
