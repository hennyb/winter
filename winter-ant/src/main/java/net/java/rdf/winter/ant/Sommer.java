/*
 * Sommer.java
 *
 * Created on December 5, 2006, 7:57 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.java.rdf.winter.ant;

import net.java.rdf.winter.JavassistClassRewriter;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * @author Tim Boudreau
 */
public class Sommer extends MatchingTask {
	
	protected static transient Logger logger = LoggerFactory.getLogger(JavassistClassRewriter.class.getName());
	
    private Path compileClasspath;
    ArrayList<FileSet> filesets = new ArrayList<FileSet>();
    private File todir;

    public Sommer() {
    }

    /**
     * Set the classpath to be used for this compilation.
     *
     * @param classpath an Ant Path object containing the compilation classpath.
     */
    public void setClasspath(Path classpath) {
        if (compileClasspath == null) {
            compileClasspath = classpath;
        } else {
            compileClasspath.append(classpath);
        }
        logger.info("CompileClasspath set to {}", compileClasspath);
    }

    /**
     * the directory to which the files should be copied
     * @param dir
     */
    public void setToDir(File dir) {
        todir = dir;
        logger.info("Direcoty to copy to is {}", todir.toString());
    }

    /**
     * Adds a set of files to be deleted.
     * @param set the set of files to be deleted
     */
     public void addFileset(FileSet set) {
         filesets.add(set);
     }


    private boolean failIfEmpty = true;

    public void setFailIfEmpty(boolean val) {
        failIfEmpty = val;
    }

    private String markerName;

    /**
     * the name of static fields to appear in rewritten classes
     * @param name
     */
    public void setMarkerName(String name) {
         markerName = name;
         logger.info("Markername set to {}", markerName);
    }

    private String markerValue;
    /**
     * the value of a static field, named by marker name
     * @param value
     */
    public void setMakerValue(String value) {
        markerValue = value;
        logger.info("Markervalue set to {}", markerValue);
    }

    public void execute() {

        if (compileClasspath == null || compileClasspath.size() == 0) {
            throw new BuildException("Set a classpath");
        }

        if (markerName == null)
            throw new BuildException("You need the 'markerName' attribute set on your sommer ant task!");

        ArrayList<String> files = new ArrayList<String>();
        int i = 1;
        logger.info("Scanning Files ......");
        for (FileSet fileset: filesets) {
            DirectoryScanner ds = fileset.getDirectoryScanner(getProject());
            for (String file: ds.getIncludedFiles()) {
                files.add(ds.getBasedir()+ File.separator+file);
                logger.info("File {} : {}", i++, file);
            }
        }
        if (files.size() == 0) {
            throw new BuildException("No files available in fileset"); //todo: do we really need this?
        }


        try {
            JavassistClassRewriter.transformFiles(Arrays.asList( compileClasspath.list()), files, (todir==null)?null:todir.getAbsolutePath(),
                                                  markerName, (markerName !=null && markerValue ==null)?new Date().toString():markerValue);
        } catch (Exception e) {
           e.printStackTrace();
           throw new BuildException("error recompiling classes",e);
        }
    }

}
