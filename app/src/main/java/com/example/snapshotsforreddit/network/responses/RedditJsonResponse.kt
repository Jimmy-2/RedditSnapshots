package com.example.snapshotsforreddit.network.responses

import com.example.snapshotsforreddit.network.responses.Data

//example of reddit front page json: https://api.reddit.com/

data class RedditJsonResponse(val kind: String?, val data: Data?)

/*

  "kind": "Listing",
  "data": {

*/

