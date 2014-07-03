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
import java.net.URL;

import de.uniko.west.winter.exceptions.WinterException;

/**
 * @author Stefan Scheglmann, Frederik Jochum
 *
 */
public class PreprocessTransfer implements WinterConfigurable {

	private static final String PREPROCESS_CONFIG_FILE = "/config/winter_preprocess_transfer_config.xml";
	
	public static final String KEY_WINTER_CLASSES = "winter.preprocess.classnames";
	public static final String KEY_WINTER_ID_PREFIX = "winter.preprocess.classid.";
	
	private static final String KEY_TRANSFER_FILE = "transferFile";
	
	private static File transferFile = null;
	/* (non-Javadoc)
	 * @see de.uniko.west.winter.utils.WinterConfigurable#getConfigurationFileURI()
	 */
	public URL getConfigurationFileURL() {
		return PreprocessTransfer.class.getResource(PREPROCESS_CONFIG_FILE);
	}
	
	public static File createNewTransferFile(){
		
		setTransferFileRaw();

		if (transferFile.exists()){
			if (!transferFile.delete()){
				throw new WinterException("Could not overwrite transfer file.");
			}
		}

		try {
			transferFile.createNewFile();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return transferFile;
	}
	
	public static WinterConfiguration loadTransferFile(){
		setTransferFileRaw();
		return WinterConfiguration.loadConfiguration(transferFile);
	}

	private static void setTransferFileRaw(){
		if (transferFile!= null){
			return;
		}
		WinterConfiguration transferConfig = WinterConfiguration.loadClassConfiguration(new PreprocessTransfer());
		String transferFileName = transferConfig.getProperty(KEY_TRANSFER_FILE);
		transferFile = new File(WinterConstants.getActualWinterUserDir(), transferFileName);
	}
}
