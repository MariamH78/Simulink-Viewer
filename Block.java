package simulink;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Block {
    private String name;
    private String type;
    private String sid;
    private List<Integer> ports;
    private List<Integer> position;
    private int zOrder;
    private String iconShape;
    private String inputs;
    private String scopeSpecificationString;
    private int numInputPorts;
    private String floating;
    private String blockMirror;
    private int sampleTime;
    private String hasFrameUpgradeWarning;

    public Block(Element BlockElement ) {
    	this.name = BlockElement.getAttribute("Name");
		this.type = BlockElement.getAttribute("BlockType");
		this.sid = BlockElement.getAttribute("SID");
		System.out.println("Block Name: " + this.name + ", Type: " + this.type + ", SID: " + this.sid);

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
                	    this.ports = portList;
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
                	case "ZOrder":
                	    this.zOrder = Integer.parseInt(SubBlockValue.trim());
                	    break;
                	case "IconShape":
                	    this.iconShape = SubBlockValue.trim();
                	    break;
                	case "Inputs":
                	    this.inputs = SubBlockValue.trim();
                	    break;
                	case "ScopeSpecificationString":
                	    this.scopeSpecificationString = SubBlockValue.trim();
                	    break;
                	case "NumInputPorts":
                	    this.numInputPorts = Integer.parseInt(SubBlockValue.trim());
                	    break;
                	case "Floating":
                	    this.floating = SubBlockValue.trim();
                	    break;
                	case "BlockMirror":
                	    this.blockMirror = SubBlockValue.trim();
                	    break;
                	case "SampleTime":
                	    this.sampleTime = Integer.parseInt(SubBlockValue.trim());
                	    break;
                	case "HasFrameUpgradeWarning":
                	    this.hasFrameUpgradeWarning = SubBlockValue.trim();
                	    break;
                }
            }
        }
    }

	public void plot() {
    	System.out.println("Plotting...");
    }

	public void print() {
		System.out.println("Block Name: " + this.name + ", Type: " + this.type + ", SID: " + this.sid);
		System.out.println("  Property Name: Ports" + ", Value: " + this.ports);
		System.out.println("  Property Name: position" + ", Value: " + this.position);
		System.out.println("  Property Name: zOrder" + ", Value: " + this.zOrder);
		System.out.println("  Property Name: iconShape" + ", Value: " + this.iconShape);
		System.out.println("  Property Name: inputs" + ", Value: " + this.inputs);
		System.out.println("  Property Name: scopeSpecificationString" + ", Value: " + this.scopeSpecificationString);
		System.out.println("  Property Name: numInputPorts" + ", Value: " + this.numInputPorts);
		System.out.println("  Property Name: blockMirror" + ", Value: " + this.blockMirror);
		System.out.println("  Property Name: sampleTime" + ", Value: " + this.sampleTime);
		System.out.println("  Property Name: hasFrameUpgradeWarning" + ", Value: " + this.hasFrameUpgradeWarning);
		System.out.println("  Property Name: floating" + ", Value: " + this.floating);
	}

	public List<Integer> getPorts() {
	    return ports;
	}

	public List<Integer> getPosition() {
	    return position;
	}

	public int getZOrder() {
	    return zOrder;
	}

	public String getIconShape() {
	    return iconShape;
	}

	public String getInputs() {
	    return inputs;
	}

	public String getBlockMirror() {
	    return blockMirror;
	}

	public int getSampleTime() {
	    return sampleTime;
	}
	
	public String getHasFrameUpgradeWarning() {
	    return hasFrameUpgradeWarning;
	}
	
	public String getScopeSpecificationString() {
	    return scopeSpecificationString;
	}
	
	public int getNumInputPorts() {
	    return numInputPorts;
	}
	
	public String getFloating() {
	    return floating;
	}
	
	public String getName() {
	    return name;
	}
	
	public String getType() {
	    return type;
	}
	
	public String getSid() {
	    return sid;
	}
}