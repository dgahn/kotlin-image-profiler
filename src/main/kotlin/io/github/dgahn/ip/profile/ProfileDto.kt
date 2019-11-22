package io.github.dgahn.ip.profile

import java.io.File


data class ProfileSummaryDto(val id: Long, val name: String)

data class ProfileRegisterDto(val file: File, val name: String)
