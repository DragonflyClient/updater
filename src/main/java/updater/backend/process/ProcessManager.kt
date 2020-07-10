package updater.backend.process

import kotlinx.coroutines.*
import updater.DragonflyUpdater
import updater.DragonflyUpdater.frame
import updater.DragonflyUpdater.isHeadless
import updater.DragonflyUpdater.isRequiringInstaller
import updater.DragonflyUpdater.isUpdateDone
import updater.DragonflyUpdater.log
import updater.DragonflyUpdater.targetVersion
import updater.backend.installer.InstallerLauncher
import updater.backend.utils.SystemTrayBridge
import updater.frontend.panes.FinishedPane
import java.awt.TrayIcon
import javax.swing.JOptionPane
import kotlin.system.exitProcess

/**
 * Executes the processes and updates the updates the UI based on the results.
 */
object ProcessManager {

    /**
     * The job that serves all currently running processes.
     */
    lateinit var processesJob: Job

    /**
     * Whether an error occurred during the execution of one process.
     */
    var errorOccurred = false

    /**
     * Execute the process while catching and handling all occurring exceptions.
     */
    private suspend fun Process.execute() {
        try {
            frame.updatingPane.setInfo(description)
            log("Executing process: $description")
            run()
        } catch (exception: Exception) {
            if (exception is CancellationException) {
                log("Executing coroutine for current job cancelled!")
                return
            }

            errorOccurred = true
            log("Process failed")
            exception.printStackTrace()

            if (isHeadless) {
                SystemTrayBridge.displayNotification(
                    "Dragonfly Updater",
                    "Error while updating Dragonfly, restore point reactivated.",
                    TrayIcon.MessageType.ERROR
                )
            } else {
                JOptionPane.showOptionDialog(null,
                    "A fatal error occurred while running the Dragonfly updater! " +
                            "The previously created restore point is now reactivated.",
                    "Error while updating",
                    -1, JOptionPane.ERROR_MESSAGE, null, arrayOf("OK"), "OK"
                )
            }

            log("Exception during process execution!")
            DragonflyUpdater.cancelAndRestore()
            throw exception
        }
    }

    /**
     * Runs all processes.
     */
    fun startProcesses() {
        processesJob = GlobalScope.launch {
            if (isHeadless) {
                SystemTrayBridge.displayNotification(
                    "Dragonfly Updater",
                    "Dragonfly is being updated in the background",
                    TrayIcon.MessageType.INFO
                )
            }

            log("Starting all processes")

            createRestorePoint.execute(); yield()
            downloadAssets.execute(); yield()
            downloadClient.execute(); yield()
            deleteRestorePoint.execute(); yield()

            isUpdateDone = true

            if (isHeadless) {
                SystemTrayBridge.displayNotification(
                    "Dragonfly Updater",
                    "Dragonfly has been successfully updated to $targetVersion",
                    TrayIcon.MessageType.INFO
                )

                delay(10_000)
                log("Exit: Headless update finished")
                exitProcess(0)
            } else {
                frame.contentPane.removeAll()
                frame.contentPane.add(FinishedPane(targetVersion))
                frame.contentPane.revalidate()
                frame.contentPane.repaint()
            }
        }
    }
}