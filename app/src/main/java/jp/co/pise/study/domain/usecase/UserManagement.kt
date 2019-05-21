package jp.co.pise.study.domain.usecase

import jp.co.pise.study.data.repository.IUserRepository
import javax.inject.Inject

class UserManagement @Inject constructor(private val userRepository: IUserRepository) : Usecase() {
}