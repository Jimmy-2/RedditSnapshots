package com.example.snapshotsforreddit.api

import com.example.snapshotsforreddit.network.services.RedditApiTest

import junit.framework.Assert.assertNotNull
import org.junit.Test

class ApiTest {
    private val api = RedditApiTest.RETROFIT_SERVICE_TEST_TEST

    @Test
    fun getFrontPage25Posts() {
        val response = api.getListOfPostsTest(null).execute()
        println(response.raw().toString())
        println(response.body())
        assertNotNull(response.body())
    }

    /*
    @Test
    fun getPostDetailInformation() {
        val response = api.getPostDetailsTest("r/cats/comments/tl5hed/what_bread_is_my_cat/").execute()
        println(response.raw().toString())
        assertNotNull(response.body())
    }
     */





}