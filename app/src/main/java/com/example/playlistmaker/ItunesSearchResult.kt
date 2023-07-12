package com.example.playlistmaker

import android.icu.util.TimeUnit
import android.os.Parcel
import android.os.Parcelable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ItunesSearchResult(
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
    val artworkUrl100: String,
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(trackId)
        parcel.writeString(trackName)
        parcel.writeString(artistName)
        parcel.writeString(trackTimeMillis)
        parcel.writeString(artworkUrl100)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ItunesSearchResult> {
        override fun createFromParcel(parcel: Parcel): ItunesSearchResult {
            return ItunesSearchResult(parcel)
        }

        override fun newArray(size: Int): Array<ItunesSearchResult?> {
            return arrayOfNulls(size)
        }
    }
}

