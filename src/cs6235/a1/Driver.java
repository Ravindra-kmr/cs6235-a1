package cs6235.a1;


import cs6235.a1.submission.PointsToAnalysis;
import soot.PackManager;
import soot.Transform;

public class Driver {
	public static void main (String [] args) {
		String classPath = "tests";
		String queriesPath = "q.txt";
		Options.queriesPath = queriesPath;
		String mainClass = "Main";
		
		String [] sootArgs = {
				"-v",
				"-cp", classPath,
				"-pp",
				"-w", "-app",
				//"-p", "jb", "use-original-names:true",
				"-p", "cg.cha", "enabled:true",
				"-p", "cg.spark", "enabled:false",
				//"-f", "J",
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
