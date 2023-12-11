package com.simplifymindfulness.composeroommvvmflow.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.simplifymindfulness.composeroommvvmflow.room.Item
import com.simplifymindfulness.composeroommvvmflow.viewmodel.ItemViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemListScreen(viewModel: ItemViewModel) {
    val items = viewModel.items.collectAsState().value
    var showDialog by remember { mutableStateOf(false) }
    var currentEditItem by remember { mutableStateOf<Item?>(null) }
    var itemToDelete by remember { mutableStateOf<Item?>(null) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    ) { paddingValues ->
        LazyColumn(contentPadding = paddingValues) {
            items(items) { item ->
                ItemRow(
                    item = item,
                    onItemEdit = { editItem ->
                        currentEditItem = editItem
                        showDialog = true
                    },
                    onItemDelete = { deleteItem ->
                        itemToDelete = deleteItem
                        showDeleteConfirmDialog = true
                    }
                )
                Spacer(modifier = Modifier.height(8.dp)) // Spacer for padding between items
            }
        }
    }

    // Add Dialog and Delete Confirmation Dialog
    if (showDialog) {
        if (currentEditItem == null) {
            ItemAddDialog(onDismiss = { showDialog = false }, onAddItem = { itemName ->
                viewModel.insert(Item(name = itemName))
                showDialog = false
            })
        } else {
            ItemEditDialog(item = currentEditItem!!, onDismiss = {
                showDialog = false
                currentEditItem = null
            }, onEditItem = { editedItem ->
                viewModel.update(editedItem)
                showDialog = false
                currentEditItem = null
            })
        }
    }

    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text("Confirm Delete") },
            text = { Text("Are you sure you want to delete this item?") },
            confirmButton = {
                Button(
                    onClick = {
                        itemToDelete?.let { viewModel.delete(it) }
                        showDeleteConfirmDialog = false
                    }
                ) { Text("Yes") }
            },
            dismissButton = {
                Button(onClick = { showDeleteConfirmDialog = false }) { Text("No") }
            }
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemRow(item: Item, onItemEdit: (Item) -> Unit, onItemDelete: (Item) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
            )
            Row {
                IconButton(onClick = { onItemEdit(item) }) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = { onItemDelete(item) }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemAddDialog(onDismiss: () -> Unit, onAddItem: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Item") },
        text = {
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Item Name") }
            )
        },
        confirmButton = {
            Button(onClick = { onAddItem(text) }) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemEditDialog(item: Item, onDismiss: () -> Unit, onEditItem: (Item) -> Unit) {
    var text by remember { mutableStateOf(item.name) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Item") },
        text = {
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Item Name") }
            )
        },
        confirmButton = {
            Button(onClick = { onEditItem(item.copy(name = text)) }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
