package io.github.dgahn.ip.profile.embedded

import javax.persistence.Embeddable


@Embeddable
data class Metadata(
    val name: String,
    val width: Int,
    val height: Int,
    val shootingTime: String
)
