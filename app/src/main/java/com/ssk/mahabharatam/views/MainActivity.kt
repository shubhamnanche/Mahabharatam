/**
 * Mahabharatam is a comprehensive Android app offering an engaging and
 * accessible way to explore the epic's timeless stories, teachings,
 * and characters.
 * This file is part of the Mahabharatam app.
 * Copyright (C) 2024  Shubham Nanche <https://github.com/shubhamnanche>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.ssk.mahabharatam.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ripple
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssk.mahabharatam.R
import com.ssk.mahabharatam.source.BookFactory
import com.ssk.mahabharatam.source.Mahabharatam
import com.ssk.mahabharatam.ui.theme.MahabharatamTheme
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val TAG = this::class.java.simpleName

    @Inject
    lateinit var bookFactory: BookFactory

    private lateinit var bookNames: List<String>

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        bookNames = Mahabharatam.getBookNames()

        setContent {
            MahabharatamTheme {
                MainScreen()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun MainScreen(modifier: Modifier = Modifier) {
        var searchQuery by rememberSaveable { mutableStateOf("") }
        var expanded by rememberSaveable { mutableStateOf(false) }
        var menuExpanded by rememberSaveable { mutableStateOf(false) }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                if (expanded.not()) {
                    CenterAlignedTopAppBar(
                        title = { Text(getString(R.string.app_name)) },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.primary,
                            actionIconContentColor = MaterialTheme.colorScheme.inversePrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        ) { innerPadding ->
            Box(
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                Column(Modifier.fillMaxSize()) {
                    SearchBar(
                        inputField = {
                            SearchBarDefaults.InputField(query = searchQuery,
                                onQueryChange = { searchQuery = it },
                                onSearch = { expanded = false },
                                expanded = expanded,
                                onExpandedChange = { expanded = it },
                                placeholder = { Text(stringResource(R.string.search)) },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Search,
                                        contentDescription = null
                                    )
                                },
                                trailingIcon = {
                                    Column {
                                        Icon(
                                            Icons.Default.MoreVert,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .padding(8.dp)
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .clickable(
                                                    interactionSource = remember { MutableInteractionSource() },
                                                    indication = ripple(
                                                        bounded = true,
                                                        radius = 16.dp
                                                    )
                                                ) {
                                                    menuExpanded = true
                                                }
                                        )


                                        DropdownMenu(
                                            expanded = menuExpanded,
                                            onDismissRequest = { menuExpanded = false },
                                            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                                        ) {
                                            DropdownMenuItem(
                                                onClick = {
                                                    menuExpanded = false
                                                    // Handle first menu item click
                                                },
                                                text = { Text("Option 1") }
                                            )
                                            DropdownMenuItem(
                                                onClick = {
                                                    menuExpanded = false
                                                    // Handle second menu item click
                                                },
                                                text = { Text("Option 2") }
                                            )
                                            DropdownMenuItem(
                                                onClick = {
                                                    menuExpanded = false
                                                    // Handle third menu item click
                                                },
                                                text = { Text("Option 3") }
                                            )
                                        }
                                    }
                                }
                            )
                        },
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                        modifier = Modifier
                            .fillMaxWidth()

                    ) {

                    }

                    LazyVerticalGrid(
                        contentPadding = PaddingValues(10.dp),
                        columns = GridCells.Adaptive(minSize = 150.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(count = bookNames.size) { index ->
                            val bookNumber = index + 1
                            ItemCard(
                                title = "Book $bookNumber",
                                onClick = { launchBookDetails(bookNumber) },
                                modifier = Modifier
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ItemCard(title: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(2.dp)
                .clip(shape = RoundedCornerShape(16.dp))
                .clickable { onClick() }
                .shadow(8.dp, shape = RoundedCornerShape(16.dp)),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground
            ),
            elevation = CardDefaults.cardElevation()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }


    private fun launchBookDetails(book: Int) {
        Log.d(TAG, "launching book $book...")
        val intent = Intent(this@MainActivity, BookDetails::class.java)
        intent.putExtra("book_number", book)
        startActivity(intent)
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    @Preview(showBackground = true, showSystemUi = true)
    private fun MainScreenPreview() {
        MainScreen()
    }

}