
package crawler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Salmane Tamo
 */
public class Crawler {
    private String seedUrl;
    private String targetDirectory;
    private int maxCrawlingDepth;
    private static final int INTERVAL_PER_FETCH = 1;
    private HashMap<String, URLNode> urls;
    private static int currentPageID;
    private String lastUrlVisited;
    private HTMLParser htmlParser;
    
    public Crawler(String seedUrl, String targetDirectory, int maxCrawlingDepth){
        this.seedUrl = seedUrl;
        this.targetDirectory = targetDirectory;
        this.maxCrawlingDepth = maxCrawlingDepth;
        this.currentPageID = 0;
        this.lastUrlVisited = "";
        this.htmlParser = new HTMLParser();
        this.urls = new HashMap<>();        
    }
    
    /**
     * This method downloads the html of the web page from the provided
     * url. It then store the url, the depth and the html string in a new file 
     * to be stored in target directory.
     * 
     * @param url the URL to the web page
     * @param depth the depth of this page
     * 
     * @return the html string 
     */
    public String getPage(String url, int depth){
        //If url is not unique (not the seed url and present in dictionary)
        //or is invalid return null
        if((!url.equals(this.seedUrl) && this.urls.containsKey(url)) ||
                !this.htmlParser.normalizeUrl(url)){
            return null;
        }
        
        //Updating pageID and lastVisitedUrl with current info
        String pageID = Integer.toString(this.currentPageID + 1);
        this.lastUrlVisited = url; 
        
        //Setting this url to visited
        this.urls.get(url).getUrl().setVisited(true);
        
        //Creating string to store html
        String html = "";
        
        try{
            //Using URL object to access the web page and download html
            URL linkToPage = new URL(url);
            
            //Creating a reader for the html file
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(linkToPage.openStream()));
            
            //Creating a writer for new file added to targetDirectory folder
            //using pageID as file name
            BufferedWriter bufferedWriter = new BufferedWriter(
                    new FileWriter(this.targetDirectory + "/" + pageID));
            
            //Adding the url and the depth to the first two lines of the file
            bufferedWriter.write(url + "\n" + (depth + 1) + "\n");
            
            //Going through html stream and writing to our new file
            String line = bufferedReader.readLine();
            while(line != null){
                bufferedWriter.write(line);
                html += line;
                line = bufferedReader.readLine();
            }
            
            //Closing reader and writer
            bufferedReader.close();
            bufferedWriter.close();
            
        } catch(IOException e){
            System.out.println("IOException has been raised");
        }
        
        return html;
    }
    
    /**
     * This method goes through the html and extract the urls it 
     * contains, and add those urls in our dictionary if they do not already 
     * exist. It also ensures different nodes are connected.
     * 
     * @param html the html string of the page
     * @param url the URL to the web page
     * @param depth the depth of this page
     * 
     */
    public void extractUrls(String html, String url, int depth){
        //Getting the list of urls in this html using the htmlParser
        ArrayList<String> urlsList = new ArrayList<>();
        int getUrlList = this.htmlParser.getNextUrl(html, url, urlsList, 0);
        
        //Adding the url in our dictionary if it's not already there
        if(this.urls.get(url) != null){
            URLNode urlNode = new URLNode(new URLObj(url, depth));
            this.urls.put(url, urlNode);
        }
        
        //Going through the urls to connect the different nodes
        for(int i = 0; i < urlsList.size(); i++){
            
            URLNode previousNode;
            if(i == 0){
                //First time, previous node is node of url that was passed in
                previousNode = this.urls.get(url);
            } else {
                previousNode = this.urls.get(urlsList.get(i - 1));
            }
            
            //Adding the url in our dictionary if it's not already there
            if(this.urls.get(urlsList.get(i)) != null){
                //These urls are one level below the one that was passed,
                //they are at level depth + 1
                URLNode urlNode = new URLNode(new URLObj(urlsList.get(i), depth + 1));
                
                //Connecting nodes
                urlNode.setPrevious(previousNode);
                previousNode.setNext(urlNode);
                
                //Adding node to dictionary
                this.urls.put(url, urlNode);
            }
        }
    }
    
    /**
     * This method uses the getPage() and the extractUrls() methods to retrieve
     * all the urls contained in the web pages until it reaches the maximum
     * depth of pages starting with the page at seedUrl.
     * 
     */
    public void visitLinks(){
        //Getting the html in page at seed url
        String seedHtml = this.getPage(this.seedUrl, 0);
        
        //If there is nothing at seed Url, then inform the user we cannot proceed
        if(seedHtml.equals("")){
            System.out.println("Cannot crawl SEED_URL");
            return;
        }
        
        //Adding the seed url to our dictionary
        this.urls.put(seedUrl, new URLNode(new URLObj(seedUrl, 0)));
        
        String currentHtml = seedHtml;  //html content to parse
        String currentUrl = this.seedUrl;  //url link to visit
        int currentDepth = 0;  //the depth of the current page
        
        //Going through html pages until maximum depth is reached
        while(this.urls.get(this.lastUrlVisited).getUrl().getDepth() <= this.maxCrawlingDepth){
            //Extracting the urls in this current page
            this.extractUrls(currentHtml, currentUrl, currentDepth);
            
            //Updating url to url from next node
            currentUrl = this.urls.get(this.lastUrlVisited).getNext().getUrl().getUrl();
            
            //Depth is depth of last url visited
            currentDepth = this.urls.get(this.lastUrlVisited).getUrl().getDepth();
            
            //Getting new html from next page
            currentHtml = this.getPage(currentUrl, currentDepth);
        }
    }
    
}
