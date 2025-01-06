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

package com.ssk.mahabharatam.utils

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

val SharedPreferences.delegates get() = SharedPreferenceDelegates(this)

class SharedPreferenceDelegates(private val prefs: SharedPreferences) {
    fun boolean(key: String? = null, default: Boolean = false): ReadWriteProperty<Any, Boolean> =
        create(key, default, prefs::getBoolean, prefs.edit()::putBoolean)

    fun int(key: String? = null, default: Int = 0): ReadWriteProperty<Any, Int> =
        create(key, default, prefs::getInt, prefs.edit()::putInt)

    fun float(key: String? = null, default: Float = 0F): ReadWriteProperty<Any, Float> =
        create(key, default, prefs::getFloat, prefs.edit()::putFloat)

    fun long(key: String? = null, default: Long = 0L): ReadWriteProperty<Any, Long> =
        create(key, default, prefs::getLong, prefs.edit()::putLong)

    fun string(key: String? = null, default: String = ""): ReadWriteProperty<Any, String> =
        create(key, default, { k, d -> prefs.getString(k, d) as String }, prefs.edit()::putString)

    fun stringSet(
        key: String? = null,
        default: Set<String> = emptySet<String>()
    ): ReadWriteProperty<Any, Set<String>> =
        create(
            key,
            default,
            { k, d -> prefs.getStringSet(k, d) as Set<String> },
            prefs.edit()::putStringSet
        )

    private fun <T : Any> create(
        key: String? = null,
        default: T,
        getter: (key: String, default: T) -> T,
        setter: (key: String, value: T) -> SharedPreferences.Editor
    ) = object : ReadWriteProperty<Any, T> {

        private fun key(property: KProperty<*>) = key ?: property.name

        override fun getValue(thisRef: Any, property: KProperty<*>): T {
            return getter(key(property), default)
        }

        override fun setValue(
            thisRef: Any,
            property: KProperty<*>,
            value: T
        ) {
            setter(key(property), value).apply()
        }

    }

}