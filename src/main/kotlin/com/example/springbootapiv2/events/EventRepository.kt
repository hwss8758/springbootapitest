package com.example.springbootapiv2.events

import org.springframework.data.jpa.repository.JpaRepository

interface EventRepository: JpaRepository<Event, Int> {
}