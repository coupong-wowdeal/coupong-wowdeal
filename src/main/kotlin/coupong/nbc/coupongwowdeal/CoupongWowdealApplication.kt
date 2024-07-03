package coupong.nbc.coupongwowdeal

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class CoupongWowdealApplication

fun main(args: Array<String>) {
    runApplication<CoupongWowdealApplication>(*args)
}
