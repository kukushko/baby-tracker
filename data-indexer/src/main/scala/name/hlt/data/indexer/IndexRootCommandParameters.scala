package name.hlt.data.indexer

import com.beust.jcommander.{Parameter, Parameters}

@Parameters(commandDescription = "Index root command parameters")
class IndexRootCommandParameters {

  @Parameter(names = Array("-r", "--root"), description = "Root folder", required = true)
  var root: String = _
}
