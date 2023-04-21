package com.doach.mediasearchapp.android.di

import android.content.Context
import com.doach.mediasearchapp.android.R
import com.doach.mediasearchapp.android.data.ImageRepositoryImpl
import com.doach.mediasearchapp.android.data.MediaRepositoryImpl
import com.doach.mediasearchapp.android.data.SearchHistoryRepositoryImpl
import com.doach.mediasearchapp.android.data.VideoRepositoryImpl
import com.doach.mediasearchapp.android.data.remote.retrofit.ApiService
import com.doach.mediasearchapp.android.data.local.AppPreferencesImpl
import com.doach.mediasearchapp.android.data.remote.ImageRemoteDataSource
import com.doach.mediasearchapp.android.data.remote.VideoRemoteDataSource
import com.doach.mediasearchapp.android.data.remote.retrofit.AuthenticationInterceptor
import com.doach.mediasearchapp.android.data.remote.retrofit.ResultCallAdapterFactory
import com.doach.mediasearchapp.android.domain.repository.ImageRepository
import com.doach.mediasearchapp.android.domain.repository.MediaRepository
import com.doach.mediasearchapp.android.domain.repository.SearchHistoryRepository
import com.doach.mediasearchapp.android.domain.repository.VideoRepository
import com.doach.mediasearchapp.android.domain.usecase.GetLatestMediaByQueryUseCase
import com.doach.mediasearchapp.android.domain.usecase.GetLatestMediaByQueryUseCaseImpl
import com.doach.mediasearchapp.android.domain.usecase.GetMediaFlowByQueryUseCase
import com.doach.mediasearchapp.android.domain.usecase.GetMediaFlowByQueryUseCaseImpl
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

/**
 * 의존성 주입을 위한 컨테이너
 * **/
class AppContainer(context: Context) {
    // TODO("시간이 남는 경우 hilt 로 migration")

    private val retrofitClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(AuthenticationInterceptor(context))
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(context.getString(R.string.kakao_api_base_url))
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ResultCallAdapterFactory())
            .client(retrofitClient)
            .build()
    }

    /**
     * Local
     * **/
    private val appPreferences: AppPreferencesImpl by lazy { AppPreferencesImpl(context) }

    /**
     * Remote
     * **/
    private val apiService: ApiService by lazy { retrofit.create() }
    private val imageRemoteDataSource: ImageRemoteDataSource by lazy { ImageRemoteDataSource(apiService) }
    private val videoRemoteDataSource: VideoRemoteDataSource by lazy { VideoRemoteDataSource(apiService) }

    /**
     * Repository
     * **/
    val imageRepository: ImageRepository by lazy {
        ImageRepositoryImpl(
            api = apiService,
            imageRemoteDataSource = imageRemoteDataSource,
            mediaDao = appPreferences,
            ioDispatcher = Dispatchers.IO
        )
    }

    val videoRepository: VideoRepository by lazy {
        VideoRepositoryImpl(
            api = apiService,
            videoRemoteDataSource = videoRemoteDataSource,
            mediaDao = appPreferences,
            ioDispatcher = Dispatchers.IO
        )
    }

    val mediaRepository: MediaRepository by lazy {
        MediaRepositoryImpl(
            api = apiService,
            appPreferences = appPreferences,
            ioDispatcher = Dispatchers.IO
        )
    }

    val searchRepository: SearchHistoryRepository by lazy {
        SearchHistoryRepositoryImpl(
            searchDao = appPreferences
        )
    }

    /**
     * UseCase
     * **/
    fun provideGetLatestNewsByQueryUseCase(): GetLatestMediaByQueryUseCase {
        return GetLatestMediaByQueryUseCaseImpl(imageRepository, videoRepository)
    }

    fun provideGetMediaFlowByQueryUseCase(): GetMediaFlowByQueryUseCase {
        return GetMediaFlowByQueryUseCaseImpl(mediaRepository)
    }

}
