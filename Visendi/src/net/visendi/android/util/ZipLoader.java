package net.visendi.android.util;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class ZipLoader {

    final int BUFFER = 2048;

    public void downloadFile(String remotefileUri, String fileToSave) throws MalformedURLException, IOException {
        URL url = new URL(remotefileUri);
        URLConnection urlConnection = url.openConnection();
        BufferedInputStream bis = new BufferedInputStream(urlConnection.getInputStream());

        File file = new File(fileToSave);
        FileOutputStream fos = new FileOutputStream(file);
        BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);
        
        int count = 0;
        byte data[] = new byte[BUFFER];

        while ((count = bis.read(data, 0, BUFFER)) != -1) {
            dest.write(data, 0, count);
        }
        dest.flush();
        dest.close();
        
        bis.close();

    }

    public void unzipFile(String fileToLoad, String folderToUnzip) throws IOException {
        makeDir(folderToUnzip);

        FileInputStream fis = new FileInputStream(fileToLoad);
        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));

        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            System.out.println("Extracting: " + entry);

            int count;
            byte data[] = new byte[BUFFER];

            FileOutputStream fos = new FileOutputStream(folderToUnzip + entry.getName());
            BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);
            while ((count = zis.read(data, 0, BUFFER)) != -1) {
                dest.write(data, 0, count);
            }
            dest.flush();
            dest.close();

        }
        zis.close();
    }

    public void makeDir(String folderName) throws IOException {
        File folder = new File(folderName);
        
        if (!folder.exists() && !folder.mkdir()) {
            throw new IOException("Can't create folder " + folderName);
        }
        assert (folder.exists());
        assert (folder.isDirectory());
        assert (folder.canWrite());
    }

}
