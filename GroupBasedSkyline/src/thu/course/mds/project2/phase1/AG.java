package thu.course.mds.project2.phase1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AG {
	
	public long agCalculate(int k , ProcessResult result){
		
		List<DSGNode> dsg = result.DSG;
		RefDSG refdsg = new RefDSG(dsg);
		
		long finalCount = 0;
		
		List<Group> workList = new ArrayList<Group>();
		workList.add(new Group());
		
		for (int i=1;i<=k;i++) {
			List<Group> nextWorkList = new ArrayList<Group>();
			for (Group g : workList) {
				Set<RefNode> childrenSet = new HashSet<RefNode>();
				for (RefNode n : g.commonUnitNodes()) {
					childrenSet.addAll(n.descendants);
				}
				List<Integer> tailSet = new ArrayList<Integer>();
				for (int j=g.tailIdx+1;j<refdsg.numNodes;j++) {
					if (!childrenSet.contains(refdsg.orderedNodes[j])) {
						finalCount += C2(k-i, refdsg.numNodes-i);
					}
					else {
						tailSet.add(j);
					}
				}
				for (int j : tailSet) {
					Group newGroup = new Group(g, j);
					newGroup.addUnit(refdsg.orderedUnits[j], j);
					Set<RefNode> commonUnitNodes = newGroup.commonUnitNodes();
					if (commonUnitNodes.size() < k) {
						finalCount += C2(k-i, refdsg.numNodes-i);
					}
					else if (commonUnitNodes.size() == k) {
						int invalidCombinations = C2(k-i, newGroup.commonDescendants().size()); 
						finalCount += C2(k-i, refdsg.numNodes-i) - invalidCombinations;
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
	
	
    //求排列数  
    public static int A(int up,int bellow)  {  
        int result=1;  
        for(int i=up;i>0;i--)  {  
            result*=bellow;  
            bellow--;  
        }  
        return result;  
    }  
    //求组合数 
    public static int C2(int up,int below)  
    {      
        int denominator=A(up,up);   
        int numerator=A(up,below);  
        return numerator/denominator;   
    }
    
//    public static void main(String[]args) {
//    	System.out.println(C2(0,4));
//    }
//		
	class Group {
		
		int tailIdx = -1;
		Set<RefNode> nodes = new HashSet<RefNode>();
		Set<RefNode> totalAncestors = new HashSet<RefNode>();
		Set<Unit> units = new HashSet<Unit>();
		
		public Group() {
			
		}
		
		
		public Group(Group g, int newTailIdx) {
			this.nodes.addAll(g.nodes);
			this.totalAncestors.addAll(g.totalAncestors);
			this.tailIdx = newTailIdx;
			this.units.addAll(g.units);
		}

		
		public Set<RefNode> commonUnitNodes() {
			Set<RefNode> result = new HashSet<RefNode>();
			if (this.units.isEmpty()) {
				return result;
			}
			boolean empty = true; 
			for (Unit u : this.units) {
				if (empty) {
					result.addAll(u.unitNodes);
					empty = false; 
				}
				else {
					result.removeAll(u.unitNodes);
				}
				if (result.isEmpty()) {
					break;
				}
			}
			return result;
		}
		
		public Set<RefNode> commonDescendants() {
			Set<RefNode> result = new HashSet<RefNode>();
			if (this.units.isEmpty()) {
				return result;
			}
			boolean empty = true; 
			for (RefNode n : this.nodes) {
				if (empty) {
					result.addAll(n.descendants);
					empty = false; 
				}
				else {
					result.removeAll(n.descendants);
				}
				if (result.isEmpty()) {
					break;
				}
			}
			return result;
		}
		
		public void addUnit(Unit u, int tailIdx) {
			this.nodes.addAll(u.unitNodes);
			this.totalAncestors.addAll(u.ancestors);
			this.units.add(u);
			this.tailIdx = tailIdx;
		}
	}
	
	
	class Unit {
		
		Set<RefNode> ancestors = new HashSet<RefNode>();
		Set<RefNode> unitNodes = new HashSet<RefNode>();
		RefNode node;
		
		Unit(RefNode node) {
			if (node.ancestors != null) {
				this.ancestors.addAll(node.ancestors);
				this.unitNodes.addAll(node.ancestors);
			}
			this.node = node;
			this.unitNodes.add(node);
		}
	
	}

	
	class RefNode {
		int originPointIdx = 0;
		Set<RefNode> ancestors = null;
		Set<RefNode> descendants = null;
		
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
			orderedNodes = new RefNode[numNodes];
			
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
				orderedUnits[i] = new Unit(orderedNodes[i]);
			}
		}	
	}

}
