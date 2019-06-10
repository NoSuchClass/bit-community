package com.bit.community.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.bit.community.interceptor.LoginRequiredInterceptor;
import com.bit.community.interceptor.PassportInterceptor;

@Component
public class CommunityWebConfiguration implements WebMvcConfigurer {
	@Autowired
	PassportInterceptor passportInterceptor;
	
	@Autowired
	LoginRequiredInterceptor loginRequiredInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// TODO Auto-generated method stub
		registry.addInterceptor(passportInterceptor);
		registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/user/*");
		registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/msg/*");
		WebMvcConfigurer.super.addInterceptors(registry);
	}
}
