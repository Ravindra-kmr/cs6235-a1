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
* `q.txt` is a sample file containing some aliasing queries. **Note** that you do not have to read-in or parse its contents. That is taken care of behind the scenes. However, since you will need to build your own test cases, it is beneficial to know its strcture. It is as follows:
  1. First line contains an integer M - indicates the number of methods that have queries
  2. Next two lines contains a Method-Name, followed by an integer N - that indicates the number of alias queries for Method-Name
  3. Next N lines contain the alias queries for Method-Name, in the format `lhs,rhs` -- to be read as "Does `lhs` alias `rhs`?"
  4. Items ii and iii repeat M times, once for each Method-Name
  
     For example:
     ```
     2
     Driver.main
     3
     x,y
     w,z
     p,q
     C.fooBar
     2
     a,b
     d,e
     ```
     
     Here there 2 methods for which there are aliasing queries - `Driver.main`, and `C.fooBar`. There are 3 queries for `Driver.main` -- "Does `x` alias `y`?", "Does `w` alias `z`?" and "Does `p` alias `q`?". Similarly there there 2 queries for `C.fooBar` -- "Does `a` alias `b`?" and "Does `d` alias `e`?"
     
     
* The input queries will be available to you via the `getQueries` method of your analysis. It is already loaded behind the scenes. Simply execute `Map<String, List<Query>> queries = this.getQueries();` inside your analysis to obtain a map using which you can obtain the queries per method.


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
