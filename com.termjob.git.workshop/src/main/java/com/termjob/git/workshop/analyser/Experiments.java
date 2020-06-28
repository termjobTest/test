/**
 * 
 */
package com.termjob.git.workshop.analyser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anoop
 *
 */
public class Experiments {

	private final List foo;  // inline initialization also fine

	public Experiments() {
		foo = new ArrayList(); // final must be initialized in constructor, bcz compiler knows only once the
								// construcot called for each object
		foo.add("foo"); // Modification-1
	}

	public void setFoo(List foo) {
		foo.add("anu");
//		this.foo = foo; // compilation error, bcz compiler knows method can be call multiple time.
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		Experiments experiment=new Experiments();
//		experiment.foo.add("anoop");
//		System.out.println(experiment.foo);
		
		String mango = "mango";
		String mango3 = new String("mango");
		System.out.println(mango != mango3);
		System.out.println(mango == mango3);
	}

	
}
