package db_exo1;

import java.util.Set;

/**
 * CoreLogger is used to display messages and results.
 *
 */
public interface CoreLogger {
	
	public void printMessage(String message);
	public void printAttributeAdded(Attribute a);
	public void printAttributeAdded(Set<Attribute> a);
	public void printFunctionDependencyAdded(FunctionalDependency fd);
	public void printAll(Set<Attribute> attributes, Set<FunctionalDependency> functionalDependencies);
	public void printComputation(Set<Attribute> attributes);
	public void printNewLine();
	public void help();
	public void error(String message);
	
}
