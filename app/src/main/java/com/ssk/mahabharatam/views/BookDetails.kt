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
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssk.mahabharatam.data.repository.settings.SettingsRepository
import com.ssk.mahabharatam.data.repository.source.Book
import com.ssk.mahabharatam.data.repository.source.BookFactory
import com.ssk.mahabharatam.ui.theme.MahabharatamTheme
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
class BookDetails : ComponentActivity() {

    @Inject
    lateinit var bookFactory: BookFactory
    private lateinit var book: Book

    private var bookNumber by Delegates.notNull<Int>()

    @Inject
    lateinit var settingsRepo: SettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        bookNumber = intent.getIntExtra("book_number", 1)
        book = bookFactory.createBook(bookNumber)

        setContent {
            BookDetailsScreen(book, bookNumber)
        }
    }

    @Composable
    fun BookDetailsScreen(book: Book, bookNumber: Int) {
        MahabharatamTheme {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                Surface(modifier = Modifier.padding(innerPadding)) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .semantics { isTraversalGroup = true }
                    ) {
                        LazyVerticalGrid(
                            contentPadding = PaddingValues(10.dp),
                            columns = GridCells.Adaptive(minSize = 150.dp)
                        ) {
                            val verses = book.getVerses()
                            val chapters = verses[verses.size - 1].chapter

                            items(chapters) {
                                val chapterNumber = it + 1

                                ChapterItem(
                                    text = "Chapter $chapterNumber",
                                    highlightItem = settingsRepo.lastReadBook == bookNumber && settingsRepo.lastReadChapter == chapterNumber,
                                    highlightColor = MaterialTheme.colorScheme.surfaceTint,
                                    cardModifier = Modifier
                                        .fillMaxSize()
                                        .padding(2.dp)
                                        .align(Alignment.Center)
                                        .clip(RoundedCornerShape(20.dp))
                                        .clickable(true) {},
                                    textModifier = Modifier
                                        .fillMaxSize()
                                        .align(Alignment.Center)
                                        .padding(0.dp, 20.dp),
                                ) {
                                    val intent =
                                        Intent(this@BookDetails, ChapterDetails::class.java).also {
                                            it.putExtra("book_number", bookNumber)
                                            it.putExtra("chapter_number", chapterNumber)
                                        }
                                    startActivity(intent)
                                    settingsRepo.lastReadChapter = chapterNumber
                                }

                            }
                        }
                    }
                }

            }
        }
    }

    @Composable
    private fun ChapterItem(
        text: String,
        highlightItem: Boolean = false,
        highlightColor: Color = MaterialTheme.colorScheme.surfaceTint,
        cardModifier: Modifier = Modifier.fillMaxSize(),
        textModifier: Modifier = Modifier.fillMaxSize(),
        onClick: () -> Unit
    ) {
        val containerColor by animateColorAsState(
            targetValue = if (highlightItem) highlightColor else MaterialTheme.colorScheme.background,
            animationSpec = tween(durationMillis = 300),
            label = "ContainerAnimation"
        )

        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(),
            onClick = onClick,
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
            colors = CardDefaults.cardColors(
                containerColor = containerColor,
                contentColor = MaterialTheme.colorScheme.onBackground
            ),
            modifier = cardModifier,
        ) {
            Text(text, textAlign = TextAlign.Center, modifier = textModifier)
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun BookDetailsPreview() {
//    BookDetailsScreen( )
    }

}