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
package de.uniko.west.winter.core.interfaces;

import java.util.Collection;

/**
 * 
 * Interface providing access to core features of winter.
 * 
 * 
 * @author Stefan Scheglmann, Frederik Jochum
 *
 */
public interface Winter {
//	works with sharedInstance
	
	/**
	 * @param object
	 */
	public void add(RDFSerializable object);
	/**
	 * @param objects
	 */
	public void add(Collection<RDFSerializable> objects);
	/**
	 * @param object
	 */
	public void deleteRek(RDFSerializable object);

	/**
	 * Registers customized serializers in winter during runtime. <br>
	 * Should be called right after initialization and before adding related objects.
	 * 
	 * @param serializer object implementing <code>de.uniko.west.winter.core.interfaces.Winterserializer</code> 
	 * @param fieldName related field name
	 * @see <code>de.uniko.west.winter.core.interfaces.Winterserializer</code>
	 */
	public void registerSerializer(WinterSerializer serializer, String fieldName);

	
//	operations on mapped objects	
	/**
	 * @param fieldName
	 */
	public void readField(String fieldName);
	/**
	 * 
	 */
	public void delete();
	/**
	 * @param fieldName
	 */
	public void deleteField(String fieldName);
	/**
	 * 
	 */
	public void write();
	/**
	 * @param fieldName
	 */
	public void writeField(String fieldName);
	
}
