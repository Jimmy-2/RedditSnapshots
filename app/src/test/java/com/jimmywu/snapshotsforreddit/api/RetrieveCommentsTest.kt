package com.jimmywu.snapshotsforreddit.api

import com.jimmywu.snapshotsforreddit.network.services.RedditApiTest
import junit.framework.Assert
import org.junit.Test


class RetrieveCommentsTest {
    private val api = RedditApiTest.RETROFIT_SERVICE_TEST_TEST


    @Test
    fun getCommentsTree() {
        val response = api.getPostComments("aww","vsu6bl").execute()
        println(response.raw().toString())
        println(response.body())
        Assert.assertNotNull(response.body())
    }
}