package com.example.clothesshop.di

import com.example.clothesshop.data.userdata.UserDataSource
import com.example.clothesshop.data.userdata.UserDataSourceSharedPrefs
import com.example.clothesshop.data.basket.BasketSource
import com.example.clothesshop.data.basket.BasketSourceImp
import com.example.clothesshop.data.category.CategorySource
import com.example.clothesshop.data.category.CategorySourceImp
import com.example.clothesshop.data.order.OrderSource
import com.example.clothesshop.data.order.OrderSourceImp
import com.example.clothesshop.data.product.ProductSource
import com.example.clothesshop.data.product.ProductSourceImp
import com.example.clothesshop.data.singup.SingUpSource
import com.example.clothesshop.data.singup.SingUpSourceImp
import com.example.clothesshop.data.type.TypeSource
import com.example.clothesshop.data.type.TypeSourceImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


//todo add many module
@Module
@InstallIn(SingletonComponent::class)
abstract class UserDataModule {

    @Binds
    abstract fun bindsUserData(
        userDataSourceSharedPrefs: UserDataSourceSharedPrefs
    ): UserDataSource

    @Binds
    abstract fun bindsBasketSource(
        basketSourceImp: BasketSourceImp
    ):BasketSource


    @Binds
    abstract fun bindsCategorySource(
        categorySourceImp: CategorySourceImp
    ):CategorySource

    @Binds
    abstract fun bindsOrderSource(
        orderSourceImp: OrderSourceImp
    ):OrderSource

    @Binds
    abstract fun bindsProductSource(
        productSourceImp: ProductSourceImp
    ):ProductSource


    @Binds
    abstract fun bindsSingUpSource(
        singUpSourceImp: SingUpSourceImp
    ):SingUpSource

    @Binds
    abstract fun bindsTypeSource(
        typeSourceImp: TypeSourceImp
    ):TypeSource


}