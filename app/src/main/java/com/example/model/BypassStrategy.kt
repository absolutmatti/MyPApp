package com.example.model

data class BypassStrategy(
  val id: String,
  val name: String,
  val command: String,
  val description: String,
  val latencyMs: Int = 0,
  val isSelected: Boolean = false
)
