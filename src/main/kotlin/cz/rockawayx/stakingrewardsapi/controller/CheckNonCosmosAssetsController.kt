package cz.rockawayx.stakingrewardsapi.controller

import cz.rockawayx.stakingrewardsapi.model.ProviderYaml
import cz.rockawayx.stakingrewardsapi.service.StakingRewardsService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/check-non-cosmos-assets")
class CheckNonCosmosAssetsController(
    private val stakingRewardsService: StakingRewardsService
) {

    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun checkNonCosmosAssets(): ProviderYaml {
        return stakingRewardsService.checkNonCosmosAssets()
    }
}
