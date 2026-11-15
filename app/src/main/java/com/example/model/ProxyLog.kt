package com.example.model

data class ProxyLog(
  val id: String = java.util.UUID.randomUUID().toString(),
  val timestamp: String,
  val level: LogLevel,
  val message: String
)

enum class LogLevel {
  INFO, SUCCESS, WARN, ERROR
}
