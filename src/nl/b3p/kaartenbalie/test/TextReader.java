import java.io.*;
import java.util.*;

public class TextReader {
    
    /**
     * Fetch the entire contents of a text file, and return it in a String.
     * This style of implementation does not throw Exceptions to the caller.
     *
     * @param aFile is a file which already exists and can be read.
     */
    public StringBuffer getContent(File aFile) {
        //...checks on aFile are elided
        StringBuffer contents = new StringBuffer();
        
        //declared here only to make visible to finally clause
        BufferedReader input = null;
        try {
            //use buffering, reading one line at a time
            //FileReader always assumes default encoding is OK!
            input = new BufferedReader( new FileReader(aFile) );
            String line = null; //not declared within while loop
            
                        /*
                         * readLine is a bit quirky :
                         * it returns the content of a line MINUS the newline.
                         * it returns null only for the END of the stream.
                         * it returns an empty String if two newlines appear in a row.
                         */
            while (( line = input.readLine()) != null){
                contents.append(line);
                contents.append(System.getProperty("line.separator"));
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (input!= null) {
                    //flush and close both "input" and its underlying FileReader
                    input.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return contents;
    }
    
    /**
     * Change the contents of text file in its entirety, overwriting any
     * existing text.
     *
     * This style of implementation throws all exceptions to the caller.
     *
     * @param aFile is an existing file which can be written to.
     * @throws IllegalArgumentException if param does not comply.
     * @throws FileNotFoundException if the file does not exist.
     * @throws IOException if problem encountered during write.
     */
    public void setContent(File aFile, String aContents) throws FileNotFoundException, IOException {
        if (aFile == null) {
            throw new IllegalArgumentException("File should not be null.");
        }
        if (!aFile.exists()) {
            throw new FileNotFoundException("File does not exist: " + aFile);
        }
        if (!aFile.isFile()) {
            throw new IllegalArgumentException("Should not be a directory: " + aFile);
        }
        //if (!aFile.canWrite()) {
        //	throw new IllegalArgumentException("File cannot be written: " + aFile);
        //}
        
        //declared here only to make visible to finally clause; generic reference
        Writer output = null;
        try {
            //use buffering
            //FileWriter always assumes default encoding is OK!
            output = new BufferedWriter( new FileWriter(aFile) );
            output.write( aContents );
        } finally {
            //flush and close both "output" and its underlying FileWriter
            if (output != null) output.close();
        }
    }
    
    
    
    /**
     * Simple test harness.
     */
    public TextReader() throws IOException {
        File f = new File("tfw");
        File files[] = f.listFiles();
        for(int i=0; i < files.length; i++) {
            File testFile = new File("tfw\\" + files[i].getName());
            
            StringBuffer content = this.getContent(testFile);
            //System.out.println("Original file contents: " + content.toString());
            //System.out.println("Going to change the file now.");
            int first      = content.indexOf("1.000000");
            int firstlast  = first + 8;
            //System.out.println("first :" + first);
            //System.out.println("firstlast :" + firstlast);
            int second     = content.indexOf("-1.000000");
            int secondlast = second + 9;
            //System.out.println("second :" + second);
            //System.out.println("secondlast :" + secondlast);
            
            
            //Start replacement //3.000000
            content.replace(first, firstlast, "3.030303");
            content.replace(second, secondlast, "-3.030303");
            File dest = new File(files[i].getName());
            boolean created = false;
            if(!dest.exists()) {
                created = dest.createNewFile();
            }
            //System.out.println("Has been created: " + created);
            setContent(dest, content.toString());
            //System.out.println("New file contents: " + getContent(dest));
        }
    }
    
    public static void main(String [] args) {
        try {
            TextReader tr = new TextReader();
        } catch (Exception e){
            System.out.println("Error occured : " + e);
        }
    }
}