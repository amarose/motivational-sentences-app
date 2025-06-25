package com.example.motivationalsentencesapp.di

import org.koin.dsl.module

import com.example.motivationalsentencesapp.data.repository.UserPreferencesRepository
import com.example.motivationalsentencesapp.data.repository.UserPreferencesRepositoryImpl
import com.example.motivationalsentencesapp.data.repository.QuoteRepository
import com.example.motivationalsentencesapp.data.repository.QuoteRepositoryImpl
import com.example.motivationalsentencesapp.domain.usecase.*
import androidx.lifecycle.SavedStateHandle
import com.example.motivationalsentencesapp.ui.home.HomeViewModel
import com.example.motivationalsentencesapp.ui.main.MainViewModel
import com.example.motivationalsentencesapp.ui.notification.NotificationProvider
import com.example.motivationalsentencesapp.ui.notification.NotificationScheduler
import com.example.motivationalsentencesapp.ui.notification.NotificationSchedulerImpl
import com.example.motivationalsentencesapp.ui.onboarding.OnboardingViewModel
import com.example.motivationalsentencesapp.ui.settings.SettingsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // Data Layer
    single<UserPreferencesRepository> { UserPreferencesRepositoryImpl(androidContext()) }
    single<QuoteRepository> { QuoteRepositoryImpl() }
    single<NotificationScheduler> { NotificationSchedulerImpl(androidContext(), get()) }

    // Domain Layer
    factory<GetOnboardingStatusUseCase> { GetOnboardingStatusUseCaseImpl(get()) }
    factory<SetOnboardingCompletedUseCase> { SetOnboardingCompletedUseCaseImpl(get()) }
    factory<GetNotificationPreferencesUseCase> { GetNotificationPreferencesUseCaseImpl(get()) }
    factory<UpdateNotificationPreferencesUseCase> { UpdateNotificationPreferencesUseCaseImpl(get()) }
    factory<GetRandomQuoteUseCase> { GetRandomQuoteUseCaseImpl(get()) }

    // Presentation Layer
    viewModel { MainViewModel(get()) }
    viewModel { OnboardingViewModel(androidApplication(), get(), get(), get()) }
    viewModel { (handle: SavedStateHandle) -> HomeViewModel(get(), get(), handle) }
    viewModel { SettingsViewModel(get(), get(), get()) }

    single { NotificationProvider(androidContext()) }

}
