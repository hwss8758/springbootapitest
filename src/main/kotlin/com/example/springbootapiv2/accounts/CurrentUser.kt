package com.example.springbootapiv2.accounts

import org.springframework.security.core.annotation.AuthenticationPrincipal

/**
 * 인증되지 않은 사용자의 요청일 경우에는 account가 아닌 'anonymousUser'라는 문자열이 들어오므로
 * expression에서 아래와 같이 처리해준다.
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : account")
annotation class CurrentUser