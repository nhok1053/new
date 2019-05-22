package jp.co.pise.studyapp.domain.model

import io.reactivex.annotations.NonNull
import jp.co.pise.studyapp.data.entity.News

class GetNewsResult(val news: List<GetNewsItemModel>? = null) : ProcessResultModel()

class GetNewsItemModel(val id: String,
                       val imageUrl: String?,
                       val description: String?,
                       val url: String?,
                       val sortOrder: Int) {

    fun toEntity(): News {
        return News(this.id, this.imageUrl, this.description, this.url, this.sortOrder)
    }

    companion object {
        internal fun fromEntity(@NonNull news: News): GetNewsItemModel {
            return GetNewsItemModel(
                    news.id,
                    news.imageUrl,
                    news.description,
                    news.url,
                    news.sortOrder)
        }
    }
}

class SaveNewsChallenge(val news: List<News>?) : ProcessChallengeModel()

class SaveNewsResult : ProcessResultModel()