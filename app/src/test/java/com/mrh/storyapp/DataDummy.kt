package com.mrh.storyapp

import com.mrh.storyapp.data.stories.ListStoryItem
import java.time.LocalDate

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                "https://source.unsplash.com/TCpfPxKPOvk/480x240",
                LocalDate.now().toString(),
                "Owner + $i",
                "Description + $i",
                1.0,
                i.toString(),
                1.0
            )
            items.add(story)
        }
        return items
    }
}