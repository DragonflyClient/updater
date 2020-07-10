package updater.backend.process

import updater.DragonflyUpdater
import updater.backend.installer.InstallationProperties
import updater.backend.utils.download
import updater.backend.utils.unzip
import java.io.File
import java.net.URL

/**
 * The Minecraft home directory, usually a .minecraft folder.
 */
private val minecraftHome = InstallationProperties.minecraftHome ?: System.getenv("appdata") + "\\.minecraft\\"

/**
 * The Dragonfly home directory inside the [minecraftHome].
 */
private val dragonflyHome = "${minecraftHome}dragonfly\\"

/**
 * The folder in which the Dragonfly version is stored.
 */
private val dragonflyVersion = "${minecraftHome}versions\\Dragonfly-1.8.8\\"

/**
 * The location of the restore point.
 */
private val restore = System.getenv("appdata") + "\\..\\Local\\Temp\\dragonfly-updater-restore-point\\"

/**
 * Creates a restore point by copying the version and the whole "dragonfly" folder.
 */
val createRestorePoint = object : Process("Creating restore point") {
    override suspend fun run() {
        !0
        File(restore).mkdirs()

        val version = File(dragonflyVersion)
        val dragonfly = File(dragonflyHome)

        !3
        version.copyRecursively(File(restore + "version\\"), overwrite = true)
        !6
        dragonfly.copyRecursively(File(restore + "dragonfly\\"), overwrite = true)
    }
}

/**
 * Downloads the Dragonfly assets as a zip file and unzips them.
 */
val downloadAssets = object : Process("Downloading assets") {
    val assets = File(minecraftHome + "dragonfly\\assets\\")
    val url = URL("https://cdn.icnet.dev/dragonfly/assets.zip")

    override suspend fun run() {
        assets.deleteRecursively()
        !8

        val destination = File("$dragonflyHome\\assets.zip")
        destination.delete()
        !10

        url.download(destination)
        !15

        destination.unzip(File(dragonflyHome))
        destination.delete()
        !20
    }
}

/**
 * Download the Dragonfly JAR and JSON files
 */
val downloadClient = object : Process("Downloading client") {
    val json = URL("https://cdn.icnet.dev/dragonfly/${DragonflyUpdater.targetVersion}/Dragonfly-1.8.8.json")
    val jar = URL("https://cdn.icnet.dev/dragonfly/${DragonflyUpdater.targetVersion}/Dragonfly-1.8.8.jar")

    override suspend fun run() {
        with(File(dragonflyVersion)) {
            deleteRecursively()
            mkdirs()
        }

        json.download(File(dragonflyVersion + "Dragonfly-1.8.8.json"))
        !25
        jar.download(File(dragonflyVersion + "Dragonfly-1.8.8.jar")) {
            !(25 + (it / 100.0) * 70).toInt()
        }
    }
}

/**
 * Deletes the previously created restore point.
 */
val deleteRestorePoint = object : Process("Deleting restore point") {
    override suspend fun run() {
        File(restore).deleteRecursively()
        !100
    }
}

/**
 * Reactivates the previously created restore point by copying the files back.
 */
val reactivateRestorePoint = object : Process("Reactivating restore point") {
    override suspend fun run() {
        val version = File(dragonflyVersion)
        val dragonfly = File(dragonflyHome)

        version.delete()
        dragonfly.delete()

        File(restore + "version\\").copyRecursively(version, overwrite = true)
        File(restore + "dragonfly\\").copyRecursively(dragonfly, overwrite = true)

        deleteRestorePoint.run()
    }
}

/**
 * Sets the value of the progress bar
 */
private operator fun Int.not() = DragonflyUpdater.frame.updatingPane.setProgress(this)