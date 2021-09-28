package com.learnkotlin.kotlinspring.config

import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.multipart.support.MissingServletRequestPartException

class FromTokenArgumentResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(p0: MethodParameter): Boolean {
        return true
    }

    override fun resolveArgument(
        methodParameter: MethodParameter,
        modelAndViewContainer: ModelAndViewContainer?,
        nativeWebRequest: NativeWebRequest,
        webDataBinderFactory: WebDataBinderFactory?
    ): Any {
        return nativeWebRequest.getAttribute(methodParameter.parameterName!!, RequestAttributes.SCOPE_REQUEST)
            ?: throw MissingServletRequestPartException(methodParameter.parameterName!!)
    }
}
