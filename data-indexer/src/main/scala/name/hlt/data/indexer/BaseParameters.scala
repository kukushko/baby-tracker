package name.hlt.data.indexer

import com.beust.jcommander.{Parameter, Parameters}

@Parameters(commandDescription = "Base indexer parameters")
class BaseParameters {

  @Parameter(names = Array("-h", "--help"), help = true)
  var help: Boolean = _

}
