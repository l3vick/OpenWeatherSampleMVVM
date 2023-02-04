package es.pablorojas.openweathersamplemvvm.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.pablorojas.openweathersamplemvvm.BuildConfig.API_KEY
import es.pablorojas.openweathersamplemvvm.BuildConfig.BASE_URL
import es.pablorojas.openweathersamplemvvm.network.WeatherApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(logging: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(QueryParameterInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    class QueryParameterInterceptor() : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val url = chain.request().url.newBuilder()
                .addQueryParameter("appid", API_KEY)
                .build()

            val request = chain.request().newBuilder()
                .url(url)
                .build()
            return chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    fun provideApiInterface(retrofit: Retrofit): WeatherApi {
        return retrofit.create(WeatherApi::class.java)
    }
}