***************Setup:***************
0. Install - java SE 11

1. Download the soot jar file

2. write a simple java program and execute it.

3. make sure u have soot jar and .java .class files in same location

4. Run $java -jar soot-4.3.0-jar-with-dependencies.jar -pp -cp Main

5. use -w to run in full program analysis mode, Run $java -jar soot-4.3.0-jar-with-dependencies.jar -w -pp -cp Main

6. to emit jimple files run $java -jar soot-4.3.0-jar-with-dependencies.jar -w -app -pp -cp . -f J Main

7. u can use -d to emit to the directory you want

8. make sure jimple files have Main.jimple

9. install eclipse and create a new workspace and create a new project(use an execution environment JRE : java SE-11)

10. right click on project name , click build path and add external archives, select the soot jar file, gets added to the referenced libraries.

11. install git

12. clone the repository : https://github.com/shashinh/cs6235-soot-intro/

***************Mid_Evaluation Submittion guidelines:***************
Programming Assignment 1 is located here. The purpose of the mid-evaluation is to motivate you to begin working on the assignment early. To that end, you will be required to submit 5 non-trivial test cases for the assignment. Each test case will comprise of three parts:

    P.java - An input program in TACoJava.
    Q.txt - An input queries file
    O.txt - A file containing the expected output.

Please go here for an explanation of these files and the input/output format. You will submit an archive containing 5 sets of tests - [P1.java, Q1.txt, O1.txt], [P2.java, Q2.txt, O2.txt].... and so on.



***************Final Submission guidelines:***************

Please ensure the following before submission:

1. You are submitting only code within the cs6235.a1.submission package and nothing else. We will be using a script that copies over the src/cs6235/a1/submission directory to our test environment and discards the rest. Ensure that you are not submitting any libraries, class files, test programs, etc.

2. You have compiled-run-tested your code with Java 8, and the supplied Driver class (i.e., ensure that your analysis class is cs6235.a1.submission.PointsToAnalysis, for instance). The testbed will use only Java 8 to compile and run your code, and should there be any issues - your submission will be considered non-working.

3. Name your archive <ROLL-NUMBER>.A1.zip/tar/etc. For example CS20S003.A1.tgz.


