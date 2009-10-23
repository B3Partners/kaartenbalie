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
        InputStream in = new FileInputStream(mapFile);

        InputStreamReader converter = new InputStreamReader(in);
        BufferedReader buffer = new BufferedReader(converter);

        String line = "";
        while ((line = buffer.readLine()) != null) {
            if (line.equals("MAP")) {
                parseMap(line, buffer);
            }else if (line.equals("END")) {
                break;
            }
        }
    }

    private void parseMap(String line, BufferedReader buffer) throws IOException {
        while ((line = buffer.readLine()) != null) {
            if (line.equals("  WEB")) {
                parseWeb(line, buffer);
            }else if(line.equals("  END")){
                break;
            }
        }
    }

    private void parseWeb(String line, BufferedReader buffer) throws IOException {
        while ((line = buffer.readLine()) != null) {
            if (line.toUpperCase().contains("    METADATA")) {
                parseMetaData(line, buffer);
            }else if(line.equals("    END")){
                break;
            }
        }
    }

    private void parseMetaData(String line, BufferedReader buffer) throws IOException {
        while ((line = buffer.readLine()) != null) {
            if (line.contains("'")) {
                // Line example: "      'wms_srs'             'EPSG:4326'"
                String[] lineParts = line.split("'");

                if (lineParts.length == 3) {
                    // K/V has no value
                    // {space}{key}{space}
                    webMetadata.put(lineParts[1], "");
                } else if (lineParts.length == 4) {
                    // {space}{key}{space}{value}
                    webMetadata.put(lineParts[1], lineParts[3]);
                }
            } else if (line.toUpperCase().contains("END")) {
                break;
            }
        }
    }

    public Map<String, String> getWebMetadata() {
        return webMetadata;
    }
}
