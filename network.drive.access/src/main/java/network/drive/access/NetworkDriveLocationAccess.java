package network.drive.access;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.msfscc.fileinformation.FileAllInformation;
import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation;
import com.hierynomus.msfscc.fileinformation.FileStandardInformation;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import com.hierynomus.smbj.share.File;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;

public class NetworkDriveLocationAccess {

    private static String domain = "domain";
    private static String userName = "username";
    private static String password = "password";
    private static String hostname = "hostname";
    private static String remoteFilePath = "path";

    public static void main(String[] args) throws Exception {
        sharedFolderAccessUsingJCIFSLibrary();
        sharedFolderAccessUsingSMBJLibrary();
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

    private static void sharedFolderAccessUsingSMBJLibrary() throws Exception {
        List<File> files = new ArrayList<>();
        try (SMBClient client = new SMBClient(); Connection connection = client.connect(hostname)) {
            AuthenticationContext ac = new AuthenticationContext(userName, password.toCharArray(), domain);
            Session session = connection.authenticate(ac);

            try (DiskShare share = (DiskShare) session.connectShare("Users")) {
                getFile(remoteFilePath, share, files);
            }
        }
    }

    private static void getFile(String path, DiskShare share, List<File> files) throws IOException {
        for (FileIdBothDirectoryInformation f : share.list(path)) {
            String currentFilePath = path + "/" + f.getFileName();
            FileAllInformation fileAllInfo = share.getFileInformation(currentFilePath);
            FileStandardInformation fileStandardInformation = fileAllInfo.getStandardInformation();
            if (fileStandardInformation.isDirectory() && !f.getFileName()
                .equals(".")
                && !f.getFileName()
                    .equals("..")) {
                getFile(currentFilePath, share, files);
            } else if (!f.getFileName()
                .equals(".")
                && !f.getFileName()
                    .equals("..")) {
                Set<SMB2ShareAccess> s = new HashSet<>();
                s.add(SMB2ShareAccess.ALL.iterator().next());
                File file = share.openFile(currentFilePath, EnumSet.of(AccessMask.READ_CONTROL), null, s, null, null);
                try (InputStream in = file.getInputStream(); BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in))) {
                    String content = bufferedReader.lines()
                        .collect(Collectors.joining("\n"));
                    System.out.println(content);
                }
            }
        }
    }

}
