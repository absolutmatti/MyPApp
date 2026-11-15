package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.ui.screens.*
import com.example.ui.theme.ByeByeDPITheme
import com.example.viewmodel.MyPGalleryViewModel

class MainActivity : ComponentActivity() {

  private val viewModel: MyPGalleryViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      ByeByeDPITheme {
        var currentTab by remember { mutableStateOf<NavigationItem>(NavigationItem.Explore) }
        val activePlayerMedia by viewModel.activePlayerMedia.collectAsState()

        Scaffold(
          modifier = Modifier.fillMaxSize(),
          bottomBar = {
            if (activePlayerMedia == null) {
              NavigationBar {
                val items = listOf<NavigationItem>(
                  NavigationItem.Explore,
                  NavigationItem.Favorites,
                  NavigationItem.Downloads,
                  NavigationItem.Sync,
                  NavigationItem.Settings
                )
                items.forEach { item ->
                  NavigationBarItem(
                    icon = { Icon(item.icon, contentDescription = item.title) },
                    label = { Text(item.title) },
                    selected = currentTab == item,
                    onClick = { currentTab = item }
                  )
                }
              }
            }
          }
        ) { innerPadding ->
          val modifier = Modifier.padding(innerPadding)
          if (activePlayerMedia != null) {
            PlayerScreen(
              viewModel = viewModel,
              mediaItem = activePlayerMedia!!,
              onBack = { viewModel.setActivePlayerMedia(null) }
            )
          } else {
            when (currentTab) {
              is NavigationItem.Explore -> ExploreScreen(viewModel) { item -> viewModel.setActivePlayerMedia(item) }
              is NavigationItem.Favorites -> FavoritesHistoryScreen(viewModel) { item -> viewModel.setActivePlayerMedia(item) }
              is NavigationItem.Downloads -> DownloadsScreen(viewModel) { item -> viewModel.setActivePlayerMedia(item) }
              is NavigationItem.Sync -> SyncScreen(viewModel)
              is NavigationItem.Settings -> SettingsScreen(viewModel)
            }
          }
        }
      }
    }
  }
}

sealed class NavigationItem(val title: String, val icon: ImageVector) {
  object Explore : NavigationItem("Explore", Icons.Default.Explore)
  object Favorites : NavigationItem("Library", Icons.Default.Favorite)
  object Downloads : NavigationItem("Downloads", Icons.Default.Download)
  object Sync : NavigationItem("Wi-Fi Sync", Icons.Default.Sync)
  object Settings : NavigationItem("Settings", Icons.Default.Settings)
}
