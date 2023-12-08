package rusiru.project;

import java.util.Random;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Node extends StackPane {
    private int num;
    
      public Node(int Nodenum, Pane root){
        num=Nodenum;
        double radius = 25;
        double paneSize = 2 * radius;
        StackPane nodePane = new StackPane();
         Random random = new Random();
         Circle dot = new Circle();
         dot.setRadius(radius);
        nodePane.setLayoutX((random.nextInt(550)+10));
        nodePane.setLayoutY(random.nextInt(400)+10);
       
        
        Label txt = new Label(Integer.toString(Nodenum));
        txt.setTextFill(Color.WHITE);
      
       
        nodePane.getChildren().addAll(dot,txt);
        root.getChildren().add(nodePane);
        nodePane.setPrefSize(paneSize, paneSize);
        nodePane.setMaxSize(paneSize, paneSize);
       nodePane.setMinSize(paneSize, paneSize);

       
    }
    
    public StackPane getStackPane(){
        return this;}

        public int getNodeNum(){
            return num;
        }
}
