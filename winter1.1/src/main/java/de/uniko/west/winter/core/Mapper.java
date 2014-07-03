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
package de.uniko.west.winter.core;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniko.west.winter.core.interfaces.RDFSerializable;
import de.uniko.west.winter.infostructure.ObjectNode;
import de.uniko.west.winter.utils.ClassReflection;

/**
 * @author Stefan Scheglmann, Frederik Jochum
 *
 */
public class Mapper {
	
	protected static transient Logger logger = LoggerFactory.getLogger(Mapper.class.getSimpleName());
	
	public static void init(boolean includeSuperClassFields) {
		ClassReflection.includeSuperClassFields = includeSuperClassFields;
	}
	
	static Vector<ObjectNode> generateNodeStructure(Collection<RDFSerializable> rdfSerializables){
		logger.info("generateNodeStructure called for {} target(s)", rdfSerializables.size());
		
		Set<RDFSerializable> targets = ClassReflection.createObjectSet(new HashSet<RDFSerializable>(rdfSerializables));	
		
		logger.info("generateNodeStructure total {} target(s) & subclass(es)", targets.size());
		
		Vector<ObjectNode> result = new Vector<ObjectNode>();
		
		for (RDFSerializable target : targets) {
			if (target.winter() == null){
				logger.info("generating InfoNode... target: {}", target.getClass().getSimpleName());
				ObjectNode node = new ObjectNode(target);
				
				result.add(node);
			}
		}
		for (ObjectNode node : result) {
			node.inflate();
		}
		return result;
	}
	
}
