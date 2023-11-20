package com.engin.swiftreader.di.local

import com.engin.swiftreader.common.data.local.DataStoreImpl
import com.engin.swiftreader.common.domain.PersistenceKeyValueRepository
import com.engin.swiftreader.common.util.DataStorePreferences
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class PersistenceModule {

    @Binds
    @DataStorePreferences
    abstract fun bindPersistenceKeyValue(dataStoreImpl: DataStoreImpl) : PersistenceKeyValueRepository
}