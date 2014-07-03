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
package de.uniko.west.winter.test.infostructure;

import java.util.Arrays;
import java.util.Vector;

import de.uniko.west.winter.annotation.winter_query;
import de.uniko.west.winter.annotation.winter_ref;
import de.uniko.west.winter.annotation.winter_var;
import de.uniko.west.winter.core.interfaces.RDFSerializable;
import de.uniko.west.winter.core.interfaces.Winter;



/**
 * @author Stefan Scheglmann, Frederik Jochum
 *
 */
@winter_query("?obj1 test:pred1 ?obj2, ?refObj1. " +
			  "?obj2 test:pred1 ?obj1;" +
			  "test:pred2 ?obj3." +
			  "?refObj2 test:pred3 ?obj1." +
			  "?obj3 test:pred3 ?obj1.")
public class RelationObject implements RDFSerializable{
	
	Winter winter = null;
	
	@winter_var("obj1")
	SmallObject obj1 = null;

	@winter_var("obj2")
	Vector<SmallObject> obj2 = null;
	
	@winter_var("obj3")
	LargeObject obj3 = null;
	
	@winter_ref({"refObj1", "refObj2"})
	SubrelationObject subrel = null;
	
	public RelationObject(LargeObject object){
		obj1 = new SmallObject();
		obj2 = new Vector<SmallObject>(Arrays.asList(new SmallObject(), new SmallObject()));
		obj3 = object;
		if(object == null) obj3 = new LargeObject("RelObjObj3");
		subrel = new SubrelationObject(null);
	}

	/* (non-Javadoc)
	 * @see de.west.winter.core.interfaces.RdfSerialisable#winter()
	 */
	@Override
	public Winter winter() {
		return winter;
	}

	/* (non-Javadoc)
	 * @see de.west.winter.core.interfaces.RdfSerialisable#setWinter(de.west.winter.core.Winter)
	 */
	@Override
	public void setWinter(Winter winter) {
		this.winter = winter;
	}
	
}
