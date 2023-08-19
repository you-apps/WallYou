package com.bnyro.wallpaper.api

abstract class CommunityApi : Api() {

    abstract val defaultCommunityName: String
    private val communityKey get() = name + "community"

    fun setCommunityName(name: String) {
        setPref(communityKey, name)
    }

    fun getCommunityName(): String {
        return getPref(communityKey, defaultCommunityName)
    }
}