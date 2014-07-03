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
package de.uniko.west.winter.infostructure;

import de.uniko.west.winter.core.serializing.Binding;

// TODO useless class... to be removed on next infostructure refactoring
/**
 * @author Stefan Scheglmann, Frederik Jochum
 *
 */
@Deprecated
public class LeafNode extends InfoNode {
	Object value;
		
	public LeafNode(FieldNode parent, Object object){
		this.value = object;
		parents.add(parent);
	}
	
	/* (non-Javadoc)
	 * @see de.west.winter.core.InfoNode#getBindingForVar(java.lang.String)
	 */
	@Override
	public Binding getBindingForVar(String Var) {
		return null;
	}

	/* (non-Javadoc)
	 * @see de.west.winter.infostructure.InfoNode#inflate()
	 */
	@Override
	public void inflate() {
		// TODO Auto-generated method stub
		
	}
	
	public Object acceptVisitor(InfoStructureVisitor visitor, Object data){
		return visitor.visit(this, data);
	}
	
	@Override
	public String toString() {
		return "LeafNode[value: " + value.toString() + "]";
	}
}
