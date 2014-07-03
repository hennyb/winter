package de.uniko.west.winter.core;

import java.util.Set;

public interface QueryResult {
	
	public boolean isBooleanResult();
	public boolean wasSuccessful();

	public Set<String> getBindingForVar(String var);
	public Set<String> getVars();
}
