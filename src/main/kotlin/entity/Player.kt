package entity

/**
 * Entity to represent a player in the game "Kombi-Duell".
 *
 * Each player has a [name] and a [score] to track their points during the game.
 *
 * The player also manages two sets of cards:
 * - The hand cards ([handCards]): Cards currently held by the player (max. 10 cards).
 * - The discard pile ([discardPile]): Cards that have been played as valid combinations.
 *
 * The player interacts with these card lists by drawing cards and playing valid combinations
 * to collect points according to the game rules.
 *
 * @property name The name of the player.
 * @property score The current score of the player.
 */


class Player(val name: String) {

    var score: Int = 0

    // Liste für Handkarten (0..*)
    val handCards: MutableList<Card> = mutableListOf()

    val performedActions: MutableList<PlayerAction> = mutableListOf()

    // Liste für Ablagestapel (discardPile) (0..*)
    val discardPile: MutableList<Card> = mutableListOf()

    override fun toString(): String =
        "$name | Punkte: $score | Handkarten: $handCards | Ablagestapel: $discardPile"

}
