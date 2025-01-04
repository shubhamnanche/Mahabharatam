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

package com.ssk.mahabharatam.utils

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt

fun Resources.resolveDimenAttr(context: Context, @AttrRes attr: Int): Int? {
    val typedValue = TypedValue()
    if (context.theme.resolveAttribute(attr, typedValue, true)) {
        val actionBarHeight =
            TypedValue.complexToDimensionPixelSize(typedValue.data, displayMetrics)
        return actionBarHeight
    }
    return null
}

/**
 * @param context Context
 * @param attr eg. R.attr.color
 * @return @ColorInt color
 * Same as:
 * int color = MaterialColors.getColor(context, R.attr.theme_color, Color.BLACK)
 */
fun Resources.resolveColorAttr(context: Context, @AttrRes attr: Int): Int? {
    val typedValue = TypedValue()
    val theme = context.theme
    theme.resolveAttribute(attr, typedValue, true)
    val color: Int = typedValue.data
    return color
}
