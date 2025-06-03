package com.example.tugas11dan12webview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tugas11dan12webview.ui.theme.Tugas11dan12webviewTheme

// Define your hex color for the TopAppBar
val TopBarColor = Color(0xFFFFA500) // Example: Orange color

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Tugas11dan12webviewTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text("My App") },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = TopBarColor
                            )
                        )
                    }
                ) { innerPadding ->
                    // Content of the screen
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        var nimText by remember { mutableStateOf("") }
                        var namaText by remember { mutableStateOf("") }
                        val programStudiList = listOf("Teknik Informatika", "Sistem Informasi", "Manajemen Informatika", "Teknik Komputer")
                        var expandedProgramStudi by remember { mutableStateOf(false) }
                        var selectedProgramStudi by remember { mutableStateOf(programStudiList[0]) }

                        TextField(
                            value = nimText,
                            onValueChange = { nimText = it },
                            label = { Text("NIM") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            TextField(
                                value = namaText,
                                onValueChange = { namaText = it },
                                label = { Text("Nama") },
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    // Handle search action
                                }
                            ) {
                                Text("Cari")
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // ExposedDropdownMenuBox for Program Studi
                            Box(
                                modifier = Modifier.weight(1f) // Spinner takes available space
                            ) {
                                ExposedDropdownMenuBox(
                                    expanded = expandedProgramStudi,
                                    onExpandedChange = { expandedProgramStudi = !expandedProgramStudi }
                                ) {
                                    TextField(
                                        value = selectedProgramStudi,
                                        onValueChange = {}, // Not editable
                                        readOnly = true,
                                        label = { Text("Program Studi") },
                                        trailingIcon = {
                                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedProgramStudi)
                                        },
                                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                                        modifier = Modifier
                                            .menuAnchor() // Anchor the dropdown menu
                                            .fillMaxWidth()
                                    )
                                    ExposedDropdownMenu(
                                        expanded = expandedProgramStudi,
                                        onDismissRequest = { expandedProgramStudi = false }
                                    ) {
                                        programStudiList.forEach { selectionOption ->
                                            DropdownMenuItem(
                                                text = { Text(selectionOption) },
                                                onClick = {
                                                    selectedProgramStudi = selectionOption
                                                    expandedProgramStudi = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // ImageView (using a placeholder)
                            Image(
                                painter = painterResource(id = android.R.drawable.ic_menu_gallery), // Replace with your image
                                contentDescription = "Image Preview",
                                modifier = Modifier.size(64.dp) // Adjust size as needed
                            )
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, widthDp = 380)
@Composable
fun DefaultPreviewWithAllFieldsAndSpinner() {
    Tugas11dan12webviewTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("My App") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = TopBarColor
                    )
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                var nimText by remember { mutableStateOf("") }
                var namaText by remember { mutableStateOf("") }
                val programStudiList = listOf("Teknik Informatika", "Sistem Informasi", "Manajemen Informatika")
                var expandedProgramStudi by remember { mutableStateOf(false) }
                var selectedProgramStudi by remember { mutableStateOf(programStudiList[0]) }

                TextField(
                    value = nimText,
                    onValueChange = { nimText = it },
                    label = { Text("NIM") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextField(
                        value = namaText,
                        onValueChange = { namaText = it },
                        label = { Text("Nama") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { /*TODO*/ }) {
                        Text("Cari")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        ExposedDropdownMenuBox(
                            expanded = expandedProgramStudi,
                            onExpandedChange = { expandedProgramStudi = !expandedProgramStudi }
                        ) {
                            TextField(
                                value = selectedProgramStudi,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Program Studi") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedProgramStudi)
                                },
                                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                                modifier = Modifier.menuAnchor().fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = expandedProgramStudi,
                                onDismissRequest = { expandedProgramStudi = false }
                            ) {
                                programStudiList.forEach { selectionOption ->
                                    DropdownMenuItem(
                                        text = { Text(selectionOption) },
                                        onClick = {
                                            selectedProgramStudi = selectionOption
                                            expandedProgramStudi = false
                                        }
                                    )
                                }
                            }
                        }
                    }


                    Spacer(modifier = Modifier.width(8.dp))

                    Image(
                        painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                        contentDescription = "Image Preview",
                        modifier = Modifier.size(70.dp)
                    )
                }
            }
        }
    }
}