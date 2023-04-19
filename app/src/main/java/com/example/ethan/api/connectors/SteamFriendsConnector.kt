package com.example.ethan.api.connectors

import com.example.ethan.BuildConfig
import com.example.ethan.api.interfaces.RestInterface
import org.json.JSONObject

class SteamFriendsConnector {
    private val restInterface = RestInterface()

    // gets friends based on steamid
    fun get(steamId: String): List<Pair<String, Int>> {
        val url = "http://api.steampowered.com/ISteamUser/GetFriendList/v0001/?key=${BuildConfig.API_KEY_STEAM}&steamid=$steamId&relationship=friend"
        val response = restInterface.get(url)
        val jsonResponse = JSONObject(response!!)

        val friendList = jsonResponse.getJSONObject("friendslist").getJSONArray("friends")
        val friends = mutableListOf<Pair<String, Int>>()

        for (i in 0 until friendList.length()) {
            val friend = friendList.getJSONObject(i)
            val friendSteamId = friend.getString("steamid")

            val friendStatusUrl = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=${BuildConfig.API_KEY_STEAM}&steamids=$friendSteamId"
            val friendStatusResponse = restInterface.get(friendStatusUrl)
            val friendStatusJsonResponse = JSONObject(friendStatusResponse!!)
            val firstFriend = friendStatusJsonResponse.getJSONObject("response").getJSONArray("players").getJSONObject(0)
            val friendStatus = firstFriend.getInt("personastate")

            friends.add(Pair(firstFriend.getString("personaname"), friendStatus))
        }

        return friends
    }
}