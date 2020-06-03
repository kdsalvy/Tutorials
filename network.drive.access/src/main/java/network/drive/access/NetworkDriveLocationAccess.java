package network.drive.access;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import com.hierynomus.protocol.commons.buffer.Buffer.BufferException;
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

    private static String domain = "domain";
    private static String userName = "username";
    private static String password = "Password";
    private static String remoteFolderPath = "remotepath";
    private static String remoteFilePath = "remotepath";

    public static void main(String[] args) throws Exception {
        // sharedFolderAccessUsingJCIFSLibrary();
         sharedFolderAccessUsingSMBJLibrary();
        // deleteFile();
    }

    private static void sharedFolderAccessUsingJCIFSLibrary() throws IOException {
        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(domain, userName, password);
        String sharedSMBPath = "smb:".concat(remoteFolderPath);
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
        SmbPath smbPath = SmbPath.parse(remoteFolderPath);
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

    private static void getFile(String path, DiskShare share, List<File> files) throws IOException, BufferException {
        for (FileIdBothDirectoryInformation f : share.list(path)) {
            String currentFilePath = path + "/" + f.getFileName();
            FileAllInformation fileAllInfo = share.getFileInformation(currentFilePath);
            FileStandardInformation fileStandardInformation = fileAllInfo.getStandardInformation();
            if (fileStandardInformation.isDirectory() && !f.getFileName()
                .equals(".")
                && !f.getFileName()
                    .equals("..")
                && !f.getFileName()
                    .equals("otherFolder")) {
                getFile(currentFilePath, share, files);
            } else if (!f.getFileName()
                .equals(".")
                && !f.getFileName()
                    .equals("..")
                && f.getFileName()
                    .matches("file.txt")
                && !f.getFileName()
                    .equals("otherFolder")) {
                Set<SMB2ShareAccess> s = new HashSet<>();
                s.add(SMB2ShareAccess.ALL.iterator()
                    .next());
                String fileUNCPath = "";
                try (File file = share.openFile(currentFilePath, EnumSet.of(AccessMask.GENERIC_READ), null, s, SMB2CreateDisposition.FILE_OPEN, null)) {
                    System.out.println(f.getFileName());
                    System.out.println(Paths.get(file.getDiskShare()
                        .getSmbPath()
                        .toUncPath(), currentFilePath));
                    int size = (int) fileStandardInformation.getEndOfFile();
                    System.out.println(size);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    file.read(baos);
                    System.out.println(baos.toString());
                    throw new Exception("Dummy Exception");
                } catch(Exception ex) {
                    moveFileInRemote(Paths.get(share.getSmbPath().toUncPath(),currentFilePath).toString());
                }
            }
        }
    }

    private static void deleteFile() throws IOException {
        SmbPath smbPath = SmbPath.parse(remoteFilePath);
        try (SMBClient client = new SMBClient(); Connection connection = client.connect(smbPath.getHostname())) {
            AuthenticationContext ac = new AuthenticationContext(userName, password.toCharArray(), domain);
            Session session = connection.authenticate(ac);

            try (DiskShare share = (DiskShare) session.connectShare(smbPath.getShareName())) {
                share.rm(smbPath.toUncPath());
            }
        }
    }

    private static void moveFileInRemote(String currentFilePath) throws IOException, BufferException {
        System.out.println(currentFilePath);
        SmbPath smbPath = SmbPath.parse(currentFilePath);
        Path destPath = Paths.get(Paths.get(smbPath.getPath())
            .getParent()
            .toString(), "otherFolder/subFolder/subsubFolder",
            Paths.get(smbPath.getPath())
                .getFileName()
                .toString());
        try (SMBClient client = new SMBClient(); Connection connection = client.connect(smbPath.getHostname())) {
            AuthenticationContext ac = new AuthenticationContext(userName, password.toCharArray(), domain);
            Session session = connection.authenticate(ac);

            Set<SMB2ShareAccess> s = new HashSet<>();
            s.addAll(SMB2ShareAccess.ALL);

            try (DiskShare share = (DiskShare) session.connectShare(smbPath.getShareName()); File srcFile = share.openFile(smbPath.getPath(), EnumSet.of(AccessMask.GENERIC_READ), null, s, SMB2CreateDisposition.FILE_OPEN, null)) {
                recursiveFolderCreate(share, destPath.getParent());
                File destFile = share.openFile(destPath.toString(), EnumSet.of(AccessMask.GENERIC_WRITE), null, s, SMB2CreateDisposition.FILE_OPEN_IF, null);
                srcFile.remoteCopyTo(destFile);
                share.rm(smbPath.getPath());
                destFile.close();
            }
        }
    }

    private static void recursiveFolderCreate(DiskShare share, Path multiSubDirPath) {
        if (!share.folderExists(multiSubDirPath.toString())) {
            recursiveFolderCreate(share, multiSubDirPath.getParent());
            share.mkdir(multiSubDirPath.toString());
        }
    }

}
