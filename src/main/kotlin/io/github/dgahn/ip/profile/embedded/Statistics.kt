package io.github.dgahn.ip.profile.embedded

import javax.persistence.Embeddable


@Embeddable
data class Statistics(
    var rMax: Int = 0,
    var gMax: Int = 0,
    var bMax: Int = 0,
    var rMin: Int = 255,
    var bMin: Int = 255,
    var gMin: Int = 255,
    var rAvg: Double = 0.0,
    var bAvg: Double = 0.0,
    var gAvg: Double = 0.0
)