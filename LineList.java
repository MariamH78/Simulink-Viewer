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
                ArrayList <Point2D> branchPts = getLines((Element) branchNode);
                if (branchPts != null){
                    branchPts.add(0, starting_pt);
                    for (int i = 1; i < branchPts.size(); i++)
                        lineList.add(new Pair(branchPts.get(i - 1), branchPts.get(i)));
                }
            }
        }
        return lineList;
    }
    
    private ArrayList<Point2D> getLines(Element lineElement){
        ArrayList<Point2D> pointList = new ArrayList<>();
    
        NodeList propertyList = lineElement.getElementsByTagName("P");
        for (int j = 0; j < propertyList.getLength(); j++) {
            Node SubBlockNode = propertyList.item(j);
            if (SubBlockNode.getNodeType() == Node.ELEMENT_NODE) {
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
                            starting_pt = Block.getBlockList().get(source_id).getOutPorts().get(port_no);
                        else
                            starting_pt = Block.getBlockList().get(source_id).getInPorts().get(port_no);
                        break;
                        
                    case "Dst":
                        String dstStringParts[] = propertyValue.split("#");
                        int destination_id = Integer.parseInt(dstStringParts[0]); 
                        dstStringParts = dstStringParts[1].split(":");
                        port_no = Integer.parseInt(dstStringParts[1]);
                        if (dstStringParts[0].equals("out"))
                            ending_pt = Block.getBlockList().get(destination_id).getOutPorts().get(port_no);
                        else
                            ending_pt = Block.getBlockList().get(destination_id).getInPorts().get(port_no);
                        break;
                        
                    case "Points":
                        String[] pointStrings = propertyValue.substring(1, propertyValue.length() - 1).split(";");
                        for (String point : pointStrings){
                            String[] pointString = point.split(",");
                            ArrayList<Integer> pointInts = new ArrayList<>();
                            for (String i : pointString) {
                               int coord = Integer.parseInt(i.trim());
                               pointInts.add(coord);
                            }
                            for (int i = 0; i < pointInts.size(); i+=2){
                                pointList.add(new Point2D(pointInts.get(i), pointInts.get(i - 1)));
                            }
                        }
                        break;
                }    
            }
        }
        
        if (starting_pt != null)
            pointList.add(0, starting_pt);
        else
            pointList.get(0);
        if (ending_pt != null)
            pointList.add(ending_pt);

        
        return pointList;    
    }
    
    public ArrayList<Pair<Point2D, Point2D>> getLineList(){
        return this.lineList;
    }
    
}
