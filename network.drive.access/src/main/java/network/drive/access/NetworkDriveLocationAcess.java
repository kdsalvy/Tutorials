package network.drive.access;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.integration.smb.session.SmbSession;
import org.springframework.integration.smb.session.SmbSessionFactory;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;

public class NetworkDriveLocationAcess {

    private static String domain = "LL";
    private static String userName = "username";
    private static String password = "password";
    private static String host = "192.168.137.1";// "WKWIN2407161";
    private static String remoteFilePath = "/Users/saukedia1/Desktop/POCFolder/";

    public static void main(String[] args) throws Exception {
        // sharedFolderAccessUsingJCIFSLibrary();
        sharedFolderAccessUsingSpringSMBLibrary();
    }

    /*private static void sharedFolderAccessUsingJCIFSLibrary() throws IOException {
        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(domain, userName, password);
        SmbFile dir = new SmbFile(smbProtocol.concat(remoteFilePath), auth);
        for (SmbFile f : dir.listFiles()) {
            try (SmbFileInputStream in = new SmbFileInputStream(f)) {
                byte[] bytes = new byte[(int) f.length()];
                in.read(bytes);
                System.out.println(new String(bytes));
            }
        }
    }*/

    private static void sharedFolderAccessUsingSpringSMBLibrary() throws IOException {
        SmbSessionFactory smbSessionFactory = new SmbSessionFactory();
        smbSessionFactory.setDomain(domain);
        smbSessionFactory.setUsername(userName);
        smbSessionFactory.setPassword(password);
        smbSessionFactory.setShareAndDir(remoteFilePath);
        smbSessionFactory.setHost(host);
        SmbSession smbSession = smbSessionFactory.getSession();
        System.out.println(smbSession.isOpen());
        SmbFile[] files = smbSession.list("/");
        Arrays.stream(files)
            .filter(file -> fileTypeFilterPredicate(file))
            .forEach(file -> readFile(file));
    }

    private static void readFile(SmbFile f) {
        try (SmbFileInputStream in = new SmbFileInputStream(f)) {
            System.out.println("NetworkDriveLocationAcess.readFile() :: " + f.getName());
            byte[] bytes = new byte[(int) f.length()];
            in.read(bytes);
            System.out.println("File Content ::");
            System.out.println(new String(bytes) + "\n");
        } catch (SmbException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean fileTypeFilterPredicate(SmbFile f) {
        try {
            return f.isFile();
        } catch (SmbException e) {
            e.printStackTrace();
        }
        return false;
    }

}
