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

package com.ssk.mahabharatam.source

import android.app.Activity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ssk.mahabharatam.models.Verse
import java.io.BufferedReader
import java.io.InputStreamReader

class Book(private val activity: Activity, private val bookFileName: String) {

    // Holds the list of verses (lazy loading)
    private var verses: List<Verse>? = null

    // Lazy loading logic
    fun getVerses(): List<Verse> {
        if (verses == null) {
            // Load the verses from the JSON file
            verses = loadVersesFromJson(bookFileName)
        }
        return verses!!
    }

    fun getVerses(chapter: Int): List<Verse> {
        return getVerses().filter {
            it.chapter == chapter
        }.toList()
    }

    fun getVerse(chapter: Int, verse: Int): List<Verse> {
        return getVerses(chapter).filter {
            it.shloka == verse
        }.toList()
    }

    // Helper to load and parse JSON
    private fun loadVersesFromJson(fileName: String): List<Verse> {
        val json = activity.assets.open("books/$fileName").use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                reader.readText()
            }
        }

        // Parse JSON into a list of Verse objects using Gson
        val listType = object : TypeToken<List<Verse>>() {}.type
        return Gson().fromJson(json, listType)
    }
}

