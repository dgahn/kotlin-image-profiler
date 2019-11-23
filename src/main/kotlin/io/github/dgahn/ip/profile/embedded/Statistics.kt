package io.github.dgahn.ip.profile.embedded

import javax.persistence.Embeddable


@Embeddable
class Statistics() {
    var rMax = 0
        set(value) {
            if (rMax < value) field = value
        }
    var gMax = 0
        set(value) {
            if (gMax < value) field = value
        }
    var bMax = 0
        set(value) {
            if (bMax < value) field = value
        }
    var rMin = 255
        set(value) {
            if (rMin > value) field = value
        }
    var bMin = 255
        set(value) {
            if (bMin > value) field = value
        }
    var gMin = 255
        set(value) {
            if (gMin > value) field = value
        }
    var rAvg: Double = 0.0
    var bAvg: Double = 0.0
    var gAvg: Double = 0.0

    constructor(
        rMax: Int, gMax: Int, bMax: Int,
        rMin: Int, bMin: Int, gMin: Int,
        rAvg: Double, bAvg: Double, gAvg: Double
    ): this() {
        this.rMax = rMax
        this.gMax = gMax
        this.bMax = bMax
        this.rMin = rMin
        this.bMin = bMin
        this.gMin = gMin
        this.rAvg = rAvg
        this.bAvg = bAvg
        this.gAvg = gAvg
    }
}
