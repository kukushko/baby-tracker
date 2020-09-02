package hlt.name.site

import org.springframework.boot.SpringApplication

object Program {

  def main(args: Array[String]): Unit = {
    SpringApplication.run(classOf[SiteConfig], args: _*)
  }
}
