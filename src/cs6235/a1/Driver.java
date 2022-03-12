package cs6235.a1;


import cs6235.a1.submission.PointsToAnalysis;
import soot.PackManager;
import soot.Transform;

public class Driver {
	public static void main (String [] args) {
		String classPath = "tests";
		String queriesPath = "tests/CS21M050/Q1.txt";
		Options.queriesPath = queriesPath;
		String mainClass = "P1";
		
		String [] sootArgs = {
				"-v",
				"-cp", classPath,
				"-pp",
				"-w", "-app",
				//"-p", "jb", "use-original-names:true",
				"-p", "cg.cha", "enabled:true",
				"-p", "cg.spark", "enabled:false",
				//START disable some optimizations that eliminate locals
				"-p", "jb", "preserve-source-annotations:true",
				"-p", "jb", "stabilize-local-names:true",
				"-p", "jb.ulp", "enabled:false",
				"-p", "jb.dae", "enabled:false",
				"-p", "jb.cp-ule", "enabled:false",
				"-p", "jb.cp", "enabled:false",
				"-p", "jb.lp", "enabled:false",
				"-p", "jb.lns", "enabled:false",
				"-p", "jb.dtr", "enabled:false",
				"-p", "jb.ese", "enabled:false",
				"-p", "jb.sils", "enabled:false",
				"-p", "jb.a", "enabled:false",
				"-p", "jb.ule", "enabled:false",
				"-p", "jb.ne", "enabled:false",
				"-p", "jb.uce", "enabled:false",
				"-p", "jb.tt", "enabled:false",
				//END disable some optimizations that eliminate locals
				//now tell the Jimple-Body pack to preserve variable names from Source, wherever applicable
				"-p", "jb", "use-original-names:true",
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
