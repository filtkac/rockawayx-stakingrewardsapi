package cz.rockawayx.stakingrewardsapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class StakingRewardsApiApplication

fun main(args: Array<String>) {
	runApplication<StakingRewardsApiApplication>(*args)
}
