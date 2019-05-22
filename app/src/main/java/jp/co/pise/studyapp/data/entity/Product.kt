package jp.co.pise.studyapp.data.entity

import com.github.gfx.android.orma.annotation.Column
import com.github.gfx.android.orma.annotation.PrimaryKey
import com.github.gfx.android.orma.annotation.Setter
import com.github.gfx.android.orma.annotation.Table
import io.reactivex.annotations.NonNull

@Table
data class Product(@Setter @NonNull @PrimaryKey(auto = false) @Column("id") val id: String,
                   @Setter @Column("name") val name: String,
                   @Setter @Column("description") val description: String,
                   @Setter @Column("imageUrl") val imageUrl: String,
                   @Setter @Column("priceWithoutTax") val priceWithoutTax: Int,
                   @Setter @Column("priceInTax") val priceInTax: Int)
