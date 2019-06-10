package network.drive.access;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.msfscc.fileinformation.FileAllInformation;
import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation;
import com.hierynomus.msfscc.fileinformation.FileStandardInformation;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.common.SmbPath;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import com.hierynomus.smbj.share.File;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;

public class NetworkDriveLocationAccess {

    private static String domain = "LL";
    private static String userName = "saukedia1";
    private static String password = "Pass@may2019";
    private static String remoteFilePath = "\\\\WKWIN2407161\\Users\\saukedia1\\Desktop\\POCFolder";

    public static void main(String[] args) throws Exception {
        sharedFolderAccessUsingJCIFSLibrary();
        sharedFolderAccessUsingSMBJLibrary();
    }

    private static void sharedFolderAccessUsingJCIFSLibrary() throws IOException {
        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(domain, userName, password);
        String sharedSMBPath = "smb:".concat(remoteFilePath);
        System.out.println(sharedSMBPath);
        SmbFile dir = new SmbFile(sharedSMBPath, auth);
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
        SmbPath smbPath = SmbPath.parse(remoteFilePath);
        System.out.println(smbPath.getHostname());
        System.out.println(smbPath.getShareName());
        System.out.println(smbPath.getPath());
        try (SMBClient client = new SMBClient(); Connection connection = client.connect(smbPath.getHostname())) {
            AuthenticationContext ac = new AuthenticationContext(userName, password.toCharArray(), domain);
            Session session = connection.authenticate(ac);

            try (DiskShare share = (DiskShare) session.connectShare(smbPath.getShareName())) {
                getFile(smbPath.getPath(), share, files);
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
                s.add(SMB2ShareAccess.ALL.iterator()
                    .next());
                try (File file = share.openFile(currentFilePath, EnumSet.of(AccessMask.GENERIC_READ), null, s, SMB2CreateDisposition.FILE_OPEN, null)) {
                    System.out.println(f.getFileName());
                    int size = (int) fileStandardInformation.getEndOfFile();
                    System.out.println(size);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    file.read(baos);
                    System.out.println(baos.toString());
                }
            }
        }
    }

}
