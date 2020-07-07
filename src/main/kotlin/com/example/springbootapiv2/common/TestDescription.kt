package com.example.springbootapiv2.common

import java.lang.annotation.ElementType
import java.lang.annotation.RetentionPolicy

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class TestDescription (val value: String)