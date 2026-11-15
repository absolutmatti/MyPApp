package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.viewmodel.MainViewModel

@Composable
fun StrategiesScreen(viewModel: MainViewModel) {
  val strategies by viewModel.strategies.collectAsState()

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
          text = "Bypass Strategies",
          style = MaterialTheme.typography.headlineMedium,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = "Select DPI bypass command profile",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
      Button(onClick = { viewModel.testLatencies() }) {
        Icon(Icons.Default.Speed, contentDescription = "Test Latency")
        Spacer(modifier = Modifier.width(4.dp))
        Text("Test Ping")
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    LazyColumn(
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      items(strategies) { strategy ->
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(
            containerColor = if (strategy.isSelected)
              MaterialTheme.colorScheme.primaryContainer
            else
              MaterialTheme.colorScheme.surfaceVariant
          )
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = strategy.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
              )
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surface
              ) {
                Text(
                  text = "${strategy.latencyMs} ms",
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                  style = MaterialTheme.typography.bodySmall,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.primary
                )
              }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = strategy.command,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.primary,
              fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = strategy.description,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.End
            ) {
              if (strategy.isSelected) {
                Button(
                  onClick = { },
                  enabled = false,
                  colors = ButtonDefaults.buttonColors(disabledContainerColor = MaterialTheme.colorScheme.primary)
                ) {
                  Icon(Icons.Default.Check, contentDescription = "Active")
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("Active Strategy")
                }
              } else {
                OutlinedButton(
                  onClick = { viewModel.selectStrategy(strategy.id) }
                ) {
                  Text("Use Strategy")
                }
              }
            }
          }
        }
      }
    }
  }
}
