package com.djd.fun.thumbsup.models;

import com.djd.fun.thumbsup.util.MoreMoreFiles;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

// represents either File or Folder
public interface Asset {

  enum FileType {
    FOLDER, IMAGE, UNSUPPORTED;

    public static FileType findType(Path path) {
      if (Files.isDirectory(path)) {
        return FOLDER;
      }
      if (MoreMoreFiles.isImageType(path)) {
        return IMAGE;
      }
      return UNSUPPORTED;
    }
  }

  Path getFilePath();

  FileType getFileType();

  File getFile();

  Path getThumbnailPath();

  long getFileByteSize();

  String getReadableFileSize();
}
