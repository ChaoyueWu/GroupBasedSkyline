package thu.course.mds.project2.phase1;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UnitWise2 {
	public List<List<Integer>> unitWiseCalculate(int k , List<DSGNode> dsg){
		
		List<List<Integer>> resultIdx = new ArrayList<List<Integer>>();
		
		RefDSG refdsg = new RefDSG(dsg);
		/*
		 * p1	0
		 * p6	1
		 * p11	2
		 * 
		 * p3	3
		 * p8	4
		 * p10	5
		 * 
		 * p2	6
		 * p5	7
		 * p9	8
		 * 
		 * p4	9
		 * p7	10
		 * */
		
		UnitGroup[] singleUnitGroups = refdsg.orderedUnitGroups;
		
		for (int a=0;a<singleUnitGroups.length;a++) {
						
			UnitGroup singleUnitGroup = singleUnitGroups[a];
			
			// check Glast
			HashSet<RefNode> glast = new HashSet<RefNode>();
			glast.addAll(singleUnitGroup.nodes);
			for (int j=a+1;j<singleUnitGroups.length;j++) {
				glast.addAll(singleUnitGroups[j].nodes);
			}
			if (glast.size() == k) {
				List<Integer> oneResult = new ArrayList<Integer>();
				for (RefNode rn : glast) {
					oneResult.add(rn.originPointIdx);
				}
				resultIdx.add(oneResult);
				break;
			}
			else if (glast.size() < k) {
				break;
			}
			
			List<UnitGroup> candiGroups = new ArrayList<UnitGroup>();
			candiGroups.add(singleUnitGroup);

			while (true) {
				
				if (candiGroups.isEmpty()) {
					break;
				}
				List<UnitGroup> newCandiGroups = new ArrayList<UnitGroup>();
					
				for (UnitGroup curCandiGroup : candiGroups) {
					
					Set<RefNode> ps = curCandiGroup.nodes;
					
					for (int c=curCandiGroup.tailIdx+1;c<refdsg.numNodes;c++) {
						if (!ps.contains(refdsg.orderedNodes[c])) {
							
							UnitGroup newUnitGroup = new UnitGroup(curCandiGroup);
							newUnitGroup.addUnitGroup(refdsg.orderedUnitGroups[c], c);
							
							if (newUnitGroup.nodes.size() == k) {
								List<Integer> newResult = new ArrayList<Integer>();
								for (RefNode rn : newUnitGroup.nodes) {
									newResult.add(rn.originPointIdx);
								}
								resultIdx.add(newResult);
							}
							else if (newUnitGroup.nodes.size() < k) {
								newCandiGroups.add(newUnitGroup);	
							}
						}
					}
				}
				candiGroups = newCandiGroups;
			}		
		}	
		return resultIdx;
	}
	
		
	class UnitGroup {
		
		int tailIdx = -1;
		int originPointIdx = -1;
		Set<RefNode> nodes = new HashSet<RefNode>();
		
		public UnitGroup(UnitGroup ug) {
			this.nodes.addAll(ug.nodes);
			this.tailIdx = ug.tailIdx;
			this.originPointIdx = ug.originPointIdx;
		}
		
		public UnitGroup(RefNode rn) {
			this.nodes.add(rn);
			this.originPointIdx = rn.originPointIdx;
		}
		
		public void addUnitGroup(UnitGroup u, int newTailIdx) {
			this.nodes.addAll(u.nodes);
			this.tailIdx = newTailIdx;
		}
	}
	
	class RefNode {
		int originPointIdx = 0;
		Set<RefNode> parents = null;
		Set<RefNode> children = null;
		
		public RefNode(int originPointIdx) {
			this.originPointIdx = originPointIdx;
		}
		public void addParents(RefNode p) {
			if (parents == null) {
				parents = new HashSet<RefNode>();
			}
			parents.add(p);
			if (p.parents != null) {
				parents.addAll(p.parents);
			}
		}
		public void addChildren(RefNode p) {
			if (children == null) {
				children = new HashSet<RefNode>();
			}
			children.add(p);
			if (p.children != null) {
				children.addAll(p.children);
			}
		}
	}
	
	
	class RefDSG {
		
		RefNode[] orderedNodes = null;
		UnitGroup[] orderedUnitGroups = null;
		int numNodes = 0;
				
		public RefDSG(List<DSGNode> dsgNodes) {
			numNodes = dsgNodes.size();
			orderedNodes = new RefNode[numNodes];
			orderedUnitGroups = new UnitGroup[numNodes];
			
			for (int i=0;i<numNodes;i++) {
				orderedNodes[i] = new RefNode(dsgNodes.get(i).getPointIndex());
			}
			for (int i=0;i<numNodes;i++) {
				DSGNode dsgn = dsgNodes.get(i);
				for (int p : dsgn.getChildren()) {
					orderedNodes[i].addChildren(orderedNodes[p]);
					orderedNodes[p].addParents(orderedNodes[i]);
				}
			}
			for (int i=0;i<numNodes;i++) {
				orderedUnitGroups[i] = new UnitGroup(orderedNodes[i]);
			}
			for (int i=0;i<numNodes;i++) {
				DSGNode dsgn = dsgNodes.get(i);
				for (int p : dsgn.getChildren()) {
					orderedUnitGroups[p].nodes.addAll(orderedUnitGroups[i].nodes);
				}
			}

			
			Arrays.sort(orderedNodes, new Comparator<RefNode>(){
			public int compare(RefNode d1, RefNode d2) {
				return d2.originPointIdx - d1.originPointIdx;
			}});
			
			Arrays.sort(orderedUnitGroups, new Comparator<UnitGroup>(){
				public int compare(UnitGroup d1, UnitGroup d2) {
					return d2.originPointIdx - d1.originPointIdx;
				}});
			
			for (int i=0;i<numNodes;i++) {
				orderedUnitGroups[i].tailIdx = i;
			}
			
		}	
	}
	

}
