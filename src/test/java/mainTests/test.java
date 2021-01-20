package mainTests;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.testng.annotations.Test;

public class test {
    public static void main(String[] args) throws Exception
    {
        TransportMapping transport = new DefaultUdpTransportMapping();
        transport.listen();
        CommunityTarget comtarget = new CommunityTarget();
        comtarget.setCommunity(new OctetString("public1"));
        comtarget.setVersion(SnmpConstants.version2c);
        comtarget.setAddress(new UdpAddress("192.168.200.233" + "/" + "161"));
        comtarget.setRetries(2);
        comtarget.setTimeout(1000);
        PDU pdu = new PDU();
        pdu.add(new VariableBinding(new OID("1.3.6.1.2.1.1.5.0")));
        pdu.setType(PDU.GET);
        pdu.setRequestID(new Integer32(1));
        Snmp snmp = new Snmp(transport);

        System.out.println("Sending Request to Agent...");
        PDU requestPDU=snmp.get(pdu, comtarget).getRequest();

        System.out.println("request : "+requestPDU);
        ResponseEvent response = snmp.get(pdu, comtarget);

        if (response != null)
        {
            System.out.println("Got Response from Agent");
            PDU responsePDU = response.getResponse();

            if (responsePDU != null)
            {
                int errorStatus = responsePDU.getErrorStatus();
                int errorIndex = responsePDU.getErrorIndex();
                String errorStatusText = responsePDU.getErrorStatusText();

                if (errorStatus == PDU.noError)
                {
                    System.out.println("Snmp Get Response = " + responsePDU.getVariableBindings());
                }
                else
                {
                    System.out.println("Error: Request Failed");
                    System.out.println("Error Status = " + errorStatus);
                    System.out.println("Error Index = " + errorIndex);
                    System.out.println("Error Status Text = " + errorStatusText);
                }
            }
            else
            {
                System.out.println("Error: Response PDU is null");
            }
        }
        else
        {
            System.out.println("Error: Agent Timeout... ");
        }
        snmp.close();
    }
}
