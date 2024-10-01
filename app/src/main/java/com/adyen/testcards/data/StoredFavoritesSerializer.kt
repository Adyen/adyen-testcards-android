package com.adyen.testcards.data

import androidx.datastore.core.Serializer
import java.io.InputStream
import java.io.OutputStream

internal object StoredFavoritesSerializer : Serializer<StoredFavorites> {

    override val defaultValue: StoredFavorites = StoredFavorites.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): StoredFavorites {
        return StoredFavorites.parseFrom(input)
    }

    override suspend fun writeTo(t: StoredFavorites, output: OutputStream) {
        t.writeTo(output)
    }
}
