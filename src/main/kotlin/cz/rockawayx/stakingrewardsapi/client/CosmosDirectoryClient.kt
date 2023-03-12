package cz.rockawayx.stakingrewardsapi.client

import cz.rockawayx.stakingrewardsapi.model.ProviderResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@FeignClient(value = "cosmos-directory", url = "\${app.rockawayx-validators-cosmos-url}")
interface CosmosDirectoryClient {

    @RequestMapping(method = [RequestMethod.GET])
    fun getCosmosAssets(): ProviderResponse
}
