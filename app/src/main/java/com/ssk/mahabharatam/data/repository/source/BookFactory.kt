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

package com.ssk.mahabharatam.data.repository.source

import android.app.Activity
import jakarta.inject.Inject

class BookFactory @Inject constructor(private val activity: Activity) {

    // Lazily initialized list of books
    val books: List<Book> by lazy {
        getBookNames().map { createBook(it) }
    }

    // Creates a single book instance from bookName
    private fun createBook(bookFileName: String): Book {
        return Book(activity, bookFileName)
    }

    // Creates a single book instance from bookNumber
    fun createBook(book: Int): Book {
        val bookNames = getBookNames()
        return createBook(bookNames[book-1])
    }

    // Retrieves the list of book names
    fun getBookNames(): List<String> {
        return Mahabharatam.getBookNames()
    }

}