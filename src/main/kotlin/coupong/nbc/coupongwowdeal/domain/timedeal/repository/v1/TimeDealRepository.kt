package coupong.nbc.coupongwowdeal.domain.timedeal.repository.v1

import coupong.nbc.coupongwowdeal.domain.timedeal.model.v1.TimeDeal
import org.springframework.data.jpa.repository.JpaRepository

interface TimeDealRepository : JpaRepository<TimeDeal, Long>