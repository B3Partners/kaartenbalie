package nl.b3p.kaartenbalie.service;

import java.io.File;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Gertjan
 */
public class DirectoryParser {

    public static final String[] SHELL_FILES = {"Thumbs.db"};
    private int fileId;

    public DirectoryParser() {
        reset();
    }

    public void reset() {
        fileId = 0;
    }

    public JSONArray stepIntoDirectory(File directory) throws JSONException {
        return stepIntoDirectory(directory, new String[0]);
    }

    public JSONArray stepIntoDirectory(File directory, String[] filter) throws JSONException {
        JSONArray nodes = new JSONArray();

        if (directory.isDirectory()) {
            File[] files = sortFiles(directory.listFiles());

            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                fileId++;
                if (file.isDirectory()) {
                    JSONObject folder = new JSONObject();
                    folder.put("id", Integer.toString(fileId));
                    folder.put("title", file.getName());
                    folder.put("children", stepIntoDirectory(file));
                    folder.put("isChild", false);

                    nodes.put(folder);

                } else if (allowedFile(file, filter) || (filter.length == 0 && !allowedFile(file, SHELL_FILES))) {
                    JSONObject child = new JSONObject();
                    child.put("id", Integer.toString(fileId));
                    child.put("title", file.getName());
                    child.put("path", file.getPath());
                    child.put("isChild", true);

                    nodes.put(child);
                }
            }
        }
        return nodes;
    }

    public void deleteFileByID(File directory, int deleteAt) {
        if (directory.isDirectory()) {
            File[] files = sortFiles(directory.listFiles());

            for (int i = 0; i < files.length; i++) {
                if (++fileId == deleteAt) {
                    // File found, delete it
                    files[i].delete();
                    break;
                }
                if (files[i].isDirectory()) {
                    deleteFileByID(files[i], deleteAt);
                }
            }
        }
    }

    /**
     * Sort files and folders alphabetically and {folders, files}
     * @param files Files (and dirs) to sort
     * @return Sorted file array
     */
    private static File[] sortFiles(File[] files) {
        SortedMap<String, File> mapDirs = new TreeMap();
        SortedMap<String, File> mapFiles = new TreeMap();

        // Split and sort
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isDirectory()) {
                mapDirs.put(file.getAbsolutePath(), file);
            } else {
                mapFiles.put(file.getAbsolutePath(), file);
            }
        }

        // Add dirs
        Iterator it = mapDirs.keySet().iterator();
        int i = 0;
        while (it.hasNext()) {
            String key = (String) it.next();
            files[i++] = mapDirs.get(key);
        }

        // Add files
        it = mapFiles.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            files[i++] = mapFiles.get(key);
        }

        return files;
    }

    /**
     * Check if a file is supported by the DataStoreLinker
     * @param file File to check
     * @return File approved
     */
    public static boolean allowedFile(File file, String[] extensions) {
        String filename = file.getName();
        for (int i = 0; i < extensions.length; i++) {
            String endFix = extensions[i];
            if (filename.toLowerCase().endsWith(endFix.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
