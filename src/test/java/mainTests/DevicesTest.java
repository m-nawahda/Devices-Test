package mainTests;

import com.jcraft.jsch.*;
import org.testng.annotations.Test;
import services.connectionHelper;

import java.io.IOException;
import java.io.InputStream;

public class DevicesTest {
    connectionHelper connection;

    DevicesTest() {
        connection = new connectionHelper();
    }

//    @Test
//    public void readResponse() throws IOException, JSchException {
//        connection.readResponse("ifconfig");
//    }
    @Test
    public void readResponseViaSnmp() throws IOException, JSchException {
        connection.readResponse("snmpwalk -v2c -cpublic1 192.168.200.233 1.3.6.1.2.1.1.5");
    }
}