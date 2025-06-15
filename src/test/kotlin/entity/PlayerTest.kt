package entity

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Test cases for [Player]
 */
class PlayerTest {

    /**
     * Ensure player is initialized correctly.
     */
    @Test
    fun testPlayerInitialization() {
        val player = Player("Samer")
        assertEquals("Samer", player.name)
        assertEquals(0, player.score)
        assertTrue(player.handCards.isEmpty())
        assertTrue(player.discardPile.isEmpty())
        assertTrue(player.performedActions.isEmpty())
    }
}
