package db_exo1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.PatternSyntaxException;

/**
 * Core is the core of the program.
 * It contains all computation methods and sets of data.
 *
 */
public class Core implements Runnable{
	
	/**
	 * The Attributes of the starting set.
	 * It corresponds to X+.
	 */
	private Set<Attribute> attributes;
	
	/**
	 * All functional dependencies added by the user.
	 */
	private Set<FunctionalDependency> functionalDependencies;
	
	/**
	 * Boolean flag to indicate if the program has to stop.
	 */
	private boolean stop = false;
	
	/**
	 * Scanner used to retrieve user's input.
	 */
	private final Scanner scanner;
	
	/**
	 * Logger used to display the result in the console.
	 */
	private final static CoreLogger logger = new Log();
	
	/**
	 * Pattern used to match a valid command without argument.
	 */
	private final static String PATTERN_NO_ARGS = "^!\\w+$";
	
	/**
	 * Pattern used to match a valid command with argument.
	 */
	private final static String PATTERN_ARGS = "^!\\w+:.+$";
	
	/**
	 * Pattern used to split an input.
	 */
	private final static String PATTERN_SPLIT = "[!:]";	
	
	public Core(Scanner scanner) {
		this.attributes = new TreeSet<>();
		this.functionalDependencies = new TreeSet<>();
		this.scanner = scanner;
	}
	
	public Core() {
		this(new Scanner(System.in));
	}
	
	public Core(File file) throws FileNotFoundException {
		this(new Scanner(file));
		logger.printMessage("File loaded");
	}
	
	/**
	 * Set the core status.
	 * @param stop True if the program has to stop.
	 * @return the core status.
	 */
	private boolean setStop(boolean stop) {
		this.stop = stop;
		return this.stop;
	}
	
	/**
	 * Run the core.
	 */
	public void run() {
		while(!stop) {
			logger.printNewLine();
			parseInput(requestInput());			
		}
	}
	
	/**
	 * Get the next user's input.
	 * @return the user's input.
	 */
	public String requestInput() {
		try {
			return scanner.next();
		} catch (NoSuchElementException e) {
			this.setStop(true);
			return "";
		}
	}
	
	/**
	 * Match the input with a valid pattern and then process the command.
	 * @param input User's input to be parsed
	 * @return True if the command is valid.
	 */
	public boolean parseInput(String input) {
		logger.printMessage(input);
		String [] words = input.split(PATTERN_SPLIT, 3);
		
		if(input.matches(PATTERN_ARGS)) {
			switch(words[1]) {
				case "attributes": 	applyAndLog(Attribute::parse, this.attributes::addAll, words[2], logger::printAttributeAdded); 									break;
				case "fd": 			applyAndLog(FunctionalDependency::parse, this.functionalDependencies::add, words[2], logger::printFunctionDependencyAdded); 	break;
				case "file":		loadFile(words[2]); 				break;
				case "clear": 		clear(words); 	break;
				default: 	logger.error("Unknown command. Type !help to show available commands");
							return false;
			}
		}
		
		if(input.matches(PATTERN_NO_ARGS)) {
			switch(words[1]) {
				case "compute": compute(); 		break;
				case "quit":
				case "exit": 	setStop(true); 	break;
				case "print": 	printAll(); 	break;
				case "clear": 	clear(); 	break;
				case "help":	help(); 		break;
				default: 	logger.error("Unknown command. Type !help to show available commands");
							return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Clear the memory and remove any existent attribute or functionalDependency.
	 * This will re-analyze the user's input to determine what to remove. 
	 * @param input The user's input to parse.
	 */
	private void clear(String[] input) {
		
		List<Set<?>> sets = new ArrayList<>();
		switch(input[2]) {
			case "attributes": sets.add(this.attributes); break;
			case "fd": sets.add(this.functionalDependencies); break;
			default : logger.error("Unknown paramater");
		}
		this.clear(sets);
	}
	
	/**
	 * Clear the memory and remove any existent attribute or functionalDependency.
	 */
	private void clear() {
		List<Set<?>> sets = new ArrayList<>();
		sets.add(this.attributes);
		sets.add(this.functionalDependencies);
		this.clear(sets);
	}
	
	/**
	 * Clear the memory and remove any collection given in parameter.
	 */
	private <C extends Collection<?>> void clear(List<C> collections) {
		collections.stream().forEach(c -> c.clear());
	}

	/**
	 * Parse the user's input to create one element or more, add it to a collection and notify the logger.
	 * @param parse The function to parse the user's input and to create one element or more.
	 * @param add The consumer to add the result of the parse function to the collection.
	 * @param input The user's input to parse.
	 * @param log The method used to notify the logger.
	 */
	private <E> void applyAndLog(Function<String, E> parse, Consumer<E> add, String input, Consumer<E> log) {
		try {
			E inputParsed = parse.apply(input);
			add.accept(inputParsed);
			log.accept(inputParsed);
		} catch (PatternSyntaxException e) {
			logger.error(e.getDescription());
		}
	}
	
	/**
	 * Display both attributes and functionalDependencies sets.
	 */
	private void printAll() {
		logger.printAll(this.attributes, this.functionalDependencies);
	}
	
	/**
	 * Compute the closure of the functionalDependencies.
	 * Once done, the three sets will be displayed.
	 */
	private void compute() {
		logger.printAll(this.attributes, this.functionalDependencies);
		Set<Attribute> computedAttributesSet = this.getComputedAttributes();
		logger.printComputation(computedAttributesSet);
	}
	
	/**
	 * Compute the closure of the functionalDependencies set.
	 * The attributes set will be used as X+.
	 * A predicate is used to verify if there is still a FD to compute.
	 * We check if all the attributes of the FD's left side are in X.
	 * If so, we check if at least one attribute of the FD's right side is Not in X.
	 * Then we will process the whole functionalDependencies set and so on, while at least one FD verify the predicate.
	 * @return a the closure of the functionalDependencies set with the computed attributes.
	 */
	private Set<Attribute> getComputedAttributes() {
		Set<Attribute> computedAttributesSet = new TreeSet<>();
		computedAttributesSet.addAll(this.attributes);
		Predicate<? super FunctionalDependency> p = (fd -> 
															computedAttributesSet.containsAll(fd.getLeft()) 
															&& !computedAttributesSet.containsAll(fd.getRight())
													);
		while(this.functionalDependencies.stream().anyMatch(p)) {
			this.functionalDependencies.stream()
												.filter(p)
												.peek(logger::printFunctionDependencyAdded)
												.forEach(fd -> computedAttributesSet.addAll(fd.getRight()));
		}
		return computedAttributesSet;
	}
	
	/**
	 * Display the help command.
	 */
	private void help() {
		logger.help();
	}
	
	/**
	 * Load a file execute each line as a command.
	 * @param file The file to be read.
	 */
	private void loadFile(String file) {
		File f = new File(file);
		try {
			new Thread(new Core(f)).start();
		} catch (FileNotFoundException e) {
			logger.error("File not found");
		}
	}

}