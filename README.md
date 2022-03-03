# CS6235-A1
A reference implementation for CS6235 A1 - a Soot-based Flow- and Context-Insensitive Interprocedural Alias Analysis of Java Programs, utilizing a Call Graph built on CHA.

## Notes
* You will need to implement your analysis by extending the `AnalysisBase` class.
  * This will force you to implement two abstract methods:
    * `internalTransform` - where you will implement your actual analysis.
    * `getResultString` - where you will emit your analysis' output in the required string format.
* The input queries will be available to you via the `getQueries` method of your Analysis. It is already loaded behind the scenes.
* Ensure that any code you write is within the `cs6235.a1` package.
* You will eventually submit only an archive containing the code for the `cs6235.a1` package.
