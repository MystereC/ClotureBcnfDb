package db_exo1;

import java.util.Set;

/**
 * Log is used to display messages and results in the console. 
 *
 */
public class Log implements CoreLogger{

	public final static String LINE_START = "$ " ;
	
	private void print(String message) {
		System.out.print(LINE_START + message);
	}
	
	private void println(String message) {
		System.out.println(LINE_START + message);
	}
	
	@Override
	public void printNewLine() {
		println("");
		print("");
	}
	
	@Override
	public void printMessage(String message) {
		println(message);
	}

	@Override
	public void printAttributeAdded(Attribute a) {
		println("Attribute " + a.getName() + " added");
	}

	@Override
	public void printAttributeAdded(Set<Attribute> a) {
		a.stream().forEach(this::printAttributeAdded);
	}
	
	@Override
	public void printFunctionDependencyAdded(FunctionalDependency fd) {
		println("FD " + fd + " added");
		
	}

	@Override
	public void printAll(Set<Attribute> attributes, Set<FunctionalDependency> functionalDependencies) {
		println("--- Attributes ---");
		attributes.stream().map(a -> a.toString()).forEach(this::println);
		println("---");
		println("");
		println("--- Functional Dependencies ---");
		functionalDependencies.stream().map(fd -> fd.toString()).forEach(this::println);
		println("---");
	}

	@Override
	public void printComputation(Set<Attribute> attributes) {
		println("--- Computed Attributes ---");
		attributes.stream().map(a -> a.toString()).forEach(this::println);
		println("---");
	}

	@Override
	public void help() {
		println("----- Help -----");
		println("--- Commands ---");
		println(" !exit | Exit the program.");
		println(" !help | Display the help.");
		println(" !file:test.txt| Load and execute each line of the given file.");
		println(" !print | Display the attributes and functional dependencies sets.");
		println(" !clear | Clear the memory and drop any existent attribute or functional dependency.");
		println(" !clear:attributes | Clear the memory and drop any existent attribute.");
		println(" !clear:fd| Clear the memory and drop any existent functional dependency.");
		println(" !compute | Compute the closure of the functional dependencies set using the attributes set as X+.");
		println(" !fd:AB->CD | create a functional dependency A,B -> C,D.");
		println(" !attributes:ABC | create the attributes with the given names, here 3 attributes A, B , C and add them to X+.");
		
	}
	
	@Override
	public void error(String message) {
		println("Error : " + message);
	}
}
