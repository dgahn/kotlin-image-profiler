package io.github.dgahn.ip.profile.embedded

import javax.persistence.Embeddable


@Embeddable
data class Statistics(
    val average: Double,
    val min: Double,
    val max: Double
)
