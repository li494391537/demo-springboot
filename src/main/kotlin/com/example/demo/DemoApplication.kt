package com.example.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*


@SpringBootApplication
class DemoApplication
fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}
@Configuration
class SocialApplication {
	@Bean
	fun configure(http: HttpSecurity): SecurityFilterChain {
		http.csrf { c ->
			c.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		}.logout { l ->
			l.logoutSuccessUrl("/").permitAll()
		}.authorizeRequests{ a ->
			a.antMatchers("/", "/error", "/ping").permitAll().anyRequest().authenticated()
//		}.exceptionHandling { e ->
//			e.authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
//		}.oauth2Client { oauth2Client ->
//			oauth2Client.authorizationCodeGrant { authorizationCodeGrant ->
//				authorizationCodeGrant.authorizationRequestRepository(this.authorizationRequestRepository())
//			}
		}.oauth2Login()
		return http.build()
	}
}
@RestController
class TestController {
	@RequestMapping("/ping")
	fun pingController() : String {
		return "pong"
	}
	@GetMapping("/user")
	fun user(@AuthenticationPrincipal principal: OAuth2User): Map<String, String?> {
		return Collections.singletonMap<String, String?>("name", principal.getAttribute("name")).plus(
			Collections.singletonMap<String, String?>("email", principal.getAttribute("email"))
		)
	}
}