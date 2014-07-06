/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package codeanalyzer;
/*
import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.authentication.SshAuthenticationClient;
import com.sshtools.j2ssh.authentication.PasswordAuthenticationClient;
import com.sshtools.j2ssh.session.SessionChannelClient;
import com.sshtools.j2ssh.authentication.AuthenticationProtocolState;
*/
import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.authentication.SshAuthenticationClient;
import com.sshtools.j2ssh.authentication.PasswordAuthenticationClient;
import com.sshtools.j2ssh.session.SessionChannelClient;
import com.sshtools.j2ssh.authentication.AuthenticationProtocolState;

import java.io.InputStream;
import java.io.OutputStream;




/**
 *
 * @author susdeshp
 */
public class scpconnect {
    
    private com.sshtools.j2ssh.SshClient SourceServerSSH;
    private SessionChannelClient SourceSession;
    private OutputStream SourceOut;
    private InputStream SourceIn;
    private byte buffer[];
    private boolean isconnected;
    StringBuffer OutputBuffer;
    
    public scpconnect(String HostName, String UserName, String Password){
        System.out.println(HostName+" "+UserName+" "+Password);
        SourceServerSSH = new SshClient();
        isconnected = false;
        
        try {
            SourceServerSSH.connect(HostName);
        
            PasswordAuthenticationClient auth = new PasswordAuthenticationClient();
            auth.setUsername(UserName); 
            auth.setPassword(Password);
        
            int result = SourceServerSSH.authenticate(auth);
        
        
            if(result==AuthenticationProtocolState.FAILED)  
                 System.out.println("The authentication failed");  
  
            if(result==AuthenticationProtocolState.PARTIAL)  
                 System.out.println("The authentication succeeded but another"+ "authentication is required");  
  
            if(result==AuthenticationProtocolState.COMPLETE) { 
                 System.out.println("The authentication is complete");
                 SourceSession = SourceServerSSH.openSessionChannel();
                 SourceOut = SourceSession.getOutputStream();
                 SourceIn = SourceSession.getInputStream();
                 buffer = new byte[255];
                 OutputBuffer = new StringBuffer("");
                 if(SourceSession.requestPseudoTerminal("ansi", 80, 24, 0, 0, "")) {
                      if (SourceSession.startShell()) { 
                          System.out.println("kolavari");
                          isconnected = true;
                      
                      }
                 }
            }
        }catch(Exception e){
            System.out.println("IOEXception");
        }
    }
    
    public String Status() {
        if (isconnected == true) return "Connection Successful";
        else return "Not able to Connect the server";
    }
    
    public String ExecuteCommand(String Command) {
        
        OutputBuffer = new StringBuffer("Not Connected");
        
        if (isconnected){
            OutputBuffer = new StringBuffer("");
             try {
                 SourceOut.write(Command.getBytes());
                 SourceOut.write(("\n").getBytes());
                 SourceOut.flush();
                 Thread.sleep(1000);
                 // InputStream SourceIn = SourceSession.getInputStream();
                 
                 buffer = new byte[255];
                 int readstatus; 
                 while((readstatus = SourceIn.read(buffer)) > 0 ) {
                     System.out.println(readstatus);
                     String CommandOutput = new String(buffer, 0, readstatus);  
                     OutputBuffer.append(CommandOutput);  
                     if (SourceIn.available()==0) break;
                 }  
        
                 System.out.println("kolavari");
            }catch(Exception e){
                System.out.println(" IOException while creating SessionChannelClient in Execute Command");
                return "HEMAN";
            }
        
            System.out.println("bhumberi bhum");

        }
        return OutputBuffer.toString();
    }
}
