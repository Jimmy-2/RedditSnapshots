package com.example.snapshotsforreddit.api

import com.example.snapshotsforreddit.network.services.RedditApi

import junit.framework.Assert.assertNotNull
import org.junit.Test

class ApiTest {
    private val api = RedditApi.retrofitServiceTest

    @Test
    fun getFrontPage() {
        val response = api.getListOfPostsTest().execute()
        println(response.raw().toString())
        assertNotNull(response.body())
    }


    @Test
    fun getPostDetailInformation() {
        val response = api.getPostDetailsTest("r/cats/comments/tl5hed/what_bread_is_my_cat/").execute()
        println(response.raw().toString())
        assertNotNull(response.body())
    }
}