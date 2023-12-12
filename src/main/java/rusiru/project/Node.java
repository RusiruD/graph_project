package rusiru.project;

import java.util.Random;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Shape;
import rusiru.project.controllers.SecondaryController;

public class Node extends StackPane {
    private int num;
    private int inDegree;
    private boolean hasSelfLoop;
    private int outDegree;
    double sceneX, sceneY, layoutX, layoutY;
    private double xOffset, yOffset;
    private Circle dot;

      public Node(int Nodenum, Pane root){
        num=Nodenum;
        double radius = 25;
        double paneSize = 2 * radius;
        StackPane nodePane = new StackPane();
         Random random = new Random();
        dot = new Circle();
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
       SecondaryController.nodes.add(this);
    }


    @FXML
    private void onNodeClicked(MouseEvent event, Pane root){
        StackPane currentStackPane = (StackPane) event.getSource();


        if(AppState.alreadyClicked){
            AppState.previousStackPane.getChildren().get(0).setStyle("-fx-fill: Black;");
            
            if(AppState.undirected && !this.equals(AppState.previousNode)){
           
          
                Edge edge = new Edge(AppState.previousNode, this, 0);
                Edge edge1 = new Edge(this, AppState.previousNode, 0);
               
                SecondaryController.edges.add(edge);
                SecondaryController.edges.add(edge1);
                 SecondaryController.adjacencyList.get(edge.getSource().getNodeNum()).add(edge.getDestinationNode().getNodeNum());
                  SecondaryController.adjacencyList.get(edge.getSource().getNodeNum()).add(edge.getDestinationNode().getNodeNum());
            }

            //self loop

             else if(this.equals(AppState.previousNode) && !(this.hasSelfLoop)){
                Edge edge = new Edge(this, this, 0);
                SecondaryController.edges.add(edge);
                SecondaryController.adjacencyList.get(edge.getSource().getNodeNum()).add(edge.getDestinationNode().getNodeNum());
          
       
                QuadCurve arc = createSelfLoop(AppState.previousStackPane, currentStackPane, this, AppState.previousNode, root, edge);
                root.getChildren().add(arc);
                StackPane arrow = createSelfLoopArrow(arc);
                root.getChildren().add(arrow);
            }
          
        //directed edge
        
            else if (!AppState.undirected && !this.equals(AppState.previousNode)){
                Edge edge = new Edge(AppState.previousNode, this, 0);
                Line line = createLine(AppState.previousStackPane, currentStackPane, AppState.previousNode, this, root, edge);
                root.getChildren().add(line);
                line.toBack();
                StackPane arrow = createEdgeArrow(line, AppState.previousNode, this, this.getCircle());
                root.getChildren().add(arrow);
           
      
                SecondaryController.adjacencyList.get(edge.getSource().getNodeNum()).add(edge.getDestinationNode().getNodeNum());
                SecondaryController.adjacencyList.get(edge.getDestinationNode().getNodeNum()).add(edge.getSource().getNodeNum());
                SecondaryController.edges.add(edge);
      
            }

        AppState.alreadyClicked = false;
        }

        else{
             
            currentStackPane.getChildren().get(0).setStyle("-fx-fill: red;");
            AppState.alreadyClicked = true;
            AppState.previousStackPane=currentStackPane ;
            AppState.previousNode = this;
        }
    }
    

    private QuadCurve createSelfLoop(StackPane startStackPane, StackPane endStackPane, Node startNode, Node endNode, Pane root, Edge edge){

        startNode.setoutDegree(startNode.getoutDegree()+1);
        endNode.setinDegree(endNode.getinDegree()+1);
     
        double x = startStackPane.getLayoutX() + startStackPane.getTranslateX() + startStackPane.getWidth() / 2.0;
        double y = startStackPane.getLayoutY() + startStackPane.getTranslateY() + startStackPane.getHeight() / 2.0;
        
        QuadCurve arc = new QuadCurve(x,y, x+15, y-150, x+25, y-5);
        arc.startXProperty().bind(startStackPane.layoutXProperty().add(startStackPane.translateXProperty()).add(startStackPane.widthProperty().divide(2)));
        arc.startYProperty().bind(startStackPane.layoutYProperty().add(startStackPane.translateYProperty()).add(startStackPane.heightProperty().divide(2)).subtract(25));

        arc.controlXProperty().bind(startStackPane.layoutXProperty().add(startStackPane.translateXProperty()).add(startStackPane.widthProperty().divide(2)).add(25));
        arc.controlYProperty().bind(startStackPane.layoutYProperty().add(startStackPane.translateYProperty()).add(startStackPane.heightProperty().divide(2)).subtract(100));
        
        arc.endXProperty().bind(startStackPane.layoutXProperty().add(startStackPane.translateXProperty()).add(startStackPane.widthProperty().divide(2)).add(15));
        arc.endYProperty().bind(startStackPane.layoutYProperty().add(startStackPane.translateYProperty()).add(startStackPane.heightProperty().divide(2)).subtract(20));

        arc.setStroke(Color.BLUE);
        arc.setStrokeWidth(2);
        arc.setFill(Color.TRANSPARENT);

        if(AppState.weighted){
           makeWeightedEdge(root, null, arc, true, edge);
        }
        
        return arc;



    }

    
    private Line createLine(StackPane startStackPane, StackPane endStackPane, Node startNode, Node endNode, Pane root, Edge edge){
        
        startNode.setoutDegree(startNode.getoutDegree()+1);
        endNode.setinDegree(endNode.getinDegree()+1);
     
        Line line = new Line();
        line.setStroke(Color.BLUE);
        line.setStrokeWidth(2);
        
        line.startXProperty().bind(startStackPane.layoutXProperty().add(startStackPane.translateXProperty()).add(startStackPane.widthProperty().divide(2)));
        line.startYProperty().bind(startStackPane.layoutYProperty().add(startStackPane.translateYProperty()).add(startStackPane.heightProperty().divide(2)));
        line.endXProperty().bind(endStackPane.layoutXProperty().add(endStackPane.translateXProperty()).add(endStackPane.widthProperty().divide(2)));
        line.endYProperty().bind(endStackPane.layoutYProperty().add(endStackPane.translateYProperty()).add(endStackPane.heightProperty().divide(2)));

        if(AppState.weighted){
            makeWeightedEdge(root, line, null, false, edge);
        

          
           
        }
        

        return line;
       }


       private StackPane createSelfLoopArrow(QuadCurve arc){
        double size = 12; // Arrow size
        StackPane arrow = new StackPane();
        arrow.setStyle("-fx-background-color:#000000 ;-fx-border-width:1px;-fx-border-color:yellow ;-fx-shape: \"M0,-4L4,0L0,4Z\"");//
        arrow.setPrefSize(size, size);
        arrow.setMaxSize(size, size);
        arrow.setMinSize(size, size);
        arrow.translateXProperty().bind(arc.endXProperty().subtract(size / 2).subtract(0));
        arrow.translateYProperty().bind(arc.endYProperty().subtract(size / 2).subtract(5));
        arrow.setRotate(90);
        return arrow;
       }

       private StackPane createEdgeArrow(Line line, StackPane startDot, StackPane endDot, Circle circle) {
            double size = 12; // Arrow size
            StackPane arrow = new StackPane();
            arrow.setStyle("-fx-background-color:#000000 ;-fx-border-width:1px;-fx-border-color:yellow ;-fx-shape: \"M0,-4L4,0L0,4Z\"");//
            arrow.setPrefSize(size, size);
            arrow.setMaxSize(size, size);
            arrow.setMinSize(size, size);

      
            arrow.rotateProperty().bind(Bindings.createDoubleBinding(() ->
                Math.toDegrees(Math.atan2(line.getEndY() - line.getStartY(), line.getEndX() - line.getStartX())),
                line.startXProperty(), line.startYProperty(), line.endXProperty(), line.endYProperty()));

                DoubleProperty xDifferenceProperty = new SimpleDoubleProperty();

            xDifferenceProperty.bind(Bindings.createDoubleBinding(
            () -> line.getEndX() - line.getStartX(),
            line.startXProperty(), line.endXProperty()
            ));

            DoubleProperty yDifferenceProperty = new SimpleDoubleProperty();
            yDifferenceProperty.bind(Bindings.createDoubleBinding(
            () -> line.getEndY() - line.getStartY(),
            line.startYProperty(), line.endYProperty()
           
            ));
   
            DoubleProperty angle = new SimpleDoubleProperty();
            angle.bind(Bindings.createDoubleBinding(
            () -> (Math.atan2(yDifferenceProperty.get(), xDifferenceProperty.get())),
            xDifferenceProperty, yDifferenceProperty
            ));

            DoubleProperty y = new SimpleDoubleProperty();
            y.bind(Bindings.createDoubleBinding(
            () -> 31 * Math.sin((angle.get())),
            angle
            ));

            DoubleProperty x = new SimpleDoubleProperty();
            x.bind(Bindings.createDoubleBinding(
            () -> 31 * Math.cos((angle.get())),
            angle
            ));
       
       
      
      
            arrow.layoutXProperty().bind(line.endXProperty().subtract(6).subtract(x));
    
            arrow.layoutYProperty().bind(line.endYProperty().subtract(6).subtract(y));
       
    
            return arrow;
    }
       
      
    public void makeWeightedEdge(Pane root, Line line, QuadCurve arc, boolean isSelfLoop, Edge edge){
       
            Label weightLbl = new Label("0");
            TextField weightTextField = new TextField();
            weightTextField.setVisible(false);

             weightLbl.setOnMouseClicked(event -> {
            weightLbl.setVisible(false);
            weightTextField.setText(weightLbl.getText());
            weightTextField.setPrefWidth(40);
            
            weightTextField.setVisible(true);
            weightTextField.requestFocus();
            });

            weightTextField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                weightLbl.setText(weightTextField.getText());
                weightTextField.setVisible(false);
                weightLbl.setVisible(true);
                edge.setWeight(Integer.parseInt(weightLbl.getText()));
            }});

        if(isSelfLoop){
            
            weightLbl.layoutXProperty().bind(arc.startXProperty().add(14));
            weightLbl.layoutYProperty().bind(arc.startYProperty().subtract(70));
            weightTextField.layoutXProperty().bind(arc.startXProperty().add(14));
            weightTextField.layoutYProperty().bind(arc.startYProperty().subtract(80));
        
        }
        else{
        
            // Create the label and bind its position to the midpoint
            weightLbl.layoutXProperty().bind(line.startXProperty().add(line.endXProperty()).divide(2));
            weightLbl.layoutYProperty().bind(line.startYProperty().add(line.endYProperty()).divide(2));

            weightTextField.layoutXProperty().bind(line.startXProperty().add(line.endXProperty()).divide(2));
            weightTextField.layoutYProperty().bind(line.startYProperty().add(line.endYProperty()).divide(2));
        }

        root.getChildren().addAll(weightTextField, weightLbl);
    
    }

    public StackPane getStackPane(){
        return this;}

       

        public int getNodeNum(){
            return num;
        }
        public int getinDegree(){
            return inDegree;
        }
        public int getoutDegree(){
            return outDegree;
        }
        public void setinDegree(int inDegree){
            this.inDegree = inDegree;
        }
        public void setoutDegree(int outDegree){
            this.outDegree = outDegree;
        }
        public Circle getCircle(){
            return dot;
        }

        

        
}
