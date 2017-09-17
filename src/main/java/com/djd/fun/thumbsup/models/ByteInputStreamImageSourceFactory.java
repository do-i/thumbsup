package com.djd.fun.thumbsup.models;

import java.io.InputStream;

public interface ByteInputStreamImageSourceFactory {

  ByteInputStreamImageSource createByteInputStreamImageSource(InputStream inputStream);
}
