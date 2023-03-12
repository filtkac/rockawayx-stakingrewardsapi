package cz.rockawayx.stakingrewardsapi.model

import java.math.BigDecimal

data class ProviderResponse(
    val name: String,
    val balanceUsd: BigDecimal?,
    val users: Int?,
    val supportedAssets: List<SupportedAsset>
)

data class SupportedAsset(
    val name: String,
    val slug: String,
    val usersTotal: Int?,
    val balanceUsdTotal: BigDecimal?,
    val balanceTokenTotal: BigDecimal?,
    val feeTotal: BigDecimal?,
    val nodes: List<Node>
)

data class Node(
    val address: String,
    val fee: BigDecimal?,
    val users: Int?,
    val balanceUsd: BigDecimal?,
    val balanceToken: BigDecimal?
)
