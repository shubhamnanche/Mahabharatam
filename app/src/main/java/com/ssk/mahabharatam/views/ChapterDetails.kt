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

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ssk.mahabharatam.data.models.Verse
import com.ssk.mahabharatam.data.repository.settings.SettingsRepository
import com.ssk.mahabharatam.data.repository.source.Book
import com.ssk.mahabharatam.data.repository.source.BookFactory
import com.ssk.mahabharatam.ui.theme.MahabharatamTheme
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import mx.platacard.pagerindicator.PagerWormIndicator
import kotlin.properties.Delegates

@AndroidEntryPoint
class ChapterDetails : ComponentActivity() {

    @Inject
    lateinit var bookFactory: BookFactory
    private lateinit var book: Book

    var chapterNumber by Delegates.notNull<Int>()
    var bookNumber by Delegates.notNull<Int>()
    var verseNumber by Delegates.notNull<Int>()

    lateinit var verses: List<Verse>

    @Inject
    lateinit var settingsRepo: SettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        bookNumber = intent.getIntExtra("book_number", 1)
        chapterNumber = intent.getIntExtra("chapter_number", 1)
        verseNumber = intent.getIntExtra("verse_number", 1)

        book = bookFactory.createBook(bookNumber)
        verses = book.getVerses(chapterNumber)

        setContent {
            MahabharatamTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val clampedInitialPage =
                        verses.indexOf(verses.find {
                            it.shloka == verseNumber
                        }).coerceIn(0, verses.size - 1)

                    val pagerState = rememberPagerState(
                        initialPage = clampedInitialPage,
                        pageCount = { verses.size }
                    )

                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        // ViewPager Content
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.weight(1f)
                        ) { page ->

                            val verse = verses[page]

                            settingsRepo.lastReadVerse = verse.shloka

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(getPageColor(page)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = verse.text,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color.White,
                                    modifier = Modifier
                                        .scrollable(
                                            state = rememberScrollState(0),
                                            orientation = Orientation.Vertical
                                        )
                                        .padding(16.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        // Pager Indicators
                        PagerWormIndicator(
                            pagerState = pagerState,
                            activeDotColor = getPageColor(pagerState.currentPage),
                            dotColor = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .padding(top = 10.dp, start = 50.dp, end = 50.dp)
                                .align(Alignment.CenterHorizontally)
                        )

                        // Navigation Buttons
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            val coroutineScope = rememberCoroutineScope()

                            TextButton(onClick = {
                                coroutineScope.launch {
                                    if (pagerState.currentPage > 0) pagerState.animateScrollToPage(
                                        pagerState.currentPage - 1
                                    )
                                }
                            }) {
                                Text("Prev")
                            }

                            Text(
                                text = "$bookNumber.$chapterNumber.${verses[pagerState.currentPage].shloka}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center
                            )

                            TextButton(onClick = {
                                coroutineScope.launch {
                                    if (pagerState.currentPage < verses.size - 1) pagerState.animateScrollToPage(
                                        pagerState.currentPage + 1
                                    )
                                }
                            }) {
                                Text("Next")
                            }
                        }
                    }

                }
            }
        }
    }
}


@Composable
private fun getPageColor(page: Int): Color {
    val colors = listOf(
        Color(0xFFEF5350), // Red
        Color(0xFF42A5F5), // Blue
        Color(0xFF66BB6A), // Green
        Color(0xFFFFCA28), // Yellow
        Color(0xFFAB47BC), // Purple
        Color(0xFF26C6DA)  // Teal
    )

    // Generate a random color for the page but ensure it's consistent using page as the seed
    return colors[page % colors.size]
}

