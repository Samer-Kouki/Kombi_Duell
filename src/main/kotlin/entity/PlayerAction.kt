package entity

/**
 * Enum to distinguish between the possible player actions in the game "Kombi-Duell".
 *
 * A player can choose from three main actions during their turn:
 * - DRAW_CARD: Draw a card from the draw pile.
 * - SWAP_CARD: Swap a card from hand with one from the swap pile.
 * - PLAY_COMBINATION: Play a valid card combination to gain points.
 *
 * Additionally, the player can choose to PASS, which does not count as an action.
 *
 * Per turn, a player may perform up to two different actions.
 */

enum class PlayerAction {
    DRAW_CARD,
    SWAP_CARD,
    PLAY_COMBINATION,
    PASS
}