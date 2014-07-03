/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.java.rdf.winter;

import java.io.File;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.repository.sail.SailRepositoryConnection;
import org.openrdf.sail.SailException;
import org.openrdf.sail.memory.MemoryStore;
import org.openrdf.sail.memory.model.MemValueFactory;

/**
 * The sesame http memory init. Initializing http store, sail repository connection and value factory 
 * @author Stefan Scheglmann 
 */
public class SesameMemoryHttpInit extends Init {
   
	  File dataDir = null;
      ValueFactory vf =   new MemValueFactory();
      RepositoryConnection con;
      boolean inferencing = false;
      
    /**
     * @return file directory 
     */
    public File getDataDir(){
    	  return dataDir;
      }
      
    /* (non-Javadoc)
     * @see net.java.rdf.SesameInit#getValueFactory()
     */
    public ValueFactory getValueFactory() {
         return vf;
      }
      
    /* (non-Javadoc)
     * @see net.java.rdf.SesameInit#getConnection()
     */
    public RepositoryConnection getConnection() {
         return con;
      }
      
    /* (non-Javadoc)
     * @see net.java.rdf.SesameInit#hasInferencing()
     */
    public boolean hasInferencing() {
         return inferencing;
      }
   
	/**
	 * Memory sail init, for file sail
	 * @param dir File store address
	 */
    public SesameMemoryHttpInit(String url) {
    	  try{
      		  HTTPRepository http = new HTTPRepository(url);
    		  con = http.getConnection();
    		  vf = http.getValueFactory();
    	  }catch(RepositoryException e){
    		  e.printStackTrace();
    		  throw new Error("jojo");
    	  }
      }
}
