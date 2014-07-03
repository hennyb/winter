package net.java.rdf.util;

public class FieldNotSetException extends Exception {
    
	/**
     * Constructs a new exception with <code>null</code> as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
	public FieldNotSetException() {
		// TODO Auto-generated constructor stub
	}
		
    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param   message   the detail message. The detail message is saved for
     *          later retrieval by the {@link #getMessage()} method.
     */
	public FieldNotSetException(String s){
		super(s);
	}

}
