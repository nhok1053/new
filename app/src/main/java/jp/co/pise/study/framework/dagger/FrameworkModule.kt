package jp.co.pise.study.framework.dagger

import android.content.Context
import android.util.Log
import com.github.gfx.android.orma.migration.ManualStepMigration
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import jp.co.pise.study.BuildConfig
import jp.co.pise.study.definition.DbConfig
import jp.co.pise.study.data.entity.OrmaDatabase
import jp.co.pise.study.definition.ApiConfig
import jp.co.pise.study.framework.kotshi.AppJsonAdapterFactory
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
            .addInterceptor {
                val request = it.request().newBuilder().build()
                it.proceed(request)
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