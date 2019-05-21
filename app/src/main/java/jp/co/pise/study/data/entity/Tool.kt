package jp.co.pise.study.data.entity

import com.github.gfx.android.orma.annotation.*

@Table
class Tool(
        @Setter @PrimaryKey(auto = false) @Column("id") private var id: String,
        @Setter @Column("name") private var name: String? = null
) {
    @Getter
    fun getId() = this.id

    @Getter
    fun getName() = this.name
    fun setName(name: String?) { this.name = name }
}