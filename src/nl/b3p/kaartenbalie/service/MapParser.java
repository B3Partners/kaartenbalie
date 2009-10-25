/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.b3p.kaartenbalie.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Gertjan
 */
public class MapParser {

    private final File mapFile;
    private Map<String, String> webMetadata = new HashMap<String, String>();

    public MapParser(File mapFile) throws IOException {
        if (!mapFile.getName().toLowerCase().endsWith(".map")) {
            throw new IOException("File is not a .map file");
        }
        this.mapFile = mapFile;
    }

    public void parse() throws FileNotFoundException, IOException {
        InputStream in = null;
        try {
            in = new FileInputStream(mapFile);

            InputStreamReader converter = new InputStreamReader(in);
            BufferedReader buffer = new BufferedReader(converter);

            String line = "";
            while ((line = buffer.readLine()) != null) {
//            if (line.trim().toUpperCase().equals("MAP")) {
                parseMap(line, buffer);
                break;
//            }
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }

    }

    private void parseMap(String line, BufferedReader buffer) throws IOException {
        while ((line = buffer.readLine()) != null) {
            if (line.trim().toUpperCase().equals("WEB")) {
                parseWeb(line, buffer);
                break;
            }
        }
    }

    private void parseWeb(String line, BufferedReader buffer) throws IOException {
        while ((line = buffer.readLine()) != null) {
            if (line.trim().toUpperCase().equals("METADATA")) {
                parseMetaData(line, buffer);
                break;
            }
        }
    }

    private void parseMetaData(String line, BufferedReader buffer) throws IOException {
        while ((line = buffer.readLine()) != null) {
            if (line.trim().toLowerCase().indexOf("wms_title") >= 0) {
                webMetadata.put("wms_title", lastFromSplitLine(line));
            } else if (line.trim().toLowerCase().indexOf("wms_onlineresource") >= 0) {
                webMetadata.put("wms_onlineresource", lastFromSplitLine(line));
            } else if (line.trim().toLowerCase().indexOf("wms_srs") >= 0) {
                webMetadata.put("wms_srs", lastFromSplitLine(line));
            } else if (line.trim().toUpperCase().equals("END")) {
                break;
            }
        }
    }

    private String[] splitLine(String line) {
        if (line == null) {
            return null;
        }
        String[] splitLine = null;
        line = line.replace('\'', '\"');
        splitLine = line.split("\"");
        return splitLine;
    }

    private String lastFromSplitLine(String line) {
        String[] splitLine = splitLine(line);
        for (int i = splitLine.length - 1; i >= 0; i--) {
            String lineFrag = splitLine[i].trim();
            if (lineFrag.length() > 0) {
                return lineFrag;
            }
        }
        return null;
    }

    public Map<String, String> getWebMetadata() {
        return webMetadata;
    }
}
