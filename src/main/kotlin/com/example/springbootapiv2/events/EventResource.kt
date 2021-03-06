package com.example.springbootapiv2.events

import com.example.springbootapiv2.index.IndexController
import com.fasterxml.jackson.annotation.JsonUnwrapped
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.Link
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder

// href 내역 추가를 위해서 RepresentationModel 클래스 상속 클래스 생성
//data class EventResource(@JsonUnwrapped var event: Event) : RepresentationModel<EventResource>()

// RepresentationModel 상속에서 EntityModel 상속으로 변경
class EventResource(@JsonUnwrapped var event: Event, vararg links: Link?) : EntityModel<Event>() {
    init {
        add(WebMvcLinkBuilder.linkTo(EventController::class.java)
                .slash("api")
                .slash("events")
                .slash(event.id)
                .withSelfRel())
    }
}

//-------------------------------------------------------------
// @JsonUnwrapped 을 붙이지 않으면 아래와 같이 칼럼이 event안에 감싸진다.
//-------------------------------------------------------------
//{
//    "event" : -{
//    "id" : 14,
//    "name" : spring,
//    "description" : REST API Development with Spring,
//    "beginEnrollmentDateTime" : 2018-11-23T14:21:00,
//    "closeEnrollmentDateTime" : 2018-11-24T14:21:00,
//    "beginEventDateTime" : 2018-11-28T14:21:00,
//    "endEventDateTime" : 2018-11-29T14:21:00,
//    "location" : ê°ë¨ì­ D2 ì¤íí í©í ë¦¬,
//    "basePrice" : 100,
//    "maxPrice" : 200,
//    "limitOfEnrollment" : 100,
//    "offline" : true,
//    "free" : false,
//    "eventStatus" : DRAFT
//},
//    "_links" : -{
//    "query-events" : -{
//        "href" : http://localhost/api/events/14
//    },
//    "self" : -{
//        "href" : http://localhost/api/events/14
//    },
//    "update-event" : -{
//        "href" : http://localhost/api/events/14
//    }
//}
//}
//-------------------------------------------------------------
// @JsonUnwrapped 을 붙일 경우
//-------------------------------------------------------------
//{
//    "id" : 18,
//    "name" : spring,
//    "description" : REST API Development with Spring,
//    "beginEnrollmentDateTime" : 2018-11-23T14:21:00,
//    "closeEnrollmentDateTime" : 2018-11-24T14:21:00,
//    "beginEventDateTime" : 2018-11-28T14:21:00,
//    "endEventDateTime" : 2018-11-29T14:21:00,
//    "location" : ê°ë¨ì­ D2 ì¤íí í©í ë¦¬,
//    "basePrice" : 100,
//    "maxPrice" : 200,
//    "limitOfEnrollment" : 100,
//    "offline" : true,
//    "free" : false,
//    "eventStatus" : DRAFT,
//    "_links" : -{
//    "query-events" : -{
//        "href" : http://localhost/api/events/18
//    },
//    "self" : -{
//        "href" : http://localhost/api/events/18
//    },
//    "update-event" : -{
//        "href" : http://localhost/api/events/18
//    }
//}
//}