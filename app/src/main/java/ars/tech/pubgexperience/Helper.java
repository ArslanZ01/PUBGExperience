package ars.tech.pubgexperience;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {
    static StringBuilder listOFiles;
    static int count_files;
    static int count_directories;

    static File device_id_file = new File(Environment.getDataDirectory().toString()+ "/data/com.tencent.ig/shared_prefs/device_id.xml");

    static String device_id_sample = "<?xml version='1.0' encoding='utf-8' standalone='yes' ?>\n" +
            "<map>\n" +
            "    <string name=\"install\"></string>\n" +
            "    <string name=\"random\"></string>\n" +
            "    <string name=\"uuid\">sample</string>\n" +
            "</map>\n";

    public static String increment(String string) throws NullPointerException {
        char[] uuid_char = null;
        try {
            uuid_char = string.toLowerCase().toCharArray();
            for (int i = uuid_char.length - 1; i >= 0; i--) {
                if (uuid_char[i] == 'z') {
                    uuid_char[i] = 'a';
                } else if (uuid_char[i] == '9') {
                    uuid_char[i] = '0';
                } else {
                    uuid_char[i]++;
                    break;
                }
            }
        } finally {
            return String.valueOf(uuid_char);
        }
    }

    public static String decrement(String string) throws NullPointerException {
        char[] uuid_char = null;
        try {
            uuid_char = string.toLowerCase().toCharArray();
            for (int i = uuid_char.length - 1; i >= 0; i--) {
                if (uuid_char[i] == 'a') {
                    uuid_char[i] = 'z';
                } else if (uuid_char[i] == '0') {
                    uuid_char[i] = '9';
                } else {
                    uuid_char[i]--;
                    break;
                }
            }
        } finally {
            return String.valueOf(uuid_char);
        }
    }

    public static void copyFile(File sourceFile, File destinationFile) throws IOException {
        File dir = new File(destinationFile.getParent());
        InputStream is = null;
        OutputStream os = null;
        try {
            if (!dir.exists())
                dir.mkdirs();
            is = new FileInputStream(sourceFile);
            os = new FileOutputStream(destinationFile);
            byte[] b = new byte[1024];
            int length;
            while ((length = is.read(b)) > 0) {
                os.write(b, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            is.close();
            os.close();
        }
    }

    public static void copyDirectory(File sourceDirectory, File destinationDirectory) throws IOException {
        if (!destinationDirectory.exists())
            destinationDirectory.mkdirs();
        for (File element : sourceDirectory.listFiles()) {
            if (element.isDirectory()) {
                File directory = new File(destinationDirectory, element.getName());
                directory.mkdir();
                copyDirectory(element, directory);
            } else {
                File file = new File(destinationDirectory, element.getName());
                copyFile(element, file);
            }
        }
    }

    public static void writeStringToFile(String data, File destination) throws IOException {
        File dir = new File(destination.getParent());
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        try {
            if (!dir.exists())
                dir.mkdirs();
            destination.createNewFile();
            fos = new FileOutputStream(destination);
            osw = new OutputStreamWriter(fos);
            osw.append(data);
        } finally {
            if (osw != null)
                osw.close();
            if (fos != null){
                fos.flush();
                fos.close();
            }
        }
    }

    public static String readFileToString(File source) throws IOException {
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        StringBuilder sb;
        try {
            is = new FileInputStream(source);
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            sb = new StringBuilder();
            for (String line; (line = br.readLine()) != null; ) {
                sb.append(line).append('\n');
            }
        } finally {
            if (br != null)
                br.close();
            if (isr != null)
                isr.close();
            if (is != null)
                is.close();
        }
        return sb.toString();
    }

    public static String randomAlphanumeric(int length){
        String s = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random r = new Random();
        StringBuilder sb = new StringBuilder(length);
        for(int i = 0; i < length; i++)
            sb.append(s.charAt(r.nextInt(s.length())));
        return sb.toString();
    }

    public static String stringBetween(String string, String first, String second){
        Pattern pattern = Pattern.compile(first + "(.*?)" + second, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(string);
        if (matcher.find())
            return matcher.group(1);
        return null;
    }

    public static String getFilesCount(File directory){
        listOFiles = new StringBuilder();
        count_files = 0;
        count_directories = 0;
        getFiles(directory);
        return "Files : " + count_files + "\n" +
                "Directories : " + count_directories + "\n" +
                "List of Files and Directories:-" + "\n" + listOFiles.toString();
    }

    public static void getFiles(File directory) {
        File[] fl = directory.listFiles();
        if (fl != null) {
            for (File f: fl) {
                listOFiles.append(f.getPath() + "\n");
                if (f.isFile())
                    count_files++;
                if (f.isDirectory()){
                    count_directories++;
                    getFiles(f);
                }
            }
        }
    }

    public static void delete(File element) {
        if (element.isDirectory())
            for (File child : element.listFiles())
                delete(child);
        element.delete();
    }
}
