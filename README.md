# CS6235-A1
Base repository for CS6235 A1 - a Soot-based Flow- and Context-Insensitive Interprocedural Alias Analysis of Java Programs, utilizing a Call Graph built on CHA.

## Notes
* You will need to implement your analysis by extending the `AnalysisBase` class.
  * This will force you to implement two abstract methods:
    * `internalTransform` - where you will implement your actual analysis.
    * `getResultString` - where you will emit your analysis' output in the required string format.
* For your convenience, a placeholder analysis - `PointsToAnalysis.java` is already provided, you are welcome to add your code in there.
* The input queries will be available to you via the `getQueries` method of your analysis. It is already loaded behind the scenes. Simply execute `Map<String, List<Query>> queries = this.getQueries();` inside your analysis. 
* Ensure that any code you write is within the `cs6235.a1.submission` package.
* You will eventually submit only an archive containing the code for the `cs6235.a1.submission` package.
* Even if you submit other items, we will only copy over the `cs6235.a1.submission` package during evaluation - so make sure all your submission code is within that package.
