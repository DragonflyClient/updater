package updater.backend.utils

import updater.DragonflyUpdater.log
import java.awt.*
import java.net.URL

/**
 * A bridge to the [SystemTray] for sending notifications to the user.
 */
object SystemTrayBridge {

    /**
     * The system tray instance or null if system tray is not supported.
     */
    private val tray = if (SystemTray.isSupported()) SystemTray.getSystemTray() else null

    /**
     * The created tray icon or null if system tray is not supported.
     */
    private val trayIcon = tray?.let {
        TrayIcon(
            Toolkit.getDefaultToolkit().createImage(URL("https://cdn.icnet.dev/dragonfly/assets/img/512x.png")),
            "Dragonfly Updater"
        ).apply {
            isImageAutoSize = true
            addActionListener {
                log("Double click action performed!")
            }
        }
    }

    init {
        tray?.add(trayIcon)
        log("Added tray icon to system tray")
    }

    /**
     * Displays a tray notification of the [type] with the given [caption] and [text].
     */
    fun displayNotification(caption: String, text: String, type: TrayIcon.MessageType) {
        trayIcon?.displayMessage(caption, text, type) ?: return log("System tray is not supported!")
        log("Displaying notification: $caption - $text [$type]")
    }
}