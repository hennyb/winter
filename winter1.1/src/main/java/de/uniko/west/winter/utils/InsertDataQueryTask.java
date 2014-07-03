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

import java.net.URI;
import java.util.Map;

import de.uniko.west.winter.infostructure.InfoNode;
import de.uniko.west.winter.utils.interfaces.QueryResultListener;
import de.uniko.west.winter.utils.interfaces.RepositoryConnection;
import de.uniko.west.winter.utils.visitors.PatternReductionVisitor;
import de.uniko.west.winter.utils.visitors.VarSubstitutionVisitor;

/**
 * @author Stefan Scheglmann, Frederik Jochum
 *
 */
public class InsertDataQueryTask extends DefaultQueryTask {
	public InsertDataQueryTask(InfoNode node, URI graph, Map<String, URI> prefixMap, QueryTaskExecutor executor, String ... var) {
		super(node, graph, prefixMap, executor, var);
		
	}
	
	protected void insertTask() {
		
		VarSubstitutionVisitor vsVisitor = new VarSubstitutionVisitor(node);
		PatternReductionVisitor prVisitor = new PatternReductionVisitor();
		visitQueryPattern(vsVisitor, prVisitor);
		
		insertGraph();

		sb.insert(0, "INSERT DATA {\n");
		sb.append("}\n");
		
	}
	
	@Override
	public void execute(RepositoryConnection<?> repCon, QueryResultListener listener) {
		listener.finished(
				repCon.evaluateQuery(
						this));
	}

	
}
