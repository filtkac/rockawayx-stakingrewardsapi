package cz.rockawayx.stakingrewardsapi.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.JacksonYAMLParseException
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import cz.rockawayx.stakingrewardsapi.client.CosmosDirectoryClient
import cz.rockawayx.stakingrewardsapi.exception.NoAssetDirectoryDefined
import cz.rockawayx.stakingrewardsapi.model.NodeYaml
import cz.rockawayx.stakingrewardsapi.model.ProviderResponse
import cz.rockawayx.stakingrewardsapi.model.ProviderYaml
import cz.rockawayx.stakingrewardsapi.model.SupportedAssetYaml
import feign.FeignException
import org.slf4j.LoggerFactory
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import java.math.BigDecimal
import java.nio.file.Files
import java.nio.file.Path

@Service
@RegisterReflectionForBinding(ProviderYaml::class, SupportedAssetYaml::class, NodeYaml::class)
class StakingRewardsService(
    private val cosmosDirectoryClient: CosmosDirectoryClient,
) {

    @Value("\${app.rockawayx-assets-directory}")
    private val rockawayxAssetsDirectory: String? = null

    private val logger = LoggerFactory.getLogger(javaClass)

    fun getProvider(): ProviderResponse {

        rockawayxAssetsDirectory ?: throw NoAssetDirectoryDefined("No asset directory defined in application properties.")
        val cacheDirectory = "$rockawayxAssetsDirectory/cached"
        Files.createDirectories(Path.of(cacheDirectory))

        val cosmosAssets = loadAndSaveRemoteCosmosAssets(cacheDirectory)
            ?: loadCachedCosmosAssets(cacheDirectory)
        val nonCosmosAssets = (loadAndSaveNonCosmosAssets(cacheDirectory)
            ?: loadCachedNonCosmosAssets(cacheDirectory))?.mapToApiModel()

        return ProviderResponse(
            name = DEFAULT_PROVIDER_NAME,
            balanceUsd = (cosmosAssets?.balanceUsd ?: BigDecimal.ZERO).add(nonCosmosAssets?.balanceUsd ?: BigDecimal.ZERO),
            users = (cosmosAssets?.users ?: 0) + (nonCosmosAssets?.users ?: 0),
            supportedAssets = (cosmosAssets?.supportedAssets ?: emptyList()) + (nonCosmosAssets?.supportedAssets ?: emptyList())
        )
    }

    private fun loadAndSaveRemoteCosmosAssets(cacheDirectory: String): ProviderResponse? {
        try {
            val cosmosAssets = cosmosDirectoryClient.getCosmosAssets()

            val cosmosAssetsCachedFile = File("$cacheDirectory/$CACHED_COSMOS_ASSETS_FILE_NAME")
            try {
                mapper.writeValue(cosmosAssetsCachedFile, cosmosAssets)
            } catch (e: Exception) {
                logger.error("Error while caching cosmos assets.", e)
            }

            logger.info("Successfully loaded and cached remote cosmos assets.")
            return cosmosAssets
        } catch (e: FeignException) {
            logger.error("An error occurred fetching cosmos assets. Using cached assets instead.", e)
        }

        return null
    }

    private fun loadCachedCosmosAssets(cacheDirectory: String): ProviderResponse? {
        val cosmosAssetsCachedFile = File("$cacheDirectory/$CACHED_COSMOS_ASSETS_FILE_NAME")
        if (cosmosAssetsCachedFile.exists()) {
            try {
                val cachedCosmosAssets: ProviderResponse = mapper.readValue(cosmosAssetsCachedFile)
                logger.info("Successfully loaded cached cosmos assets.")
                return cachedCosmosAssets
            } catch (e: JacksonYAMLParseException) {
                logger.error("Error parsing cached cosmos assets yaml.", e)
            }
        } else {
            logger.warn("No cached cosmos assets file exists: {}", cosmosAssetsCachedFile.absolutePath)
        }

        return null
    }

    private fun loadAndSaveNonCosmosAssets(cacheDirectory: String): ProviderYaml? {
        val nonCosmosAssetsFile = File("$rockawayxAssetsDirectory/$NON_COSMOS_ASSETS_FILE_NAME")
        if (!nonCosmosAssetsFile.exists()) {
            logger.error("No custom non-cosmos assets defined!")
            return null
        }

        try {
            val nonCosmosAssets: ProviderYaml = mapper.readValue(nonCosmosAssetsFile)

            val nonCosmosAssetsCachedFile = File("$cacheDirectory/$CACHED_NON_COSMOS_ASSETS_FILE_NAME")
            try {
                mapper.writeValue(nonCosmosAssetsCachedFile, nonCosmosAssets)
            } catch (e: Exception) {
                logger.error("Error while caching non-cosmos assets.", e)
            }

            logger.info("Successfully loaded and cached non-cosmos assets.")
            return nonCosmosAssets
        } catch (e: Exception) {
            logger.error("Error parsing non-cosmos assets yaml. Using cached non-cosmos assets instead.", e)
        }

        return null
    }

    private fun loadCachedNonCosmosAssets(cacheDirectory: String): ProviderYaml? {
        val nonCosmosAssetsCachedFile = File("$cacheDirectory/$CACHED_NON_COSMOS_ASSETS_FILE_NAME")
        if (nonCosmosAssetsCachedFile.exists()) {
            try {
                val cachedNonCosmosAssets: ProviderYaml = mapper.readValue(nonCosmosAssetsCachedFile)
                logger.info("Successfully loaded cached non-cosmos assets.")
                return cachedNonCosmosAssets
            } catch (e: JacksonYAMLParseException) {
                logger.error("Error parsing cached non-cosmos assets yaml.", e)
            }
        } else {
            logger.warn("No cached non-cosmos assets file exists: {}", nonCosmosAssetsCachedFile.absolutePath)
        }

        return null
    }

    companion object {
        private val mapper = ObjectMapper(YAMLFactory()).registerModule(KotlinModule.Builder().build())
    }
}
