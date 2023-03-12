package cz.rockawayx.stakingrewardsapi.controller

import cz.rockawayx.stakingrewardsapi.model.ProviderResponse
import cz.rockawayx.stakingrewardsapi.service.StakingRewardsService
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/provider")
class StakingRewardsController(
    private val stakingRewardsService: StakingRewardsService
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getProvider(): ProviderResponse {
        return stakingRewardsService.getProvider();
    }
}
