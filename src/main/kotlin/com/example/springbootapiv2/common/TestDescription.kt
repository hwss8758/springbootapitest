package com.example.springbootapiv2.common

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class TestDescription (val value: String)