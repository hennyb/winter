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

import java.lang.reflect.Constructor;

import de.uniko.west.winter.core.WinterImpl;
import de.uniko.west.winter.core.interfaces.RDFSerializable;
import de.uniko.west.winter.core.interfaces.Winter;
import de.uniko.west.winter.core.interfaces.WinterSerializer;
import de.uniko.west.winter.exceptions.WinterException;

/**
 * Used as default serializer for classes(implementing RDFSerializable) appearing as fields.
 * Or can be used for extending.
 * 
 * 
 * @author Stefan Scheglmann, Frederik Jochum
 *
 */
public class DefaultClassSerializer implements WinterSerializer{

	private Class<?> initClazz;
	
	public DefaultClassSerializer(Class<?> clazz) {
		initClazz = clazz;
		if (RDFSerializable.class.isAssignableFrom(clazz)){
			try {
				if (clazz.getConstructor(new Class[]{}) == null){
					throw new WinterException("Class "+ clazz+ " does not provide a default constructor.");
				}
			} catch (Exception e) {
				throw new WinterException("Class "+ clazz+ " does not provide a default constructor.", e);
			}
				
		}
	}
	
	@Override
	public String serialize(Object object) {
		if ( !canSerialize(object.getClass()) ){
			return null;
		}
		Winter w = ((RDFSerializable)object).winter();
		return (String) ((WinterImpl)w).getObjectNode().getID().getValue();
	}

	@Override
	public Object deserialize(String serObject) {
		try {
			Constructor<?> constr = initClazz.getConstructor(new Class[]{});
			//not added to Winter here... TODO check if already in winter somewhere, else 
			//add with 'WinterImpl.getSharedInstance().add( xxx );' 
			
			return constr.newInstance(new Object[]{});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public boolean canSerialize(Class<?> clazz) {
		return clazz == initClazz;
	}
	
}
