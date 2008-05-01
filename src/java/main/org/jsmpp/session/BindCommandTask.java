package org.jsmpp.session;

import java.io.IOException;
import java.io.OutputStream;

import org.jsmpp.BindType;
import org.jsmpp.InterfaceVersion;
import org.jsmpp.NumberingPlanIndicator;
import org.jsmpp.PDUSender;
import org.jsmpp.PDUStringException;
import org.jsmpp.TypeOfNumber;

/**
 * @author uudashr
 *
 */
public class BindCommandTask extends AbstractSendCommandTask {
    private final BindType bindType;
    private final String systemId;
    private final String password;
    private final String systemType;
    private final InterfaceVersion interfaceVersion;
    private final TypeOfNumber addrTon;
    private final NumberingPlanIndicator addrNpi;
    private final String addressRange;
    
    public BindCommandTask(PDUSender pduSender,
            BindType bindType, String systemId, String password,
            String systemType, InterfaceVersion interfaceVersion,
            TypeOfNumber addrTon, NumberingPlanIndicator addrNpi,
            String addressRange) {
        super(pduSender);
        this.bindType = bindType;
        this.systemId = systemId;
        this.password = password;
        this.systemType = systemType;
        this.interfaceVersion = interfaceVersion;
        this.addrTon = addrTon;
        this.addrNpi = addrNpi;
        this.addressRange = addressRange;
    }
    
    public void executeTask(OutputStream out, int sequenceNumber)
            throws PDUStringException, IOException {
        pduSender.sendBind(out, bindType, sequenceNumber, systemId, password,
                systemType, interfaceVersion, addrTon, addrNpi, addressRange);
    }
    
    public String getCommandName() {
        return "bind";
    }
}
