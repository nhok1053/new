package jp.co.pise.studyapp.framework.dagger

import dagger.Module
import dagger.Provides
import jp.co.pise.studyapp.data.entity.OrmaDatabase
import jp.co.pise.studyapp.data.repository.ICouponRepository
import jp.co.pise.studyapp.data.repository.INewsRepository
import jp.co.pise.studyapp.data.repository.IProductRepository
import jp.co.pise.studyapp.data.repository.IUserRepository
import jp.co.pise.studyapp.data.repository.impl.CouponRepository
import jp.co.pise.studyapp.data.repository.impl.NewsRepository
import jp.co.pise.studyapp.data.repository.impl.ProductRepository
import jp.co.pise.studyapp.data.repository.impl.UserRepository
import jp.co.pise.studyapp.framework.retrofit.CouponApiInterface
import jp.co.pise.studyapp.framework.retrofit.NewsApiInterface
import jp.co.pise.studyapp.framework.retrofit.ProductApiInterface
import jp.co.pise.studyapp.framework.retrofit.UserApiInterface

@Module
class RepositoryModule {
    @Provides
    fun provideNewsRepository(newsApi: NewsApiInterface, db: OrmaDatabase): INewsRepository {
        return NewsRepository(newsApi, db)
    }

    @Provides
    fun provideCouponRepository(couponApi: CouponApiInterface, userApi: UserApiInterface, db: OrmaDatabase): ICouponRepository {
        return CouponRepository(couponApi, userApi, db)
    }

    @Provides
    fun provideUserRepository(userApi: UserApiInterface, db: OrmaDatabase): IUserRepository {
        return UserRepository(userApi, db)
    }

    @Provides
    fun provideProductRepository(productApi: ProductApiInterface, db: OrmaDatabase): IProductRepository {
        return ProductRepository(productApi, db)
    }
}