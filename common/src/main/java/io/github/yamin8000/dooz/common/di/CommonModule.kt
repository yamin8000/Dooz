package io.github.yamin8000.dooz.common.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.yamin8000.dooz.common.data.datasource.DataStore.settings
import io.github.yamin8000.dooz.common.data.repository.SettingsDataStoreRepository
import io.github.yamin8000.dooz.common.domain.repository.SettingsRepository
import io.github.yamin8000.dooz.common.util.datastore.DataStoreHelper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {

    @Provides
    @Singleton
    fun providesSettingsRepository(
        helper: DataStoreHelper
    ): SettingsRepository {
        return SettingsDataStoreRepository(helper)
    }

    @Provides
    @Singleton
    fun providesDataStoreHelper(
        @ApplicationContext
        context: Context
    ): DataStoreHelper {
        return DataStoreHelper(context.settings)
    }
}