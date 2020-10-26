package com.dnedev.favorite.places.data.venues

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "venues")
data class Venue(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "category_id")
    val categoryId: String,
    val name: String,
    val latitude: Double,
    val longitude: Double
)