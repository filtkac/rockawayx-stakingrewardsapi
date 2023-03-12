package cz.rockawayx.stakingrewardsapi.exception

class NoAssetDirectoryDefined(message: String): Exception(message)

class ErrorLoadingNonCosmosAssets(message: String): Exception(message)
