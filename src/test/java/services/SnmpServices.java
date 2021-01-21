package services;

import java.io.IOException;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;


public class SnmpServices {

    Snmp snmp = null;
    String address;

    public SnmpServices(String add) {
        address = add;
    }

    public void start() throws IOException {
        TransportMapping transport = new DefaultUdpTransportMapping();
        snmp = new Snmp(transport);
        transport.listen();
    }

    public String readResponse(OID oid) throws IOException {
        OID[] oids = new OID[]{oid};
        PDU pdu = new PDU();
        for (OID oidObj : oids) {
            pdu.add(new VariableBinding(oidObj));
        }
        pdu.setType(PDU.GET);
        Target target = getTarget();
        ResponseEvent event = snmp.send(pdu, target, null);

        return event.getResponse().get(0).getVariable().toString();
    }

    private Target getTarget() {
        System.out.println("try to set community target");
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString("public1"));
        target.setAddress(GenericAddress.parse(address));
        target.setRetries(4);
        target.setTimeout(3500);
        target.setVersion(SnmpConstants.version2c);
        System.out.println("Success setting community target");
        return target;
    }

}