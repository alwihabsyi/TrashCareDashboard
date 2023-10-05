package com.p2mw.trashcaredashboard.model.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class UserAddress(
    val namaJalan: String,
    val kelurahan: String,
    val kecamatan: String,
    val kota: String,
    val provinsi: String,
    val kodePos: String,
    val detailAlamat: String,
    val judulAlamat: String,
    val addressId: String = UUID.randomUUID().toString() + judulAlamat
) : Parcelable {
    constructor(): this("", "", "", "" , "", "", "", "")
}
