package fe.linksheet.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import fe.linksheet.BuildConfig

object AdManager {
    /**
     * Determines if ads should be shown based on user status and connectivity
     * @param context Application context
     * @return true if ads should be shown (free user + internet available)
     */
    fun shouldShowAds(context: Context): Boolean {
        return isFreeUser(context) && context.isOnline()
    }
    
    /**
     * Check if user is on free version (not pro)
     * @param context Application context
     * @return true if user is on free version
     */
    private fun isFreeUser(context: Context): Boolean {
        // TODO: Replace with actual BillingManager integration
        // For now, assume free user to show ads
        return true
    }
    
    /**
     * Check if device has internet connectivity
     * @param context Application context
     * @return true if internet is available
     */
    private fun Context.isOnline(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        
        return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
               activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
    
    /**
     * Get AdMob unit ID based on build type
     * @return AdMob unit ID string
     */
    fun getAdUnitId(): String {
        return if (BuildConfig.DEBUG) {
            "ca-app-pub-3940256099942544/6300978111" // Test ad unit ID
        } else {
            "ca-app-pub-XXXXXXXXXXXXXXX/YYYYYYYYYY" // TODO: Replace with actual production ad unit ID
        }
    }
}