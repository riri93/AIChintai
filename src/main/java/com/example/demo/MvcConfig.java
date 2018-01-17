//package com.example.demo;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//
//@Configuration
//
//public class MvcConfig extends WebMvcConfigurerAdapter {
//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		 registry.addResourceHandler("/app/**").addResourceLocations("classpath:/app/**");
////		registry.addResourceHandler("/**").addResourceLocations("classpath:/**");
//	}
//
//	public void configure(WebSecurity web) throws Exception {
//		web.ignoring().antMatchers("app/**", "/app/**", "/vendor/**", "/resources/**", "/fonts/**", "/font-awesome/**",
//				"/");
//	}
//
//}
