package com.example.springbootapiv2.common

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.boot.jackson.JsonComponent
import org.springframework.validation.Errors

@JsonComponent // objectMapper에 등록한다. @JsonComponent를 사용
class ErrorsSerializer : JsonSerializer<Errors>() {
    override fun serialize(value: Errors?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        //-----------------------------------------------------------
        // errors 안의 여러개의 에러는 배열로 담아주기 위해서 아래의 함 사용
        // gen?.writeStartArray()
        // gen?.writeEndArray()
        //-----------------------------------------------------------
        gen?.writeStartArray()

        // 필드에러 관련
        value?.fieldErrors?.stream()?.forEach {
            gen?.writeStartObject()
            gen?.writeStringField("field", it.field)
            gen?.writeStringField("objectName", it.objectName)
            gen?.writeStringField("code", it.code)
            gen?.writeStringField("defaultMessage", it.defaultMessage)

            val rejectedValue = it.rejectedValue
            if (rejectedValue != null) {
                gen?.writeStringField("rejectedValue", rejectedValue.toString())
            }


            gen?.writeEndObject()
        }

        // 글로벌에러 관련
        value?.globalErrors?.forEach {
            gen?.writeStartObject()
            gen?.writeStringField("objectName", it.objectName)
            gen?.writeStringField("code", it.code)
            gen?.writeStringField("defaultMessage", it.defaultMessage)
            gen?.writeEndObject()
        }

        gen?.writeEndArray()
    }
}
