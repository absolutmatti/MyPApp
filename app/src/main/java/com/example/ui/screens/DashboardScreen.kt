package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.MainViewModel

@Composable
fun DashboardScreen(viewModel: MainViewModel) {
  val isConnected by viewModel.isConnected.collectAsState()
  val uptimeSeconds by viewModel.uptimeSeconds.collectAsState()
  val bytesTransferred by viewModel.bytesTransferred.collectAsState()
  val strategies by viewModel.strategies.collectAsState()
  val categories by viewModel.siteCategories.collectAsState()

  val activeStrategy = strategies.find { it.isSelected } ?: strategies.first()

  val buttonColor by animateColorAsState(
    targetValue = if (isConnected) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
    label = "buttonColor"
  )

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Header title
    Text(
      text = "ByeByeDPI Control Center",
      style = MaterialTheme.typography.headlineMedium,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.onSurface
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = "Advanced DPI Bypass & Proxy Engine",
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(24.dp))

    // Big Connection Button / Shield
    Box(
      modifier = Modifier
        .size(180.dp)
        .clip(CircleShape)
        .background(
          if (isConnected) MaterialTheme.colorScheme.errorContainer
          else MaterialTheme.colorScheme.primaryContainer
        ),
      contentAlignment = Alignment.Center
    ) {
      Button(
        onClick = { viewModel.toggleConnection() },
        modifier = Modifier
          .size(150.dp)
          .testTag("connection_button"),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(
            imageVector = if (isConnected) Icons.Default.Lock else Icons.Default.LockOpen,
            contentDescription = "Connection Toggle",
            modifier = Modifier.size(48.dp),
            tint = Color.White
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = if (isConnected) "STOP" else "START",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Status Badge
    Surface(
      shape = RoundedCornerShape(16.dp),
      color = if (isConnected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
      modifier = Modifier.padding(vertical = 4.dp)
    ) {
      Row(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(12.dp)
            .clip(CircleShape)
            .background(if (isConnected) Color(0xFF10B981) else Color.Gray)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = if (isConnected) "Status: Protected & Running" else "Status: Disconnected",
          fontWeight = FontWeight.SemiBold,
          color = MaterialTheme.colorScheme.onSurface
        )
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Stats Card (Uptime & Traffic)
    if (isConnected) {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          horizontalArrangement = Arrangement.SpaceAround
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Timer, contentDescription = "Uptime", tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Uptime", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            val mins = uptimeSeconds / 60
            val secs = uptimeSeconds % 60
            Text(text = String.format("%02d:%02d", mins, secs), fontWeight = FontWeight.Bold)
          }
          Divider(modifier = Modifier
            .height(40.dp)
            .width(1.dp))
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.SwapVert, contentDescription = "Traffic", tint = MaterialTheme.colorScheme.secondary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Traffic", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            val kb = bytesTransferred / 1024
            Text(text = "$kb KB", fontWeight = FontWeight.Bold)
          }
        }
      }
      Spacer(modifier = Modifier.height(16.dp))
    }

    // Active Strategy Card
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Tune, contentDescription = "Strategy", tint = MaterialTheme.colorScheme.primary)
          Spacer(modifier = Modifier.width(8.dp))
          Text(text = "Active Strategy", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = activeStrategy.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
        Text(text = activeStrategy.command, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = activeStrategy.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Quick Site Bypass Summary Card
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.VerifiedUser, contentDescription = "Bypass Lists", tint = MaterialTheme.colorScheme.secondary)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Active Bypass Lists", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
          }
          val activeCount = categories.count { it.isEnabled }
          Text(text = "$activeCount / ${categories.size} enabled", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          categories.take(3).forEach { cat ->
            AssistChip(
              onClick = { viewModel.toggleCategory(cat.id) },
              label = { Text(cat.name, fontSize = 12.sp) },
              leadingIcon = {
                Box(
                  modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(if (cat.isEnabled) Color(0xFF10B981) else Color.Gray)
                )
              }
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(32.dp))
  }
}
