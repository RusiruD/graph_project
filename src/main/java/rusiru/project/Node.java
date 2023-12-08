package rusiru.project;

import java.util.Random;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import rusiru.project.controllers.SecondaryController;

public class Node extends StackPane {
    private int num;
    double sceneX, sceneY, layoutX, layoutY;
    private double xOffset, yOffset;

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

        
      nodePane.setOnMousePressed(e -> {
        sceneX = e.getSceneX();
        sceneY = e.getSceneY();
        layoutX = nodePane.getLayoutX();
        layoutY = nodePane.getLayoutY();
    });

    nodePane.setOnMouseClicked(event -> onNodeClicked(event,root));
   nodePane.setOnMousePressed(event -> {
        xOffset = event.getSceneX() - nodePane.getLayoutX();
        yOffset = event.getSceneY() - nodePane.getLayoutY();
    });

   nodePane.setOnMouseDragged(event -> {
        nodePane.setLayoutX(event.getSceneX() - xOffset);
        nodePane.setLayoutY(event.getSceneY() - yOffset);
    });
       
    }
    @FXML
    private void onNodeClicked(MouseEvent event, Pane root){
        System.out.println("Node clicked");
        StackPane currentStackPane = (StackPane) event.getSource();
        if(AppState.alreadyClicked){
            AppState.previousStackPane.getChildren().get(0).setStyle("-fx-fill: Black;");
           
            Line line = createLine(AppState.previousStackPane, currentStackPane, this, AppState.previousNode);
            root.getChildren().add(line);
            AppState.alreadyClicked = false;
        }
        else{
             
            currentStackPane.getChildren().get(0).setStyle("-fx-fill: red;");
            AppState.alreadyClicked = true;
            AppState.previousStackPane=currentStackPane ;
            AppState.previousNode = this;
        }
    }
    private Line createLine(StackPane startStackPane, StackPane endStackPane, Node startNode, Node endNode){
        
        Edge edge = new Edge(startNode, endNode, 0);
        Edge edge1 = new Edge(endNode, startNode, 0);
        SecondaryController.edges.add(edge);
        SecondaryController.edges.add(edge1);
        Line line = new Line();
        line.setStroke(Color.BLUE);
        line.setStrokeWidth(2);
        line.startXProperty().bind(startStackPane.layoutXProperty().add(startStackPane.translateXProperty()).add(startStackPane.widthProperty().divide(2)));
        line.startYProperty().bind(startStackPane.layoutYProperty().add(startStackPane.translateYProperty()).add(startStackPane.heightProperty().divide(2)));
        line.endXProperty().bind(endStackPane.layoutXProperty().add(endStackPane.translateXProperty()).add(endStackPane.widthProperty().divide(2)));
        line.endYProperty().bind(endStackPane.layoutYProperty().add(endStackPane.translateYProperty()).add(endStackPane.heightProperty().divide(2)));
        return line;
       }

    public StackPane getStackPane(){
        return this;}

        public int getNodeNum(){
            return num;
        }

        

        
}
