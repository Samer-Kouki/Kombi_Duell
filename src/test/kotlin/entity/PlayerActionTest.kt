package entity

import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * Test cases for [PlayerAction]
 */
class PlayerActionTest {

    /**
     * Ensure all actions are present in enum.
     */
    @Test
    fun testPlayerActionValues() {
        val actions = PlayerAction.values()
        assertTrue(actions.contains(PlayerAction.DRAW_CARD))
        assertTrue(actions.contains(PlayerAction.SWAP_CARD))
        assertTrue(actions.contains(PlayerAction.PLAY_COMBINATION))
        assertTrue(actions.contains(PlayerAction.PASS))
    }
}
