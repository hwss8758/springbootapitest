package com.example.springbootapiv2.events

import org.springframework.stereotype.Component
import org.springframework.validation.Errors

@Component
class EventValidator {

    fun validate(eventDto: EventDto, errors: Errors) {
        if (eventDto.basePrice!! > eventDto.maxPrice!! && eventDto.maxPrice!! != 0) {

            //----------------------------------------------------------------------------------------
            // 에러에는 2가지 설정 방법이 있음
            // 1. field error : errors.rejectValue("basePrice", "wrongValue", "baseprice is worng")
            // 2. global error : errors.reject("wrongPrices", "Values fo prices are wrong")
            //----------------------------------------------------------------------------------------
            errors.rejectValue("basePrice", "wrongValue", "baseprice is worng")
            errors.rejectValue("maxPrice", "wrongValue", "maxprice is worng")
            //errors.reject("wrongPrices", "Values fo prices are wrong")
        }

        val endEventDateTime = eventDto.endEventDateTime
        if (endEventDateTime.isBefore(eventDto.beginEventDateTime) ||
                endEventDateTime.isBefore(eventDto.beginEnrollmentDateTime) ||
                endEventDateTime.isBefore(eventDto.closeEnrollmentDateTime)) {
            errors.rejectValue("endEventDateTime", "wrongValue", "endEventDateTime is worng")
        }
    }
}