package updater.backend.installer

import kotlinx.coroutines.runBlocking
import updater.backend.utils.download
import java.io.File
import java.net.URL
import javax.swing.JOptionPane

/**
 * Handles the installation process if the installer is required to perform the update to the
 * new version.
 */
object InstallerLauncher {

    /**
     * The location of the installer in the Dragonfly local storage.
     */
    private val installer = File(System.getenv("appdata") + "\\Dragonfly\\Dragonfly-Installer.jar")

    /**
     * Asks the user whether he wants to continue with the installation if the installer is required
     * to download the latest version.
     */
    fun askUser() {
        val options = arrayOf<Any>("Continue", "Cancel")
        val result = JOptionPane.showOptionDialog(null,
            "The Dragonfly Installer is required to download the latest update. " +
                    "The updater will download and run the latest version of the installer. " +
                    "Do you want to continue?",
            "Continue installation?",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE,
            null, options, options[0]
        )

        if (result == 0) {
            downloadInstaller()
            runInstaller()
        }
    }

    /**
     * Downloads the latest version of the installer from the Dragonfly content delivery network.
     */
    private fun downloadInstaller() {
        installer.delete()
        val remote = URL("https://cdn.icnet.dev/dragonfly/installer/Dragonfly-Installer.jar")

        runBlocking {
            remote.download(installer)
        }
    }

    /**
     * Executes the downloaded installer.
     */
    private fun runInstaller() {
        Runtime.getRuntime().exec("java -jar ${installer.absolutePath}")
    }
}