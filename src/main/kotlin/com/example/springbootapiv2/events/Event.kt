package com.example.springbootapiv2.events

import com.example.springbootapiv2.accounts.Accounts
import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class Event(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int? = null,
        var name: String = "",
        var description: String = "",
        var beginEnrollmentDateTime: LocalDateTime = LocalDateTime.now(),
        var closeEnrollmentDateTime: LocalDateTime = LocalDateTime.now(),
        var beginEventDateTime: LocalDateTime = LocalDateTime.now(),
        var endEventDateTime: LocalDateTime = LocalDateTime.now(),
        var location: String = "", // (optional) 이게 없으면 온라인 모임
        var basePrice: Int? = null, // (optional)
        var maxPrice: Int? = null, // (optional)
        var limitOfEnrollment: Int? = null,
        var offline: Boolean = true,
        var free: Boolean = false,
        @Enumerated(EnumType.STRING)
        var eventStatus: EventStatus = EventStatus.DRAFT,
        @ManyToOne
        var manager: Accounts? = null) {
    override fun hashCode(): Int {
        return id!!
    }

    fun update() {
        this.free = this.basePrice == 0 &&
                this.maxPrice == 0

        this.offline = !(this.location.isEmpty())
    }
}
