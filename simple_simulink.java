package simulink;

import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/*
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.SAXException;
*/

// extends Application
public class simple_simulink {
    public static void main(String[] args) {

        try (Scanner in = new Scanner(System.in)) {

            String inputFileName = in.nextLine();
            String MDL_FileText = Files.readString(Paths.get(inputFileName));

            Pattern XML_Pattern = Pattern.compile("(?s)(?<=__MWOPC_PART_BEGIN__ /simulink/systems/system_root\\.xml).*?(?=__MWOPC_PART_BEGIN__)(?-s)");
            Matcher regexMatcher = XML_Pattern.matcher(MDL_FileText);
            regexMatcher.find();
            String matchedXML = regexMatcher.group().trim();

            InputSource is = new InputSource(new StringReader(matchedXML));
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            NodeList BlockList = doc.getElementsByTagName("Block");
            List<Block> blocks = new ArrayList<Block>();
            
            for (int i = 0; i < BlockList.getLength(); i++) {
                Node BlockNode = BlockList.item(i);
                if (BlockNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element BlockElement = (Element) BlockNode;
                    blocks.add(new Block(BlockElement));
                }
            }

            for (int i = 0; i < blocks.size(); i++) {
                Block ex = blocks.get(i);
                ex.print();
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
