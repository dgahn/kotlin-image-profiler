package io.github.dgahn.ip.profile

import io.github.dgahn.ip.profile.embedded.Histogram
import io.github.dgahn.ip.profile.embedded.Metadata
import io.github.dgahn.ip.profile.embedded.Statistics
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id


@Entity
data class Profile(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "profile_id")
    var id: Long? = null,
    @Embedded
    val metadata: Metadata,
    @Embedded
    val statistics: Statistics,
    @Embedded
    val histogram: Histogram
)
