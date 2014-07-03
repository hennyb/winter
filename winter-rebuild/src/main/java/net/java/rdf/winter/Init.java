package net.java.rdf.winter;

import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openrdf.model.ValueFactory;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.trig.TriGWriter;

abstract class Init{

    public abstract boolean hasInferencing();

    public abstract RepositoryConnection getConnection();

    public abstract ValueFactory getValueFactory();
    
    public void export(PrintStream out) {
       try {
          getConnection().export(new TriGWriter(out));
       } catch (RepositoryException ex) {
          Logger.getLogger(SesameMemorySailInit.class.getName()).log(Level.SEVERE, null, ex);
       } catch (RDFHandlerException ex) {
          Logger.getLogger(SesameMemorySailInit.class.getName()).log(Level.SEVERE, null, ex);
       }
    }
 }
