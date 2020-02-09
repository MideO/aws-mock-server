package com.github.mideo.mockserver

import com.github.mideo.apitestkit.StubBuilder
import org.slf4j.{Logger, LoggerFactory}

object MockServer extends App {
  val logger: Logger = LoggerFactory.getLogger(this.getClass)
  logger.debug("starting mock server.....")
  val options = new StubBuilder()
    .disableRequestJournal()
      .enableResponseTemplateTransformerForAllResponse()
    .getOptions
    .enableBrowserProxying(true)


  new StubBuilder().startWireMock(options)

  logger.info("server running at..... http://localhost:5000")

}
