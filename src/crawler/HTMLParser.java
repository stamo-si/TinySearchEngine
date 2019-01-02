
package crawler;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Salmane Tamo
 */
public class HTMLParser {
    private final int MAX_URL_LENGTH;
    private final int MAX_URL_PER_PAGE;
    private final ArrayList<String> ALLOWED_EXTENSIONS;
    
    public HTMLParser(){
        this.MAX_URL_LENGTH = 1000;
        this.MAX_URL_PER_PAGE = 1000;
        
        this.ALLOWED_EXTENSIONS = new ArrayList<>();        
        String[] extensions = {".htm", ".HTM", ".php", ".jsp"};        
        this.ALLOWED_EXTENSIONS.addAll(Arrays.asList(extensions));
    }
    
    public HTMLParser(int maxUrlLength, int maxUrlPerPage){
        this.MAX_URL_LENGTH = maxUrlLength;
        this.MAX_URL_PER_PAGE = maxUrlPerPage;
        
        this.ALLOWED_EXTENSIONS = new ArrayList<>();        
        String[] extensions = {".htm", ".HTM", ".php", ".jsp"};        
        this.ALLOWED_EXTENSIONS.addAll(Arrays.asList(extensions));
    }
    
    public HTMLParser(int maxUrlLength, int maxUrlPerPage, 
            ArrayList<String> allowedExtensions){
        this.MAX_URL_LENGTH = maxUrlLength;
        this.MAX_URL_PER_PAGE = maxUrlPerPage;
        this.ALLOWED_EXTENSIONS = allowedExtensions;
    }
    
    /**
    * This method is called recursively to extract the urls contained in
    * the html one url at a time starting at the beginning of the html 
    * to its end.
    * 
    * @param html the entire html that needs to be parsed
    * @param url the url link of the web page
    * @param result list containing the urls seen so far
    * @param position position of the most recent url found
    * 
    * @return the position of the url found
    * 
    */
    public int getNextUrl(String html, String url, 
            ArrayList<String> result, int position){
        
        //The first time the function is called, remove whitespace in html
        if(position == 0){
            this.removeWhiteSpace(html);
        }
        
        while(position < html.length()){
            if(html.charAt(position) == '<' && 
                    (html.charAt(position + 1) == 'a' ||
                    html.charAt(position + 1) == 'A')){
                break;
            }
            
            position++;
        }
        return 1;
    }
    
    /**
    * This method normalizes the url by removing the trailing slash and 
    * returns its validity depending on its length and the extension of 
    * the file it may link to.
    * 
    * @param url the url that needs to be validated
    * 
    * @return true if url is valid, false otherwise
    * 
    */
    public boolean normalizeUrl(String url){
        
        //If the url is too short or too long, then it is not valid
        if(url.length() <= 1 || url.length() > this.MAX_URL_LENGTH){
            return false;
        }
        
        //Removing the trailing slash from the url
        if(url.charAt(url.length() - 1) == '/'){
            url = url.substring(0, url.length() - 1);
        }
        
        //Retrieving the indices of the last slash and the last dot
        int lastSlashIndex = url.lastIndexOf("/");
        int lastDotIndex = url.lastIndexOf(".");
        
        //Checking whether the url links to a file
        if(lastSlashIndex >= 7 && lastDotIndex > lastSlashIndex && 
                lastDotIndex + 2 < url.length()){
            
            //If the extension of the extension of the file is not in our 
            //ALLOWED_EXTENSIONS list, then the url is discarded
            if(!this.ALLOWED_EXTENSIONS.contains(url.substring(lastDotIndex, url.length()))){
                return false;
            }
        }
        
        return true;
    }
    
    /**
    * This method removes the whitespace characters contained in the provided
    * html string.
    * 
    * @param html the html string
    * 
    * @return new html without whitespace
    */
    public String removeWhiteSpace(String html){
        String newHtml = "";  //This stores the html without spaces
        
        //Going through the html and adding only characters that are not spaces
        for(int i = 0; i < html.length(); i++){
            if(html.charAt(i) != ' '){
                newHtml += html.charAt(i);
            }
        }
        
        //Returning the newly built string without spaces
        return newHtml;
    }

}
