package jp.co.pise.study.framework.dagger

import dagger.Module
import dagger.Provides
import jp.co.pise.study.data.entity.OrmaDatabase
import jp.co.pise.study.data.repository.IUserRepository
import jp.co.pise.study.data.repository.impl.UserRepository
import retrofit2.Retrofit

@Module
class RepositoryModule {
    @Provides
    fun provideIUserRepository(retrofit: Retrofit, orma: OrmaDatabase) : IUserRepository = UserRepository(retrofit, orma)
}