package simulink;

import java.util.ArrayList;
import javafx.geometry.Point2D;
import javafx.util.Pair;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class LineList {
    private ArrayList<Pair<Point2D, Point2D>> lineList;
    private Point2D starting_pt;
    private Point2D ending_pt;
    
    public LineList(Element lineElement){
        this.lineList = new ArrayList<>();
        ArrayList<Point2D> pointList = getLines(lineElement);
        for (int i = 1; i < pointList.size(); i++)
            this.lineList.add(new Pair(pointList.get(i - 1), pointList.get(i)));

        ArrayList<Pair<Point2D, Point2D>> branchList = getBranches(lineElement, pointList.get(pointList.size() - 1));
        if (branchList != null)
            this.lineList.addAll(branchList);
    }
    
    private ArrayList<Pair<Point2D, Point2D>> getBranches(Element lineElement, Point2D starting_pt){
        NodeList branchList = lineElement.getElementsByTagName("Branch");
        for (int j = 0; j < branchList.getLength(); j++) {
            Node branchNode = branchList.item(j);
            
            if (branchNode.getNodeType() == Node.ELEMENT_NODE) {
                this.starting_pt = starting_pt;
                ArrayList <Point2D> branchPts = getLines((Element) branchNode);
                if (branchPts != null){
                    Point2D pt1;
                    Point2D pt2;
                    //branchPts.add(0, starting_pt);
                    for (int i = 1; i < branchPts.size(); i++){
                        pt1 = branchPts.get(i-1);
                        pt2 = branchPts.get(i);
                        lineList.add(new Pair(pt1, pt2));
                    }
                }
                this.ending_pt = null;
               
            } 
        }
        return lineList;
    }
    
    private ArrayList<Point2D> getLines(Element lineElement){
        ArrayList<Point2D> pointList = new ArrayList<>();
    
        NodeList propertyList = lineElement.getElementsByTagName("P");
        
        for (int j = 0; j < propertyList.getLength(); j++) {
            Node SubBlockNode = propertyList.item(j);
            if (SubBlockNode.getNodeType() == Node.ELEMENT_NODE && SubBlockNode.getParentNode().isSameNode(lineElement)) {
                Element SubBlockElement = (Element) SubBlockNode;

                String propertyName = SubBlockElement.getAttribute("Name");
                String propertyValue = SubBlockElement.getTextContent();
                int port_no;
                switch(propertyName){
                    case "Src":
                        String srcStringParts[] = propertyValue.split("#");
                        int source_id = Integer.parseInt(srcStringParts[0]);
                        srcStringParts = srcStringParts[1].split(":");
                        port_no = Integer.parseInt(srcStringParts[1]);
                        if (srcStringParts[0].equals("out"))
                            starting_pt = Block.getBlockList().get(source_id).getOutPorts().get(port_no - 1);
                        else
                            starting_pt = Block.getBlockList().get(source_id).getInPorts().get(port_no - 1);

                        break;
                        
                    case "Dst":
                        String dstStringParts[] = propertyValue.split("#");
                        int destination_id = Integer.parseInt(dstStringParts[0]); 
                        dstStringParts = dstStringParts[1].split(":");
                        port_no = Integer.parseInt(dstStringParts[1]);
                        if (dstStringParts[0].equals("out"))
                            ending_pt = Block.getBlockList().get(destination_id).getOutPorts().get(port_no - 1);
                        else
                            ending_pt = Block.getBlockList().get(destination_id).getInPorts().get(port_no - 1);

                        break;
                        
                    case "Points":
                        String[] pointStrings = propertyValue.substring(1, propertyValue.length() - 1).split(";|$");
                        
                        ArrayList<Double> pointInts = new ArrayList<>();
                        for (String point : pointStrings){
                            String[] pointString = point.split(",");
                            for (String i : pointString) {
                               double coord = Integer.parseInt(i.trim());
                               pointInts.add(coord);
                            }
                        }
                        
                        for (int i = 1; i < 3; i+=2){
                            pointInts.set(i, starting_pt.getY() + pointInts.get(i));
                            pointInts.set(i - 1, starting_pt.getX() + pointInts.get(i - 1));
                            pointList.add(new Point2D(pointInts.get(i - 1), pointInts.get(i)));
                        }
                        for (int i = 3; i < pointInts.size(); i+=2){
                            pointInts.set(i, pointInts.get(i) + pointInts.get(i - 2));
                            pointInts.set(i - 1, pointInts.get(i - 1) + pointInts.get(i - 3));
                            pointList.add(new Point2D(pointInts.get(i - 1), pointInts.get(i)));
                        }
                        break;
                }    
            }
        }
        if (starting_pt != null)
            pointList.add(0, starting_pt);
        
        if (ending_pt != null)
            pointList.add(ending_pt);

        
        return pointList;    
    }
    
    public ArrayList<Pair<Point2D, Point2D>> getLineList(){
        return this.lineList;
    }
    
}
