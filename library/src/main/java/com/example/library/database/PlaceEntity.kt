package com.example.library.database

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@RequiresApi(Build.VERSION_CODES.Q)
@Entity(tableName = "place_table", indices = [Index(value = ["idPlace"], unique = true)])
data class PlaceEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val idPlace: String?,
    val name : String?,
    val bookmark: Boolean
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readBoolean()
    )

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(idPlace)
        parcel.writeString(name)
        parcel.writeBoolean(bookmark)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlaceEntity> {
        override fun createFromParcel(parcel: Parcel): PlaceEntity {
            return PlaceEntity(parcel)
        }

        override fun newArray(size: Int): Array<PlaceEntity?> {
            return arrayOfNulls(size)
        }
    }
}
