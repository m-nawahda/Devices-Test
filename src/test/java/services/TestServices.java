package services;

import com.jcraft.jsch.JSchException;
import devicesInformation.SwitchInformation;
import org.snmp4j.smi.OID;

import java.io.IOException;
import java.net.InetAddress;

public class TestServices {
    private static SSHServices sshConnection;
    private static SnmpServices snmpConnection;

    public TestServices() {
        sshConnection = new SSHServices();
        snmpConnection = new SnmpServices(SwitchInformation.host + "/161");

    }
    public boolean isPingable(String ipAddress) {
        InetAddress inet;
        try {
            inet = InetAddress.getByName(ipAddress);
            System.out.println("Sending Ping Request to " + ipAddress);
            return inet.isReachable(5000);
        } catch (Exception e) {
            System.out.println("Exception:" + e.getMessage());
            return false;
        }
    }

    public String readResponseViaSnmp() throws IOException {
        snmpConnection.start();
        String sysName = snmpConnection.readResponse(new OID("1.3.6.1.2.1.1.5.0"));
        return sysName;
    }

    public String readResponseViaServer() throws IOException, JSchException {
        return sshConnection.readResponse("snmpwalk -v2c -cpublic1 192.168.200.233 1.3.6.1.2.1.1.5.0");
    }
    //    @Test
//    public void readResponse() throws IOException, JSchException {
//        connection.readResponse("show running-config");
//    }
}
