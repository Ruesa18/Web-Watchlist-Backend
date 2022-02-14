package ch.ruefenacht.sandro.webwatchlist.dto

import java.util.*

data class UserShowDto(val uuid: UUID, val username: String, val firstname: String, val lastname: String, val email: String, val favorites: Set<MediaShowDto>, val watchlist: Set<MediaShowDto>)
