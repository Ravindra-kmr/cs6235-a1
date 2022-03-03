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
* `q.txt` is a sample file containing some aliasing queries. Its structure is as follows:
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
