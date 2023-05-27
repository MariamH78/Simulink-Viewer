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
import static javafx.application.Application.launch;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Pair;

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

            NodeList lineList = doc.getElementsByTagName("Line");
            List<LineList> lines = new ArrayList<LineList>();
            
            for (int i = 0; i < lineList.getLength(); i++) {
                Node lineListNode = lineList.item(i);
                if (lineListNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element lineListElement = (Element) lineListNode;
                    lines.add(new LineList(lineListElement));
                    

                }
            }
            Pane root = new Pane();
            for (int i = 0; i < blocks.size(); i++) {
                int x = blocks.get(i).getPosition().get(0);
                int y = blocks.get(i).getPosition().get(1);
                int width = blocks.get(i).getPosition().get(2) - x;
                int height = blocks.get(i).getPosition().get(3) - y;

                Rectangle rectangle = new Rectangle(x, y, width, height);
                rectangle.setFill(Color.LIGHTBLUE);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(2);

                root.getChildren().add(rectangle);

                Text textNode = new Text(blocks.get(i).getName());
                textNode.setFont(new Font("Arial", 8));
                textNode.setFill(Color.BLACK);
                textNode.setX((x + 10));
                textNode.setY((y + height/2 + textNode.getLayoutBounds().getHeight()/2));
                root.getChildren().add(textNode);
            }
            
            for (int i = 0; i < lines.size(); i++) {
                ArrayList<Pair<Point2D, Point2D>> currentList = lines.get(i).getLineList();
                for (int j = 0; j < currentList.size(); j++){
                    int x1 = (int) currentList.get(j).getKey().getX();
                    int y1 = (int) currentList.get(j).getKey().getY();
                    int x2 = (int) currentList.get(j).getValue().getX();
                    int y2 = (int) currentList.get(j).getValue().getY();
                
                    Line line = new Line(x1, y1, x2, y2);
                
                    root.getChildren().add(line);

                    Rectangle rectangle1 = new Rectangle(x1, y1, 5, 5);
                    rectangle1.setFill(Color.LIGHTBLUE);
                    rectangle1.setStroke(Color.BLACK);
                    rectangle1.setStrokeWidth(2);

                    root.getChildren().add(rectangle1);
                    
                    Rectangle rectangle2 = new Rectangle(x2, y2, 5, 5);
                    rectangle2.setFill(Color.LIGHTBLUE);
                    rectangle2.setStroke(Color.BLACK);
                    rectangle2.setStrokeWidth(2);

                    root.getChildren().add(rectangle2);

                }
            }

            Scene scene = new Scene(root, 800, 600);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            System.err.println(e);
        } 
    }
}