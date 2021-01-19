package devicesInformation;


import com.jcraft.jsch.UserInfo;

public class SwitchInformation  {
     private static String host="192.168.200.91";
     private static String user="root";
         private static String password="root";

     public String getHost() {
         return host;
     }

     public String getUser() {
         return user;
     }

     public String getPassword() {
         return password;
     }

}
