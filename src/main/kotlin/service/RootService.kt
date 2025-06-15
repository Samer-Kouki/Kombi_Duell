package service

import entity.KombiDuell

/**
 * The root service class is responsible for managing services and the entity layer reference.
 * This class acts as a central hub for every other service within the application.
 *
 */



/**
 * RootService class for Kombi-Duell.
 * Manages the current game state and connects all service components.
 */
class RootService {

    // Service instances
    val modelService = ModelService(this)
    val gameService = GameService(this)
    val drawService = DrawCardService(this)
    val swapService = SwapCardService(this)
    val playCardsService = PlayCardsService(this)
    val passService = PassService(this)

    /**
     * The current game instance. Null until a game is started.
     */
    var currentGame: KombiDuell? = null

    /**
     * Registers a single Refreshable (e.g., a scene) to all services.
     */
    fun addRefreshable(newRefreshable: Refreshable) {
        modelService.addRefreshable(newRefreshable)
        gameService.addRefreshable(newRefreshable)
        drawService.addRefreshable(newRefreshable)
        swapService.addRefreshable(newRefreshable)
        playCardsService.addRefreshable(newRefreshable)
        passService.addRefreshable(newRefreshable)
    }

    /**
     * Registers multiple Refreshables at once.
     */
    fun addRefreshables(vararg newRefreshables: Refreshable) {
        newRefreshables.forEach { addRefreshable(it) }
    }
}
