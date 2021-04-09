package com.horrors.newhorror.di.module

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


@Entity
class Serial(
    @PrimaryKey var id: String,
    @SerializedName("name")
    @Expose
    var title: String,
    @SerializedName("poster_path")
    @Expose
    var poster: String,
    var opisanie: String,
    var favor: String,
    var commonStar: String
)

@Entity(
    foreignKeys = [ForeignKey(
        entity = Serial::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class SerialRepository(
    @PrimaryKey var id: String,
    var name: String,
    var forksCount: Int,
    var userId: String
)
