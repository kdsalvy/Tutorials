package network.drive.access;

import java.io.IOException;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;

public class NetworkDriveLocationAcess {

    private static String domain = "domain";
    private static String userName = "username";
    private static String password = "password";
    private static String remoteFilePath = "/Users/kdsalvy/Desktop/POCFolder/";

    public static void main(String[] args) throws Exception {
        sharedFolderAccessUsingJCIFSLibrary();
    }

    private static void sharedFolderAccessUsingJCIFSLibrary() throws IOException {
        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(domain, userName, password);
        SmbFile dir = new SmbFile("smb:".concat(remoteFilePath), auth);
        for (SmbFile f : dir.listFiles()) {
            try (SmbFileInputStream in = new SmbFileInputStream(f)) {
                byte[] bytes = new byte[(int) f.length()];
                in.read(bytes);
                System.out.println(new String(bytes));
            }
        }
    }

}
