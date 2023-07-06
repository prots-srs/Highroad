package com.protsprog.highroad.compose.accessibility.data

import android.content.Context
import com.protsprog.highroad.compose.accessibility.data.interests.InterestsRepository
import com.protsprog.highroad.compose.accessibility.data.posts.PostsRepository

/**
 * Dependency Injection container at the application level.
 */
interface AppJetNewsContainer {
    val postsRepository: PostsRepository
    val interestsRepository: InterestsRepository
}

/**
 * Implementation for the Dependency Injection container at the application level.
 *
 * Variables are initialized lazily and the same instance is shared across the whole app.
 */
class AppJetNewsContainerImpl(private val applicationContext: Context) : AppJetNewsContainer {

    override val postsRepository: PostsRepository by lazy {
        PostsRepository()
    }

    override val interestsRepository: InterestsRepository by lazy {
        InterestsRepository()
    }
}
