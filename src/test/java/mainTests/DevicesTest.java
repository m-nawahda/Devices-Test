package mainTests;

import com.jcraft.jsch.JSchException;
import devicesInformation.ServerInformation;
import devicesInformation.SwitchInformation;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import services.SnmpServices;
import services.SSHServices;
import org.snmp4j.smi.OID;
import org.testng.annotations.Test;
import services.TestServices;

import java.io.IOException;

public class DevicesTest {
    TestServices services;

    DevicesTest() {
        services = new TestServices();
    }

    @BeforeClass
    public void checkAccessibility() {
        String ipSwitchAddress = SwitchInformation.host;
        System.out.println("Try connect to the Switch... ");
        Assert.assertTrue(services.isPingable(ipSwitchAddress), "Switch is NOT reachable.");
        System.out.println("Switch is reachable.");
        String ipServerAddress = ServerInformation.host;
        System.out.println("Try connect to the Server... ");
        Assert.assertTrue(services.isPingable(ipServerAddress), "Server is NOT reachable.");
        System.out.println("Server is reachable.");
    }
    @Test
    public void testMatching() throws IOException, JSchException {
        String snmpResponse = services.readResponseViaSnmp();
        String serverResponse = services.readResponseViaServer();
        System.out.println("Server Response is : "+ serverResponse);
        System.out.println("SNMP Response is : "+ snmpResponse);
        Assert.assertEquals(snmpResponse,serverResponse,"the response from SNMP doesn't match with the response from the server..");
        System.out.println("the response from SNMP is match with the response from the server..");
    }

}
