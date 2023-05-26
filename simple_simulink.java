package simulink;

import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javafx.application.Application;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

//extends Application
public class simple_simulink  extends Application {
 public static void main(String[] args) {
     launch(args);
 }

 @Override
 public void start(Stage primaryStage) throws Exception {
     primaryStage.setTitle("Rectangles");

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

         Pane root = new Pane();
         for (int i = 0; i < blocks.size(); i++) {
        	 blocks.get(i).print();
        	 int x = blocks.get(i).getPosition().get(0);
        	 int y = blocks.get(i).getPosition().get(1);
             int width = blocks.get(i).getPosition().get(3) - x;
             int higth = blocks.get(i).getPosition().get(4) - y;
        	 Rectangle rectangle = new Rectangle(x, y, width, higth);

             root.getChildren().add(rectangle);

             Text textNode = new Text(blocks.get(i).getName());
             textNode.setFont(new Font("Arial", 12));
             textNode.setX(x + width/2);
             textNode.setY(y + higth/2);
             root.getChildren().add(textNode);
         }

         Scene scene = new Scene(root, 800, 600);
         primaryStage.setScene(scene);
         primaryStage.show();

     } catch (Exception e) {
         System.err.println(e);
     } 
 }
}