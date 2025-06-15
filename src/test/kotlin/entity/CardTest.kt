package entity

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test cases for [Card]
 */
class CardTest {

    /**
     * Ensure card values are set correctly.
     */
    @Test
    fun testCardCreation() {
        val card = Card(CardValue.ACE, CardSuit.SPADES)
        assertEquals(CardValue.ACE, card.value)
        assertEquals(CardSuit.SPADES, card.color)
    }
}
