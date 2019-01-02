
package crawler;

/**
 *
 * @author Salmane Tamo
 */
public class URLNode {
    private URLNode next;  //Pointer to URLNode after this one
    private URLNode previous;  //Pointer to URLNode before this one
    private URLObj url;  //URL object stored in this node
    
    public URLNode(URLObj url){
        this.next = null;
        this.previous = null;
        this.url = url;
    }
    
    public URLNode(URLNode next, URLNode previous, URLObj url){
        this.next = next;
        this.previous = previous;
        this.url = url;
    }

    /**
     * @return the next
     */
    public URLNode getNext() {
        return next;
    }

    /**
     * @param next the next to set
     */
    public void setNext(URLNode next) {
        this.next = next;
    }

    /**
     * @return the previous
     */
    public URLNode getPrevious() {
        return previous;
    }

    /**
     * @param previous the previous to set
     */
    public void setPrevious(URLNode previous) {
        this.previous = previous;
    }

    /**
     * @return the url
     */
    public URLObj getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(URLObj url) {
        this.url = url;
    }
    
    /**
     * @return true if the two objects have the same url, false otherwise
     */
    @Override
    public boolean equals(Object o){
        if(o instanceof URLNode){
            URLNode otherURLNode = (URLNode) o;
            return this.url.equals(otherURLNode);
        }
        return false;
    }
}
