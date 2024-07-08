package coupong.nbc.coupongwowdeal.domain.common.aop

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class Transactional(
    _advice: TransactionalAdvice
) {

    init {
        advice = _advice
    }

    companion object {
        private lateinit var advice: TransactionalAdvice

        operator fun <T> invoke(function: () -> T): T {
            return advice(function)
        }
    }

    @Component
    class TransactionalAdvice {

        @Transactional
        operator fun <T> invoke(business: () -> T): T {
            return business()
        }
    }
}