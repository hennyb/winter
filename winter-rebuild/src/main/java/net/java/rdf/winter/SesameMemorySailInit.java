/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.java.rdf.winter;

import java.io.File;
import java.util.HashMap;

import org.openrdf.model.ValueFactory;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.repository.sail.SailRepositoryConnection;
import org.openrdf.sail.SailException;
import org.openrdf.sail.memory.MemoryStore;
import org.openrdf.sail.memory.model.MemValueFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;;

/**
 * The sesame memory sail init. Initializing memory store, sail repository connection and value factory 
 * @author Stefan Scheglmann 
 */
public class SesameMemorySailInit extends Init {
	 protected static transient Logger logger = LoggerFactory.getLogger(SesameMemorySailInit.class.getName());
   
	  File dataDir = null;
      ValueFactory vf =   new MemValueFactory();
      SailRepositoryConnection con;
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
     * Memory sail init, for pure memory sail
     */
    public SesameMemorySailInit() {
    	logger.debug("SesameMemorySailInit : new MemorySail");
         try {
            MemoryStore mem = new org.openrdf.sail.memory.MemoryStore();
            mem.initialize();
            org.openrdf.repository.sail.SailRepository sail = new org.openrdf.repository.sail.SailRepository(mem);
            
            con = sail.getConnection();
            vf = sail.getValueFactory();
         } catch (SailException e) {
        	logger.error("Sail exception : {}", e.toString());
            e.printStackTrace(); //todo: decide what exception to throw
            throw new Error("todo something better", e);
         } catch (RepositoryException e) {
         	logger.error("Repository exception : {}", e.toString());
            e.printStackTrace(); //todo: decide what exception to throw
            throw new Error("todo something better", e);
         }
     	logger.info("New blanc init created");
      }
      
      /**
       * Memory sail init, for file sail
     * @param dir File store address
     */
    public SesameMemorySailInit(String dir) {
    	logger.debug("SesameMemorySailInit : new MemorySail");
    	  try{
    		  dataDir = new File(dir);
    		  MemoryStore mem = new org.openrdf.sail.memory.MemoryStore(dataDir);
    		  mem.initialize();
    		  SailRepository sail = new SailRepository(mem);
    		  con = sail.getConnection();
    		  vf = sail.getValueFactory();
    	  }catch(SailException e){
          	  logger.error("Sail exception : {}", e.toString());
    		  e.printStackTrace();
    		  throw new Error("jojo");
    	  }catch(RepositoryException e){
          	  logger.error("Sail exception : {}", e.toString());
    		  e.printStackTrace();
    		  throw new Error("jojo");
    	  }
       	logger.info("New file init created");
      }
}
