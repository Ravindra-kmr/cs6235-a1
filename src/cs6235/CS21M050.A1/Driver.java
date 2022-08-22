package cs6235.a1;


import java.util.Arrays;

import cs6235.a1.submission.PointsToAnalysis;
import soot.PackManager;
import soot.Transform;

public class Driver {
	public static void main (String [] args) {

		String classPath = "tests/CS21M050";
		String queriesPath = "tests/CS21M050/Q3.txt";
		String mainClass = "P3";
		if(args != null && args.length > 0) {
			int i = 0;
			while(true) {
				if(args[i].equals("-cp")) {
					classPath = args[i+1];
					i += 2;
				} else if (args[i].equals("-mainClass")) {
					mainClass = args[i + 1];
					i += 2;
				} else if (args[i].equals("-qp")) {
					queriesPath = args[i + 1];
					i += 2;
				}
				
				if(i + 1 > args.length) break;
			}
		}
		

		Options.queriesPath = queriesPath;
		
		String [] sootArgs = {
				"-v",
				"-cp", classPath,
				"-pp",
				"-w", "-app",
				"-src-prec", "java",
				"-p", "cg.cha", "enabled:true",
				"-p", "cg.spark", "enabled:false",
				"-f", "J",
				//"-d", "output",
				mainClass
				
		};
		
		//System.out.println("The soot arguments are " + Arrays.toString(sootArgs));
		
		PointsToAnalysis pta = new PointsToAnalysis();
		//load in the queries
		pta.loadQueries();
		
		PackManager.v().getPack("wjtp").add(new Transform("wjtp.pta", pta));
		soot.Main.main(sootArgs);
		
		//emit the output string
		String analysisResult = pta.getResultString();
		System.out.println(analysisResult);
		
	}
}
