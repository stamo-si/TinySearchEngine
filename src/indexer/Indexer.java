
package indexer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author Salmane Tamo
 */
public class Indexer {
    private String targetDirectory;
    private HashMap<String, HashMap<Integer, Integer>> index;
    
    public Indexer(String targetDirectory){
        this.targetDirectory = targetDirectory;
        this.index = new HashMap<>();
    }
    
    /**
     * This method goes through the provided file, removes its html tags and
     * add each word to our index dictionary with the number of occurrences of
     * that word.
     * 
     * @param fileIdentifier the id of the page in target directory
     */
    public void loadFile(String fileIdentifier){
        try{
            //Reader to go through the file in target directory
            BufferedReader bufferedReader = new BufferedReader(
                    new FileReader(this.targetDirectory + fileIdentifier));
            
            //The first two lines store the url and the depth of the page, so
            //we are skipping those two lines
            String line = bufferedReader.readLine();
            line = bufferedReader.readLine();
            line = bufferedReader.readLine();
            
            //Storing all the html content from the file
            String html = "";
            
            while(line != null){
                html += line;
                line = bufferedReader.readLine();
            }
            
            //Removing the html tags from the html string
            String htmlWithoutTags = this.removeHtmlTags(html).toLowerCase();
            
            //Storing the page identifier
            int pageId = Integer.parseInt(fileIdentifier);
            
            //Different delimiters to split words
            String delimiters = 
                    "\\t|,|;|\\.|\\?|!|-|:|@|\\[|\\]|\\(|\\)|\\{|\\}|_|\\*|/";
            
            //Going through html text and updating index
            for(String paragraph : htmlWithoutTags.split("\n")){
                
                //Splitting with our delimiters
                for(String word : paragraph.split(delimiters)){
                    
                    //If word is absent, add it to index
                    if(this.index.get(word) == null){
                        HashMap<Integer, Integer> pageIdToOccurrences = 
                                new HashMap<>();
                        pageIdToOccurrences.put(pageId, 1);
                        
                        this.index.put(word, pageIdToOccurrences);
                    
                    //Word exists already, check if it exists for this page
                    } else {
                        //Word absent for this, add it
                        if(this.index.get(word).get(pageId) == null){
                            this.index.get(word).put(pageId, 1);
                        
                        //Word exists already for this page, increment occurences
                        } else {
                            int occurrences = this.index.get(word).get(pageId);
                            this.index.get(word).put(pageId, occurrences + 1);
                        }
                    }
                }
            }
            
            //Closing file
            bufferedReader.close(); 
        } catch(IOException e){
            System.out.println("Couldn't process file");
            e.printStackTrace();
        }
    }
    
    /**
     * This method goes through the provided html and remove all the html tags
     * that it contains
     * 
     * @param html the html String
     */
    public String removeHtmlTags(String html){
        //TODO
        return "";
    }
    
    /**
     * This method goes through all the files in target directory and uses the 
     * loadFile() method to update our index. Once the index is created, it 
     * also stores the index in a new file.
     * 
     */
    public void storeIndex(){
        //Getting all the files in target directory
        File[] files = new File(this.targetDirectory).listFiles();
        
        //Going through the files and using loadFile() to update index
        for(File file : files){
            this.loadFile(file.getName());
        }
        
        try{
            //Creating a new file to store the index
            BufferedWriter bufferedWriter = 
                    new BufferedWriter(new FileWriter("index"));
            
            //For each word, a new line is added to the file
            for(String word : this.index.keySet()){
                //Word is added to the line
                bufferedWriter.write("\n" + word + " ");
                
                //On the same line, adding (pageId, frequency)
                for(int pageId : this.index.get(word).keySet()){
                    bufferedWriter.write("(" + pageId + ", " + 
                            this.index.get(word).get(pageId) + ") ");
                }
            }
            
            //Closing file
            bufferedWriter.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
