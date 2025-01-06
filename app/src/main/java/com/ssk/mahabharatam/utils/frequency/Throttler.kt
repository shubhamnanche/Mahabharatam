/**
 * Mahabharatam is a comprehensive Android app offering an engaging and
 * accessible way to explore the epic's timeless stories, teachings,
 * and characters.
 * This file is part of the Mahabharatam app.
 * Copyright (C) 2025  Shubham Nanche <https://github.com/shubhamnanche>
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

package com.ssk.mahabharatam.utils.frequency

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class Throttler(
    private val intervalMs: Long,
    private val coroutineContext: CoroutineContext = Dispatchers.Main
) {
    private var lastExecutionTime = 0L

    fun throttle(action: () -> Unit) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastExecutionTime >= intervalMs) {
            lastExecutionTime = currentTime
            CoroutineScope(coroutineContext).launch {
                action()
            }
        }
    }
}