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

/**
 * @author Stefan Scheglmann, Frederik Jochum
 *
 */
public class WinterConstants {

	private static final String USERHOME_WILDCARD = "%USERHOME%";

	public static final String WINTER_USER_DIR = USERHOME_WILDCARD + File.separator + ".winter";
	
	public static File getActualWinterUserDir(){
		String userHome = System.getProperty("user.home");
		String winterHome = WINTER_USER_DIR.replaceAll(USERHOME_WILDCARD, userHome);
		File result = new File(winterHome);
		if (result.exists() && result.isDirectory()){
			return result;
		}else {
			result.mkdir();
			return result;
		}
	}
}
