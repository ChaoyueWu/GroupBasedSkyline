package fornba;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AG {
	
	public long agCalculate(int k , ProcessResult result){
		
		List<DSGNode> dsg = result.DSG;
		
		RefDSG refdsg = new RefDSG(dsg);
		
		long finalCount = 0;
		
		Group[] startGroups = refdsg.startGroups;
		List<Group> workList = new ArrayList<Group>();
		workList.addAll(Arrays.asList(startGroups));
		
		for (int i=1;i<k;i++) {
			List<Group> nextWorkList = new ArrayList<Group>();
			for (Group g : workList) {
				Set<RefNode> childrenSet = g.totalDescendants;
				List<Integer> tailSet = new ArrayList<Integer>();
				for (int j=g.tailIdx+1;j<refdsg.numNodes;j++) {
					if (!childrenSet.contains(refdsg.orderedNodes[j])) {
						finalCount += C2(k-i-1, refdsg.numNodes-i-1);
					}
					else {
						tailSet.add(j);
					}
				}
				for (int j : tailSet) {
					Group newGroup = new Group(g, j);
					newGroup.addNode(refdsg.orderedNodes[j], j);
					if (newGroup.totalAncestors.size() < k) {
						finalCount += C2(k-i-1, refdsg.numNodes-i-1);
					}
					else if (newGroup.totalAncestors.size() == k) {
						int invalidCombinations = C2(k-i-1, newGroup.totalDescendants.size()); 
						finalCount += C2(k-i-1, refdsg.numNodes-i-1) - invalidCombinations;
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
		Set<RefNode> totalDescendants = new HashSet<RefNode>();
		
		public Group(RefNode n, int tailIdx) {
			this.nodes.add(n);
			this.totalAncestors.addAll(n.ancestors);
			this.totalDescendants.addAll(n.descendants);
			this.tailIdx = tailIdx;
		}	
		
		public Group(Group g, int newTailIdx) {
			this.nodes.addAll(g.nodes);
			this.totalAncestors.addAll(g.totalAncestors);
			this.totalDescendants.addAll(g.totalDescendants);
			this.tailIdx = newTailIdx;
		}
		
		public void addNode(RefNode node, int tailIdx) {
			this.tailIdx = tailIdx;
			this.nodes.add(node);
			this.totalAncestors.addAll(node.ancestors);
			this.totalDescendants.addAll(node.descendants);
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
		Group[] startGroups = null;
		int numNodes = 0;
				
		public RefDSG(List<DSGNode> dsgNodes) {
			numNodes = dsgNodes.size();
			orderedNodes = new RefNode[numNodes];
			startGroups = new Group[numNodes];
			
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
				startGroups[i] = new Group(orderedNodes[i], i);
			}
		}	
	}

}
