package db_exo1;

import java.util.Set;
import java.util.TreeSet;
import java.util.regex.PatternSyntaxException;

/**
 * Attribute represents an attribute simply with his name.
 *
 */
public class Attribute implements Comparable<Attribute>{

	/**
	 * The name of the attribute.
	 */
	private String name;
	
	/**
	 * Pattern use to match any valid Attribute represented by a String.
	 */
	public final static String PATTERN = "^\\w+";
	
	public Attribute(String name) {
		this.name = name;
	}
		
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Attribute))
			return false;
		
		return this.name.equals( ((Attribute) obj).name);
	}
	
	@Override
	public int compareTo(Attribute o) {
		return this.name.compareTo(o.name);
	}
	
	/**
	 * Parse a set of Attribute represented by the given string.
	 * The String much match to the pattern.
	 * Then each letter will used to create a new Attribute.
	 * @param attributes The attributes to parse.
	 * @return a set of newly created attributes.
	 */
	public static Set<Attribute> parse(String attributes) {
		if(!attributes.matches(PATTERN))
			throw new PatternSyntaxException("Wrong pattern, expecting : " + PATTERN, attributes, -1);
		
		Set<Attribute> attributesSet = new TreeSet<>();
		char[] chars = attributes.toCharArray();
		for(int i = 0; i < chars.length; i++) {
			attributesSet.add(new Attribute(Character.toString(chars[i])));
		}
		
		return attributesSet;
	}	
	
}
