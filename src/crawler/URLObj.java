
package crawler;

/**
 *
 * @author Salmane Tamo
 */
public class URLObj {
    private String url;  //URL link to the webpage
    private int depth;   //Depth of the webpage in the tree
    private boolean visited;  //True if this page has been visited
    
    public URLObj(String url, int depth){
        this.url = url;
        this.depth = depth;
        this.visited = false;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the depth
     */
    public int getDepth() {
        return depth;
    }

    /**
     * @param depth the depth to set
     */
    public void setDepth(int depth) {
        this.depth = depth;
    }

    /**
     * @return the visited
     */
    public boolean isVisited() {
        return visited;
    }

    /**
     * @param visited the visited to set
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }
    
    /**
     * @return true if the two objects have the same url, false otherwise
     */
    @Override
    public boolean equals(Object o){
        if(o instanceof URLObj){
            URLObj otherURL = (URLObj) o;
            return this.url.equals(otherURL.getUrl());
        }
        return false;
    }
}
