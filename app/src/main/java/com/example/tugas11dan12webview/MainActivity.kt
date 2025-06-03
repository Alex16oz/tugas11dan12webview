package com.example.tugas11dan12webview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // Import this
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tugas11dan12webview.ui.theme.Tugas11dan12webviewTheme

// Define your hex color for the TopAppBar
val TopBarColor = Color(0xFF00BCD4) // Example: Orange color

// Data class for list items
data class Mahasiswa(
    val nim: String,
    val nama: String,
    val programStudi: String,
    val photoResId: Int // Using a drawable resource ID for the photo
)

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Tugas11dan12webviewTheme {
                // Sample data for the list
                val mahasiswaList = remember {
                    mutableStateListOf(
                        Mahasiswa("2331730108", "Moh. Ridho Yuga P.", "Teknik Informatika", R.drawable.ic_launcher_foreground), // Replace with actual image
                        Mahasiswa("1234567890", "Jane Doe", "Sistem Informasi", R.drawable.ic_launcher_foreground), // Replace with actual image
                        Mahasiswa("0987654321", "John Smith", "Manajemen Informatika", R.drawable.ic_launcher_foreground) // Replace with actual image
                    )
                }

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
                        val programStudiList = listOf("Manajemen Informatika", "Teknik Mesin", "Akuntansi", "Teknik Elektro")
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

                        Spacer(modifier = Modifier.height(16.dp)) // Added spacer for separation

                        // Row for Tambah, Ubah, Hapus buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly // Distributes space evenly
                        ) {
                            Button(onClick = {
                                //TODO: Handle Tambah action - e.g., add to mahasiswaList
                                if (nimText.isNotBlank() && namaText.isNotBlank()) {
                                    mahasiswaList.add(
                                        Mahasiswa(
                                            nimText,
                                            namaText,
                                            selectedProgramStudi,
                                            android.R.drawable.ic_menu_gallery // Default image, replace as needed
                                        )
                                    )
                                    // Clear fields after adding
                                    nimText = ""
                                    namaText = ""
                                }
                            }) {
                                Text("Tambah")
                            }
                            Button(onClick = { /*TODO: Handle Ubah action*/ }) {
                                Text("Ubah")
                            }
                            Button(onClick = { /*TODO: Handle Hapus action*/ }) {
                                Text("Hapus")
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // ListView to display items
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(mahasiswaList) { mahasiswa ->
                                MahasiswaItemView(mahasiswa = mahasiswa)
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MahasiswaItemView(mahasiswa: Mahasiswa) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("NIM: ${mahasiswa.nim}", style = MaterialTheme.typography.bodyLarge)
                Text("Nama: ${mahasiswa.nama}", style = MaterialTheme.typography.bodyMedium)
                Text("Program Studi: ${mahasiswa.programStudi}", style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Image(
                painter = painterResource(id = mahasiswa.photoResId),
                contentDescription = "Foto ${mahasiswa.nama}",
                modifier = Modifier.size(64.dp), // Adjust size as needed
                contentScale = ContentScale.Crop
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, widthDp = 380, heightDp = 700)
@Composable
fun DefaultPreviewWithAllFieldsAndSpinnerAndList() {
    Tugas11dan12webviewTheme {
        val mahasiswaList = remember {
            mutableStateListOf(
                Mahasiswa("2331730108", "Moh. Ridho Yuga P.", "Teknik Informatika", android.R.drawable.ic_menu_gallery),
                Mahasiswa("1234567890", "Jane Doe", "Sistem Informasi", android.R.drawable.ic_menu_gallery),
                Mahasiswa("0987654321", "John Smith", "Manajemen Informatika", android.R.drawable.ic_menu_gallery)
            )
        }
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Moh. Ridho Yuga P.  2331730108") },
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

                Spacer(modifier = Modifier.height(16.dp)) // Added spacer for separation

                // Row for Tambah, Ubah, Hapus buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly // Distributes space evenly
                ) {
                    Button(onClick = { /*TODO: Handle Tambah action*/ }) {
                        Text("Tambah")
                    }
                    Button(onClick = { /*TODO: Handle Ubah action*/ }) {
                        Text("Ubah")
                    }
                    Button(onClick = { /*TODO: Handle Hapus action*/ }) {
                        Text("Hapus")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // ListView to display items
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(mahasiswaList) { mahasiswa ->
                        MahasiswaItemView(mahasiswa = mahasiswa)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}