package dev.deadzone.core

import dev.deadzone.core.survivor.SurvivorService
import kotlin.reflect.KClass

/**
 * Central registry for retrieving player-scoped game services (e.g., [SurvivorService], [InventoryService]).
 */
class PlayerServiceLocator {
    private val services = mutableMapOf<KClass<*>, Any>()
    private val factories = mutableMapOf<KClass<*>, () -> Any>()

    fun <T : Any> registerFactory(type: KClass<T>, factory: () -> T) {
        factories[type] = factory
    }

    /**
     * Retrieves a service of type [T], creating it via its registered factory on first access.
     *
     * @throws IllegalStateException if no factory has been registered for the requested type.
     */
    internal inline fun <reified T : Any> get(): T {
        @Suppress("UNCHECKED_CAST")
        return services.getOrPut(T::class) {
            val factory = factories[T::class] ?: error("No factory for ${T::class.simpleName}")
            factory.invoke()
        } as T
    }
}
