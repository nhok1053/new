package jp.co.pise.studyapp.data.entity

import com.github.gfx.android.orma.annotation.Column
import com.github.gfx.android.orma.annotation.PrimaryKey
import com.github.gfx.android.orma.annotation.Setter
import com.github.gfx.android.orma.annotation.Table

@Table
data class User(@Setter @PrimaryKey(auto = false) @Column("id") val id: String,
           @Setter @Column("accessToken") val accessToken: String,
           @Setter @Column("refreshToken") val refreshToken: String)