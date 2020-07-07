package com.example.springbootapiv2.events

import java.time.LocalDateTime

// 입력을 받을수 있는 값만 명시
data class EventDto(var name: String,
                    var description: String,
                    var beginEnrollmentDateTime: LocalDateTime,
                    var closeEnrollmentDateTime: LocalDateTime,
                    var beginEventDateTime: LocalDateTime,
                    var endEventDateTime: LocalDateTime,
                    var location: String, // (optional) 이게 없으면 온라인 모임
                    var basePrice: Int?, // (optional)
                    var maxPrice: Int?, // (optional)
                    var limitOfEnrollment: Int?)
