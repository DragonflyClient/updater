package updater.backend.installer

import com.google.gson.JsonParser
import java.io.File

/**
 * Contains all required information specified in the `installation_properties.json` that
 * is created by the installer.
 */
object InstallationProperties {

    /**
     * The file in which the properties are stored.
     */
    private val file = File(System.getenv("appdata") + "\\Dragonfly\\installation_properties.json")

    /**
     * The json content of the file or null if it doesn't exist.
     */
    private val json = if (file.exists()) JsonParser().parse(file.readText()).asJsonObject else null

    /**
     * The channel property specified in the file or null, if it doesn't exist.
     */
    val channel = json?.get("channel")?.asString

    /**
     * The minecraft home property specified in the file or null, if it doesn't exist.
     */
    val minecraftHome = json?.get("minecraftHome")?.asString
}