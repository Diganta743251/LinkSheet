package fe.linksheet.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.ParcelFileDescriptor
import com.google.gson.Gson
import com.google.gson.JsonObject
import fe.composekit.intent.buildIntent
import fe.linksheet.R
import fe.linksheet.extension.android.bufferedWriter
import fe.std.result.StdResult
import fe.std.result.tryCatch
import fe.std.result.unaryPlus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.time.toJavaInstant

/**
 * Utility for exporting link history using Storage Access Framework
 * No dangerous permissions required - uses SAF for secure file access
 */
class HistoryExporter(
    private val context: Context,
    private val clock: Clock,
    private val zoneId: ZoneId
) {
    companion object {
        private val exportIntent = buildIntent(Intent.ACTION_CREATE_DOCUMENT) {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/json"
        }
        
        private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm")
    }
    
    /**
     * Creates an intent to export link history
     * @param historyData List of link history entries
     * @return Intent for file creation
     */
    fun createExportIntent(historyData: List<LinkHistoryEntry>): Intent {
        val nowString = clock.now().toJavaInstant().atZone(zoneId).format(dateTimeFormatter)
        return Intent(exportIntent)
            .putExtra(
                Intent.EXTRA_TITLE,
                context.getString(R.string.history_export_file_name, nowString)
            )
    }
    
    /**
     * Exports link history to the specified URI using SAF
     * @param uri Destination URI from SAF
     * @param historyData List of link history entries
     * @return Result indicating success or failure
     */
    suspend fun exportHistoryToUri(uri: Uri, historyData: List<LinkHistoryEntry>): StdResult<Unit> = 
        withContext(Dispatchers.IO) {
            tryCatch {
                val jsonObject = JsonObject().apply {
                    addProperty("export_date", clock.now().toJavaInstant().toString())
                    addProperty("app_version", context.packageManager.getPackageInfo(context.packageName, 0).versionName)
                    addProperty("total_entries", historyData.size)
                    add("history", Gson().toJsonTree(historyData))
                }
                
                val jsonString = Gson().toJson(jsonObject)
                
                context.contentResolver.openFileDescriptor(uri, "w")
                    ?.bufferedWriter()
                    ?.use { it.write(jsonString) }
                    ?: throw Exception("Failed to open file for writing")
            }
        }
    
    /**
     * Opens file descriptor safely using SAF
     * @param uri File URI
     * @param mode File access mode ("r" for read, "w" for write)
     * @return ParcelFileDescriptor or null if failed
     */
    @Throws(Exception::class)
    private fun openDescriptor(uri: Uri, mode: String): ParcelFileDescriptor? {
        return context.contentResolver.openFileDescriptor(uri, mode)
    }
}

/**
 * Data class representing a link history entry
 */
data class LinkHistoryEntry(
    val url: String,
    val title: String?,
    val timestamp: Long,
    val appPackage: String?,
    val appName: String?
)