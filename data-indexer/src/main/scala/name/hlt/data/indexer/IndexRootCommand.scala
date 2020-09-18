package name.hlt.data.indexer

import java.io.File
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.{Files, Path, Paths}
import java.time.{LocalDateTime, ZoneId}

import name.hlt.data.indexer.dal.{DALScanRootFileInfo, DALScanRootFileInfoRepository, DALScanRootItem, DALScanRootItemRepository}
import org.slf4j.LoggerFactory
import org.apache.commons.codec.binary.Hex
import scala.collection.JavaConverters._

@CommandParameters(classOf[IndexRootCommandParameters])
class IndexRootCommand(fileInfoRepository: DALScanRootFileInfoRepository, rootRepository: DALScanRootItemRepository, parameters: IndexRootCommandParameters) extends Command {

  private val log = LoggerFactory.getLogger(getClass)

  private def fileHash(p: String): String = {
    import sun.misc.BASE64Encoder
    import java.io.BufferedInputStream
    import java.io.FileInputStream
    import java.security.MessageDigest

    val buffer = new Array[Byte](1024*1024*10)
    var count = 0
    val digest = MessageDigest.getInstance("SHA-256")
    val bis = new BufferedInputStream(new FileInputStream(p))
    while (
      { count = bis.read(buffer); count } > 0
    ) {
      digest.update(buffer, 0, count)
    }
    bis.close()

    val hash = digest.digest
    new String(Hex.encodeHex(hash))
  }

  private def fillIndex(root: DALScanRootItem): Unit = {
    val all = fileInfoRepository.findAllByRoot(root).asScala
    val itemByPath = all.map(t => (t.path, t)).toMap
    var counter = 0
    Files.walk(Paths.get(root.root)).filter(x => new File(x.toString).isFile).forEach {
      fp =>
        log.info(s"processing file $counter - $fp")
        if (!itemByPath.contains(fp.toString)) {
          val att = Files.readAttributes(fp, classOf[BasicFileAttributes])
          val item = new DALScanRootFileInfo
          item.createTime = LocalDateTime.ofInstant(att.creationTime().toInstant, ZoneId.systemDefault())
          item.path = fp.toString
          item.size = att.size()
          item.root = root
          item.hash = fileHash(fp.toString)
          fileInfoRepository.save(item)
        }

        counter += 1
    }
  }

  override def execute(): Unit = {
    val f = new File(parameters.root)
    if (!f.exists()) {
      throw new IllegalArgumentException(s"Path ${parameters.root} does not exist")
    }
    if (!f.isDirectory) {
      throw new IllegalArgumentException(s"Path ${parameters.root} is not a directory")
    }

    val rootOpt = rootRepository.findByRoot(parameters.root)
    val root = if (!rootOpt.isPresent) {
      log.info("Root entry not found in DB, creating")
      val r = new DALScanRootItem
      r.root = parameters.root
      rootRepository.save(r)
      r
    } else
      rootOpt.get()

    log.info(s"Root entry ID: ${root.id}")

    fillIndex(root)
    //println("*************")
    //println(fileInfoRepository.findById(6848).get().path)
  }
}
