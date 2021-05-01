package com.enveu.EnveuCategoryServices

import com.google.gson.annotations.SerializedName

data class CustomFields(

        @field:SerializedName("TYPE-RAIL")
        val typeRail: String? = null,

        @field:SerializedName("RAIL-TITLE")
        val railTile: String? = null,
        @field:SerializedName("GENRE")
        val genre: String? = null,
        @field:SerializedName("GENRE-RULE")
        val genreRule: String? = null,
        @field:SerializedName("MEDIA-TYPES")
        val mediaType: String? = null
)
