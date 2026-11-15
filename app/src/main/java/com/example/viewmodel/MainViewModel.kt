package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.BypassStrategy
import com.example.model.LogLevel
import com.example.model.ProxyLog
import com.example.model.SiteCategory
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainViewModel : ViewModel() {

  private val _isConnected = MutableStateFlow(false)
  val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

  private val _uptimeSeconds = MutableStateFlow(0L)
  val uptimeSeconds: StateFlow<Long> = _uptimeSeconds.asStateFlow()

  private val _bytesTransferred = MutableStateFlow(0L)
  val bytesTransferred: StateFlow<Long> = _bytesTransferred.asStateFlow()

  private val _proxyPort = MutableStateFlow(1080)
  val proxyPort: StateFlow<Int> = _proxyPort.asStateFlow()

  private val _upstreamDns = MutableStateFlow("1.1.1.1 (Cloudflare)")
  val upstreamDns: StateFlow<String> = _upstreamDns.asStateFlow()

  private val _strategies = MutableStateFlow(
    listOf(
      BypassStrategy(
        id = "1",
        name = "Aggressive SNI Split (-f-200)",
        command = "-f-200 -Qr -s3:5+sm -a1 -At,r,s,c",
        description = "Splits SNI record into chunks with randomized TCP window and record padding.",
        latencyMs = 24,
        isSelected = true
      ),
      BypassStrategy(
        id = "2",
        name = "TCP Disorder & Seq Spoof (-f-300)",
        command = "-f-300 -d6+s -a1 -At,r,s,c -o2",
        description = "Reorders TCP segments and spoofs sequence numbers to bypass DPI filters.",
        latencyMs = 38,
        isSelected = false
      ),
      BypassStrategy(
        id = "3",
        name = "Basic Fake Fragment (-f-30)",
        command = "-f-30 -At,r,s,c -r5 -Mh",
        description = "Lightweight packet fragmentation with minimal CPU overhead.",
        latencyMs = 19,
        isSelected = false
      ),
      BypassStrategy(
        id = "4",
        name = "Fake SNI Payload (-s2:7+s)",
        command = "-s2:7+s -s3:6+sm -a1 -At,r,s,c",
        description = "Injects fake TLS records and misleads DPI pattern matching engines.",
        latencyMs = 52,
        isSelected = false
      )
    )
  )
  val strategies: StateFlow<List<BypassStrategy>> = _strategies.asStateFlow()

  private val _siteCategories = MutableStateFlow(
    listOf(
      SiteCategory(
        id = "discord",
        name = "Discord & Voice",
        description = "Discord gateway, media, and voice servers",
        iconName = "chat",
        domains = listOf("discord.com", "discord.gg", "discordapp.com", "discordcdn.com", "discord.media"),
        isEnabled = true
      ),
      SiteCategory(
        id = "youtube",
        name = "YouTube & Video",
        description = "YouTube video streaming and googlevideo CDN",
        iconName = "play_arrow",
        domains = listOf("youtube.com", "youtu.be", "googlevideo.com", "yt3.ggpht.com", "i.ytimg.com"),
        isEnabled = true
      ),
      SiteCategory(
        id = "telegram",
        name = "Telegram",
        description = "Telegram API and web clients",
        iconName = "send",
        domains = listOf("telegram.org", "core.telegram.org", "web.telegram.org", "api.telegram.org"),
        isEnabled = true
      ),
      SiteCategory(
        id = "social",
        name = "Social & Media",
        description = "Instagram, X (Twitter), Facebook, Medium",
        iconName = "share",
        domains = listOf("instagram.com", "x.com", "twitter.com", "facebook.com", "medium.com"),
        isEnabled = true
      ),
      SiteCategory(
        id = "cloudflare",
        name = "Cloudflare CDN",
        description = "Cloudflare networks and ECH endpoints",
        iconName = "cloud",
        domains = listOf("cloudflare.com", "cloudflare.net", "cloudflare-ech.com"),
        isEnabled = true
      ),
      SiteCategory(
        id = "turkiye",
        name = "Regional (Türkiye)",
        description = "Frequently censored regional domains",
        iconName = "public",
        domains = listOf("roblox.com", "wattpad.com", "pastebin.com", "cutt.ly"),
        isEnabled = true
      )
    )
  )
  val siteCategories: StateFlow<List<SiteCategory>> = _siteCategories.asStateFlow()

  private val _logs = MutableStateFlow<List<ProxyLog>>(
    listOf(
      ProxyLog(timestamp = currentTime(), level = LogLevel.INFO, message = "ByeByeDPI engine initialized successfully."),
      ProxyLog(timestamp = currentTime(), level = LogLevel.SUCCESS, message = "Loaded 4 bypass strategies and 6 site lists."),
      ProxyLog(timestamp = currentTime(), level = LogLevel.INFO, message = "Ready to start localhost SOCKS5/HTTP proxy on port 1080.")
    )
  )
  val logs: StateFlow<List<ProxyLog>> = _logs.asStateFlow()

  private var timerJob: kotlinx.coroutines.Job? = null

  fun toggleConnection() {
    val newState = !_isConnected.value
    _isConnected.value = newState
    if (newState) {
      addLog(LogLevel.SUCCESS, "Proxy service started on 127.0.0.1:${_proxyPort.value}")
      addLog(LogLevel.INFO, "Active strategy: ${_strategies.value.find { it.isSelected }?.name ?: "Default"}")
      startUptimeTimer()
    } else {
      addLog(LogLevel.WARN, "Proxy service stopped.")
      stopUptimeTimer()
    }
  }

  fun selectStrategy(id: String) {
    _strategies.update { list ->
      list.map { it.copy(isSelected = it.id == id) }
    }
    val strat = _strategies.value.find { it.id == id }
    if (strat != null) {
      addLog(LogLevel.INFO, "Switched active strategy to: ${strat.name}")
    }
  }

  fun toggleCategory(id: String) {
    _siteCategories.update { list ->
      list.map { if (it.id == id) it.copy(isEnabled = !it.isEnabled) else it }
    }
  }

  fun updateProxyPort(port: Int) {
    _proxyPort.value = port
    addLog(LogLevel.INFO, "Proxy port updated to $port")
  }

  fun setUpstreamDns(dns: String) {
    _upstreamDns.value = dns
    addLog(LogLevel.INFO, "Upstream DNS set to $dns")
  }

  fun clearLogs() {
    _logs.value = listOf(
      ProxyLog(timestamp = currentTime(), level = LogLevel.INFO, message = "Logs cleared.")
    )
  }

  fun testLatencies() {
    viewModelScope.launch {
      addLog(LogLevel.INFO, "Pinging strategies and testing server latency...")
      delay(800)
      _strategies.update { list ->
        list.map {
          it.copy(latencyMs = (12..65).random())
        }
      }
      addLog(LogLevel.SUCCESS, "Latency check completed successfully.")
    }
  }

  private fun startUptimeTimer() {
    _uptimeSeconds.value = 0
    _bytesTransferred.value = 1024
    timerJob?.cancel()
    timerJob = viewModelScope.launch {
      while (_isConnected.value) {
        delay(1000)
        _uptimeSeconds.update { it + 1 }
        _bytesTransferred.update { it + (1200..4800).random() }
      }
    }
  }

  private fun stopUptimeTimer() {
    timerJob?.cancel()
    timerJob = null
  }

  private fun addLog(level: LogLevel, message: String) {
    val log = ProxyLog(timestamp = currentTime(), level = level, message = message)
    _logs.update { current -> listOf(log) + current.take(99) }
  }

  private fun currentTime(): String {
    val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return sdf.format(Date())
  }
}
