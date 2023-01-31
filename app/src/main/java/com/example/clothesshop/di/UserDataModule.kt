package com.example.clothesshop.di

import com.example.clothesshop.data.UserDataSource
import com.example.clothesshop.data.UserDataSourceSharedPrefs
import com.example.clothesshop.data.basket.BasketSource
import com.example.clothesshop.data.basket.BasketSourceImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UserDataModule {

    @Binds
    abstract fun bindsUserData(
        userDataSourceSharedPrefs: UserDataSourceSharedPrefs
    ):UserDataSource

    @Binds
    abstract fun bindsBasketSource(
        basketSourceImp: BasketSourceImp
    ):BasketSource


}