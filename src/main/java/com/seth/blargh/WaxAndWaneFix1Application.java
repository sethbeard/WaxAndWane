package com.seth.blargh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.seth.blargh")
public class WaxAndWaneFix1Application extends SpringBootServletInitializer {



		@Override
		protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
			return application.sources(WaxAndWaneFix1Application.class);
		}
		public static void main(String[] args) {
			SpringApplication.run(WaxAndWaneFix1Application.class, args);
		}

}

