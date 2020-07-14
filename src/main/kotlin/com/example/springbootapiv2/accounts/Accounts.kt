package com.example.springbootapiv2.accounts

import javax.persistence.*

@Entity
data class Accounts(
        @Id
        @GeneratedValue
        val id: Int? = null,
        val email: String = "",
        val password: String = "",
        // 하나의 enum만 있는게 아니라 여러가지 enum이 있을수 있음으로 @ElementCollection 사용
        @ElementCollection(fetch = FetchType.EAGER)
        @Enumerated(EnumType.STRING)
        val roles: Set<AccountRole>? = null) {
    override fun hashCode(): Int {
        return id!!
    }
}