package com.example.springbootapiv2.events

import com.example.springbootapiv2.common.TestDescription
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.MethodSource

class EventTest {

    @Test
    fun builder() {
        val event = Event()
        assertThat(event).isNotNull()
    }

    @Test
    fun javaBean() {
        val name: String = "Event"
        val description: String = "Spring"

        val event = Event()
        event.name = name
        event.description = description

        println(event)
        assertThat(event.name).isEqualTo(name)
        assertThat(event.description).isEqualTo(description)


    }

    //----------------------------------------------------
    // junit5, 매개변수를 이용한 테스트
    // https://www.baeldung.com/junit-5-kotlin
    //----------------------------------------------------
    companion object {
        @JvmStatic
        fun generateParams() = listOf(
                Arguments.of(0, 0, true),
                Arguments.of(100, 0, false),
                Arguments.of(0, 100, false)
        )

        @JvmStatic
        fun testOffline() = listOf(
                Arguments.of("강남역", true),
                Arguments.of("청담동", true),
                Arguments.of("", false)
        )
    }

    @ParameterizedTest
    @MethodSource("generateParams")
    //@CsvSource("0,0,true", "100, 0, false", "0, 100, false")
    fun testFree_MethodSource(_basePrice: Int, _maxPrice: Int, _isFree: Boolean) {

        // given
        var event: Event = Event(basePrice = _basePrice,
                maxPrice = _maxPrice)

        // when
        event.update()

        //then
        assertThat(event.free).isEqualTo(_isFree)
    }

    @ParameterizedTest
    //@MethodSource("generateParams")
    @CsvSource("0,0,true", "100, 0, false", "0, 100, false")
    fun testFree_CsvSource(_basePrice: Int, _maxPrice: Int, _isFree: Boolean) {

        // given
        var event: Event = Event(basePrice = _basePrice,
                maxPrice = _maxPrice)

        // when
        event.update()

        //then
        assertThat(event.free).isEqualTo(_isFree)
    }

    @ParameterizedTest
    @MethodSource
    // @MethodSource에 함수명을 쓰지 않을 경우 함수와 같은명의 함수를 찾는다.
    fun testOffline(_location: String, _isOffline: Boolean) {
        // given
        var event: Event = Event(location = _location)

        // when
        event.update()

        //then
        assertThat(event.offline).isEqualTo(_isOffline)
    }
}