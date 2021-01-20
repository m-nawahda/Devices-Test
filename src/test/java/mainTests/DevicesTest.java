package mainTests;

import services.connectionHelper;
import org.snmp4j.smi.OID;
import org.testng.annotations.Test;
import java.io.IOException;

public class DevicesTest {
    connectionHelper connection;

    DevicesTest() {
        connection = new connectionHelper();
    }

    //    @Test
//    public void readResponse() throws IOException, JSchException {
//        connection.readResponse("ifconfig");
//    }
//    @Test
//    public void readResponseViaSnmp() throws IOException, JSchException {
//        // connection.readResponse("snmpwalk -v2c -cpublic1 192.168.200.233 1.3.6.1.2.1.1.5");
//    }

    @Test
    public void testSnmp() throws IOException {
        SnmpTest client = new SnmpTest("udp:192.168.200.233/161");
        client.start();

        String sysDescr = client.getAsString(new OID("1.3.6.1.2.1.1.5"));
        System.out.println(sysDescr);
    }
}
