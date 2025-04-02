package com.kazurayam.ant.util;

import java.io.File;
import java.util.Stack;
import java.util.StringTokenizer;

import com.kazurayam.ant.BuildException;
import com.kazurayam.ant.launch.Locator;
import com.kazurayam.ant.taskdefs.condition.Os;

public class FileUtils {

    private static final FileUtils PRIMARY_INSTANCE = new FileUtils();

    private static final boolean ON_NETWARE = Os.isFamily("netware");
    private static final boolean ON_DOS = Os.isFamily("dos");
    private static final boolean ON_WIN9X = Os.isFamily("win9x");
    private static final boolean ON_WINDOWS = Os.isFamily("windows");

    /**
     * Empty constructor.
     */
    protected FileUtils() {
    }

    /**
     * Method to retrieve The FileUtils, which is shared by all users of this
     * method.
     * @return an instance of FileUtils.
     * @since Ant 1.6.3
     */
    public static FileUtils getFileUtils() {
        return PRIMARY_INSTANCE;
    }

    /**
     * Verifies that the specified filename represents an absolute path.
     * Differs from new java.io.File("filename").isAbsolute() in that a path
     * beginning with a double file separator--signifying a Windows UNC--must
     * at minimum match "\\a\b" to be considered an absolute path.
     * @param filename the filename to be checked.
     * @return true if the filename represents an absolute path.
     * @throws java.lang.NullPointerException if filename is null.
     * @since Ant 1.6.3
     */
    public static boolean isAbsolutePath(String filename) {
        if (filename.isEmpty()) {
            return false;
        }
        int len = filename.length();
        char sep = File.separatorChar;
        filename = filename.replace('/', sep).replace('\\', sep);
        char c = filename.charAt(0);
        if (!ON_DOS && !ON_NETWARE) {
            return c == sep;
        }
        if (c == sep) {
            // CheckStyle:MagicNumber OFF
            if (!ON_DOS || len <= 4 || filename.charAt(1) != sep) {
                return false;
            }
            // CheckStyle:MagicNumber ON
            int nextsep = filename.indexOf(sep, 2);
            return nextsep > 2 && nextsep + 1 < len;
        }
        int colon = filename.indexOf(':');
        return (Character.isLetter(c) && colon == 1
                && filename.length() > 2 && filename.charAt(2) == sep)
                || (ON_NETWARE && colon > 0);
    }

    /**
     * Constructs a file path from a <code>file:</code> URI.
     *
     * <p>Will be an absolute path if the given URI is absolute.</p>
     *
     * <p>Swallows '%' that are not followed by two characters,
     * doesn't deal with non-ASCII characters.</p>
     *
     * @param uri the URI designating a file in the local filesystem.
     * @return the local file system path for the file.
     * @since Ant 1.6
     */
    public String fromURI(String uri) {
        /*
        synchronized (cacheFromUriLock) {
            if (uri.equals(cacheFromUriRequest)) {
                return cacheFromUriResponse;
            }
            String path = Locator.fromURI(uri);
            String ret = isAbsolutePath(path) ? normalize(path).getAbsolutePath() : path;
            cacheFromUriRequest = uri;
            cacheFromUriResponse = ret;
            return ret;
        }
         */
        String path = Locator.fromURI(uri);
        return isAbsolutePath(path) ? normalize(path).getAbsolutePath() : path;
    }

    /**
     * &quot;Normalize&quot; the given absolute path.
     *
     * <p>This includes:
     * <ul>
     *   <li>Uppercase the drive letter if there is one.</li>
     *   <li>Remove redundant slashes after the drive spec.</li>
     *   <li>Resolve all ./, .\, ../ and ..\ sequences.</li>
     *   <li>DOS style paths that start with a drive letter will have
     *     \ as the separator.</li>
     * </ul>
     * <p>Unlike {@link File#getCanonicalPath()} this method
     * specifically does not resolve symbolic links.</p>
     *
     * <p>If the path tries to go beyond the file system root (i.e. it
     * contains more ".." segments than can be travelled up) the
     * method will return the original path unchanged.</p>
     *
     * @param path the path to be normalized.
     * @return the normalized version of the path.
     *
     * @throws java.lang.NullPointerException if path is null.
     */
    public File normalize(final String path) {
        Stack<String> s = new Stack<>();
        String[] dissect = dissect(path);
        s.push(dissect[0]);

        StringTokenizer tok = new StringTokenizer(dissect[1], File.separator);
        while (tok.hasMoreTokens()) {
            String thisToken = tok.nextToken();
            if (".".equals(thisToken)) {
                continue;
            }
            if ("..".equals(thisToken)) {
                if (s.size() < 2) {
                    // Cannot resolve it, so skip it.
                    return new File(path);
                }
                s.pop();
            } else { // plain component
                s.push(thisToken);
            }
        }
        StringBuilder sb = new StringBuilder();
        final int size = s.size();
        for (int i = 0; i < size; i++) {
            if (i > 1) {
                // not before the filesystem root and not after it, since root
                // already contains one
                sb.append(File.separatorChar);
            }
            sb.append(s.elementAt(i));
        }
        return new File(sb.toString());
    }

    /**
     * Dissect the specified absolute path.
     * @param path the path to dissect.
     * @return String[] {root, remaining path}.
     * @throws java.lang.NullPointerException if path is null.
     * @since Ant 1.7
     */
    public String[] dissect(String path) {
        char sep = File.separatorChar;
        path = path.replace('/', sep).replace('\\', sep);

        // make sure we are dealing with an absolute path
        if (!isAbsolutePath(path)) {
            throw new BuildException(path + " is not an absolute path");
        }
        String root;
        int colon = path.indexOf(':');
        if (colon > 0 && (ON_DOS || ON_NETWARE)) {

            int next = colon + 1;
            root = path.substring(0, next);
            char[] ca = path.toCharArray();
            root += sep;
            //remove the initial separator; the root has it.
            next = (ca[next] == sep) ? next + 1 : next;

            final StringBuilder sbPath = new StringBuilder();
            // Eliminate consecutive slashes after the drive spec:
            for (int i = next; i < ca.length; i++) {
                if (ca[i] != sep || ca[i - 1] != sep) {
                    sbPath.append(ca[i]);
                }
            }
            path = sbPath.toString();
        } else if (path.length() > 1 && path.charAt(1) == sep) {
            // UNC drive
            int nextsep = path.indexOf(sep, 2);
            nextsep = path.indexOf(sep, nextsep + 1);
            root = (nextsep > 2) ? path.substring(0, nextsep + 1) : path;
            path = path.substring(root.length());
        } else {
            root = File.separator;
            path = path.substring(1);
        }
        return new String[] {root, path};
    }

    /**
     * Removes a leading path from a second path.
     *
     * <p>This method uses {@link #normalize} under the covers and
     * does not resolve symbolic links.</p>
     *
     * @param leading The leading path, must not be null, must be absolute.
     * @param path The path to remove from, must not be null, must be absolute.
     *
     * @return path's normalized absolute if it doesn't start with
     * leading; path's path with leading's path removed otherwise.
     *
     * @since Ant 1.5
     */
    public String removeLeadingPath(File leading, File path) {
        String l = normalize(leading.getAbsolutePath()).getAbsolutePath();
        String p = normalize(path.getAbsolutePath()).getAbsolutePath();
        if (l.equals(p)) {
            return "";
        }
        // ensure that l ends with a /
        // so we never think /foo was a parent directory of /foobar
        if (!l.endsWith(File.separator)) {
            l += File.separator;
        }
        return (p.startsWith(l)) ? p.substring(l.length()) : p;
    }

    /**
     * Interpret the filename as a file relative to the given file
     * unless the filename already represents an absolute filename.
     * Differs from <code>new File(file, filename)</code> in that
     * the resulting File's path will always be a normalized,
     * absolute pathname.  Also, if it is determined that
     * <code>filename</code> is context-relative, <code>file</code>
     * will be discarded and the reference will be resolved using
     * available context/state information about the filesystem.
     *
     * @param file the "reference" file for relative paths. This
     * instance must be an absolute file and must not contain
     * &quot;./&quot; or &quot;../&quot; sequences (same for \ instead
     * of /).  If it is null, this call is equivalent to
     * <code>new java.io.File(filename).getAbsoluteFile()</code>.
     *
     * @param filename a file name.
     *
     * @return an absolute file.
     * @throws java.lang.NullPointerException if filename is null.
     */
    public File resolveFile(File file, String filename) {
        if (!isAbsolutePath(filename)) {
            char sep = File.separatorChar;
            filename = filename.replace('/', sep).replace('\\', sep);
            if (isContextRelativePath(filename)) {
                file = null;
                // on cygwin, our current directory can be a UNC;
                // assume user.dir is absolute or all hell breaks loose...
                String udir = System.getProperty("user.dir");
                if (filename.charAt(0) == sep && udir.charAt(0) == sep) {
                    filename = dissect(udir)[0] + filename.substring(1);
                }
            }
            filename = new File(file, filename).getAbsolutePath();
        }
        return normalize(filename);
    }

    /**
     * On DOS and NetWare, the evaluation of certain file
     * specifications is context-dependent.  These are filenames
     * beginning with a single separator (relative to current root directory)
     * and filenames with a drive specification and no intervening separator
     * (relative to current directory of the specified root).
     * @param filename the filename to evaluate.
     * @return true if the filename is relative to system context.
     * @throws java.lang.NullPointerException if filename is null.
     * @since Ant 1.7
     */
    public static boolean isContextRelativePath(String filename) {
        if (!(ON_DOS || ON_NETWARE) || filename.isEmpty()) {
            return false;
        }
        char sep = File.separatorChar;
        filename = filename.replace('/', sep).replace('\\', sep);
        char c = filename.charAt(0);
        int len = filename.length();
        return (c == sep && (len == 1 || filename.charAt(1) != sep))
                || (Character.isLetter(c) && len > 1
                && filename.charAt(1) == ':'
                && (len == 2 || filename.charAt(2) != sep));
    }
}
