package com.ssk.mahabharatam.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ssk.mahabharatam.models.Verse
import com.ssk.mahabharatam.source.Book
import com.ssk.mahabharatam.source.BookFactory
import com.ssk.mahabharatam.source.Mahabharatam
import com.ssk.mahabharatam.views.ui.theme.MahabharatamTheme
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

    lateinit var verses: List<Verse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        bookNumber = intent.getIntExtra("book_number", 1)
        chapterNumber = intent.getIntExtra("chapter_number", 1)

        book = bookFactory.createBook(Mahabharatam.getBookNames()[bookNumber - 1])
        verses = book.getVerses(chapterNumber)

        setContent {
            MahabharatamTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val pagerState = rememberPagerState(pageCount = { verses.size })

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
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(getPageColor(page)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = verses[page].text,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color.White,
                                    modifier = Modifier.padding(16.dp),
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
                                text = "$bookNumber.$chapterNumber.${pagerState.currentPage + 1}",
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

