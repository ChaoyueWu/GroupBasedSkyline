package thu.course.mds.project2;

import java.util.ArrayList;
import java.util.List;

public class Test {

	public static void main(String[] args) {
		List<Point> points  = new ArrayList<Point>();
		double[]d = {4,400};
		Point p = new Point(1,d);
		points.add(p);
		double[]d1 = {24,380};
		Point p1 = new Point(2,d1);
		points.add(p1);
		double[]d2 = {14,340};
		Point p2 = new Point(3,d2);
		points.add(p2);
		double[]d3 = {36,300};
		Point p3 = new Point(4,d3);
		points.add(p3);
		double[]d4 = {26,280};
		Point p4 = new Point(5,d4);
		points.add(p4);
		double[]d5 = {8,260};
		Point p5 = new Point(6,d5);
		points.add(p5);
		double[]d6 = {40,200};
		Point p6 = new Point(7,d6);
		points.add(p6);
		double[]d7 = {20,180};
		Point p7 = new Point(8,d7);
		points.add(p7);
		double[]d8 = {34,140};
		Point p8 = new Point(9,d8);
		points.add(p8);
		double[]d9 = {28,120};
		Point p9 = new Point(10,d9);
		points.add(p9);
		double[]d10 = {16,60};
		Point p10 = new Point(11,d10);
		points.add(p10);
		
		DSGGenerator sg = new DSGGenerator(4,points,2);
		DSG dsg = sg.generateDSG();
		for(DSGNode node:dsg.DSG){
			node.showNode();
		}
	}

}
