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

package de.uniko.west.winter.utils.repcon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.update.UpdateAction;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateRequest;

import de.uniko.west.winter.core.JenaQueryResult;
import de.uniko.west.winter.core.QueryResult;
import de.uniko.west.winter.utils.DefaultQueryTask;
import de.uniko.west.winter.utils.DeleteDataQueryTask;
import de.uniko.west.winter.utils.InsertDataQueryTask;
import de.uniko.west.winter.utils.SelectQueryTask;
import de.uniko.west.winter.utils.interfaces.RepositoryConnection;

public class JenaRepositoryConnection implements RepositoryConnection<Model> {

	protected static transient Logger logger = LoggerFactory.getLogger(JenaRepositoryConnection.class.toString());
	
	private Model repConModel;
	
	public JenaRepositoryConnection(Model repConModel) {
		this.repConModel = repConModel;
	}
	
	@Override
	public Model getRepository() {
		return repConModel;
	}
	
	public void evaluateUpdateRequest(String updateString){
		UpdateRequest update = UpdateFactory.create(updateString);
		executeUpdateRequest(update);
	}
	

	public QueryResult evaluateQuery(String queryString) {
		ResultSet resultSet = null;
		Query query = QueryFactory.create(queryString);
		switch (query.getQueryType()) {
		case 111:
			logger.info(":evaluateQuery executing SELECT query {}", query.toString());
			resultSet = executeSelectQuery(query);
			break;
		case 444:
			logger.info(":evaluateQuery executing ASK query");
			resultSet = executeAskQuery(query);
		}
		QueryResult qr;
		if (resultSet == null){
			qr = new JenaQueryResult(false);
		}else{
			qr = new JenaQueryResult(resultSet);
		}
		return qr;
	}
	
	public void executeUpdateRequest(UpdateRequest update){
//		System.out.println(repConModel);
		UpdateAction.execute(update, repConModel);
		
	}
	
	private ResultSet executeSelectQuery(Query query){
		logger.info(":executeSelectQuery query");
		QueryExecution queryExec = QueryExecutionFactory.create(query, repConModel);
		ResultSet result = queryExec.execSelect();
		return result;
	}

	public ResultSet executeAskQuery(String queryString){
		Query query = QueryFactory.create(queryString);
		return executeAskQuery(query);
	}
	
	private ResultSet executeAskQuery(Query query){
		QueryExecution queryExec = QueryExecutionFactory.create(query, repConModel);
		ResultSet results = queryExec.execSelect();
		return results;
	}	
	
	@Override
	public void connectRepository(Model repCon) {
		this.repConModel = (Model)repCon;	
	}

	@Override
	public void setRepository(Model repCon) {
		this.repConModel = (Model)repCon;
		
	}

	/* (non-Javadoc)
	 * @see de.uniko.west.winter.utils.interfaces.RepositoryConnection#evaluateQuery(de.uniko.west.winter.utils.DefaultQueryTask)
	 */
	@Override
	public QueryResult evaluateQuery(DefaultQueryTask queryTask) {
		try {
			UpdateRequest update = UpdateFactory.create(queryTask.getQuery());
			UpdateAction.execute(update, repConModel);
			return new JenaQueryResult(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new JenaQueryResult(false);
	}

	/* (non-Javadoc)
	 * @see de.uniko.west.winter.utils.interfaces.RepositoryConnection#evaluateQuery(de.uniko.west.winter.utils.InsertDataQueryTask)
	 */
	@Override
	public QueryResult evaluateQuery(InsertDataQueryTask queryTask) {
		return evaluateQuery((DefaultQueryTask)queryTask);
	}

	/* (non-Javadoc)
	 * @see de.uniko.west.winter.utils.interfaces.RepositoryConnection#evaluateQuery(de.uniko.west.winter.utils.SelectQueryTask)
	 */
	@Override
	public QueryResult evaluateQuery(SelectQueryTask queryTask) {
		logger.info(":evaluate:SelectQueryTask {}", queryTask.getQuery());
		Query query = QueryFactory.create(queryTask.getQuery());
		ResultSet rs = executeSelectQuery(query);
		return new JenaQueryResult( rs );
	}

	/* (non-Javadoc)
	 * @see de.uniko.west.winter.utils.interfaces.RepositoryConnection#evaluateQuery(de.uniko.west.winter.utils.DeleteDataQueryTask)
	 */
	@Override
	public QueryResult evaluateQuery(DeleteDataQueryTask queryTask) {
		return evaluateQuery((DefaultQueryTask)queryTask);
	}

}
