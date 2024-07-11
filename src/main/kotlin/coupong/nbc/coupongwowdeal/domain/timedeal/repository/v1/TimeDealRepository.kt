package coupong.nbc.coupongwowdeal.domain.timedeal.repository.v1

import coupong.nbc.coupongwowdeal.domain.timedeal.model.v1.TimeDeal

interface TimeDealRepository {
    fun save(timeDeal: TimeDeal): TimeDeal
    fun findAll(): List<TimeDeal>
    fun findById(timeDealId: Long): TimeDeal?
    fun deleteById(timeDealId: Long)
}