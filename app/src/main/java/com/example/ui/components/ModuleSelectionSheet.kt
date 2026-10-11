package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppModule
import com.example.model.StringsHolder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModuleSelectionSheet(
    sheetState: SheetState,
    currentModules: List<AppModule>,
    strings: StringsHolder,
    onSaveModules: (List<AppModule>) -> Unit,
    onDismiss: () -> Unit
) {
    var workingList by remember(currentModules) {
        // Collect all modules preserving order of active ones, then append missing ones
        val activeSet = currentModules.toSet()
        val allOther = AppModule.entries.filter { !activeSet.contains(it) }
        mutableStateOf(currentModules + allOther)
    }

    // Set of active modules
    var enabledSet by remember(currentModules) {
        mutableStateOf(currentModules.toSet())
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.testTag("module_customizer_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = strings.customizeModules,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = strings.chooseActiveModules,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = strings.close)
                }
            }

            Text(
                text = strings.activeModulesHint,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 6.dp, bottom = 12.dp)
            )

            Divider(color = MaterialTheme.colorScheme.outlineVariant)

            Spacer(modifier = Modifier.height(10.dp))

            // Modules list
            LazyColumn(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(workingList) { index, module ->
                    val isEnabled = enabledSet.contains(module)
                    val isFirst = index == 0
                    val isLast = index == workingList.size - 1

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isEnabled)
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
                            else
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("module_toggle_${module.id}")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (isEnabled) MaterialTheme.colorScheme.primary
                                            else MaterialTheme.colorScheme.surfaceVariant
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = module.icon,
                                        contentDescription = null,
                                        tint = if (isEnabled) MaterialTheme.colorScheme.onPrimary
                                        else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = module.getTitle(strings),
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontWeight = if (isEnabled) FontWeight.SemiBold else FontWeight.Normal
                                        ),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    if (module.isCore) {
                                        Text(
                                            text = "Асноўны модуль",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.outline
                                        )
                                    }
                                }
                            }

                            // Reorder buttons (Move Up / Down)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (isEnabled) {
                                    IconButton(
                                        onClick = {
                                            if (!isFirst) {
                                                val mList = workingList.toMutableList()
                                                val temp = mList[index]
                                                mList[index] = mList[index - 1]
                                                mList[index - 1] = temp
                                                workingList = mList
                                            }
                                        },
                                        enabled = !isFirst,
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowUpward,
                                            contentDescription = "Move Up",
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }

                                    IconButton(
                                        onClick = {
                                            if (!isLast) {
                                                val mList = workingList.toMutableList()
                                                val temp = mList[index]
                                                mList[index] = mList[index + 1]
                                                mList[index + 1] = temp
                                                workingList = mList
                                            }
                                        },
                                        enabled = !isLast,
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowDownward,
                                            contentDescription = "Move Down",
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(4.dp))

                                // Toggle Switch
                                Switch(
                                    checked = isEnabled,
                                    onCheckedChange = { checked ->
                                        if (module.isCore && !checked) {
                                            // Settings remains enabled
                                            return@Switch
                                        }
                                        enabledSet = if (checked) {
                                            enabledSet + module
                                        } else {
                                            if (enabledSet.size > 1) enabledSet - module else enabledSet
                                        }
                                    },
                                    enabled = !module.isCore,
                                    modifier = Modifier.testTag("switch_${module.id}")
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Divider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(14.dp))

            // Action buttons: Save & Reset
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 18.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        workingList = AppModule.entries
                        enabledSet = AppModule.DEFAULT_MODULES.toSet()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("reset_modules_button")
                ) {
                    Text(text = strings.reset)
                }

                Button(
                    onClick = {
                        val finalOrdered = workingList.filter { enabledSet.contains(it) }
                        val safeList = if (finalOrdered.isEmpty()) AppModule.DEFAULT_MODULES else finalOrdered
                        onSaveModules(safeList)
                    },
                    modifier = Modifier
                        .weight(1.5f)
                        .testTag("save_modules_button")
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = strings.save)
                }
            }
        }
    }
}
