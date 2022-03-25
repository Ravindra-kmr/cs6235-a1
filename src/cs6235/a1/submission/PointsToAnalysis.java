package cs6235.a1.submission;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.graph.Graph;

import cs6235.a1.AnalysisBase;
import cs6235.a1.Query;
import soot.Local;
import soot.RefLikeType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.ArrayRef;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InvokeStmt;
import soot.jimple.NewExpr;
import soot.jimple.ReturnStmt;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.CompleteUnitGraph;
import soot.toolkits.graph.UnitGraph;

public class PointsToAnalysis extends AnalysisBase {

	@Override
	public String getResultString() {
		// TODO Auto-generated method stub
//		return "YES";
		return null;
	}
	public class refobj{
		SootClass c;
		SootMethod m;
		int lineno;
		refobj(SootClass c,SootMethod m,int l){
			this.c = c;
			this.m = m;
			this.lineno = l;
		}
		public String toString() {
			return this.c.getName()+"_"+this.m.getName()+"_"+this.lineno;
		}
	}
	public class summaryDS{	//For each method.
		HashMap<Value,HashSet<refobj>> row;
		HashMap<refobj,HashMap<Value,HashSet<refobj>>> sigma;
		Set<Value> ret;
		summaryDS(){
			this.row = new HashMap<Value,HashSet<refobj>>();
			this.sigma = new HashMap<refobj,HashMap<Value,HashSet<refobj>>>();
			this.ret = new HashSet<Value>();
		}
		summaryDS(HashMap<Value,HashSet<refobj>> r,HashMap<refobj,HashMap<Value,HashSet<refobj>>> s,Set<Value> ret){
			this.row = r;
			this.sigma = s;
			this.ret = ret;
		}
		public void initempty() {
			this.row = new HashMap<Value,HashSet<refobj>>();
			this.sigma = new HashMap<refobj,HashMap<Value,HashSet<refobj>>>();
			this.ret = new HashSet<Value>();
		}
		public boolean isEmpty() {
			if(this.row.isEmpty() && this.sigma.isEmpty() && this.ret.isEmpty())
				return true;
			else
				return false;
		}
		public String toString() {
			return "Row:"+this.row+"\nSigma: "+this.sigma+"\n Return value: "+ret;
		}
	}
	public class methodSummary{
//		SootClass c;
//		SootMethod m;
//		summaryDS summary;
		HashMap<SootClass,HashMap<SootMethod,summaryDS>> msummary;
		methodSummary(SootClass c, SootMethod m, summaryDS summary){
//			this.c = c;
//			this.m = m;
//			this.summary = summary;
			this.msummary = new HashMap<SootClass,HashMap<SootMethod,summaryDS>>();
			HashMap<SootMethod,summaryDS> temp = new HashMap<SootMethod,summaryDS>();
			temp.put(m, summary);
			this.msummary.put(c, temp);
		}
		methodSummary(SootClass c, SootMethod m){
//			this.c = c;
//			this.m = m;
			summaryDS temp =  new summaryDS();
//			temp.initempty();
			
			this.msummary = new HashMap<SootClass,HashMap<SootMethod,summaryDS>>();
			HashMap<SootMethod,summaryDS> temp1 = new HashMap<SootMethod,summaryDS>();
			temp1.put(m, temp);
			this.msummary.put(c, temp1);
		}
		public String toString() {
			return msummary.toString();
		}
	}
	
	@Override
	protected void internalTransform(String phaseName, Map<String, String> options) {
		// TODO Auto-generated method stub
//		System.out.println("phaseName:"+phaseName);
//		System.out.println("options:"+options);
		System.out.println("hello from PointToAnalysis.");
		CallGraph cg = Scene.v().getCallGraph();
		SootClass mainClass = Scene.v().getMainClass();
		SootMethod mainMethod = mainClass.getMethodByName("main");
		LinkedList<HashMap<SootClass,HashMap<SootMethod,summaryDS>>> methodSummaryList= new LinkedList<HashMap<SootClass,HashMap<SootMethod,summaryDS>>>();	//SummaryList
		LinkedList<SootMethod> workingList = new LinkedList<SootMethod>();	//Working list
		workingList.add(mainMethod);
		
		while(!workingList.isEmpty()) {
			SootMethod currentMethod = workingList.poll();
			
			System.out.println(currentMethod.getActiveBody());
			System.out.println("************************");

			System.out.println(currentMethod.getActiveBody().getLocals());
//			UnitGraph cfg = new BriefUnitGraph(Main)
			System.out.println("**************Analysis Starts: **********");
			//iterating over the "units" of the main method's body in some naive order
			for(Unit unit : currentMethod.getActiveBody().getUnits()) {
				System.out.println("now processing :" +unit);
				
				Stmt stmt = (Stmt) unit;
				
				if(stmt instanceof DefinitionStmt) {
					DefinitionStmt ds = (DefinitionStmt) stmt;
					Value lhs = ds.getLeftOp();
					Value rhs = ds.getRightOp();
					System.out.println("lhs " + lhs + " rhs " + rhs);
					
					// ********************
					if(lhs instanceof Local) {
						System.out.println("Inside lhs = local variable.");
						if(rhs instanceof NewExpr) {
							System.out.println("ALLOCATION STATEMENT: LHS:" + lhs+" declared type: "+lhs.getType()+ " valuetype:"+ rhs.getType());
//							unit = 
						}
						else if(rhs instanceof Local || rhs instanceof Constant) {	// Copy instruction
							System.out.println("COPY STATEMENT.: lhs:"+lhs+" rhs:"+rhs);			
						}
						else if(rhs instanceof InstanceFieldRef || rhs instanceof ArrayRef)		// Load instruction
						{
							InstanceFieldRef i = (InstanceFieldRef)rhs;
							System.out.println("LOAD STATEMENT: Load to:"+ lhs + " value of obj:"+i.getBase()+" and field is:"+ i.getField());
						}
						else
							System.out.println("Not one of 4 statements we want.");
//						else if(rhs instanceof)
					}
					// ******************
					if(lhs.getType() instanceof RefLikeType) {		//Store instruction
						
						if(lhs instanceof InstanceFieldRef || lhs instanceof ArrayRef) {
							//Q1. if control is here, what can you say about rhs?
							//rhs HAS to be a ref type
							//rhs HAS to be either a local or a constant
							InstanceFieldRef i = (InstanceFieldRef) lhs;
							if(rhs instanceof Local) {
								//if control is inside here
								//ex: x.f = y
								//also called a store statement
								System.out.println("we know how to handle a store statement with local variables.");							
								System.out.println("STORE STATEMENT: Receiver is " + i.getBase() + " and field is " + i.getField());
								
							}
							// *********************
							else if (rhs instanceof Constant) {
								System.out.println("we know how to handle a store statement with constant.");							
								System.out.println("STORE STATEMENT: Receiver is " + i.getBase() + " and field is " + i.getField());
								// something similar;
							}
							// *************************
						}
						
						
						
					} else {
						//lhs is a scalar, do something
					}
					
					
				} else if (stmt instanceof InvokeStmt) {
					
				} else if (stmt instanceof ReturnStmt) {
					
				} else {
					System.out.println("I do not care about this statement");
				}
				
				
			}
			
			
			
			
			
			
			
			
//			if(methodSummaryList.isEmpty()) {
////				methodSummary temp = new methodSummary(mainClass,currentMethod);
////				methodSummaryList.add(temp);
//				HashMap<SootClass,HashMap<SootMethod,summaryDS>> temp = new HashMap<SootClass,HashMap<SootMethod,summaryDS>>();
//				HashMap<SootMethod,summaryDS> temp1 = new HashMap<SootMethod,summaryDS>();
//				summaryDS temp2 =  new summaryDS();
////				for(int i =0;i < currentMethod.getParameterCount();i++) {
////					System.out.println("Parameter: "+currentMethod.getParameterType(i));
//////					if(currentMethod.getParameterType(i))
////				}
////				temp2.initempty();
//				temp1.put(currentMethod,temp2);
//				temp.put(currentMethod.getDeclaringClass(), temp1);
//				methodSummaryList.add(temp);
//				System.out.println(methodSummaryList);
//				
//			}
//			else {
//				
//			}
			
//			UnitGraph cfg = new BriefUnitGraph(currentMethod.getActiveBody());
			
			
			
			
		}
		
		
		
		
		
		
		
		
		
		
		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		//System.out.println(mainMethod);
//		//System.out.println("************************");
//
//		System.out.println(mainMethod.getActiveBody());
//		System.out.println("************************");
//		//System.out.println(mainMethod.getActiveBody().getLocals());
//		
////		To get the call Graph.
//		Iterator<SootClass> classIt = Scene.v().getApplicationClasses().iterator();
//		while(classIt.hasNext()) {
//			SootClass sclass = (SootClass) classIt.next();
//			System.out.println("ClassName"+sclass.moduleName);
//			Iterator<SootMethod> sm = sclass.getMethods().iterator();
//			while(sm.hasNext()) {
//				SootMethod smethod = (SootMethod) sm.next();
//				System.out.println("MethodName:"+smethod.getName());
//			}
//		}
////		To get the queries
//		Map<String, List<Query>> queries = this.getQueries();
//		System.out.println("Queries:"+queries);
//		
//		System.out.println("**************Analysis Starts: **********");
//		//iterating over the "units" of the main method's body in some naive order
//		for(Unit unit : mainMethod.getActiveBody().getUnits()) {
//			System.out.println("now processing :" +unit);
//			
//			Stmt stmt = (Stmt) unit;
//		}
//				
	}

}
