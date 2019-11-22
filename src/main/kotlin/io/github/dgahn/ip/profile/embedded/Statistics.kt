package io.github.dgahn.ip.profile.embedded

import javax.persistence.Embeddable


@Embeddable
data class Statistics(
    val max: Double,
    val min: Double,
    val avg: Double
)
