package de.uniko.west.winter.core;

import de.uniko.west.winter.utils.interfaces.QueryResultListener;

public class DefaultQueryResultListenerImpl implements QueryResultListener{
	
	@Override
	public void finished(QueryResult queryResult) {
		System.out.println(" -- DEFAULT QUERYRESULT OUTPUT -- ");
		
		if (queryResult.isBooleanResult()){
			System.out.println("  " + queryResult.wasSuccessful());
			return;
		}
		
		for (String var : queryResult.getVars()) {
			System.out.println(" VAR: " + var );
			for (String binding : queryResult.getBindingForVar(var)) {
				System.out.println("     -> " + binding);
			}
		}
		
	}
}
