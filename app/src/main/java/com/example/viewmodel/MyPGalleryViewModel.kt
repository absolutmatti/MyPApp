package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.MediaItem
import com.example.model.MediaType
import com.example.model.SitePlugin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyPGalleryViewModel : ViewModel() {

  private val _sites = MutableStateFlow(
    listOf(
      SitePlugin("1", "AdultStream HD", "Videos", 14500, true, "🔥"),
      SitePlugin("2", "CamLive Show", "Livestreams", 820, true, "🎥"),
      SitePlugin("3", "PhotoSets Vault", "Galleries", 32000, true, "📸"),
      SitePlugin("4", "HentaiNet & Comics", "Manga", 9400, true, "🎌"),
      SitePlugin("5", "ForumPlaza Creators", "Community", 18500, true, "💬")
    )
  )
  val sites: StateFlow<List<SitePlugin>> = _sites.asStateFlow()

  private val _mediaList = MutableStateFlow(
    listOf(
      MediaItem(
        id = "m1",
        title = "Cinematic Sunset Session & Exclusive Photoshoot",
        siteName = "AdultStream HD",
        category = "Cinematic",
        performer = "Elena Vance",
        tags = listOf("hd", "exclusive", "sunset", "outdoors"),
        type = MediaType.VIDEO,
        thumbnailUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=800",
        duration = "24:15",
        fileSize = "450 MB",
        watchProgress = 0.45f,
        isFavorite = true,
        isDownloaded = true
      ),
      MediaItem(
        id = "m2",
        title = "Neon Cyberpunk Alleyway Photoset Vol. 4",
        siteName = "PhotoSets Vault",
        category = "Abstract",
        performer = "Kira Neko",
        tags = listOf("cyberpunk", "neon", "studio"),
        type = MediaType.PHOTO_GALLERY,
        thumbnailUrl = "https://images.unsplash.com/photo-1519501025264-65ba15a82390?w=800",
        fileSize = "120 MB",
        isFavorite = false,
        isDownloaded = false
      ),
      MediaItem(
        id = "m3",
        title = "Misty Mountain Escape Livestream Recording",
        siteName = "CamLive Show",
        category = "Nature",
        performer = "Sophie Laurent",
        tags = listOf("cam", "live", "nature"),
        type = MediaType.LIVESTREAM,
        thumbnailUrl = "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?w=800",
        duration = "01:32:00",
        fileSize = "1.2 GB",
        watchProgress = 0.1f,
        isFavorite = true,
        isDownloaded = false
      ),
      MediaItem(
        id = "m4",
        title = "Modern Architecture & Interior Showcase",
        siteName = "ForumPlaza Creators",
        category = "Architecture",
        performer = "Studio Alpha",
        tags = listOf("architecture", "interior", "luxury"),
        type = MediaType.VIDEO,
        thumbnailUrl = "https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?w=800",
        duration = "18:40",
        fileSize = "320 MB",
        watchProgress = 0.9f,
        isFavorite = false,
        isDownloaded = true
      ),
      MediaItem(
        id = "m5",
        title = "Short Clip: Neon Lights Bounce",
        siteName = "AdultStream HD",
        category = "Shorts",
        performer = "Kira Neko",
        tags = listOf("short", "vertical", "neon"),
        type = MediaType.SHORT,
        thumbnailUrl = "https://images.unsplash.com/photo-1541701494587-cb58502866ab?w=800",
        duration = "00:45",
        fileSize = "18 MB",
        isFavorite = true,
        isDownloaded = false
      )
    )
  )
  val mediaList: StateFlow<List<MediaItem>> = _mediaList.asStateFlow()

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _selectedCategory = MutableStateFlow("All")
  val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

  private val _activePlayerMedia = MutableStateFlow<MediaItem?>(null)
  val activePlayerMedia: StateFlow<MediaItem?> = _activePlayerMedia.asStateFlow()

  // Settings & Customization
  private val _currentTheme = MutableStateFlow("Tokyo Night")
  val currentTheme: StateFlow<String> = _currentTheme.asStateFlow()

  private val _isPinProtected = MutableStateFlow(false)
  val isPinProtected: StateFlow<Boolean> = _isPinProtected.asStateFlow()

  private val _syncCode = MutableStateFlow("")
  val syncCode: StateFlow<String> = _syncCode.asStateFlow()

  private val _isSynced = MutableStateFlow(false)
  val isSynced: StateFlow<Boolean> = _isSynced.asStateFlow()

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun setCategory(category: String) {
    _selectedCategory.value = category
  }

  fun setActivePlayerMedia(item: MediaItem?) {
    _activePlayerMedia.value = item
  }

  fun toggleFavorite(id: String) {
    _mediaList.update { list ->
      list.map { if (it.id == id) it.copy(isFavorite = !it.isFavorite) else it }
    }
  }

  fun toggleDownload(id: String) {
    _mediaList.update { list ->
      list.map { if (it.id == id) it.copy(isDownloaded = !it.isDownloaded) else it }
    }
  }

  fun setTheme(themeName: String) {
    _currentTheme.value = themeName
  }

  fun togglePinProtection(enabled: Boolean) {
    _isPinProtected.value = enabled
  }

  fun generateSyncCode() {
    val code = (100000..999999).random().toString()
    _syncCode.value = code
  }

  fun pairAndSync() {
    viewModelScope.launch {
      delay(1200)
      _isSynced.value = true
    }
  }
}
