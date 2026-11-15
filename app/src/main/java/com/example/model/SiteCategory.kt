package com.example.model

data class SiteCategory(
  val id: String,
  val name: String,
  val description: String,
  val iconName: String,
  val domains: List<String>,
  val isEnabled: Boolean = true
)
