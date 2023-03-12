package cz.rockawayx.stakingrewardsapi.model

import java.math.BigDecimal

data class ProviderYaml(
    val name: String,
    val supportedAssets: List<SupportedAssetYaml>,
)

data class SupportedAssetYaml(
    val name: String,
    val slug: String,
    val nodes: List<NodeYaml>,
)

data class NodeYaml(
    val address: String,
    val fee: BigDecimal,
    val users: Int,
    val balanceUsd: BigDecimal,
    val balanceToken: BigDecimal,
)
