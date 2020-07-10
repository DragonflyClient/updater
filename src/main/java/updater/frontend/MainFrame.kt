package updater.frontend

import updater.DragonflyUpdater
import updater.DragonflyUpdater.isUpdateDone
import updater.DragonflyUpdater.log
import updater.DragonflyUpdater.requestCancellation
import updater.DragonflyUpdater.resumeClient
import updater.frontend.panes.UpdatingPane
import java.awt.Dimension
import java.awt.event.*
import java.net.URL
import javax.imageio.ImageIO
import javax.swing.*

/**
 * The main frame of the installer.
 *
 * Switches between different panels to display the different states of the installer.
 */
class MainFrame(version: String) : JFrame("Dragonfly Updater") {

    /**
     * The main pane showing the download progress.
     */
    val updatingPane = UpdatingPane(version)

    init {
        log("Initializing main frame")
        size = Dimension(450, 200)
        isResizable = false

        contentPane.add(updatingPane)
        iconImage = ImageIO.read(URL("https://cdn.icnet.dev/dragonfly/assets/img/64x.png"))
        defaultCloseOperation = WindowConstants.DO_NOTHING_ON_CLOSE

        setLocationRelativeTo(null)
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                if (!isUpdateDone) {
                    requestCancellation()
                } else {
                    resumeClient()
                }
            }
        })
        pack()

        isVisible = !DragonflyUpdater.isHeadless
    }
}