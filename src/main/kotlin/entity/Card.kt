package entity

/**
 * Data class for a card in the game "Kombi-Duell".
 *
 * Each card has a [CardValue] and a [CardSuit].
 *
 * @property value The value of the card (e.g., TWO, SEVEN, KING, ACE)
 * @property color The suit/color of the card (e.g., HEARTS, SPADES)
 */
data class Card(val value: CardValue, val color: CardSuit) {

    override fun toString() = "$value of $color"

    /**
     * compares two [Card]s according to the [Enum.ordinal] value of their [CardSuit]
     * (i.e., the order in which the suits are declared in the enum class)
     *
     *ojnsd
     * Um einen Drilling zu finden, müssen drei Karten den [CardValue] haben,
     * also ergibt der Vergleich des [CardValue ]einen Unterschied von 0, während die
     * [CardSuit] unterschiedlich sein sollen.
     * gleiche für vrilling
     * Für andere Kombinationen, wie eine Sequenz, müssen die [CardValue] jeweils
     * einen Unterschied von 1 haben
     * (aufeinanderfolgende Werte), wobei die [CardSuit] gleich bleiben muss.
     *
     **/

    operator fun compareTo(other: Card) = this.value.ordinal - other.value.ordinal

}
