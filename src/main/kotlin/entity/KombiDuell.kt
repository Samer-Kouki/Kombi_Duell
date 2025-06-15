
package entity

/**
 * Entity to represent the game state of "Kombi-Duell".
 *
 * This class manages:
 * - The two players: [activePlayer] and [inactivePlayer]
 * - The draw pile ([drawPile]): A stack of cards to draw from.
 * - The swap pile ([swapPile]): The area where 3 open cards can be swapped.
 *
 * @property activePlayer The player whose turn it is.
 * @property inactivePlayer The waiting player.
 */
class KombiDuell(
    var activePlayer: Player,
    var inactivePlayer: Player
) {
    var lastTurnWasPass: Boolean = false

    val drawPile: MutableList<Card> = mutableListOf()
    val swapPile: MutableList<Card> = mutableListOf()
    val performedActions: MutableList<PlayerAction> = mutableListOf()
    var round: Int = 1
}
