package simulink;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javafx.geometry.Point2D;

public class Block {
    private static HashMap <Integer, Block> blockList = new HashMap<>();

    private String name;
    private String type;
    private int sid;
    private List<Integer> position;
    private String inputs;
    private int numInputPorts;
    private int numOutputPorts;
    private boolean blockMirror;
    
    private ArrayList <Point2D> inPorts;
    private ArrayList <Point2D> outPorts;

    public Block(Element BlockElement ) {
    	this.name = BlockElement.getAttribute("Name");
        this.type = BlockElement.getAttribute("BlockType");
        this.sid = Integer.parseInt(BlockElement.getAttribute("SID"));
        this.numInputPorts = 1;
        this.numOutputPorts = 1;

        NodeList SubBlockList = BlockElement.getElementsByTagName("P");
        for (int j = 0; j < SubBlockList.getLength(); j++) {
            Node SubBlockNode = SubBlockList.item(j);
            if (SubBlockNode.getNodeType() == Node.ELEMENT_NODE) {
                Element SubBlockElement = (Element) SubBlockNode;

                String SubBlockName = SubBlockElement.getAttribute("Name");
                String SubBlockValue = SubBlockElement.getTextContent();
                switch (SubBlockName) {
                    case "Ports":
                        String[] portStrings = SubBlockValue.substring(1, SubBlockValue.length() - 1).split(",");
                        List<Integer> portList = new ArrayList<>();
                        for (String portString : portStrings) {
                            int port = Integer.parseInt(portString.trim());
                            portList.add(port);
                        }
                        if (portList.size() >= 2){
                            this.numInputPorts = portList.get(0);
                            this.numOutputPorts = portList.get(1);
                        } else {
                            this.numOutputPorts = portList.get(0);
                        }
                        break;
                    case "Position":
                            String[] positionStrings = SubBlockValue.substring(1, SubBlockValue.length() - 1).split(",");
                        List<Integer> positionList = new ArrayList<>();
                        for (String positionString : positionStrings) {
                            int position = Integer.parseInt(positionString.trim());
                            positionList.add(position);
                        }
                        this.position = positionList;
                            break;
                    case "Inputs":
                        this.inputs = SubBlockValue.trim();
                        break;
                    case "NumInputPorts":
                        this.numInputPorts = Integer.parseInt(SubBlockValue.trim());
                        break;
                    case "BlockMirror":
                        this.blockMirror = Boolean.parseBoolean(SubBlockValue.trim());
                        break;
                }
            }
        }
        Block.blockList.put(this.sid, this);
        this.inPorts = new ArrayList<>();
        this.outPorts = new ArrayList<>();
        if (this.blockMirror){
            for (int i = 0; i < this.numInputPorts; i++)
                this.inPorts.add(new Point2D((double)(i * (this.position.get(3) - this.position.get(1) - 5) / this.numInputPorts + 5), (double)(this.position.get(3))));
            for (int i = 0; i < this.numOutputPorts; i++)
                this.outPorts.add(new Point2D((double)(i * (this.position.get(3) - this.position.get(1) - 5) / this.numInputPorts + 5), (double)(this.position.get(0))));    
        } 
        else {
            for (int i = 0; i < this.numInputPorts; i++)
                this.inPorts.add(new Point2D((double)(i * (this.position.get(3) - this.position.get(1) - 5) / this.numInputPorts + 5), (double)(this.position.get(0))));
            for (int i = 0; i < this.numOutputPorts; i++)
                this.outPorts.add(new Point2D((double)(i * (this.position.get(3) - this.position.get(1) - 5) / this.numInputPorts + 5), (double)(this.position.get(3))));    
        } 
    }

    public void plot() {
        System.out.println("Plotting...");
    }

    public void print() {
        System.out.println("Block Name: " + this.name + ", Type: " + this.type + ", SID: " + this.sid);
        System.out.println("  Property Name: position" + ", Value: " + this.position);
        System.out.println("  Property Name: inputs" + ", Value: " + this.inputs);
        System.out.println("  Property Name: numInputPorts" + ", Value: " + this.numInputPorts);
        System.out.println("  Property Name: blockMirror" + ", Value: " + this.blockMirror);
    }

    public List<Integer> getPosition() {
        return position;
    }

    public String getInputs() {
        return inputs;
    }

    public boolean getBlockMirror() {
        return blockMirror;
    }

    public int getNumInputPorts() {
        return numInputPorts;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getSid() {
        return sid;
    }
    
    public static HashMap<Integer, Block> getBlockList() {
        return blockList;
    }
    public ArrayList<Point2D> getOutPorts(){
        return this.outPorts;
    }
    public ArrayList<Point2D> getInPorts(){
        return this.inPorts;
    }
}