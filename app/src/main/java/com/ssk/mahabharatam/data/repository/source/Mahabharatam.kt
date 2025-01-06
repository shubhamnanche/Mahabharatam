package com.ssk.mahabharatam.data.repository.source

internal object Mahabharatam {
    //parvas
    private var bookNames: List<String>? = null

    fun getBookNames(): List<String> {
        if (bookNames.isNullOrEmpty()) {
            val names = mutableListOf<String>()
            val name = "mahabharata_book"
            for (i in 1..18) {
                names.add("${name}_$i.json")
            }
            return names.toList()
        }
        return bookNames!!
    }


}