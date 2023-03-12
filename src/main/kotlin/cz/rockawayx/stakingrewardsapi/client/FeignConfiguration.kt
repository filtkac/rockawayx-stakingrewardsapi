package cz.rockawayx.stakingrewardsapi.client

import feign.Logger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FeignConfiguration {

    @Bean
    fun feignLoggerLevel() = Logger.Level.BASIC
}
