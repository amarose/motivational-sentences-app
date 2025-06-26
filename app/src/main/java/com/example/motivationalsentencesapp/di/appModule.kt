package com.example.motivationalsentencesapp.di

import org.koin.dsl.module

import com.example.motivationalsentencesapp.data.repository.UserPreferencesRepository
import com.example.motivationalsentencesapp.data.repository.UserPreferencesRepositoryImpl
import com.example.motivationalsentencesapp.data.repository.QuoteRepository
import com.example.motivationalsentencesapp.data.repository.QuoteRepositoryImpl
import com.example.motivationalsentencesapp.domain.usecase.*
import androidx.lifecycle.SavedStateHandle
import androidx.room.Room

import com.example.motivationalsentencesapp.data.local.AppDatabase
import com.example.motivationalsentencesapp.data.repository.ArchiveRepository
import com.example.motivationalsentencesapp.data.repository.ArchiveRepositoryImpl
import com.example.motivationalsentencesapp.ui.archive.ArchiveViewModel
import com.example.motivationalsentencesapp.ui.background.BackgroundViewModel
import com.example.motivationalsentencesapp.ui.favorites.FavoritesViewModel
import com.example.motivationalsentencesapp.ui.home.HomeViewModel
import com.example.motivationalsentencesapp.ui.main.MainViewModel

import com.example.motivationalsentencesapp.ui.notification.NotificationScheduler
import com.example.motivationalsentencesapp.ui.notification.NotificationSchedulerWorkManagerImpl
import com.example.motivationalsentencesapp.ui.onboarding.OnboardingViewModel
import com.example.motivationalsentencesapp.ui.settings.SettingsViewModel
import com.example.motivationalsentencesapp.worker.NotificationWorker
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.workmanager.dsl.worker

val appModule = module {

    // Data Layer
    single<UserPreferencesRepository> { UserPreferencesRepositoryImpl(androidContext()) }
    single<QuoteRepository> { QuoteRepositoryImpl(get()) }
    single<ArchiveRepository> { ArchiveRepositoryImpl(get()) }
    single<NotificationScheduler> { NotificationSchedulerWorkManagerImpl(androidContext(), get()) }

    // Database
        single {
            Room.databaseBuilder(androidApplication(), AppDatabase::class.java, "app-database")
                .fallbackToDestructiveMigration(false)
            .build()
    }
    single { get<AppDatabase>().quoteDao() }
    single { get<AppDatabase>().archivedQuoteDao() }

    // Domain Layer
    factory<GetOnboardingStatusUseCase> { GetOnboardingStatusUseCaseImpl(get()) }
    factory<SetOnboardingCompletedUseCase> { SetOnboardingCompletedUseCaseImpl(get()) }
    factory<GetNotificationPreferencesUseCase> { GetNotificationPreferencesUseCaseImpl(get()) }
    factory<UpdateNotificationPreferencesUseCase> { UpdateNotificationPreferencesUseCaseImpl(get()) }
    factory<GetRandomQuoteUseCase> { GetRandomQuoteUseCaseImpl(get()) }
    factory<UpdateQuoteUseCase> { UpdateQuoteUseCaseImpl(get()) }
    factory<GetFavoriteQuotesUseCase> { GetFavoriteQuotesUseCaseImpl(get()) }
    factory<GetQuoteByIdUseCase> { GetQuoteByIdUseCaseImpl(get()) }
    factory<ArchiveQuoteUseCase> { ArchiveQuoteUseCaseImpl(get()) }
    factory<GetArchivedQuotesUseCase> { GetArchivedQuotesUseCaseImpl(get()) }
    factory<CleanUpArchiveUseCase> { CleanUpArchiveUseCaseImpl(get()) }
    factory<GetSelectedBackgroundUseCase> { GetSelectedBackgroundUseCaseImpl(get()) }
    factory<UpdateSelectedBackgroundUseCase> { UpdateSelectedBackgroundUseCaseImpl(get()) }
    factory<GetAvailableBackgroundsUseCase> { GetAvailableBackgroundsUseCaseImpl() }

    // Presentation Layer
    viewModel { MainViewModel(get()) }
    viewModel { OnboardingViewModel(androidApplication(), get(), get(), get()) }
    viewModel { (handle: SavedStateHandle) -> HomeViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { SettingsViewModel(get(), get(), get()) }
    viewModel { FavoritesViewModel(get(), get()) }
    viewModel { ArchiveViewModel(get(), get()) }
    viewModel { BackgroundViewModel(get(), get(), get()) }
    worker { NotificationWorker(get(), get()) }



}
