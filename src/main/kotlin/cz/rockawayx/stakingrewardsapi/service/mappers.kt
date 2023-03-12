package cz.rockawayx.stakingrewardsapi.service

import cz.rockawayx.stakingrewardsapi.model.Node
import cz.rockawayx.stakingrewardsapi.model.NodeYaml
import cz.rockawayx.stakingrewardsapi.model.ProviderResponse
import cz.rockawayx.stakingrewardsapi.model.ProviderYaml
import cz.rockawayx.stakingrewardsapi.model.SupportedAsset
import cz.rockawayx.stakingrewardsapi.model.SupportedAssetYaml

fun ProviderYaml.mapToApiModel() = ProviderResponse(
    name = name,
    balanceUsd = supportedAssets.flatMap { it.nodes }.sumOf { it.balanceUsd },
    users = supportedAssets.flatMap { it.nodes }.sumOf { it.users },
    supportedAssets = supportedAssets.map { it.mapToApiModel() }
)

fun SupportedAssetYaml.mapToApiModel() = SupportedAsset(
    name = name,
    slug = slug,
    usersTotal = nodes.sumOf { it.users },
    balanceUsdTotal = nodes.sumOf { it.balanceUsd },
    balanceTokenTotal = nodes.sumOf { it.balanceToken },
    feeTotal = nodes.sumOf { it.fee },
    nodes = nodes.map { it.mapToApiModel() }
)

fun NodeYaml.mapToApiModel() = Node(
    address = address,
    fee = fee,
    users = users,
    balanceUsd = balanceUsd,
    balanceToken = balanceToken
)
