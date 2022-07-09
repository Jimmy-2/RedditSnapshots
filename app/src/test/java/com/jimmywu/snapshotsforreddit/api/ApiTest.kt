package com.jimmywu.snapshotsforreddit.api

import com.jimmywu.snapshotsforreddit.network.services.RedditApiTest

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

    @Test
    fun getSearchResults() {
        val response = api.getSearchResultsTest("r","aww","hello",1,null,null,25,null,null,1).execute()
        println(response.raw().toString())
        println(response.body())
        assertNotNull(response.body())
    }

    @Test
    fun getSearchResultsSubreddit() {
        val response = api.getSearchResultsSubreddit("dog","sr",null,25,null,null).execute()
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



    @Test
    fun getCommentsTree() {
        val response = api.getPostComments("aww","vsu6bl").execute()
        println(response.raw().toString())
        println(response.body())
        assertNotNull(response.body())
    }

}