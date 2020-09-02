package hlt.name.site

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.{ResourceHandlerRegistry, WebMvcConfigurerAdapter}

@Configuration
class MvcConfig extends WebMvcConfigurerAdapter {
  override def addResourceHandlers(registry: ResourceHandlerRegistry): Unit = {
    registry.addResourceHandler("/static/**")
      .addResourceLocations("classpath:/static/")
    registry.addResourceHandler("/webjars/**")
      .addResourceLocations("classpath:/META-INF/resources/webjars/")
    registry.addResourceHandler("/error/**")
      .addResourceLocations("classpath:/error/")
  }
}
