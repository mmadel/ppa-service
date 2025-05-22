package com.cob.ppa.service.batch.reader.util;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;

public class UTF8BOMSkipper {
    public static Reader skipUTF8BOM(Reader reader) throws IOException {
        PushbackReader pushbackReader = new PushbackReader(reader, 3);
        char[] bom = new char[1];
        int read = pushbackReader.read(bom);

        if (read == -1) return pushbackReader;

        // Check for BOM: 0xFEFF
        if (bom[0] != '\uFEFF') {
            pushbackReader.unread(bom);
        }

        return pushbackReader;
    }
}
