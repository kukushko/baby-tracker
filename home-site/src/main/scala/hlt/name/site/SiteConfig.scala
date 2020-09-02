package hlt.name.site

import javax.servlet.ServletContext
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.{Bean, ComponentScan, Configuration}
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.scheduling.annotation.EnableScheduling
import org.thymeleaf.spring5.SpringTemplateEngine
import org.thymeleaf.spring5.view.ThymeleafViewResolver
import org.thymeleaf.templateresolver.ServletContextTemplateResolver

@Configuration
@EnableScheduling
@EnableAutoConfiguration
@EnableConfigurationProperties
@ComponentScan(basePackages = Array("hlt.name"))
class SiteConfig {

//  @Bean
//  def servletContextTemplateResolver(servletContext: ServletContext): ServletContextTemplateResolver = {
//    val templateResolver = new ServletContextTemplateResolver(servletContext)
//    templateResolver.setPrefix("/templates/")
//    templateResolver.setSuffix(".html")
//    templateResolver.setTemplateMode("HTML5")
//    templateResolver
//  }
//
//  @Bean
//  def templateEngine(templateResolver: ServletContextTemplateResolver): SpringTemplateEngine = {
//    val templateEngine = new SpringTemplateEngine()
//    templateEngine.setTemplateResolver(templateResolver)
//    templateEngine.setTemplateEngineMessageSource(messageSource())
//    templateEngine
//  }
//
//  @Bean
//  def messageSource(): ResourceBundleMessageSource = {
//    val messageSource = new ResourceBundleMessageSource()
//    messageSource.setBasename("messages")
//    messageSource
//  }
//
//  @Bean
//  def viewResolver(templateEngine: SpringTemplateEngine): ThymeleafViewResolver = {
//    val viewResolver = new ThymeleafViewResolver()
//    viewResolver.setTemplateEngine(templateEngine)
//    viewResolver.setOrder(1)
//    viewResolver
//  }
}
