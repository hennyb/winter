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
package de.uniko.west.winter.infostructure;

import java.util.List;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniko.west.winter.core.serializing.Binding;

/**
 * @author Stefan Scheglmann, Frederik Jochum
 *
 */
public abstract class InfoNode {
	protected static transient Logger logger = LoggerFactory.getLogger(InfoNode.class.getSimpleName());
	
	protected List<InnerNode> parents =  new Vector<InnerNode>();
	protected List<InfoNode> children = new Vector<InfoNode>();

	public List<InfoNode> getChildren(){
		return children;
	}
	
	public void addChild(InfoNode child){
		children.add(child);
	}
	
	public List<InnerNode> getParents(){
		return parents;
	}
	
	public void addParent(InnerNode parent){
		if (parent == null){
			return;
		}
		parents.add(parent);
	}
	
	public void addAllParents(List<InnerNode> parents){
		parents.addAll(parents);
	}
	
	
	String bindedQueryPattern = null;
	protected String query = "";
	
	public String getQueryPattern(){
		return query;
	}
	
	public abstract void inflate();

	public abstract Binding getBindingForVar(String var);
	
	public Object acceptVisitor(InfoStructureVisitor visitor, Object data){
		return visitor.visit(this, data);
	}
}
