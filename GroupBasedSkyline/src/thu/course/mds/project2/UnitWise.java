package thu.course.mds.project2;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import javax.tools.JavaFileObject.Kind;

import org.omg.CORBA.PUBLIC_MEMBER;
import org.omg.CORBA.UnionMember;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class UnitWise {
	public List<List<Integer>> unitWiseCalculate(int k , DSG dsg){
		
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
		List<RefNode> startNodes = refdsg.startNodes;
		startNodes.sort(new Comparator<RefNode>(){
			public int compare(RefNode d1, RefNode d2) {
				return d2.originPointIdx - d1.originPointIdx;
			}
		});
		
		Map<RefNode, Unit> nodeToUnit = refdsg.nodeToUnit;
		/////////////////////
		
		int numStarters = startNodes.size();
		UnitGroup[] singleUnitGroups = new UnitGroup[numStarters];
		Unit[] startUnits = new Unit[numStarters];
		for (int i =0;i<numStarters;i++) {
			singleUnitGroups[i] = new UnitGroup(nodeToUnit.get(startNodes.get(i)), i);
			startUnits[i] = new Unit(startNodes.get(i));
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
			
			int i = 2;
			List<UnitGroup> candiGroups = new ArrayList<UnitGroup>();
			candiGroups.add(singleUnitGroup);
			Map<Integer, List<UnitGroup>> iToCandiGroups = new HashMap<Integer, List<UnitGroup>>();
			iToCandiGroups.put(1, candiGroups);
			while (true) {
				
				candiGroups = iToCandiGroups.get(i - 1);
				if (candiGroups.isEmpty()) {
					break;
				}
				List<UnitGroup> newCandiGroups = new ArrayList<UnitGroup>();
					
				int curCandiGroupIdx = 0;
				while (curCandiGroupIdx < candiGroups.size()) {
					UnitGroup curCandiGroup = candiGroups.get(curCandiGroupIdx);
					
					Set<RefNode> ps = new HashSet<RefNode>();
					for (Unit u : curCandiGroup.units) {
						ps.addAll(u.parents);
					}
					
					List<Unit> tailSetOfCandiGroup = new LinkedList<Unit>();
					List<Integer> tailIdxList = new LinkedList<Integer>();
					for (int c=curCandiGroup.tailIdx+1;c<numStarters;c++) {
						if (!ps.contains(startUnits[c].startNode)) {
							tailSetOfCandiGroup.add(startUnits[c]);
							tailIdxList.add(c);
						}
					}
					
					for (int c=0;c<tailSetOfCandiGroup.size();c++) {
						UnitGroup newUnitGroup = new UnitGroup(curCandiGroup);
						newUnitGroup.addUnit(tailSetOfCandiGroup.get(c));
						newUnitGroup.tailIdx = tailIdxList.get(c);
						newCandiGroups.add(newUnitGroup);				
					}
					curCandiGroupIdx += 1;
				}
				
				iToCandiGroups.put(i, newCandiGroups);
				for (int c =0;c<newCandiGroups.size();c++) {
					if (newCandiGroups.get(c).nodes.size() == k) {
						List<Integer> newResult = new ArrayList<Integer>();
						for (RefNode rn : newCandiGroups.get(c).nodes) {
							newResult.add(rn.originPointIdx);
						}
						resultIdx.add(newResult);
					}
					if (newCandiGroups.get(c).nodes.size() >= k) {
						newCandiGroups.remove(c);
						c --;
					}
				}
				
				i++;
			}		
		}
		/////////////////////
		
		return resultIdx;
	}
	
		
	class UnitGroup {
		
		int tailIdx = -1;
		Set<Unit> units = new HashSet<Unit>();
		Set<RefNode> nodes = new HashSet<RefNode>();
		
		public UnitGroup(Unit u, int tailIdx) {
			this.units.add(u);
			this.nodes.addAll(u.parents);
			this.nodes.add(u.startNode);
			this.tailIdx = tailIdx;
		}
		
		public UnitGroup(UnitGroup ug) {
			this.units.addAll(ug.units);
			this.nodes.addAll(ug.nodes);
			this.tailIdx = ug.tailIdx;
		}
		
		
		public void addUnit(Unit u) {
			this.units.add(u);
			this.nodes.addAll(u.parents);
			this.nodes.add(u.startNode);
			
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
		public void addParent(RefNode p) {
			if (parents == null) {
				parents = new HashSet<RefNode>();
			}
			parents.add(p);
		}
		public void addChild(RefNode p) {
			if (children == null) {
				children = new HashSet<RefNode>();
			}
			children.add(p);
		}
	}
	
	
	class RefDSG {
		
		List<RefNode> startNodes = new ArrayList<RefNode>();
		Map<RefNode, Unit> nodeToUnit = new HashMap<RefNode, Unit>();
		RefNode[] orderedNodes = null;
		Unit[] orderedUnits = null;
		int numNodes = 0;
				
		public RefDSG(DSG dsg) {
			List<DSGNode> dsgNodes = dsg.DSG;
			numNodes = dsgNodes.size();
			orderedNodes = new RefNode[numNodes];
			orderedUnits = new Unit[numNodes];
			
			for (int i=0;i<numNodes;i++) {
				orderedNodes[i] = new RefNode(dsgNodes.get(i).getPointIndex());
				orderedUnits[i] = new Unit(orderedNodes[i]);
				nodeToUnit.put(orderedNodes[i], orderedUnits[i]);
			}
			for (int i=0;i<numNodes;i++) {
				DSGNode dsgn = dsgNodes.get(i);
				
				// add only direct parents
				Set<RefNode> grandParents = new HashSet<RefNode>();
				for (int p : dsgn.getParents()) {
					if (orderedNodes[p].parents != null) {
						grandParents.addAll(orderedNodes[p].parents);
					}
				} 
				for (int p : dsgn.getParents()) {
					if (!grandParents.contains(orderedNodes[p])) {
						orderedNodes[i].addParent(orderedNodes[p]);
					}
				}
				
				
				if (orderedNodes[i].parents == null || orderedNodes[i].parents.size() <= 1) {
					startNodes.add(orderedNodes[i]);
				}
				for (int p : dsgn.getChildren()) {
					orderedNodes[i].addChild(orderedNodes[p]);
					orderedUnits[p].unionParentUnit(orderedUnits[i]);
				}
			}
		}	
	}
	

}
