package com.example.tugas11dan12webview

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.tugas11dan12webview.ui.theme.Tugas11dan12webviewTheme
import kotlin.math.min // For Levenshtein distance

// Define your hex color for the TopAppBar
val TopBarColor = Color(0xFF00BCD4) // Example: Orange color

// Data class for list items
data class Mahasiswa(
    val nim: String,
    val nama: String,
    val programStudi: String,
    val photoUri: Uri? // Using a Uri for the photo, can be null
)

// Levenshtein distance function to calculate similarity between two strings
fun levenshtein(lhs: CharSequence, rhs: CharSequence): Int {
    val lhsLength = lhs.length
    val rhsLength = rhs.length

    var cost = Array(lhsLength + 1) { it }
    var newCost = Array(lhsLength + 1) { 0 }

    for (i in 1..rhsLength) {
        newCost[0] = i
        for (j in 1..lhsLength) {
            val match = if (lhs[j - 1] == rhs[i - 1]) 0 else 1
            val costReplace = cost[j - 1] + match
            val costInsert = cost[j] + 1
            val costDelete = newCost[j - 1] + 1
            newCost[j] = minOf(costInsert, costDelete, costReplace)
        }
        val swap = cost
        cost = newCost
        newCost = swap
    }
    return cost[lhsLength]
}

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Tugas11dan12webviewTheme {
                val mahasiswaList = remember {
                    mutableStateListOf<Mahasiswa>() // Start with an empty list
                }
                var selectedMahasiswa by remember { mutableStateOf<Mahasiswa?>(null) }

                var nimText by remember { mutableStateOf("") }
                var namaText by remember { mutableStateOf("") }
                val programStudiList = listOf("Manajemen Informatika", "Teknik Mesin", "Akuntansi", "Teknik Elektro")
                var expandedProgramStudi by remember { mutableStateOf(false) }
                var selectedProgramStudi by remember { mutableStateOf(programStudiList[0]) }
                var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

                val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.PickVisualMedia(),
                    onResult = { uri ->
                        selectedImageUri = uri
                    }
                )

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
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        TextField(
                            value = nimText,
                            onValueChange = { nimText = it },
                            label = { Text("NIM") },
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = selectedMahasiswa != null && selectedMahasiswa?.nim == nimText // Optional: Make NIM read-only when editing an existing, loaded record
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            TextField(
                                value = namaText,
                                onValueChange = { namaText = it },
                                label = { Text("Nama (untuk Cari)") },
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    val query = namaText.trim().lowercase()
                                    if (query.isNotEmpty() && mahasiswaList.isNotEmpty()) {
                                        mahasiswaList.sortBy { mahasiswa ->
                                            levenshtein(mahasiswa.nama.lowercase(), query)
                                        }
                                        // After sorting, the first item is the best match.
                                        val topResult = mahasiswaList.first()
                                        selectedMahasiswa = topResult
                                        nimText = topResult.nim
                                        // Do NOT update namaText from topResult.nama, keep user's query
                                        selectedProgramStudi = topResult.programStudi
                                        selectedImageUri = topResult.photoUri
                                    } else if (query.isEmpty()) {
                                        // Optional: if query is empty, maybe reset sort or clear selection
                                        // For now, do nothing to the list order if query is empty.
                                        // You could add a default sort here, e.g., by NIM:
                                        // mahasiswaList.sortBy { it.nim }
                                        // selectedMahasiswa = null // And clear selection
                                    } else {
                                        // Query is not empty, but mahasiswaList is empty.
                                        selectedMahasiswa = null
                                    }
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
                            Box(
                                modifier = Modifier.weight(1f)
                            ) {
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
                                        modifier = Modifier
                                            .menuAnchor()
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

                            Box(modifier = Modifier
                                .size(64.dp)
                                .clickable {
                                    singlePhotoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                }
                            ) {
                                if (selectedImageUri != null) {
                                    AsyncImage(
                                        model = selectedImageUri,
                                        contentDescription = "Selected Image",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Image(
                                        painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                                        contentDescription = "Image Preview",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(onClick = {
                                if (nimText.isNotBlank() && namaText.isNotBlank()) { // Use namaText (search field) for nama when adding if desired, or have a separate "Nama Mahasiswa" field.
                                    // Assuming namaText here refers to the student's actual name for adding.
                                    // If namaText is purely for search, you'll need another state var for "student name input".
                                    // For this implementation, let's assume namaText serves as student name for adding if no selection context.
                                    mahasiswaList.add(
                                        Mahasiswa(
                                            nimText,
                                            namaText, // Using namaText as student's name for new entry.
                                            selectedProgramStudi,
                                            selectedImageUri
                                        )
                                    )
                                    nimText = ""
                                    namaText = "" // Clear after adding
                                    selectedImageUri = null
                                    selectedMahasiswa = null
                                }
                            }) {
                                Text("Tambah")
                            }
                            Button(
                                onClick = {
                                    selectedMahasiswa?.let { currentMahasiswa ->
                                        val index = mahasiswaList.indexOf(currentMahasiswa)
                                        // When updating, use the values from the fields.
                                        // The student's name to be updated should come from a dedicated name field,
                                        // or if namaText is to be used, ensure it's what the user intends for the update.
                                        // Here, we assume nimText, selectedProgramStudi, selectedImageUri are correct.
                                        // For 'nama', if namaText is the search query, we might need another field,
                                        // or update with currentMahasiswa.nama if we don't want search query to overwrite it.
                                        // Let's assume for "Ubah", the "namaText" field should contain the name to update to.
                                        if (index != -1 && nimText.isNotBlank() && namaText.isNotBlank()) {
                                            mahasiswaList[index] = currentMahasiswa.copy(
                                                nim = nimText, // Allow NIM update if not read-only
                                                nama = namaText, // Update name from namaText field
                                                programStudi = selectedProgramStudi,
                                                photoUri = selectedImageUri
                                            )
                                            nimText = ""
                                            namaText = ""
                                            selectedImageUri = null
                                            selectedMahasiswa = null
                                        }
                                    }
                                },
                                enabled = selectedMahasiswa != null
                            ) {
                                Text("Ubah")
                            }
                            Button(
                                onClick = {
                                    selectedMahasiswa?.let {
                                        mahasiswaList.remove(it)
                                        nimText = ""
                                        namaText = ""
                                        selectedImageUri = null
                                        selectedMahasiswa = null
                                    }
                                },
                                enabled = selectedMahasiswa != null
                            ) {
                                Text("Hapus")
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(mahasiswaList) { mahasiswa ->
                                MahasiswaItemView(
                                    mahasiswa = mahasiswa,
                                    onClick = { selected ->
                                        selectedMahasiswa = selected
                                        nimText = selected.nim
                                        namaText = selected.nama // Populate namaText with actual name when item clicked
                                        selectedProgramStudi = selected.programStudi
                                        selectedImageUri = selected.photoUri
                                    }
                                )
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
fun MahasiswaItemView(mahasiswa: Mahasiswa, onClick: (Mahasiswa) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(mahasiswa) },
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
            if (mahasiswa.photoUri != null) {
                AsyncImage(
                    model = mahasiswa.photoUri,
                    contentDescription = "Foto ${mahasiswa.nama}",
                    modifier = Modifier.size(64.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                    contentDescription = "Foto ${mahasiswa.nama}",
                    modifier = Modifier.size(64.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, widthDp = 380, heightDp = 800)
@Composable
fun DefaultPreviewWithAllFieldsAndSpinnerAndList() {
    Tugas11dan12webviewTheme {
        val mahasiswaListPreview = remember {
            mutableStateListOf(
                Mahasiswa("2331730108", "Moh. Ridho Yuga P.", "Manajemen Informatika", null),
                Mahasiswa("1234567890", "Jane Doe", "Teknik Mesin", null),
                Mahasiswa("0987654321", "John Smith", "Akuntansi", null),
                Mahasiswa("1122334455", "Alice Wonderland", "Teknik Elektro", null)
            )
        }
        var selectedMahasiswaForPreview by remember { mutableStateOf<Mahasiswa?>(null) }

        var nimTextPreview by remember { mutableStateOf("") }
        var namaTextPreview by remember { mutableStateOf("Test Name") } // Search query for preview
        val programStudiListPreview = listOf("Manajemen Informatika", "Teknik Mesin", "Akuntansi", "Teknik Elektro")
        var expandedProgramStudiPreview by remember { mutableStateOf(false) }
        var selectedProgramStudiPreview by remember { mutableStateOf(programStudiListPreview[0]) }
        var selectedImageUriPreview by remember { mutableStateOf<Uri?>(null) }

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
                TextField(
                    value = nimTextPreview,
                    onValueChange = { nimTextPreview = it },
                    label = { Text("NIM") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = selectedMahasiswaForPreview != null && selectedMahasiswaForPreview?.nim == nimTextPreview
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextField(
                        value = namaTextPreview,
                        onValueChange = { namaTextPreview = it },
                        label = { Text("Nama (untuk Cari)") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        val query = namaTextPreview.trim().lowercase()
                        if (query.isNotEmpty() && mahasiswaListPreview.isNotEmpty()) {
                            mahasiswaListPreview.sortBy { mahasiswa ->
                                levenshtein(mahasiswa.nama.lowercase(), query)
                            }
                            val topResult = mahasiswaListPreview.first()
                            selectedMahasiswaForPreview = topResult
                            nimTextPreview = topResult.nim
                            // namaTextPreview = topResult.nama // Keep search query
                            selectedProgramStudiPreview = topResult.programStudi
                            selectedImageUriPreview = topResult.photoUri
                        }
                    }) {
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
                            expanded = expandedProgramStudiPreview,
                            onExpandedChange = { expandedProgramStudiPreview = !expandedProgramStudiPreview }
                        ) {
                            TextField(
                                value = selectedProgramStudiPreview,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Program Studi") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedProgramStudiPreview)
                                },
                                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                                modifier = Modifier.menuAnchor().fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = expandedProgramStudiPreview,
                                onDismissRequest = { expandedProgramStudiPreview = false }
                            ) {
                                programStudiListPreview.forEach { selectionOption ->
                                    DropdownMenuItem(
                                        text = { Text(selectionOption) },
                                        onClick = {
                                            selectedProgramStudiPreview = selectionOption
                                            expandedProgramStudiPreview = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(modifier = Modifier
                        .size(64.dp)
                        .clickable { /* Not interactive */ }
                    ) {
                        if (selectedImageUriPreview != null) {
                            AsyncImage(
                                model = selectedImageUriPreview,
                                contentDescription = "Selected Image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                                error = painterResource(id = android.R.drawable.ic_menu_gallery),
                                placeholder = painterResource(id = android.R.drawable.ic_menu_gallery)
                            )
                        } else {
                            Image(
                                painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                                contentDescription = "Image Preview",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = {
                        if (nimTextPreview.isNotBlank() && namaTextPreview.isNotBlank()) { // Assuming namaTextPreview is student's name for adding
                            mahasiswaListPreview.add(
                                Mahasiswa(
                                    nimTextPreview,
                                    namaTextPreview,
                                    selectedProgramStudiPreview,
                                    selectedImageUriPreview
                                )
                            )
                            nimTextPreview = ""
                            namaTextPreview = ""
                            selectedImageUriPreview = null
                            selectedMahasiswaForPreview = null
                        }
                    }) {
                        Text("Tambah")
                    }
                    Button(
                        onClick = {
                            selectedMahasiswaForPreview?.let { current ->
                                val index = mahasiswaListPreview.indexOf(current)
                                if (index != -1 && nimTextPreview.isNotBlank() && namaTextPreview.isNotBlank()){
                                    mahasiswaListPreview[index] = current.copy(
                                        nim = nimTextPreview,
                                        nama = namaTextPreview, // Use namaTextPreview for update
                                        programStudi = selectedProgramStudiPreview,
                                        photoUri = selectedImageUriPreview
                                    )
                                    nimTextPreview = ""
                                    namaTextPreview = ""
                                    selectedImageUriPreview = null
                                    selectedMahasiswaForPreview = null
                                }
                            }
                        },
                        enabled = selectedMahasiswaForPreview != null
                    ) {
                        Text("Ubah")
                    }
                    Button(
                        onClick = {
                            selectedMahasiswaForPreview?.let {
                                mahasiswaListPreview.remove(it)
                                nimTextPreview = ""
                                namaTextPreview = ""
                                selectedImageUriPreview = null
                                selectedMahasiswaForPreview = null
                            }
                        },
                        enabled = selectedMahasiswaForPreview != null
                    ) {
                        Text("Hapus")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(mahasiswaListPreview) { mahasiswa ->
                        MahasiswaItemView(
                            mahasiswa = mahasiswa,
                            onClick = { selected ->
                                selectedMahasiswaForPreview = selected
                                nimTextPreview = selected.nim
                                namaTextPreview = selected.nama // Clicking item populates namaTextPreview
                                selectedProgramStudiPreview = selected.programStudi
                                selectedImageUriPreview = selected.photoUri
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}