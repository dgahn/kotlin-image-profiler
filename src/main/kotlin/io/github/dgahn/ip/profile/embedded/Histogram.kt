package io.github.dgahn.ip.profile.embedded

import javax.persistence.Column
import javax.persistence.Embeddable


@Embeddable
data class Histogram(
    @Column(
        name = "histograms",
        columnDefinition = "VARBINARY(3000)"
    )
    val values: LongArray
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Histogram

        if (!values.contentEquals(other.values)) return false

        return true
    }

    override fun hashCode(): Int {
        return values.contentHashCode()
    }

}
