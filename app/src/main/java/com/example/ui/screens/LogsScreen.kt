package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LogLevel
import com.example.viewmodel.MainViewModel

@Composable
fun LogsScreen(viewModel: MainViewModel) {
  val logs by viewModel.logs.collectAsState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "Connection Logs",
          style = MaterialTheme.typography.headlineMedium,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = "Real-time proxy and DPI bypass events",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
      IconButton(onClick = { viewModel.clearLogs() }) {
        Icon(Icons.Default.Delete, contentDescription = "Clear Logs")
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Card(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(logs) { log ->
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
          ) {
            Box(
              modifier = Modifier
                .padding(top = 4.dp)
                .size(8.dp)
                .clip(CircleShape)
                .background(
                  when (log.level) {
                    LogLevel.INFO -> Color(0xFF3B82F6)
                    LogLevel.SUCCESS -> Color(0xFF10B981)
                    LogLevel.WARN -> Color(0xFFF59E0B)
                    LogLevel.ERROR -> Color(0xFFEF4444)
                  }
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = log.level.name,
                  style = MaterialTheme.typography.labelSmall,
                  fontWeight = FontWeight.Bold,
                  color = when (log.level) {
                    LogLevel.INFO -> Color(0xFF3B82F6)
                    LogLevel.SUCCESS -> Color(0xFF10B981)
                    LogLevel.WARN -> Color(0xFFF59E0B)
                    LogLevel.ERROR -> Color(0xFFEF4444)
                  }
                )
                Text(
                  text = log.timestamp,
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = log.message,
                style = MaterialTheme.typography.bodySmall,
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp
              )
            }
          }
          Divider(modifier = Modifier.padding(top = 6.dp), color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
        }
      }
    }
  }
}
