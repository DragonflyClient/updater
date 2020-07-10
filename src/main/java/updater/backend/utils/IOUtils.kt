package updater.backend.utils

import kotlinx.coroutines.*
import net.lingala.zip4j.ZipFile
import updater.DragonflyUpdater.log
import java.io.*
import java.lang.Exception
import java.net.*


/**
 * Downloads the file at this url to the given [destination] invoking the [progressCallback] whenever
 * the progress changes.
 */
@Suppress("BlockingMethodInNonBlockingContext")
suspend fun URL.download(
    destination: File,
    progressCallback: ((Int) -> Unit)? = null
): Boolean = withContext(Dispatchers.IO) {
    log("Downloading ${this@download} to $destination")
    var inputStream: InputStream? = null
    var outputStream: FileOutputStream? = null

    try {
        val urlConnection: HttpURLConnection = openConnection() as HttpURLConnection
        inputStream = urlConnection.inputStream
        outputStream = FileOutputStream(destination)
        val data = ByteArray(1024)
        var count: Int

        val completeFileSize: Long = urlConnection.contentLength.toLong()
        var downloadedFileSize: Long = 0
        var progressBefore = 0

        while (inputStream.read(data, 0, 1024).also { count = it } != -1) {
            outputStream.write(data, 0, count)
            downloadedFileSize += count

            val progress = (downloadedFileSize.toDouble() / completeFileSize.toDouble() * 100.0).toInt()
            if (progress != progressBefore) {
                progressCallback?.invoke(progress)
                progressBefore = progress

                yield()
            }
        }

        log("Download succeeded")
        return@withContext true
    } finally {
        inputStream?.close()
        outputStream?.close()
    }
}

/**
 * Unzips the file into the given [destinationDirectory].
 */
@Suppress("BlockingMethodInNonBlockingContext")
suspend fun File.unzip(destinationDirectory: File) = withContext(Dispatchers.IO) {
    val zipFile = ZipFile(absoluteFile)
    zipFile.extractAll(destinationDirectory.absolutePath)
}

/**
 * Checks whether a network connection to the content delivery network is possible.
 */
fun isInternetAvailable() = try {
    val url = URL("https://cdn.icnet.dev")
    val conn = url.openConnection()
    conn.connect()
    conn.getInputStream().close()
    true
} catch (e: Exception) {
    false
}