package com.aliyun.core.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * 带读取上限的输入流包装器。
 * 用于分片上传时限制单次分片可读取的最大字节数。
 */
public class BoundedInputStream extends InputStream {

    private final InputStream delegate;
    private long remaining;

    public BoundedInputStream(InputStream delegate, long size) {
        this.delegate = delegate;
        this.remaining = Math.max(size, 0);
    }

    @Override
    public int read() throws IOException {
        if (remaining <= 0) {
            return -1;
        }
        int value = delegate.read();
        if (value != -1) {
            remaining--;
        }
        return value;
    }

    @Override
    public int read(byte[] buffer, int offset, int length) throws IOException {
        if (remaining <= 0) {
            return -1;
        }
        int maxLength = (int) Math.min(length, remaining);
        int readLength = delegate.read(buffer, offset, maxLength);
        if (readLength > 0) {
            remaining -= readLength;
        }
        return readLength;
    }

    @Override
    public void close() throws IOException {
        delegate.close();
    }
}
