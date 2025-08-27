package fe.linksheet.util

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Manages Play Billing v6 for LinkMaster pro purchases
 */
class BillingManager(private val context: Context) {
    
    companion object {
        const val PRO_SKU = "linkmaster_pro"
        const val PRO_SKU_MONTHLY = "linkmaster_pro_monthly"
        const val PRO_SKU_YEARLY = "linkmaster_pro_yearly"
    }
    
    private var billingClient: BillingClient? = null
    
    private val _isProUser = MutableStateFlow(false)
    val isProUser: StateFlow<Boolean> = _isProUser.asStateFlow()
    
    private val _purchaseState = MutableStateFlow<PurchaseState>(PurchaseState.Idle)
    val purchaseState: StateFlow<PurchaseState> = _purchaseState.asStateFlow()
    
    init {
        setupBillingClient()
    }
    
    private fun setupBillingClient() {
        billingClient = BillingClient.newBuilder(context)
            .setListener { billingResult, purchases ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                    for (purchase in purchases) {
                        handlePurchase(purchase)
                    }
                } else {
                    _purchaseState.value = PurchaseState.Error(billingResult.debugMessage ?: "Unknown error")
                }
            }
            .enablePendingPurchases()
            .build()
        
        billingClient?.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // Check if user already has pro
                    checkProStatus()
                } else {
                    _purchaseState.value = PurchaseState.Error("Billing setup failed: ${billingResult.debugMessage}")
                }
            }
            
            override fun onBillingServiceDisconnected() {
                // Retry connection
                setupBillingClient()
            }
        })
    }
    
    private fun checkProStatus() {
        billingClient?.queryPurchasesAsync(QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)
            .build()) { billingResult, purchases ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    val hasPro = purchases.any { purchase ->
                        purchase.purchaseState == Purchase.PurchaseState.PURCHASED &&
                        (purchase.products.contains(PRO_SKU) || 
                         purchase.products.contains(PRO_SKU_MONTHLY) || 
                         purchase.products.contains(PRO_SKU_YEARLY))
                    }
                    _isProUser.value = hasPro
                }
            }
    }
    
    fun purchasePro(activity: Activity, productId: String = PRO_SKU) {
        _purchaseState.value = PurchaseState.Loading
        
        billingClient?.queryProductDetailsAsync(
            QueryProductDetailsParams.newBuilder()
                .setProductList(
                    listOf(
                        QueryProductDetailsParams.Product.newBuilder()
                            .setProductId(productId)
                            .setProductType(BillingClient.ProductType.INAPP)
                            .build()
                    )
                )
                .build()
        ) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && 
                productDetailsList.isNotEmpty()) {
                
                val productDetails = productDetailsList[0]
                val flowParams = BillingFlowParams.newBuilder()
                    .setProductDetailsParamsList(
                        listOf(
                            BillingFlowParams.ProductDetailsParams.newBuilder()
                                .setProductDetails(productDetails)
                                .build()
                        )
                    )
                    .build()
                
                billingClient?.launchBillingFlow(activity, flowParams)
            } else {
                _purchaseState.value = PurchaseState.Error("Product not found")
            }
        }
    }
    
    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()
                
                billingClient?.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        _isProUser.value = true
                        _purchaseState.value = PurchaseState.Success
                    } else {
                        _purchaseState.value = PurchaseState.Error("Failed to acknowledge purchase")
                    }
                }
            } else {
                _isProUser.value = true
                _purchaseState.value = PurchaseState.Success
            }
        } else if (purchase.purchaseState == Purchase.PurchaseState.PENDING) {
            _purchaseState.value = PurchaseState.Pending
        } else if (purchase.purchaseState == Purchase.PurchaseState.UNSPECIFIED_STATE) {
            _purchaseState.value = PurchaseState.Error("Purchase state unspecified")
        }
    }
    
    fun disconnect() {
        billingClient?.endConnection()
        billingClient = null
    }
}

sealed class PurchaseState {
    object Idle : PurchaseState()
    object Loading : PurchaseState()
    object Success : PurchaseState()
    object Pending : PurchaseState()
    data class Error(val message: String) : PurchaseState()
}