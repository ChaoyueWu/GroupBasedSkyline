package thu.course.mds.project2.phase1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AGNew {
	public long agCalculate(int k , ProcessResult result) {
		List<DSGNode> dsg = result.DSG;
//		System.out.println(dsg.size());
		RefDSG refdsg = new RefDSG(dsg);
		
		long finalCount = 0;
		List<Group> workList = new ArrayList<Group>();
		for(Unit u : refdsg.orderedUnits) {
			if(u.unitNodes.size() < k) {
				finalCount += C2(k-1,refdsg.numNodes-u.node.originPointIdx - 1);
//				System.out.println("up: " +(k-1) + " below : "+ (refdsg.numNodes-u.node.originPointIdx - 1) + " result : "+(C2(k-1,refdsg.numNodes-u.node.originPointIdx - 1)));
			}
			else if(u.unitNodes.size() == k) {
				long invalidCombinations = C2(k-1, u.node.descendants.size());
				finalCount += (C2(k-1,refdsg.numNodes-u.node.originPointIdx - 1) - invalidCombinations);
			}
			else
				workList.add(new Group(u));		
		}
		System.out.println("预处理之后剩余的点数："+workList.size());
		System.out.println(finalCount);
//		workList.add(new Group());
		for(int i = 1 ; i < k ; i ++) {
			List<Group> nextWorkList = new ArrayList<Group>();
			for (Group g : workList) {
				Set<RefNode> childrenSet = new HashSet<RefNode>();
				for (RefNode n : g.commonUnit) {
					childrenSet.addAll(n.descendants);
				}
				List<Integer> tailSet = new ArrayList<Integer>();
				for (int j=g.tailIdx+1;j<refdsg.numNodes;j++) {
					if (!childrenSet.contains(refdsg.orderedNodes[j])) {
//						if(k-i-1 == 2)
//							System.out.println("up: " +(k-i-1) + " below : "+ (refdsg.numNodes-j-1) + " result : "+(C2(k-i-1, refdsg.numNodes-j-1)));
						finalCount += C2(k-i-1, refdsg.numNodes-j-1);
					}
					else {
						tailSet.add(j);
					}
				}
				for (int j : tailSet) {
					Group newGroup = new Group(g, j);
					newGroup.addNode(refdsg.orderedUnits[j]);
					if(newGroup.commonUnit.size() < k) {
						finalCount += C2(k-i-1, refdsg.numNodes-j-1);
					}
					else if(newGroup.commonUnit.size() == k) {
						long invalidCombinations = C2(k-i-1, newGroup.commonChildren.size()); 
						finalCount += (C2(k-i-1, refdsg.numNodes-j-1) - invalidCombinations);
					}
					else {
						nextWorkList.add(newGroup);
					}
				}
			}
			workList = nextWorkList;
		}
		return finalCount;
	}
	
	public static long A(int up,int bellow) {
		long result=1;  
        for(int i=up;i>0;i--)  {  
            result*=bellow;  
            bellow--;  
        }  
        return result;  
	}
	
	public static long C2(int up,int below) {
		if(below < up)
			return 0;
		if(below == up)
			return 1;
		long denominator=A(up,up);   
        long numerator=A(up,below);  
        return numerator/denominator;   
	}
	
	class Group{
		int tailIdx = -1;
		Set<RefNode> nodes = new HashSet<RefNode>();
		Set<RefNode> commonUnit = new HashSet<RefNode>();
		Set<RefNode> commonChildren = new HashSet<RefNode>();
		
		public Group() {
			
		}
		
		public Group(Unit u) {
			nodes.add(u.node);
			commonUnit.addAll(u.unitNodes);
			commonChildren.addAll(u.node.descendants);
			tailIdx = u.node.originPointIdx;
		}
		
		public Group(Group g, int newTailIdx) {
			this.nodes.addAll(g.nodes);
			this.commonUnit.addAll(g.commonUnit);
			this.commonChildren.addAll(g.commonChildren);
			this.tailIdx = newTailIdx;
		}
		
		public void addNode(Unit u) {
			nodes.add(u.node);
			commonUnit.retainAll(u.unitNodes);
			commonChildren.retainAll(u.node.descendants);
		}
	}
	
	class Unit {
		Set<RefNode> unitNodes = new HashSet<RefNode>();
		RefNode node;
		
		Unit(RefNode node) {
//			System.out.println("unit");
			if (node.ancestors != null) {
				this.unitNodes.addAll(node.ancestors);
			}
			this.node = node;
			this.unitNodes.add(node);
		}
	
	}

	
	class RefNode {
		int originPointIdx = 0;
		Set<RefNode> ancestors = new HashSet<RefNode>();
		Set<RefNode> descendants = new HashSet<RefNode>();
		
		public RefNode(int originPointIdx) {
			this.originPointIdx = originPointIdx;
		}
		public void addDescendants(RefNode p) {
			if (descendants == null) {
				descendants = new HashSet<RefNode>();
			}
			descendants.add(p);
			if (p.descendants != null) {
				descendants.addAll(p.descendants);
			}
		}
		public void addAncestors(RefNode p) {
			if (ancestors == null) {
				ancestors = new HashSet<RefNode>();
			}
			ancestors.add(p);
			if (p.ancestors != null) {
				ancestors.addAll(p.ancestors);
			}
		}
	}
	
	class RefDSG {
		RefNode[] orderedNodes = null;
		Unit[] orderedUnits = null;
		int numNodes = 0;
				
		public RefDSG(List<DSGNode> dsgNodes) {
			numNodes = dsgNodes.size();
//			System.out.println(numNodes);
			orderedNodes = new RefNode[numNodes];
			orderedUnits = new Unit[numNodes];
			
			for (int i=0;i<numNodes;i++) {
				orderedNodes[i] = new RefNode(dsgNodes.get(i).getPointIndex());
			}
			for (int i=0;i<numNodes;i++) {
				DSGNode dsgn = dsgNodes.get(i);
				for (int p : dsgn.getChildren()) {
					orderedNodes[i].addDescendants(orderedNodes[p]);
					orderedNodes[p].addAncestors(orderedNodes[i]);
				}
			}
			for (int i=0;i<numNodes;i++) {
//				System.out.println(orderedNodes[i]);
				orderedUnits[i] = new Unit(orderedNodes[i]);
			}
		}	
	}
}
