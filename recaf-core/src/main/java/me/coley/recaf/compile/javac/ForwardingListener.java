package me.coley.recaf.compile.javac;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * Diagnostic listener that forwards reports to a delegate listener.
 *
 * @author Matt Coley
 */
public class ForwardingListener implements JavacListener {
	private final JavacListener delegate;

	ForwardingListener(JavacListener delegate) {
		this.delegate = delegate;
	}

	@Override
	public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
		if (delegate != null)
		{
			delegate.report(diagnostic);
		}
	}
}
