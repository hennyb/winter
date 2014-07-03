package de.uniko.west.winter.preprocess;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.UUID;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/*

The WINTER-API is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

The WINTER-API is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with the WINTER-API.  If not, see <http://www.gnu.org/licenses/>.
*/

/**
 * @author Stefan Scheglmann, Frederik Jochum
 *
 */
public class ClassPathAnalysis {

	private static File tempDir;
	
//	public static void main(String[] args) {
//		Vector<String> classNames = getClassNamesFromClassPath();
//		for (String className : classNames) {
//			try {
//				Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
//				clazz.getAnnotations(); //usw.
//			} catch (ClassNotFoundException e) {
//				e.printStackTrace();
//			}
//		}
//	}
	
	public static Vector<String> getClassNamesFromClassPath(){
		
		// create temp directory for jar extraction
		tempDir = new File(System.getProperty("java.io.tmpdir"));
		if (!tempDir.exists()){
			System.out.println("Warning! System tempdir not existent!");
		}else{
			tempDir = new File(tempDir.getAbsolutePath() + File.separator + "winter1.1/" );
			System.out.print("creating tempDir: " + tempDir.getAbsolutePath() );
			tempDir.mkdir();
			System.out.println( (tempDir.exists()?" [OK]":" [FAIL]"));
		}
		
		//some output
		String[] classPaths = System.getProperty("java.class.path").split(
				System.getProperty("path.separator"));
		System.out.println("Found " + classPaths.length + " classpath entries:");
		for ( String path : classPaths ){
			System.out.println(" -> " + path );
		}
		
		// extract jars (if present) and put extracted directories and classpath directories together
		Vector<String> extractedClassPaths = new Vector<String>();
		for (String path : classPaths) {
			
			//  --- REMOVE THAT LATER!! --- 
			if (path.contains(".m2")){
				continue;
			}
			// ----------------------------
			
			File file = new File(path);
			if (isJarFile(file)){
				extractedClassPaths.addAll( extractJarFile(file) );
			}else if (file.isDirectory()){
				extractedClassPaths.add( path );
			}else{
				System.out.println("unknown classpath entry: " + path);
			}
		}
		
//		locate ".class"-files in these directories
		Vector<String> resultClassNames = new Vector<String>();
		File file;
		for (String path : extractedClassPaths) {
			System.out.println(path);
			file = new File(path);
			resultClassNames.addAll(getClassURLsFromDir(file, file));
		}
		
//		System.out.println("=== CLASSES FOUND ====");
//		for (String className : resultClassNames) {
//			try {
//				System.out.println("  class: " + className);
//				Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
//				System.out.println("    -> " + clazz);
//			} catch (ClassNotFoundException e) {
//				e.printStackTrace();
//			}
//		}
		
		return resultClassNames;
	}
	
	/**
	 * @param file
	 * @return
	 */
	private static boolean isJarFile(File file) {
		return file.isFile() && file.getName().endsWith(".jar");
	}

	private static Vector<String> getClassURLsFromDir(File dir, File baseDir){
		Vector<String> result = new Vector<String>();
		File[] files = dir.listFiles();
		for (File file : files) {
			if ( file.isFile() && file.getAbsoluteFile().toString().endsWith(".class") ){
				try {
					result.add(format(file.getAbsolutePath(), baseDir.toURI().toURL()));
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}else if (file.isDirectory()){
				result.addAll(getClassURLsFromDir(file, baseDir));
			}
		}
		return result;
	}
	
	
	
	
	/**
	 * 
	 */
	private static String format(String classUri, URL baseDir) {
		String result = classUri.substring(baseDir.getFile().length(), classUri.length()-".class".length()).
							replaceAll(File.separator, ".");
		return result;
	}
	
	private static Vector<String> extractJarFile(File file){
		
		File extractedJarPath = new File(tempDir.getAbsolutePath() + File.separator + UUID.randomUUID());
		System.out.println("extracting " + file + " to " + extractedJarPath);
		extractedJarPath.mkdir();
		Vector<String> result = new Vector<String>();
		try {
			JarFile jarFile = new java.util.jar.JarFile(file);
			
			Enumeration<JarEntry> enumeration = jarFile.entries();
			
			enumeration = jarFile.entries();
			while (enumeration.hasMoreElements()) {
			    JarEntry entry = enumeration.nextElement();
			    File outFile = new java.io.File(extractedJarPath + File.separator + entry.getName());
			    if (entry.isDirectory()) {
			    	outFile.mkdirs();
			        continue;
			    }else{
			    	outFile.getParentFile().mkdirs();
			    }
			    
			    InputStream is = jarFile.getInputStream(entry);
			    FileOutputStream fos = new FileOutputStream(outFile);
			    while (is.available() > 0) {  
			        fos.write(is.read());
			    }
			    fos.close();
			    is.close();
			    
			    if (isJarFile(outFile)){
			    	result.addAll(extractJarFile(outFile));
			    }
			}
			result.add(extractedJarPath.getAbsolutePath());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
		
	}
}
