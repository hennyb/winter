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
 * 
 */
package de.uniko.west.winter.preprocess;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.Pattern;

import de.uniko.west.winter.annotation.winter_id;
import de.uniko.west.winter.core.interfaces.RDFSerializable;
import de.uniko.west.winter.utils.ClassReflection;
import de.uniko.west.winter.utils.PreprocessTransfer;
import de.uniko.west.winter.utils.WinterConfigurable;
import de.uniko.west.winter.utils.WinterConfiguration;
import de.uniko.west.winter.utils.WinterIO;

/**
 * @author Stefan Scheglmann, Frederik Jochum
 *
 */
public class WinterPreprocess implements WinterConfigurable{


	//config keys
	private static final String KEY_WHITELIST = "winter.preprocess.whitelist";
	
	private static final String CLASS_SEPERATOR = ";";
	private static final String PROPS_FILE_COMMENTS = "Autogenerated winter preprocess file. Do not modify!";
	
	private Vector<String> classNameWhiteList = new Vector<String>();
	private Properties classStats;
	private WinterConfiguration config;
	private File outputFile = null;
	
	public static void main(String[] args) {
		File outputFile = PreprocessTransfer.createNewTransferFile();
		
		try {
			WinterPreprocess wpp = initPreprocess(outputFile);
			wpp.preprocess();	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static WinterPreprocess initPreprocess(File outputFile) throws IOException{
		WinterPreprocess wpp = new WinterPreprocess();
		
		if (!outputFile.exists()){
			outputFile.createNewFile();
		}
		wpp.outputFile = outputFile;
		
		wpp.setConfig(WinterConfiguration.loadClassConfiguration(wpp));
		
		List<String> storedWhiteList = wpp.getConfig().getListProperty(KEY_WHITELIST);
		for (String item : storedWhiteList) {
			if (item.trim().equals("")){
				continue;
			}
			wpp.classNameWhiteList.add(item);	
		}
		
		return wpp;
	}
		
	
	public void addWhiteListRegex(String regex){
		classNameWhiteList.add(regex);
	}

	public void preprocess() throws IOException{
		classStats = new Properties();
		
//		..........
		for (String filteritem : classNameWhiteList) {
			System.out.println("FILTER: " + filteritem);
		}
		
//		..........
		
		
		Vector<String> classNames = ClassPathAnalysis.getClassNamesFromClassPath();
		System.out.println("NUM CLASSES BEFORE FILTER: " + classNames.size());
		classNames = filterClassNames(classNames);
		System.out.println("NUM CLASSES AFTER FILTER: " + classNames.size());
		
		String rdfSerializableClassNames = "";
		for (String className : classNames) {
			try {
		
				System.out.println("Loading class: " + className);
				Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
				if (RDFSerializable.class.isAssignableFrom(clazz)){
					rdfSerializableClassNames+= CLASS_SEPERATOR + className;
				}
				
				writeType(clazz, className);
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				continue;
			}
		}
		rdfSerializableClassNames = rdfSerializableClassNames.length()>0?
				rdfSerializableClassNames.substring(1):
					"";
		
		classStats.put(PreprocessTransfer.KEY_WINTER_CLASSES, rdfSerializableClassNames);
		FileOutputStream outStream = new FileOutputStream(outputFile);
		classStats.storeToXML(outStream, PROPS_FILE_COMMENTS);
		
		WinterIO.safeClose(outStream);
	}

	
	public WinterConfiguration getConfig() {
		return config;
	}


	public void setConfig(WinterConfiguration config) {
		this.config = config;
	}


	/**
	 * @param clazz
	 */
	private void writeType(Class<?> clazz, String className) {
		Vector<Field> fields = ClassReflection.getFields(clazz);
		for (Field field : fields) {
			Vector<Annotation> annotations = ClassReflection.getWinterAnnotations(field);
			for (Annotation annotation : annotations) {
				if (annotation instanceof winter_id){
					String idVal = ((winter_id) annotation).value();
					if (idVal != null && idVal != ""){
						classStats.put(PreprocessTransfer.KEY_WINTER_ID_PREFIX+className, idVal);
					}
				}
			}
		}
	}

	public URL getConfigurationFileURL() {
		return WinterPreprocess.class.getResource("/config/winter_preprocess_config.xml");
	}
	
	private Vector<String> filterClassNames(Vector<String> classNames){
		Vector<String> result = new Vector<String>(classNames);
		for (String className : classNames) {
			boolean isWhitelisted = false;
			for (String filterString : classNameWhiteList) {
//				System.out.println("FILTERING: " + className + "   - FILTER: " + filterString);
				if (className.matches(filterString.trim())){
					isWhitelisted = true;
				}
			}
			if (!isWhitelisted){
				result.remove(className);
			}
		}
		
		return result;
	}
	
}
