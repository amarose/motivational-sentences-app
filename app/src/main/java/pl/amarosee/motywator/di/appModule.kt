package pl.amarosee.motywator.di

import org.koin.dsl.module

import pl.amarosee.motywator.data.repository.UserPreferencesRepository
import pl.amarosee.motywator.data.repository.UserPreferencesRepositoryImpl
import pl.amarosee.motywator.data.repository.QuoteRepository
import pl.amarosee.motywator.data.repository.QuoteRepositoryImpl
import pl.amarosee.motywator.data.repository.ArchiveRepository
import pl.amarosee.motywator.data.repository.ArchiveRepositoryImpl
import pl.amarosee.motywator.domain.usecase.*
import pl.amarosee.motywator.data.usecase.ShareQuoteUseCaseImpl
import pl.amarosee.motywator.domain.usecase.GetNextNotificationTimeUseCase
import pl.amarosee.motywator.domain.usecase.GetNextNotificationTimeUseCaseImpl
import androidx.room.Room
import pl.amarosee.motywator.data.local.AppDatabase
import pl.amarosee.motywator.data.local.MIGRATION_3_4
import pl.amarosee.motywator.data.datastore.SettingsDataStore
import pl.amarosee.motywator.ui.archive.ArchiveViewModel
import pl.amarosee.motywator.ui.background.BackgroundViewModel
import pl.amarosee.motywator.ui.favorites.FavoritesViewModel
import pl.amarosee.motywator.ui.home.HomeViewModel
import pl.amarosee.motywator.ui.main.MainViewModel
import pl.amarosee.motywator.ui.notification.NotificationScheduler
import pl.amarosee.motywator.ui.notification.NotificationSchedulerWorkManagerImpl
import pl.amarosee.motywator.ui.onboarding.OnboardingViewModel
import pl.amarosee.motywator.ui.settings.SettingsViewModel
import pl.amarosee.motywator.worker.NotificationWorker
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.androidx.workmanager.dsl.worker

val appModule = module {

    // Data Layer
    single<UserPreferencesRepository> { UserPreferencesRepositoryImpl(androidContext()) }
    single<QuoteRepository> { QuoteRepositoryImpl(get()) }
    single<ArchiveRepository> { ArchiveRepositoryImpl(get()) }
    single<NotificationScheduler> { NotificationSchedulerWorkManagerImpl(androidContext(), get()) }
    single { SettingsDataStore(androidContext()) }

    // Database
    single {
        val db = Room.databaseBuilder(androidApplication(), AppDatabase::class.java, "app-database")
            .fallbackToDestructiveMigration(true)
            .addMigrations(MIGRATION_3_4)
            .build()
        db
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
    factory<MarkQuoteAsSeenUseCase> { MarkQuoteAsSeenUseCaseImpl(get()) }
    factory<ResetAllQuotesToUnseenUseCase> { ResetAllQuotesToUnseenUseCaseImpl(get()) }
    factory<ArchiveQuoteUseCase> { ArchiveQuoteUseCaseImpl(get()) }
    factory<GetArchivedQuotesUseCase> { GetArchivedQuotesUseCaseImpl(get()) }
    factory<CleanUpArchiveUseCase> { CleanUpArchiveUseCaseImpl(get()) }
    factory<GetSelectedBackgroundUseCase> { GetSelectedBackgroundUseCaseImpl(get()) }
    factory<UpdateSelectedBackgroundUseCase> { UpdateSelectedBackgroundUseCaseImpl(get()) }
    factory<GetAvailableBackgroundsUseCase> { GetAvailableBackgroundsUseCaseImpl() }
    factory<GetNextNotificationTimeUseCase> { GetNextNotificationTimeUseCaseImpl(get()) }
    factory<ShareQuoteUseCase> { ShareQuoteUseCaseImpl() }

    // Presentation Layer
    viewModel { MainViewModel(get()) }
    viewModel { OnboardingViewModel(androidApplication(), get(), get(), get()) }
    viewModel {
        HomeViewModel(
            getRandomQuoteUseCase = get(),
            updateQuoteUseCase = get(),
            getQuoteByIdUseCase = get(),
            archiveQuoteUseCase = get(),
            markQuoteAsSeenUseCase = get(),
            getSelectedBackgroundUseCase = get(),
            shareQuoteUseCase = get(),
            settingsDataStore = get(),
        )
    }
    viewModel { SettingsViewModel(get(), get(), get(), get()) }
    viewModel { FavoritesViewModel(
        getFavoriteQuotesUseCase = get(),
        updateQuoteUseCase = get(),
        shareQuoteUseCase = get()
    ) }
    viewModel {
        ArchiveViewModel(
            getArchivedQuotesUseCase = get(),
            cleanUpArchiveUseCase = get(),
            shareQuoteUseCase = get()
        )
    }
    viewModel { BackgroundViewModel(get(), get(), get()) }
    worker { NotificationWorker(get(), get()) }
}
