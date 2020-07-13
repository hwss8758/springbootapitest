package com.example.springbootapiv2.events

import java.time.LocalDateTime

// 입력을 받을수 있는 값만 명시
data class EventDto(var name: String = "",
                    var description: String = "",
                    var beginEnrollmentDateTime: LocalDateTime = LocalDateTime.now(),
                    var closeEnrollmentDateTime: LocalDateTime = LocalDateTime.now(),
                    var beginEventDateTime: LocalDateTime = LocalDateTime.now(),
                    var endEventDateTime: LocalDateTime = LocalDateTime.now(),
                    var location: String = "", // (optional) 이게 없으면 온라인 모임
                    var basePrice: Int? = null, // (optional)
                    var maxPrice: Int? = null, // (optional)
                    var limitOfEnrollment: Int? = null)
