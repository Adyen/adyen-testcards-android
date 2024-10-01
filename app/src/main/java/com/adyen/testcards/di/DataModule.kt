package com.adyen.testcards.di

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import com.adyen.testcards.data.DefaultFavoriteRepository
import com.adyen.testcards.data.DefaultPaymentMethodRepository
import com.adyen.testcards.data.FavoriteRepository
import com.adyen.testcards.data.PaymentMethodRepository
import com.adyen.testcards.data.PaymentMethodService
import com.adyen.testcards.data.StoredFavorites
import com.adyen.testcards.data.StoredFavoritesSerializer
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindPaymentMethodRepository(default: DefaultPaymentMethodRepository): PaymentMethodRepository

    @Binds
    internal abstract fun bindFavoriteRepository(default: DefaultFavoriteRepository): FavoriteRepository

    companion object {

        @Singleton
        @Provides
        internal fun provideMoshi(): Moshi = Moshi.Builder().build()

        @Singleton
        @Provides
        internal fun provideRetrofit(moshi: Moshi): Retrofit = Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/adyen-examples/adyen-testcards-extension/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        @Provides
        internal fun providePaymentMethodService(retrofit: Retrofit): PaymentMethodService = retrofit.create()

        @Singleton
        @Provides
        internal fun provideFavoritesDataStore(@ApplicationContext context: Context): DataStore<StoredFavorites> =
            DataStoreFactory.create(
                serializer = StoredFavoritesSerializer,
                corruptionHandler = ReplaceFileCorruptionHandler { e ->
                    Log.e("DataModule", "", e)
                    StoredFavorites.getDefaultInstance()
                },
            ) {
                context.dataStoreFile("stored_favorites.pb")
            }
    }
}
