package coupong.nbc.coupongwowdeal.domain.timedeal.repository.v1

interface TimeDealRepository {
    fun save(timeDeal: TimeDeal): TimeDeal
    fun findAll(): List<TimeDeal>
    fun findById(timeDealId: Long): TimeDeal?
    fun deleteById(timeDealId: Long)
}