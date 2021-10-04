package com.learnkotlin.kotlinspring.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class InterceptorConfig : WebMvcConfigurer {
    @Bean
    fun authenticationHandler(): AuthenticationHandler {
        return AuthenticationHandler()
    }

    @Bean
    fun fromTokenArgumentResolver(): FromTokenArgumentResolver {
        return FromTokenArgumentResolver()
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authenticationHandler()).addPathPatterns("/**")
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(fromTokenArgumentResolver())
    }
}
