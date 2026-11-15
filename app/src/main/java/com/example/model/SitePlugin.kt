package com.example.model

data class SitePlugin(
  val id: String,
  val name: String,
  val type: String,
  val totalVideos: Int,
  val isConnected: Boolean = true,
  val iconEmoji: String
)
