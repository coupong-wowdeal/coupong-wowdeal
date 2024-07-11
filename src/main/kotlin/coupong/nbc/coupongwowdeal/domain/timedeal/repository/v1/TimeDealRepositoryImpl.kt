package coupong.nbc.coupongwowdeal.domain.timedeal.repository.v1

import coupong.nbc.coupongwowdeal.domain.timedeal.model.v1.TimeDeal
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class TimeDealRepositoryImpl(
    private val timeDealJpaRepository: TimeDealJpaRepository
): TimeDealRepository {
    override fun save(timeDeal: TimeDeal): TimeDeal {
        return timeDealJpaRepository.save(timeDeal)
    }

    override fun findAll(): List<TimeDeal> {
        return timeDealJpaRepository.findAll()
    }

    override fun findById(timeDealId: Long): TimeDeal? {
        return timeDealJpaRepository.findByIdOrNull(timeDealId)
    }

    override fun deleteById(timeDealId: Long) {
        return timeDealJpaRepository.deleteById(timeDealId)
    }
}