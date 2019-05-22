package jp.co.pise.studyapp.framework.dagger

import android.content.Context
import android.util.Log
import com.github.gfx.android.orma.migration.ManualStepMigration
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import jp.co.pise.studyapp.BuildConfig
import jp.co.pise.studyapp.definition.DbConfig
import jp.co.pise.studyapp.data.entity.OrmaDatabase
import jp.co.pise.studyapp.definition.ApiConfig
import jp.co.pise.studyapp.framework.kotshi.AppJsonAdapterFactory
import jp.co.pise.studyapp.framework.retrofit.CouponApiInterface
import jp.co.pise.studyapp.framework.retrofit.NewsApiInterface
import jp.co.pise.studyapp.framework.retrofit.ProductApiInterface
import jp.co.pise.studyapp.framework.retrofit.UserApiInterface
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class FrameworkModule {
    @Singleton
    @Provides
    fun provideMoshi(): Moshi
            = Moshi.Builder()
            .add(AppJsonAdapterFactory.INSTANCE)
            .build()

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient
            = OkHttpClient.Builder()
            .connectTimeout(ApiConfig.CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
            .readTimeout(ApiConfig.READ_TIMEOUT_SEC, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                chain.request().newBuilder().run {
                    addHeader(ApiConfig.X_API_KEY, ApiConfig.X_API_KEY_VALUE)
                    addHeader(ApiConfig.X_API_CLIENT, ApiConfig.X_API_CLIENT_VALUE)
                    addHeader(ApiConfig.X_API_DEVICE, ApiConfig.X_API_DEVICE_VALUE)

                    if (chain.request().method() != ApiConfig.REQUEST_METHOD_POST)
                        addHeader(ApiConfig.CONTENT_TYPE, ApiConfig.CONTENT_TYPE_JSON_UTF8)

                    chain.proceed(build())
                }
            }
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(oktHttpClient: OkHttpClient, moshi: Moshi)
            = Retrofit.Builder()
            .client(oktHttpClient)
            .baseUrl(BuildConfig.URL_BASE)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideCouponApiInterface(retrofit: Retrofit): CouponApiInterface {
        return retrofit.create(CouponApiInterface::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsApiInterface(retrofit: Retrofit): NewsApiInterface {
        return retrofit.create(NewsApiInterface::class.java)
    }

    @Provides
    @Singleton
    fun provideProductApiInterface(retrofit: Retrofit): ProductApiInterface {
        return retrofit.create(ProductApiInterface::class.java)
    }

    @Provides
    @Singleton
    fun provideUserApiInterface(retrofit: Retrofit): UserApiInterface {
        return retrofit.create(UserApiInterface::class.java)
    }

    @Singleton
    @Provides
    fun provideOrmaDatabase(context: Context): OrmaDatabase
        = OrmaDatabase.builder(context)
            .name(DbConfig.DB_NAME)
            .migrationStep(DbConfig.DB_VERSION_1, object : ManualStepMigration.ChangeStep() {
                override fun change(helper: ManualStepMigration.Helper) {
                    Log.d("ManualStepMigration", "Do Migration VERSION : ${DbConfig.DB_VERSION_1}")
                }
            })
            .build()
}