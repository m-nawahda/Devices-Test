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

public class SnmpTest {

    Snmp snmp = null;
    String address;

    public SnmpTest(String add) {
        address = add;
    }

    public static void main(String[] args) throws IOException {
        SnmpTest client = new SnmpTest("udp:192.168.200.233/161");
        client.start();

        String sysDescr = client.getAsString(new OID(".1.3.6.1.2.1.1.5.0"));
        System.out.println(sysDescr);
    }


    private void start() throws IOException {
        TransportMapping transport = new DefaultUdpTransportMapping();
        snmp = new Snmp(transport);
        transport.listen();
    }

    public String getAsString(OID oid) throws IOException {
        ResponseEvent event = get(new OID[]{oid});
        if (event.getResponse() == null)
            System.out.println("here");
        return event.getResponse().get(0).getVariable().toString();
    }

    public ResponseEvent get(OID oids[]) throws IOException {
        PDU pdu = new PDU();
        for (OID oid : oids) {
            pdu.add(new VariableBinding(oid));
        }
        pdu.setType(PDU.GET);
        Target target = getTarget();
        System.out.println(target.toString());
        ResponseEvent event = snmp.send(pdu, target, null);
        //ResponseEvent event = snmp.get(pdu,target);
        if (event != null) {
            return event;

        }
        throw new RuntimeException("GET timed out");
    }


    private Target getTarget() {
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString("public1"));
        target.setAddress(GenericAddress.parse(address));
        target.setRetries(2);
        target.setTimeout(2500);
        target.setVersion(SnmpConstants.version2c);
        return target;
    }

}