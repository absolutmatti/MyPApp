package com.example.model

data class MediaItem(
  val id: String,
  val title: String,
  val siteName: String,
  val category: String,
  val performer: String,
  val tags: List<String>,
  val type: MediaType,
  val thumbnailUrl: String,
  val duration: String = "",
  val fileSize: String = "18.4 MB",
  val watchProgress: Float = 0f, // 0.0 to 1.0
  val isFavorite: Boolean = false,
  val isDownloaded: Boolean = false
)

enum class MediaType {
  VIDEO, PHOTO_GALLERY, LIVESTREAM, SHORT, COMIC
}
