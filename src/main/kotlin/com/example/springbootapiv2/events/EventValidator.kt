package com.example.springbootapiv2.events

import org.springframework.stereotype.Component
import org.springframework.validation.Errors

@Component
class EventValidator {

    fun validate(eventDto: EventDto, errors: Errors) {
        if (eventDto.basePrice > eventDto.maxPrice && eventDto.maxPrice != 0) {
            errors.rejectValue("basePrice", "wrongValue", "baseprice is worng")
            errors.rejectValue("maxPrice", "wrongValue", "maxprice is worng")
        }

        val endEventDateTime = eventDto.endEventDateTime
        if (endEventDateTime.isBefore(eventDto.beginEventDateTime) ||
                endEventDateTime.isBefore(eventDto.beginEnrollmentDateTime) ||
                endEventDateTime.isBefore(eventDto.closeEnrollmentDateTime)) {
            errors.rejectValue("endEventDateTime", "wrongValue", "endEventDateTime is worng")
        }
    }
}