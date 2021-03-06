/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rossi.testfileoss.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

/**
 *
 * @author rendi
 */

@Configuration
@EnableSwagger2
@Profile(value = {"default","dev","sit","sandbox","qa", "uat"})
public class SwaggerConfig {
    
    @Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(RequestHandlerSelectors.basePackage("com.rossi.testfileoss.controller"))
          .paths(PathSelectors.any())                          
          .build()
          .apiInfo(apiInfo());                                           
    }
    
    private ApiInfo apiInfo() {
     return new ApiInfo(
       "API Finance Core Web API",
       "API Finance Core Web API",
       "1.0", 
       "Terms of service", 
       new Contact("FC_Team", "https://pay.doku.com/qrreseller/", "https://pay.doku.com/qrreseller/"),
       "License of API", "API license URL", new ArrayList<VendorExtension>());
}
    
}
