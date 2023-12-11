package com.simplifymindfulness.composeroommvvmflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.simplifymindfulness.composeroommvvmflow.composables.ItemListScreen
import com.simplifymindfulness.composeroommvvmflow.ui.theme.ComposeRoomMVVMFlowTheme
import com.simplifymindfulness.composeroommvvmflow.viewmodel.ItemViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeRoomMVVMFlowTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val itemViewModel: ItemViewModel = viewModel()
                    ItemListScreen(viewModel = itemViewModel)

                }
            }
        }
    }
}

