package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.model.MediaItem
import com.example.viewmodel.MyPGalleryViewModel

@Composable
fun PlayerScreen(viewModel: MyPGalleryViewModel, mediaItem: MediaItem, onBack: () -> Unit) {
  var isPlaying by remember { mutableStateOf(true) }
  var playbackSpeed by remember { mutableStateOf(1.0f) }
  var audioBoost by remember { mutableStateOf(false) }
  var selectedQuality by remember { mutableStateOf("1080p") }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp)
  ) {
    // Top Bar
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = onBack) {
        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
      }
      Spacer(modifier = Modifier.width(8.dp))
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = mediaItem.title,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          maxLines = 1
        )
        Text(
          text = "${mediaItem.siteName} • Performer: ${mediaItem.performer}",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Video Player Box with Controls Simulation
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .height(260.dp),
      shape = RoundedCornerShape(20.dp)
    ) {
      Box(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
          model = mediaItem.thumbnailUrl,
          contentDescription = mediaItem.title,
          modifier = Modifier.fillMaxSize(),
          contentScale = ContentScale.Crop
        )

        // Overlay controls
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.35f)),
          contentAlignment = Alignment.Center
        ) {
          Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            IconButton(onClick = { }) {
              Icon(Icons.Default.Replay10, contentDescription = "Rewind", tint = Color.White, modifier = Modifier.size(32.dp))
            }
            FloatingActionButton(
              onClick = { isPlaying = !isPlaying },
              containerColor = MaterialTheme.colorScheme.primary
            ) {
              Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = "Play/Pause",
                tint = Color.White
              )
            }
            IconButton(onClick = { }) {
              Icon(Icons.Default.Forward10, contentDescription = "Forward", tint = Color.White, modifier = Modifier.size(32.dp))
            }
          }
        }

        // Quality & Speed badges
        Row(
          modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(12.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.Black.copy(alpha = 0.6f)
          ) {
            Text(
              text = selectedQuality,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              style = MaterialTheme.typography.labelSmall,
              color = Color.White,
              fontWeight = FontWeight.Bold
            )
          }
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.Black.copy(alpha = 0.6f)
          ) {
            Text(
              text = "${playbackSpeed}x",
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              style = MaterialTheme.typography.labelSmall,
              color = Color.White,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Advanced Player Controls Card (Audio Boost, Speed, Quality)
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Playback Controls & Enhancement", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = "Audio Normalization & >100% Boost")
          Switch(
            checked = audioBoost,
            onCheckedChange = { audioBoost = it }
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = "Playback Speed")
          Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf(1.0f, 1.5f, 2.0f).forEach { speed ->
              FilterChip(
                selected = playbackSpeed == speed,
                onClick = { playbackSpeed = speed },
                label = { Text("${speed}x") }
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceAround
        ) {
          Button(onClick = { viewModel.toggleFavorite(mediaItem.id) }) {
            Icon(Icons.Default.Favorite, contentDescription = null)
            Spacer(modifier = Modifier.width(4.dp))
            Text(if (mediaItem.isFavorite) "Favorited" else "Favorite")
          }
          Button(onClick = { viewModel.toggleDownload(mediaItem.id) }) {
            Icon(Icons.Default.Download, contentDescription = null)
            Spacer(modifier = Modifier.width(4.dp))
            Text(if (mediaItem.isDownloaded) "Downloaded" else "Download")
          }
        }
      }
    }
  }
}
