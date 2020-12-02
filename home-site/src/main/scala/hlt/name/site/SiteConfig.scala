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

}
