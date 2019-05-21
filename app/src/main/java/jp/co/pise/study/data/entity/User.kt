package jp.co.pise.study.data.entity

import com.github.gfx.android.orma.annotation.*

@Table
data class User(
        @Setter @PrimaryKey(auto = false) @Column("id") private var id: String,
        @Setter @Column("name") private var name: String? = null,
        @Setter @Column("password") private var password: String? = null
) {
    @Getter
    fun getId() = this.id

    @Getter
    fun getName() = this.name
    fun setName(name: String?) = { this.name = name }

    @Getter
    fun getPassword() = this.password
    fun setPassword(password: String?) = { this.password = password }
}