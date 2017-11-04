package thu.course.mds.project2;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UnitWise {
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
		
		UnitGroup[] singleUnitGroups = new UnitGroup[refdsg.numNodes];
		for (int i =0;i<refdsg.numNodes;i++) {
			singleUnitGroups[i] = new UnitGroup(refdsg.orderedUnits[i], i);
		}
			
		
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
							newUnitGroup.addUnit(refdsg.orderedUnits[c], c);
							
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
		Set<RefNode> nodes = new HashSet<RefNode>();
		
		public UnitGroup(Unit u, int tailIdx) {
			this.nodes.addAll(u.parents);
			this.nodes.add(u.startNode);
			this.tailIdx = tailIdx;
		}
		
		public UnitGroup(UnitGroup ug) {
			this.nodes.addAll(ug.nodes);
			this.tailIdx = ug.tailIdx;
		}
		
		
		public void addUnit(Unit u, int newTailIdx) {
			this.nodes.addAll(u.parents);
			this.nodes.add(u.startNode);
			this.tailIdx = newTailIdx;
		}
	}
	
	
	class Unit {
		
		Set<RefNode> parents;
		RefNode startNode;
		
		Unit(RefNode start) {
			this.parents = new HashSet<RefNode>();
			if (start.parents != null) {
				this.parents.addAll(start.parents);
			}
			this.startNode = start;
		}
		
		public void unionParentUnit(Unit u) {
			this.parents.add(u.startNode);
			this.parents.addAll(u.parents);
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
		Unit[] orderedUnits = null;
		int numNodes = 0;
				
		public RefDSG(List<DSGNode> dsgNodes) {
			numNodes = dsgNodes.size();
			orderedNodes = new RefNode[numNodes];
			orderedUnits = new Unit[numNodes];
			
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
				orderedUnits[i] = new Unit(orderedNodes[i]);
			}
			for (int i=0;i<numNodes;i++) {
				DSGNode dsgn = dsgNodes.get(i);
				for (int p : dsgn.getChildren()) {
					orderedUnits[p].unionParentUnit(orderedUnits[i]);
				}
			}

			
			Arrays.sort(orderedNodes, new Comparator<RefNode>(){
			public int compare(RefNode d1, RefNode d2) {
				return d2.originPointIdx - d1.originPointIdx;
			}});
			
			Arrays.sort(orderedUnits, new Comparator<Unit>(){
				public int compare(Unit d1, Unit d2) {
					return d2.startNode.originPointIdx - d1.startNode.originPointIdx;
				}});
			
			
		}	
	}
	

}
