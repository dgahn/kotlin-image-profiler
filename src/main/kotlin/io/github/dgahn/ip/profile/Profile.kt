package io.github.dgahn.ip.profile

import io.github.dgahn.ip.profile.embedded.Histogram
import io.github.dgahn.ip.profile.embedded.MetaInfo
import io.github.dgahn.ip.profile.embedded.Statistics
import javax.persistence.Column
import javax.persistence.ColumnResult
import javax.persistence.ConstructorResult
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SqlResultSetMapping


@SqlResultSetMapping(name = "ProfileSummaryMapping",
    classes = [
        ConstructorResult(targetClass = ProfileSummaryDto::class, columns = [
            ColumnResult(name = "id", type = Long::class),
            ColumnResult(name = "name", type = String::class)
        ])
    ]
)
@Entity
data class Profile(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "profile_id")
    var id: Long? = null,
    @Embedded
    val metaInfo: MetaInfo,
    @Embedded
    val statistics: Statistics,
    @Embedded
    val histogram: Histogram
)
