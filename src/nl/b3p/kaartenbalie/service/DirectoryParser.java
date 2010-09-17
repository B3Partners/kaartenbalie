package nl.b3p.kaartenbalie.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

    protected int fileId;

    public DirectoryParser() {
        reset();
    }

    public void reset() {
        fileId = 0;
    }

    public JSONArray parse2JSON(File directory) throws JSONException {
        return parse2JSON(directory, null);
    }

    public JSONArray parse2JSON(File directory, String[] filter) throws JSONException {
        if (directory == null) {
            return null;
        }
        JSONArray nodes = new JSONArray();

        if (directory.isDirectory() && directory.listFiles()!=null) {
            File[] files = sortFiles(directory.listFiles());

            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                fileId++;
                if (file.isDirectory()) {
                    JSONObject folder = new JSONObject();
                    folder.put("id", Integer.toString(fileId));
                    folder.put("title", file.getName());
                    folder.put("children", parse2JSON(file, filter));
                    folder.put("isChild", false);

                    nodes.put(folder);

                } else if (allowedFile(file, filter)) {
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

        public List parse2List(File directory) {
        return parse2List(directory, null);
    }

    public List parse2List(File directory, String[] filter) {
        return parse2List(directory, filter, new ArrayList());
    }

    public List parse2List(File directory, String[] filter, List results) {
        if (directory == null || results == null) {
            return null;
        }
        if (!directory.isDirectory()) {
            return results;
        }

        File[] files = sortFiles(directory.listFiles());

        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isDirectory()) {
                results = parse2List(file, filter, results);
            } else if (allowedFile(file, filter)) {
                results.add(file.getPath());
            }
        }
        return results;
    }

    /**
     * Sort files and folders alphabetically and {folders, files}
     * @param files Files (and dirs) to sort
     * @return Sorted file array
     */
    protected static File[] sortFiles(File[] files) {
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
        if (extensions==null || extensions.length==0) {
            return true;
        }
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
