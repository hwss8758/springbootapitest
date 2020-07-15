package com.example.springbootapiv2.accounts

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider

class AccountSerializer : JsonSerializer<Accounts?>() {
    override fun serialize(value: Accounts?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        gen?.writeStartObject()
        value?.id?.let { gen?.writeNumberField("id", it) }
        gen?.writeEndObject()
    }
}