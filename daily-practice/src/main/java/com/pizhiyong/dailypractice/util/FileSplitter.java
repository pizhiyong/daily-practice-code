package com.pizhiyong.dailypractice.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileSplitter {

    public static List<File> splitFile(File inputFile, int numberOfParts) throws IOException {
        List<File> partFiles = new ArrayList<>();
        
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inputFile))) {
            long fileSize = inputFile.length();
            long partSize = fileSize / numberOfParts;
            long remainingBytes = fileSize % numberOfParts;

            byte[] buffer = new byte[1024];
            int bytesRead;
            
            for (int i = 1; i <= numberOfParts; i++) {
                File partFile = new File(inputFile.getParent(), inputFile.getName() + ".part" + i);
                try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(partFile))) {
                    long bytesToWrite = partSize + (i <= remainingBytes ? 1 : 0);
                    while (bytesToWrite > 0 && (bytesRead = bis.read(buffer, 0, (int) Math.min(buffer.length, bytesToWrite))) != -1) {
                        bos.write(buffer, 0, bytesRead);
                        bytesToWrite -= bytesRead;
                    }
                }
                partFiles.add(partFile);
            }
        }
        
        return partFiles;
    }
}