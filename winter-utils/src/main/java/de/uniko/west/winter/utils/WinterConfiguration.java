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
package de.uniko.west.winter.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import de.uniko.west.winter.exceptions.WinterException;

/**
 * Basically same as java.util.Properties.
 * Can also handle list (seperator: ';')
 * has build-in save and load. 
 * 
 * 
 * @author Stefan Scheglmann, Frederik Jochum
 *
 */
public class WinterConfiguration extends Properties{
	
	public static final String LIST_PROPERTY_SEPERATOR = ";";
	
	private static final long serialVersionUID = -3087380381275619735L;
	
	public static WinterConfiguration loadClassConfiguration(WinterConfigurable configurable){
		File configFile = new File(configurable.getConfigurationFileURL().getPath());
		if (!configFile.exists()){
			throw new WinterException("Could not find configuration file: " + configurable.getConfigurationFileURL());
		}
				
		WinterConfiguration result = loadConfiguration(configFile);
		
		try {
			createOrOverwriteConfigFromUserDir(result, configFile.getName());
		}catch (Exception e) {
			// the userconfig stuff failed... doesn't hurt too much
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	private static void createOrOverwriteConfigFromUserDir(WinterConfiguration config, String fileName){
		File winterUserDir = WinterConstants.getActualWinterUserDir();
		File userConfig = new File(winterUserDir.getAbsolutePath() + File.separator + fileName);
		if ( userConfig.exists() ){
//			winter was already launched before & user may have changed config -> overwrite
			overwriteConfiguration(config, userConfig);
		}else{
//			first winter launch -> create a user config
			storeConfiguration(config, userConfig);
		}
	}
	
	public static WinterConfiguration loadConfiguration(File xmlFile){
		return overwriteConfiguration(null, xmlFile);
	}
	
	public static WinterConfiguration overwriteConfiguration(WinterConfiguration config, File xmlFile){
		if (config == null){
			config = new WinterConfiguration();
		}
		FileInputStream in = null;
		try {
			in = new FileInputStream(xmlFile);
			config.loadFromXML(in);
		} catch (Exception e) {
			e.printStackTrace();
			throw new WinterException("Could not load configuration file: " + xmlFile, e);
		} finally{
			WinterIO.safeClose(in);
		}
		return config;
	}
	
	public static void storeConfiguration(WinterConfiguration config, File xmlFile){
		FileOutputStream out = null;
		try {
			if (!xmlFile.exists()){
				xmlFile.createNewFile();	
			}
			out = new FileOutputStream(xmlFile); 
			config.storeToXML(out, "winter configuration file");

		} catch (Exception e) {
			e.printStackTrace();
			throw new WinterException("Could not store user configuration file: " + xmlFile, e);
		} finally{
			WinterIO.safeClose(out);
		}
	}
	
	public List<String> getListProperty(String key){
		return Arrays.asList( getProperty(key).split(LIST_PROPERTY_SEPERATOR) );
	}
	
	public void setListProperty(String key, String[] list){
		if (list==null || key == null){
			return;
		}
		String value = "";
		for (int i = 0; i < list.length; i++) {
			value+= LIST_PROPERTY_SEPERATOR + list[i];
		}
		value = value.substring(LIST_PROPERTY_SEPERATOR.length());
		setProperty(key, value);
	}
	


	
}
