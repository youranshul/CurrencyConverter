package com.example.branch.app

import com.example.branch.data.CurrencyRepository
import com.example.branch.domain.GetCurrenciesUseCase
import com.example.branch.domain.GetCurrencyRatesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class CurrencyModule {

    @Provides
    @ViewModelScoped
    fun providesCurrencyUseCase(repository: CurrencyRepository) : GetCurrenciesUseCase {
        return GetCurrenciesUseCase { repository.fetchCurrencies() }
    }

    @Provides
    @ViewModelScoped
    fun providesCurrencyRatesUseCase(repository: CurrencyRepository) : GetCurrencyRatesUseCase {
        return GetCurrencyRatesUseCase { repository.fetchCurrencyRates() }
    }
}