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
package de.uniko.west.winter.core.serializing;

import java.net.URI;

import de.uniko.west.winter.core.interfaces.WinterSerializer;

/**
 * @author Stefan Scheglmann, Frederik Jochum
 *
 */
public class UriSerializer implements WinterSerializer{

	@Override
	public String serialize(Object object) {
		return object.toString();
	}

	@Override
	public Object deserialize(String serObject) {
		
		return URI.create(serObject);
	}

	@Override
	public boolean canSerialize(Class<?> clazz) {
		return clazz == URI.class;
	}

}
