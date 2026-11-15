package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.MediaItem
import com.example.model.MediaType
import com.example.viewmodel.MyPGalleryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(viewModel: MyPGalleryViewModel, onMediaClick: (MediaItem) -> Unit) {
  val sites by viewModel.sites.collectAsState()
  val mediaList by viewModel.mediaList.collectAsState()
  val searchQuery by viewModel.searchQuery.collectAsState()
  val selectedCategory by viewModel.selectedCategory.collectAsState()

  val categories = listOf("All", "Cinematic", "Nature", "Abstract", "Architecture", "Shorts")

  val filteredList = mediaList.filter { item ->
    val matchesSearch = item.title.contains(searchQuery, ignoreCase = true) ||
      item.performer.contains(searchQuery, ignoreCase = true) ||
      item.tags.any { it.contains(searchQuery, ignoreCase = true) }
    val matchesCategory = selectedCategory == "All" || item.category == selectedCategory
    matchesSearch && matchesCategory
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp)
  ) {
    // Header & Title
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "MyPGallery v1.1.2",
          style = MaterialTheme.typography.headlineMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary
        )
        Text(
          text = "Multi-site adult content & media explorer",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
      IconButton(onClick = { }) {
        Icon(Icons.Default.Verified, contentDescription = "Verified UnTouched", tint = MaterialTheme.colorScheme.primary)
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Sites Plugins Row
    Text(text = "Active Site Plugins (35+)", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
    Spacer(modifier = Modifier.height(6.dp))
    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      items(sites) { site ->
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = MaterialTheme.colorScheme.surfaceVariant,
          modifier = Modifier.height(40.dp)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(text = site.iconEmoji, fontSize = 16.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = site.name, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Multi-site Search Bar
    OutlinedTextField(
      value = searchQuery,
      onValueChange = { viewModel.setSearchQuery(it) },
      modifier = Modifier.fillMaxWidth(),
      placeholder = { Text("Search performer, tag, or category across sites...") },
      leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
      shape = RoundedCornerShape(16.dp),
      singleLine = true
    )

    Spacer(modifier = Modifier.height(10.dp))

    // Category Filter Chips
    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      items(categories) { cat ->
        FilterChip(
          selected = selectedCategory == cat,
          onClick = { viewModel.setCategory(cat) },
          label = { Text(cat) }
        )
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Media Grid
    LazyVerticalGrid(
      columns = GridCells.Fixed(2),
      horizontalArrangement = Arrangement.spacedBy(12.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp),
      modifier = Modifier.fillMaxSize()
    ) {
      items(filteredList) { item ->
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .height(230.dp)
            .clickable { onMediaClick(item) },
          shape = RoundedCornerShape(16.dp)
        ) {
          Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
              model = item.thumbnailUrl,
              contentDescription = item.title,
              modifier = Modifier.fillMaxSize(),
              contentScale = ContentScale.Crop
            )

            // Source site badge
            Surface(
              modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp),
              shape = RoundedCornerShape(8.dp),
              color = Color.Black.copy(alpha = 0.7f)
            ) {
              Text(
                text = item.siteName,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                style = MaterialTheme.typography.labelSmall,
                color = Color.White
              )
            }

            // Duration badge
            if (item.duration.isNotEmpty()) {
              Surface(
                modifier = Modifier
                  .align(Alignment.TopEnd)
                  .padding(8.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color.Black.copy(alpha = 0.7f)
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                  Spacer(modifier = Modifier.width(2.dp))
                  Text(text = item.duration, style = MaterialTheme.typography.labelSmall, color = Color.White)
                }
              }
            }

            // Bottom Info Overlay
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color.Black.copy(alpha = 0.75f))
                .padding(8.dp)
            ) {
              Column {
                Text(
                  text = item.title,
                  style = MaterialTheme.typography.bodyMedium,
                  fontWeight = FontWeight.Bold,
                  color = Color.White,
                  maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = item.performer,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Cyan
                  )
                  Row {
                    IconButton(
                      onClick = { viewModel.toggleFavorite(item.id) },
                      modifier = Modifier.size(24.dp)
                    ) {
                      Icon(
                        imageVector = if (item.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (item.isFavorite) Color.Red else Color.White,
                        modifier = Modifier.size(18.dp)
                      )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(
                      onClick = { viewModel.toggleDownload(item.id) },
                      modifier = Modifier.size(24.dp)
                    ) {
                      Icon(
                        imageVector = if (item.isDownloaded) Icons.Default.CloudDone else Icons.Default.Download,
                        contentDescription = "Download",
                        tint = if (item.isDownloaded) Color(0xFF10B981) else Color.White,
                        modifier = Modifier.size(18.dp)
                      )
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
