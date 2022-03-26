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
	public class QueryAns{
		String lhs;
		String rhs;
		String answer;
		public String getLhs() {
			return this.lhs;
		}
		public String getRhs() {
			return this.rhs;
		}
		public String getAns() {
			return this.answer;
		}
		public QueryAns(String lhs, String rhs,String answer) {
			this.lhs = lhs;
			this.rhs = rhs;
			this.answer = answer;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(this.lhs).append(" alias ").append(this.rhs).append(" ?").append(" "+this.answer);
			return sb.toString();
		}
	}

	public class refobj{
		String c;
		String m;
		int lineno;
		refobj(String c,String m,int l){
			this.c = c;
			this.m = m;
			this.lineno = l;
		}
		public String toString() {
			return this.c+"_"+this.m+"_"+this.lineno;
		}
	}
	public class summaryDS{	//For each method.
		HashMap<String,HashSet<refobj>> row;
		HashMap<refobj,HashMap<String,HashSet<refobj>>> sigma;
		Set<String> ret;
		summaryDS(){
			this.row = new HashMap<String,HashSet<refobj>>();
			this.sigma = new HashMap<refobj,HashMap<String,HashSet<refobj>>>();
			this.ret = new HashSet<String>();
		}
		summaryDS(HashMap<String,HashSet<refobj>> r,HashMap<refobj,HashMap<String,HashSet<refobj>>> s,Set<String> ret){
			this.row = r;
			this.sigma = s;
			this.ret = ret;
		}
		public void initempty() {
			this.row = new HashMap<String,HashSet<refobj>>();
			this.sigma = new HashMap<refobj,HashMap<String,HashSet<refobj>>>();
			this.ret = new HashSet<String>();
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
//	public class methodSummary{
////		SootClass c;
////		SootMethod m;
////		summaryDS summary;
//		HashMap<SootClass,HashMap<SootMethod,summaryDS>> msummary;
//		methodSummary(SootClass c, SootMethod m, summaryDS summary){
////			this.c = c;
////			this.m = m;
////			this.summary = summary;
//			this.msummary = new HashMap<SootClass,HashMap<SootMethod,summaryDS>>();
//			HashMap<SootMethod,summaryDS> temp = new HashMap<SootMethod,summaryDS>();
//			temp.put(m, summary);
//			this.msummary.put(c, temp);
//		}
//		methodSummary(SootClass c, SootMethod m){
////			this.c = c;
////			this.m = m;
//			summaryDS temp =  new summaryDS();
////			temp.initempty();
//			
//			this.msummary = new HashMap<SootClass,HashMap<SootMethod,summaryDS>>();
//			HashMap<SootMethod,summaryDS> temp1 = new HashMap<SootMethod,summaryDS>();
//			temp1.put(m, temp);
//			this.msummary.put(c, temp1);
//		}
//		public String toString() {
//			return msummary.toString();
//		}
//	}
//	HashSet<refobj> intersection = new HashSet<refobj>(localrow.get("obj1"));
//    intersection.retainAll(localrow.get("obj2"));
//    if(intersection != null) {
//    	System.out.println("Yes obj1 and obj2 are alias.");
	String result(HashSet<refobj> s1, HashSet<refobj> s2) {
		HashSet<refobj> intersection = new HashSet<refobj>(s1);
		intersection.retainAll(s2);
		if(intersection.isEmpty())
			return "NO";
		else
			return "YES";
	}
	@Override
	protected void internalTransform(String phaseName, Map<String, String> options) {
		// TODO Auto-generated method stub
//		System.out.println("phaseName:"+phaseName);
//		System.out.println("options:"+options);
		System.out.println("hello from PointToAnalysis.");
//		To get the queries
		Map<String, List<Query>> queries = this.getQueries();
		System.out.println("Queries:"+queries);
		HashMap<String,List<QueryAns>> qa = new HashMap<String,List<QueryAns>>();
		for(HashMap.Entry<String, List<Query>> q:queries.entrySet()) {
			LinkedList<QueryAns> temp = new LinkedList<QueryAns>();
			Iterator<Query> itr = q.getValue().iterator();
			while(itr.hasNext()) {
				Query querytemp = (Query) itr.next();
				QueryAns ans= new QueryAns(querytemp.getLhs(),querytemp.getRhs(),"No");
				temp.add(ans);
			}
			qa.put(q.getKey(), temp);
		}
		System.out.println("QueriesAnswer:"+qa);
		
		CallGraph cg = Scene.v().getCallGraph();
		SootClass mainClass = Scene.v().getMainClass();
		SootMethod mainMethod = mainClass.getMethodByName("main");
		LinkedList<HashMap<SootClass,HashMap<SootMethod,summaryDS>>> methodSummaryList= new LinkedList<HashMap<SootClass,HashMap<SootMethod,summaryDS>>>();	//SummaryList
		LinkedList<SootMethod> workingList = new LinkedList<SootMethod>();	//Working list
		workingList.add(mainMethod);
		
		while(!workingList.isEmpty()) {
			boolean converge = false;
			SootMethod currentMethod = workingList.poll();
			
			System.out.println(currentMethod.getActiveBody());
			System.out.println("************************");
//			HashMap<Value,Value> stackmap = new HashMap<Value,Value>();
			HashMap<String,HashSet<refobj>> localrow = new HashMap<String,HashSet<refobj>>();
			HashMap<refobj,HashMap<String,HashSet<refobj>>> localsigma = new HashMap<refobj,HashMap<String,HashSet<refobj>>>();
			System.out.println(currentMethod.getActiveBody().getLocals());
//			UnitGraph cfg = new BriefUnitGraph(Main)
			System.out.println("**************Analysis Starts: **********");
			//iterating over the "units" of the main method's body in some naive order
			while(!converge) {
				HashMap<String,HashSet<refobj>> prevrow = (HashMap<String,HashSet<refobj>>) localrow.clone();
				HashMap<refobj,HashMap<String,HashSet<refobj>>> prevsigma = (HashMap<refobj,HashMap<String,HashSet<refobj>>>) localsigma.clone();
				int linenumber = 0;
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
								HashSet<refobj> lhsref= localrow.get(lhs.toString());	
								if(lhsref == null)
									lhsref = new HashSet<refobj>();
								refobj newobj = new refobj(currentMethod.getDeclaringClass().getName(),currentMethod.getName(),linenumber);
								/* newref = oldref U newobj */
								Iterator<refobj> itr = lhsref.iterator();
								boolean add = true;
								while(itr.hasNext()) {
									refobj a = itr.next();
									if(a.toString().equals(newobj.toString()) ) {
										add = false;
										break;
									}
								}
								if(add) {
									lhsref.add(newobj);
									localrow.put(lhs.toString(),lhsref);
								}
	//							if(localrow.isEmpty() || !localrow.containsKey(lhs)) {	//create new entry
	//							}
								System.out.println("Added to localrow map");
							}
							else if(rhs instanceof Local) {	// Copy instruction  || rhs instanceof Constant
								System.out.println("COPY STATEMENT.: lhs:"+lhs+" rhs:"+rhs);			
								if(rhs instanceof Local) {
									HashSet<refobj> lhsref = localrow.get(lhs.toString());
									if(lhsref == null)
										lhsref = new HashSet<refobj>();
									if(localrow.get(rhs.toString()) != null)
										lhsref.addAll(localrow.get(rhs.toString()));
									localrow.put(lhs.toString(), lhsref);
								}
							}
							else if(rhs instanceof InstanceFieldRef)	 // Load instruction //|| rhs instanceof ArrayRef		
							{
								InstanceFieldRef i = (InstanceFieldRef)rhs;
								System.out.println("LOAD STATEMENT: Load to:"+ lhs + " value of obj:"+i.getBase()+" and field is:"+ i.getField().getName());
								HashSet<refobj> lhsref = localrow.get(lhs.toString());
								if(lhsref == null)
									lhsref = new HashSet<refobj>();
	//							HashSet<refobj> rhsref = localrow.get(rhs.toString());
								Iterator<refobj> itr = localrow.get(i.getBase().toString()).iterator();
								while(itr.hasNext()) {
									refobj temp = (refobj) itr.next();
									if(localsigma.get(temp) != null) {
									HashSet<refobj> tempref = localsigma.get(temp).get(i.getField().getName());
									if(tempref != null)
										lhsref.addAll(tempref);
									}
								}
								localrow.put(lhs.toString(), lhsref);
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
									System.out.println("STORE STATEMENT: Receiver is " + i.getBase() + " and field is " + i.getField().getName());
									
									HashSet<refobj> rhsref = localrow.get(rhs.toString());
	//								if(newref == null)
	//									newref = new HashSet<refobj>();
	//								if(localrow.get(rhs.toString()) != null)
	//									newref.addAll(localrow.get(rhs.toString()));
	//								localrow.put(lhs.toString(), newref);
									
									Iterator<refobj> itr = localrow.get(i.getBase().toString()).iterator();
									while(itr.hasNext()) {
										refobj temp = (refobj) itr.next();
										HashSet<refobj> lhsref;
										if(localsigma.get(temp) == null) {
	//										HashMap<refobj,HashMap<String,HashSet<refobj>>> localsigma = new HashMap<refobj,HashMap<String,HashSet<refobj>>>();
											localsigma.put(temp,new HashMap<String,HashSet<refobj>>());
										}
										
										lhsref = localsigma.get(temp).get(i.getField().getName());
										if(lhsref == null)
											lhsref = new HashSet<refobj>();
										if(rhsref != null)
											lhsref.addAll(rhsref);
										localsigma.get(temp).put(i.getField().getName(),lhsref);
										
										
									}
									
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
					System.out.println("LocalROW: "+localrow);
					System.out.println("LocalSigma: "+localsigma);
					linenumber++;
				}
				if (prevrow.toString().equals(localrow.toString())  && prevsigma.toString().equals(localsigma.toString())) {
					converge = true;
				}
				System.out.println("************NEXT ITERATION************");
			}
			
			for(HashMap.Entry<String, List<QueryAns>> q:qa.entrySet()) {
				Iterator<QueryAns> itr = q.getValue().iterator();
				while(itr.hasNext()) {
					QueryAns querytemp = (QueryAns)itr.next();
					querytemp.answer = result(localrow.get(querytemp.lhs),localrow.get(querytemp.rhs));
				}
				
			}
			System.out.println("QueriesAnswer:"+qa);
//				HashSet<refobj> intersection = new HashSet<refobj>(localrow.get("obj1"));
//		        intersection.retainAll(localrow.get("obj2"));
//		        if(intersection != null) {
//		        	System.out.println("Yes obj1 and obj2 are alias.");
//		        }
			
//			
			
			
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
