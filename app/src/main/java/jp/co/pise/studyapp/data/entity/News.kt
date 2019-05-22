package jp.co.pise.studyapp.data.entity

import com.github.gfx.android.orma.annotation.Column
import com.github.gfx.android.orma.annotation.PrimaryKey
import com.github.gfx.android.orma.annotation.Setter
import com.github.gfx.android.orma.annotation.Table
import io.reactivex.annotations.NonNull

@Table
data class News(@Setter @PrimaryKey(auto = false) @NonNull @Column("id") val id: String,
           @Setter @Column("imageUrl") val imageUrl: String?,
           @Setter @Column("description") val description: String?,
           @Setter @Column("url") val url: String?,
           @Setter @Column("sortOrder") val sortOrder: Int)
