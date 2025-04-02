package com.kazurayam.ant.launch;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

public class Locator {

    private static final int NIBBLE = 4;

    private static final int WORD = 16;

    /** Error string used when an invalid uri is seen */
    public static final String ERROR_NOT_FILE_URI
            = "Can only handle valid file: URIs, not ";

    /**
     * Constructs a file path from a <code>file:</code> URI.
     *
     * <p>Will be an absolute path if the given URI is absolute.</p>
     *
     * <p>Prior to Java 1.4,<!-- TODO is JDK version actually relevant? -->
     * swallows '%' that are not followed by two characters.</p>
     *
     * See <a href="https://www.w3.org/TR/xml11/#dt-sysid">dt-sysid</a>
     * which makes some mention of how
     * characters not supported by URI Reference syntax should be escaped.
     *
     * @param uri the URI designating a file in the local filesystem.
     * @return the local file system path for the file.
     * @throws IllegalArgumentException if the URI is malformed or not a legal file: URL
     * @since Ant 1.6
     */
    public static String fromURI(String uri) {
        URL url = null;
        try {
            url = new URL(uri);
        } catch (MalformedURLException emYouEarlEx) {
            // Ignore malformed exception
        }
        if (url == null || !("file".equals(url.getProtocol()))) {
            throw new IllegalArgumentException(ERROR_NOT_FILE_URI + uri);
        }
        StringBuilder buf = new StringBuilder(url.getHost());
        if (buf.length() > 0) {
            buf.insert(0, File.separatorChar).insert(0, File.separatorChar);
        }
        String file = url.getFile();
        int queryPos = file.indexOf('?');
        buf.append((queryPos < 0) ? file : file.substring(0, queryPos));

        uri = buf.toString().replace('/', File.separatorChar);

        if (File.pathSeparatorChar == ';' && uri.startsWith("\\") && uri.length() > 2
                && Character.isLetter(uri.charAt(1)) && uri.lastIndexOf(':') > -1) {
            uri = uri.substring(1);
        }
        String path = null;
        try {
            path = decodeUri(uri);
            //consider adding the current directory. This is not done when
            //the path is a UNC name
            String cwd = System.getProperty("user.dir");
            int posi = cwd.indexOf(':');
            boolean pathStartsWithFileSeparator = path.startsWith(File.separator);
            boolean pathStartsWithUNC = path.startsWith("" + File.separator + File.separator);
            if (posi > 0 && pathStartsWithFileSeparator && !pathStartsWithUNC) {
                path = cwd.substring(0, posi + 1) + path;
            }
        } catch (UnsupportedEncodingException exc) {
            // not sure whether this is clean, but this method is
            // declared not to throw exceptions.
            throw new IllegalStateException(
                    "Could not convert URI " + uri + " to path: "
                            + exc.getMessage());
        }
        return path;
    }


    /**
     * Decodes an Uri with % characters.
     * The URI is escaped
     * @param uri String with the uri possibly containing % characters.
     * @return The decoded Uri
     * @throws UnsupportedEncodingException if UTF-8 is not available
     * @since Ant 1.7
     */
    public static String decodeUri(String uri) throws UnsupportedEncodingException {
        if (!uri.contains("%")) {
            return uri;
        }
        ByteArrayOutputStream sb = new ByteArrayOutputStream(uri.length());
        CharacterIterator iter = new StringCharacterIterator(uri);
        for (char c = iter.first(); c != CharacterIterator.DONE;
             c = iter.next()) {
            if (c == '%') {
                char c1 = iter.next();
                if (c1 != CharacterIterator.DONE) {
                    int i1 = Character.digit(c1, WORD);
                    char c2 = iter.next();
                    if (c2 != CharacterIterator.DONE) {
                        int i2 = Character.digit(c2, WORD);
                        sb.write((char) ((i1 << NIBBLE) + i2));
                    }
                }
            } else if (c >= 0x0000 && c < 0x0080) {
                sb.write(c);
            } else { // #50543
                byte[] bytes = String.valueOf(c).getBytes(StandardCharsets.UTF_8);
                sb.write(bytes, 0, bytes.length);
            }
        }
        return sb.toString(StandardCharsets.UTF_8.name());
    }
}
