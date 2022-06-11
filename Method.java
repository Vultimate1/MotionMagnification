package com.spy.motionmagnificationsinglescreen;

import java.io.File;

public class Method {

    public static void load_Directory_Files(File directory) {
        File[] fileList = directory.listFiles();
        if (fileList!=null && fileList.length > 0) {
            for (File file: fileList) {
                String uri = file.getAbsolutePath();
                if (file.isDirectory()) {
                    load_Directory_Files(file);
                } else {
                    String name = file.getName().toLowerCase();
                    for (String extension: Constant.videoExtensions) {
                        if (name.endsWith(extension)) {
                            Constant.allMediaList.add(file);
                            break;
                        }
                    }
                }
            }
        }

    }

}
