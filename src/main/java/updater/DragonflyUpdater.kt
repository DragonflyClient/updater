package updater

import com.formdev.flatlaf.FlatDarculaLaf
import kotlinx.coroutines.*
import updater.backend.installer.InstallerLauncher
import updater.backend.process.ProcessManager
import updater.backend.process.reactivateRestorePoint
import updater.backend.utils.isInternetAvailable
import updater.frontend.DragonflyPalette
import updater.frontend.MainFrame
import java.awt.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.swing.JOptionPane
import javax.swing.UIManager
import kotlin.concurrent.thread
import kotlin.system.exitProcess

/**
 * The main object of the Dragonfly Updater.
 */
object DragonflyUpdater {

    /**
     * Instance of the main frame
     */
    lateinit var frame: MainFrame

    /**
     * The arguments passed to the updater when launching
     */
    private lateinit var args: Array<String>

    /**
     * Whether [cancelAndRestore] has already been called.
     */
    private var alreadyShutDown = false

    /**
     * Whether the updater should be started in headless mode.
     */
    val isHeadless: Boolean by lazy { args.contains("--headless") }

    /**
     * Whether the Dragonfly installer is required to download the latest version of Dragonfly.
     */
    val isRequiringInstaller: Boolean by lazy { args.contains("--requireInstaller") }

    /**
     * The target version which the client should be updated to.
     */
    val targetVersion: String by lazy {
        args.firstOrNull { it.startsWith("--version=") }?.replace("--version=", "")
            ?: throw IllegalStateException("Missing argument: version")
    }

    /**
     * Whether the update has been finished.
     */
    var isUpdateDone = false

    /**
     * Program entry point
     */
    @JvmStatic
    fun main(args: Array<String>) {
        log("Starting Dragonfly Updater")
        Runtime.getRuntime().addShutdownHook(thread(name = "Shutdown Hook", start = false) {
            log("Updater is shutting down...")
            cancelAndRestore()
        })

        this.args = args

        log("Requires Installer: $isRequiringInstaller")
        log("Headless: $isHeadless")
        log("Target version: $targetVersion")

        customizeTheme()

        if (!isInternetAvailable()) {
            log("No internet connection available!")
            JOptionPane.showMessageDialog(
                null,
                "We could not connect to the Inception Cloud Content Delivery Network. Please make sure your device" +
                        "is connected to the internet and try again.",
                "No internet connection",
                JOptionPane.ERROR_MESSAGE)
            ProcessManager.errorOccurred = true
            exitProcess(0)
        } else if (isRequiringInstaller) {
            InstallerLauncher.askUser()
            log("Exit: User was asked for installer")
            exitProcess(0)
        } else {
            frame = MainFrame(targetVersion)
            ProcessManager.startProcesses()
        }
    }

    /**
     * Installs and customizes the theme for the updater.
     */
    private fun customizeTheme() {
        FlatDarculaLaf.install()

        UIManager.put("Component.focusWidth", 0)
        UIManager.put("Component.innerFocusWidth", 0)

        UIManager.put("Panel.background", DragonflyPalette.background)

        UIManager.put("ProgressBar.arc", 999)
        UIManager.put("ProgressBar.background", DragonflyPalette.background.brighter())
        UIManager.put("ProgressBar.foreground", DragonflyPalette.accentNormal)

        UIManager.put("Button.background", DragonflyPalette.background)
        UIManager.put("Button.foreground", DragonflyPalette.foreground)
        UIManager.put("Button.focusedBorderColor", DragonflyPalette.background.brighter())
        UIManager.put("Button.hoverBackground", DragonflyPalette.background.brighter())
        UIManager.put("Button.hoverBorderColor", DragonflyPalette.accentNormal)

        UIManager.put("Button.default.background", DragonflyPalette.accentNormal)
        UIManager.put("Button.default.foreground", DragonflyPalette.foreground)
        UIManager.put("Button.default.focusedBorderColor", DragonflyPalette.accentNormal.darker())
        UIManager.put("Button.default.hoverBackground", DragonflyPalette.accentNormal)
        UIManager.put("Button.default.hoverBorderColor", DragonflyPalette.foreground)

        // Load the graphics environment
        val ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
        ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, DragonflyUpdater::class.java.getResourceAsStream("/Rubik.ttf")))
    }

    /**
     * Resumes the client by re-opening the Minecraft launcher. In the future, this will be able to
     * re-launch the client with the same JVM and program arguments that it was started with.
     */
    @JvmStatic
    fun resumeClient() {
        if (isHeadless) {
            log("Exit: Don't resume when headless")
            exitProcess(0)
        }

        val launcher = File("C:\\Program Files (x86)\\Minecraft Launcher\\MinecraftLauncher.exe")
        if (launcher.exists()) {
            Runtime.getRuntime().exec(launcher.absolutePath)
        }

        log("Exit: Client resumed")
        exitProcess(0)
    }

    /**
     * Opens a dialog for the user to confirm cancelling the updating process. If he does, [cancelAndRestore] is invoked.
     */
    @JvmStatic
    fun requestCancellation() {
        log("User requested cancellation")
        val options = arrayOf<Any>("Yes", "No")
        val result = JOptionPane.showOptionDialog(null,
            "Are you sure that you want to exit the Dragonfly updater? This can break your Dragonfly " +
                    "installation and may require you to download Dragonfly again!",
            "Cancel update?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1])

        if (result == JOptionPane.YES_OPTION) {
            log("Cancellation confirmed")
            cancelAndRestore()
        }
    }

    /**
     * Interrupts the updating process and restores the crated restore point.
     */
    @JvmStatic
    fun cancelAndRestore() {
        if (alreadyShutDown || isUpdateDone || isRequiringInstaller) return
        alreadyShutDown = true

        thread {
            log("Updater cancelled, shutting down safely")

            if (!ProcessManager.errorOccurred) {
                runBlocking {
                    if (isHeadless) delay(5_000)

                    log("Waiting for processes to be cancelled")
                    ProcessManager.processesJob.cancelAndJoin()
                    log("All processes cancelled, reactivating restore point")
                    reactivateRestorePoint.run()
                    log("Restore point reactivated!")
                }
            }

            if (this::frame.isInitialized)
                frame.isVisible = false

            log("Bye bye!")
            exitProcess(0)
        }
    }

    /**
     * Logs the given message to the console together with the current time.
     */
    @JvmStatic
    fun log(message: String) {
        println("[${SimpleDateFormat("HH:mm:ss").format(Date())}] [${Thread.currentThread().name}]: $message")
    }
}