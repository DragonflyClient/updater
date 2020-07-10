package updater.backend.process

/**
 * Represents a single process that is executed during the lifetime of the updater.
 *
 * @param description a brief description what the process does
 */
abstract class Process(val description: String) {

    /**
     * Called when the process is executed.
     */
    abstract suspend fun run()
}
