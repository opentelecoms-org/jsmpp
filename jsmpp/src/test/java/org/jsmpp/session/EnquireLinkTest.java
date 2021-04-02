package org.jsmpp.session;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.GeneralDataCoding;
import org.jsmpp.bean.InterfaceVersion;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.TypeOfNumber;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnquireLinkTest {

  private static final Logger log = LoggerFactory.getLogger(EnquireLinkTest.class);

  private final int PORT = 6000;

  ExecutorService executorService = Executors.newFixedThreadPool(5);

  @Before
  public void setUp() throws Exception {
    System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "trace");
  }

  @Test
  public void test_smpp33_send_no_enquiry_link_before_bind() throws Exception {
    TestSmppServer smppServer = new TestSmppServer(PORT, 3);
    smppServer.setBindTimeout(60000);
    smppServer.setEnquireLinkTimer(0);
    executorService.execute(smppServer);

    SMPPSession session = new SMPPSession();
    session.setInterfaceVersion(InterfaceVersion.IF_33);
    session.setEnquireLinkTimer(100);
    session.connect("localhost", PORT);

    Thread.sleep(1000);

    smppServer.stop();
  }

  @Test
  public void test_smpp34_send_no_enquiry_link_before_bind() throws Exception {
    TestSmppServer smppServer = new TestSmppServer(PORT, 3);
    smppServer.setBindTimeout(60000);
    smppServer.setEnquireLinkTimer(0);
    executorService.execute(smppServer);

    SMPPSession session = new SMPPSession();
    session.setInterfaceVersion(InterfaceVersion.IF_34);
    session.setEnquireLinkTimer(100);
    session.connect("localhost", PORT);

    Thread.sleep(1000);

    smppServer.stop();
  }

  @Test
  public void test_smpp50_send_enquiry_link_before_bind() throws Exception {
    TestSmppServer smppServer = new TestSmppServer(PORT, 3);
    smppServer.setBindTimeout(60000);
    smppServer.setEnquireLinkTimer(0);
    executorService.execute(smppServer);

    SMPPSession session = new SMPPSession();
    session.setInterfaceVersion(InterfaceVersion.IF_50);
    session.setEnquireLinkTimer(100);
    session.connect("localhost", PORT);

    Thread.sleep(1000);

    smppServer.stop();
  }

  @Test
  public void test_smpp34_outbind() throws Exception {
    // ESME
    log.info("*** ESME listens for outbind ***");
    TestSmppOutboundServer smppServer = new TestSmppOutboundServer(PORT, 3);
    smppServer.setInterfaceVersion(InterfaceVersion.IF_50);
    smppServer.setOutbindTimeout(60000);
    smppServer.setBindTimeout(60000);
    smppServer.setEnquireLinkTimer(100);
    executorService.execute(smppServer);

    // Wait until the listener has started
    smppServer.waitStarted();

    log.info("*** MC sends outbind and receives bind request from ESME ***");
    // MC sends outbind
    SMPPOutboundSession session = new SMPPOutboundSession();
    session.setInterfaceVersion(InterfaceVersion.IF_34);
    session.setEnquireLinkTimer(0);
    OutbindParameter outbindParameter = new OutbindParameter("mc_let_me_in", "secret");
    // outbindParameter.getInterfaceVersion()
    BindRequest bindRequest = session.connectAndOutbind("localhost", PORT, outbindParameter);
    log.info("Received bind request from ESME {} {} {}", bindRequest.getSystemId(), bindRequest.getPassword(), bindRequest.getInterfaceVersion());
    log.info("Session interface version is {}", session.getInterfaceVersion());
    String systemId = "mc_its_me";
    InterfaceVersion interfaceVersion = session.getInterfaceVersion().min(bindRequest.getInterfaceVersion());
    log.info("Accept bind response to ESME {} {}", systemId, interfaceVersion);
    bindRequest.accept(systemId, interfaceVersion);

    session.deliverShortMessage("CMT",
        TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.ISDN, "1234",
        TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.ISDN, "5678",
        new ESMClass(0),
        (byte) 0,
        (byte) 0,
        new RegisteredDelivery(),
        new GeneralDataCoding(),
        "Hello World".getBytes(StandardCharsets.ISO_8859_1)
    );

    // Allow some time, so enquiry_link can be send
    Thread.sleep(200);

    smppServer.stop();

    Assert.assertEquals(1, smppServer.get("deliver_sm"));
    Assert.assertEquals(InterfaceVersion.IF_34, session.getInterfaceVersion());
  }

  @Test
  public void test_smpp50_outbind() throws Exception {
    // ESME
    log.info("*** ESME listens for outbind ***");
    // Start with 1 so deliver_sm is not processed immediately after bind_resp
    TestSmppOutboundServer smppServer = new TestSmppOutboundServer(PORT, 3);
    smppServer.setInterfaceVersion(InterfaceVersion.IF_50);
    smppServer.setOutbindTimeout(60000);
    smppServer.setBindTimeout(60000);
    smppServer.setEnquireLinkTimer(100);
    executorService.execute(smppServer);

    // Wait until the listener has started
    smppServer.waitStarted();

    log.info("*** MC sends outbind and receives bind request from ESME ***");
    // MC sends outbind
    SMPPOutboundSession session = new SMPPOutboundSession();
    session.setInterfaceVersion(InterfaceVersion.IF_50);
    session.setEnquireLinkTimer(0);
    OutbindParameter outbindParameter = new OutbindParameter("mc_let_me_in", "secret");
    // outbindParameter.getInterfaceVersion()
    BindRequest bindRequest = session.connectAndOutbind("localhost", PORT, outbindParameter);
    log.info("Received bind request from ESME {} {} {}", bindRequest.getSystemId(), bindRequest.getPassword(), bindRequest.getInterfaceVersion());
    log.info("Session interface version is {}", session.getInterfaceVersion());
    String systemId = "mc_its_me";
    InterfaceVersion interfaceVersion = session.getInterfaceVersion().min(bindRequest.getInterfaceVersion());
    log.info("Accept bind response to ESME {} {}", systemId, interfaceVersion);
    bindRequest.accept(systemId, interfaceVersion);

    session.deliverShortMessage("CMT",
        TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.ISDN, "1234",
        TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.ISDN, "5678",
        new ESMClass(0),
        (byte) 0,
        (byte) 0,
        new RegisteredDelivery(),
        new GeneralDataCoding(),
        "Hello World".getBytes(StandardCharsets.ISO_8859_1)
    );

    // Allow some time, so enquiry_link can be send
    Thread.sleep(200);

    smppServer.stop();

    Assert.assertEquals(1, smppServer.get("deliver_sm"));
    Assert.assertEquals(InterfaceVersion.IF_50, session.getInterfaceVersion());
  }

}
