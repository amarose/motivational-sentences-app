package com.example.motivationalsentencesapp.di

import org.koin.dsl.module

import com.example.motivationalsentencesapp.data.repository.UserPreferencesRepository
import com.example.motivationalsentencesapp.data.repository.UserPreferencesRepositoryImpl
import com.example.motivationalsentencesapp.domain.usecase.*
import com.example.motivationalsentencesapp.ui.main.MainViewModel
import com.example.motivationalsentencesapp.ui.onboarding.OnboardingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // Data Layer
    single<UserPreferencesRepository> { UserPreferencesRepositoryImpl(androidContext()) }

    // Domain Layer
    factory<GetOnboardingStatusUseCase> { GetOnboardingStatusUseCaseImpl(get()) }
    factory<SetOnboardingCompletedUseCase> { SetOnboardingCompletedUseCaseImpl(get()) }
    factory<GetNotificationPreferencesUseCase> { GetNotificationPreferencesUseCaseImpl(get()) }
    factory<UpdateNotificationPreferencesUseCase> { UpdateNotificationPreferencesUseCaseImpl(get()) }

    // Presentation Layer
    viewModel { MainViewModel(get()) }
    viewModel { OnboardingViewModel(get(), get(), get()) }

}
