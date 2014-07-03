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

import java.net.URL;

/**
 * Used to configure certain classes.
 * Uses java.util.Properties to store key-value properties to given files.
 * File url is relative to buildpath.
 * To use a config file from resources/config folder, the return value of 
 * should look like: <<YOUR_CLASS>>.class.getResource("/config/<<YOUR_CONFIG_FILENAME>>.xml");
 * 
 * note that all config files are also stored in user-directory, so filenames must be unique!
 * 
 * @author Stefan Scheglmann, Frederik Jochum
 *
 */
public interface WinterConfigurable {

	public URL getConfigurationFileURL();
	
}
