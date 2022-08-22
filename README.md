# Question:
 Assignment 1: Alias Analysis

Our goal is to do alias analysis of qTACoJava programs. 

Use Soot to write in Java one or more Java files which implement inter-procedural flow-insensitive context-insensitive may alias analysis. Your main analysis file should be called A1.java (it may invoke any supplementary java files you create or those provided by Soot). Say P.java contains a program to be analyzed in TACoJava form, and Q.txt contains a sequence of alias analysis queries. Say we obtain P.class by invoking javac on P.java. On invoking Soot on P.java, to perform alias analysis using A1.java (example invocation shown here), it is expected to answer the queries given in Q.txt.  

Your program should print a series of "Yes" or "No" (each in a new line) corresponding to each query in Q.txt. The order of your answers should match the order of the alias queries.  


# CS6235-A1
Base repository for CS6235 A1 - a Soot-based Flow- and Context-Insensitive Interprocedural Alias Analysis of Java Programs, utilizing a Call Graph built on CHA.

## Notes
* You will need to implement your analysis by extending the `AnalysisBase` class.
  * This will force you to implement two abstract methods:
    * `internalTransform` - where you will implement your actual analysis.
    * `getResultString` - where you will emit your analysis' output in the required string format.
* For your convenience, a placeholder analysis - `cs6235.a1.submission.PointsToAnalysis` is already provided, you are welcome to add your code in there.
* Ensure that any code you write is within the `cs6235.a1.submission` package.
* You will eventually submit only an archive containing the code for the `cs6235.a1.submission` package.
* Even if you submit other items, we will only copy over the `cs6235.a1.submission` package during evaluation - so make sure all your submission code is within that package.

## Input/Output
* Consider the following program provided as input in `Test.java`
  ```
	//Test.java
	class Test {
		public static void main (String [] args) {
			A a = new A();
			a.f = new F();
			F p = a.f;
			
			F q = p.foo();
   
			a.bar(q);
		}
	}
	  
	class A {
		F f;
		void bar(F x) {
			F y = this.f;
			System.out.println("hello world");
		}
	}
	
	class F {
		F foo() {
			return this;
		}
	}
	
  ```

 Your code will need to answer some aliasing queries, which will ask if certain variables in this input program are aliases of each other. These queries will be supplied in an input queries text file, described below. The structure of this input queries file is as follows:
  1. First line contains an integer M - indicates the number of methods that have queries
  2. Next two lines contains a Method-Name, followed by an integer N - that indicates the number of alias queries for Method-Name
  3. Next N lines contain the alias queries for Method-Name, in the format `lhs,rhs` -- to be read as "Does `lhs` alias `rhs`?"
  4. Items ii and iii repeat M times, once for each Method-Name
  
  **Note** that you do not need to read-in or parse this text file, it is done behind the scenes.
  
   For example, consider the following input queries file `q.txt` :
```
  2
  Test.main
  3
  a,p
  a,q
  p,q
  A.bar
  1
  x,y
```
     
Here there are 2 methods for which there are aliasing queries - `Test.main`, and `A.bar`. There are 3 queries for `Test.main` -- "Does `a` alias `p`?", "Does `a` alias `q`?" and "Does `p` alias `q`?". Similarly there is 1 query for `A.bar` -- "Does `x` alias `y`?"
     
     
The input queries will be available to you via the `getQueries` method of your analysis. It is already loaded behind the scenes. Simply execute `Map<String, List<Query>> queries = this.getQueries();` inside your analysis to obtain a map using which you can obtain the queries per method.

The output should be a sequence of line-separated "YES" or "NO" strings, answering the alias queries in the respective order. For example, given the above `Test.java` and `q.txt`, your output would be:
```
NO
NO
YES
YES
```
i.e. it should convey the following:
```
Does a alias p in Test.Main? NO
Does a alias q in Test.main? NO
Does p alias q in Test.main? YES
Does x alias y in A.bar? YES
```

## Cheat Sheet
Some ready reckoners to accomplish a few essential tasks are listed below. Note that they are offered without any warranty, and you are expected to use your judgement and test things thoroughly yourself.
* Obtaining the entry point to your analysis, in case your analysis is designed to begin from the main method:
 
  `SootMethod entryPoint = Scene.v().getMainMethod();`
  
* Obtaining a SootMethod given its name and declaring class - here we obtain the SootMethod corresponding to `A:foo()`:

   `SootMethod method = Scene.v().getSootClass("A").getMethodByName("foo")`
   
   **Note** - is this safe to use always? What would it return when there are multiple `A:foo()`s?
   
   
* Given a SootMethod, obtaining its CFG:

   ```
  SootMethod m = ....;
  Body b = m.getActiveBody();
  UnitGraph g = new BriefUnitGraph(b);
    ```
  **Note** - observe that LHS is a UnitGraph, and we're instantiating a BriefUnitGraph. Why? What other forms of graphs might be available for use? 
  
* Given a `Unit`, obtaining its Control-Flow Successors:

  ```
  SootMethod m = ....;
  Unit u = ...;
  Body b = m.getActiveBody();
  UnitGraph g = new BriefUnitGraph(b);
  List<Unit> successors = g.getSuccsOf(u);
  ```

  **Note** - how will you iterate over the statements of a method in Control-Flow Order? How will you obtain the predecessors of a unit?

* Given a Soot Scene, obtaining a Call Graph:
 
   ```
   CallGraph cg = Scene.v().getCallGraph(); 
   ```
   
   **Note** - write some test cases and try to identify by observation what kind of Call Graph is being delivered in this code, CHA or something else. What other Call Graph is offered by Soot?
