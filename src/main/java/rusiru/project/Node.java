package rusiru.project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;
import javafx.scene.text.Font;
import rusiru.project.controllers.SecondaryController;

public class Node extends StackPane {
  private int num;
  private int inDegree;
  private boolean hasSelfLoop;
  private int outDegree;
  StackPane nodePane;
  double sceneX, sceneY, layoutX, layoutY;
  private double xOffset, yOffset;
  private Circle dot;
  private double angle;
  private static double radius = 25;
  private static Random random = new Random();

  public Node(int Nodenum, Pane root) {
    num = Nodenum;
    nodePane = new StackPane();
    dot = new Circle(radius);

    // Create a value binded to the left edge of the properties pane
    DoubleProperty widthProperty = new SimpleDoubleProperty();
    widthProperty.bind(root.widthProperty().subtract(300));

    // randomise the position of the node between bounds
    nodePane.setLayoutX((random.nextInt((int) (root.getWidth() - 540)) + 255));
    nodePane.setLayoutY(random.nextInt((int) (root.getHeight() - 110)) + 45);
    nodePane.setCursor(Cursor.MOVE);
    Label txt = new Label(Integer.toString(Nodenum));
    txt.setTextFill(Color.WHITE);

    nodePane.getChildren().addAll(dot, txt);
    root.getChildren().add(nodePane);

    makeNodeDraggable(nodePane, root, widthProperty);
    SecondaryController.nodes.add(this);
  }

  @FXML
  private void makeNodeDraggable(Pane nodePane, Pane root, DoubleProperty widthProperty) {
    // allow node to be dragged
    nodePane.setOnMousePressed(
        event -> {
          sceneX = event.getSceneX();
          sceneY = event.getSceneY();
          layoutX = nodePane.getLayoutX();
          layoutY = nodePane.getLayoutY();
        });

    nodePane.setOnMouseClicked(event -> onNodeClicked(event, root));

    nodePane.setOnMousePressed(
        event -> {
          xOffset = event.getSceneX() - nodePane.getLayoutX();
          yOffset = event.getSceneY() - nodePane.getLayoutY();
        });

    nodePane.setOnMouseDragged(
        event -> {
          if (event.getSceneX() - xOffset > widthProperty.get()
              || event.getSceneX() - xOffset < 270
              || event.getSceneY() - yOffset < 50
              || event.getSceneY() - yOffset > root.getHeight() - 30) {

            return;
          }
          nodePane.setLayoutX(event.getSceneX() - xOffset);
          nodePane.setLayoutY(event.getSceneY() - yOffset);
        });
  }

  @FXML
  private void onNodeClicked(MouseEvent event, Pane root) {
    StackPane currentStackPane = (StackPane) event.getSource();

    if (AppState.alreadyClicked) {
      AppState.previousStackPane.getChildren().get(0).setStyle("-fx-fill: Black;");

      // if its a self loop
      if (this.equals(AppState.previousNode) && !(this.hasSelfLoop)) {
        createSelfLoopEdge(currentStackPane, root, this);
      }

      if (AppState.undirected && !this.equals(AppState.previousNode)) {
        Edge edge = createEdge(AppState.previousNode, this);

        if (isMultiEdge(edge)) {
          createMultiEdgeArc(AppState.previousStackPane, currentStackPane, root, edge);
        } else {
          createLine(AppState.previousStackPane, currentStackPane, root, edge);
        }
      } else if (!AppState.undirected && !this.equals(AppState.previousNode)) {
        Edge edge = createEdge(AppState.previousNode, this);

        if (isMultiEdge(edge)) {
          createMultiEdgeArc(AppState.previousStackPane, currentStackPane, root, edge);

        } else {
          createLine(AppState.previousStackPane, currentStackPane, root, edge);
        }
      }

      AppState.alreadyClicked = false;
    } else {

      currentStackPane.getChildren().get(0).setStyle("-fx-fill: red;");
      AppState.alreadyClicked = true;
      AppState.previousStackPane = currentStackPane;
      AppState.previousNode = this;
    }
  }

  @FXML
  private void createSelfLoopEdge(StackPane currentStackPane, Pane root, Node currentNode) {

    currentNode.hasSelfLoop = true;
    // creates the edge
    Edge selfLoopEdge = new Edge(currentNode, currentNode, 0);
    currentNode.setOutDegree(currentNode.getOutDegree() + 1);
    currentNode.setInDegree(currentNode.getInDegree() + 1);

    SecondaryController.edges.add(selfLoopEdge);
    SecondaryController.adjacencyList
        .get(selfLoopEdge.getSource().getNodeNum())
        .add(selfLoopEdge.getDestinationNode().getNodeNum());

    // creates the visual representation
    createSelfLoopArc(currentStackPane, root, selfLoopEdge);
  }

  private void createSelfLoopArc(StackPane startStackPane, Pane root, Edge edge) {

    QuadCurve arc = new QuadCurve();

    arc.startXProperty().bind(startStackPane.layoutXProperty().add(radius));
    arc.startYProperty().bind(startStackPane.layoutYProperty().add(radius).subtract(25));

    arc.controlXProperty().bind(startStackPane.layoutXProperty().add(radius).add(25));
    arc.controlYProperty().bind(startStackPane.layoutYProperty().add(radius).subtract(100));

    arc.endXProperty().bind(startStackPane.layoutXProperty().add(radius).add(15));
    arc.endYProperty().bind(startStackPane.layoutYProperty().add(radius).subtract(20));

    arc.setStroke(Color.BLUE);
    arc.setStrokeWidth(2);
    arc.setFill(Color.TRANSPARENT);

    if (AppState.weighted) {
      makeEdgeWeighted(root, null, arc, true, false, edge);
    }
    if (!AppState.undirected) {
      makeArcDirected(arc, root);
    }
    root.getChildren().add(arc);
  }

  private Edge createEdge(Node sourceNode, Node destinationNode) {
    Edge edge = new Edge(sourceNode, destinationNode, 0);
    sourceNode.setOutDegree(sourceNode.getOutDegree() + 1);
    destinationNode.setInDegree(destinationNode.getInDegree() + 1);

    SecondaryController.edges.add(edge);
    SecondaryController.adjacencyList
        .get(edge.getSource().getNodeNum())
        .add(edge.getDestinationNode().getNodeNum());

    if (AppState.undirected) {
      Edge reverseEdge = new Edge(destinationNode, sourceNode, 0);
      destinationNode.setOutDegree(destinationNode.getOutDegree() + 1);
      sourceNode.setInDegree(sourceNode.getInDegree() + 1);
      SecondaryController.edges.add(reverseEdge);
      SecondaryController.adjacencyList
          .get(reverseEdge.getSource().getNodeNum())
          .add(reverseEdge.getDestinationNode().getNodeNum());
    }

    return edge;
  }

  private void createMultiEdgeArc(
      StackPane startStackPane, StackPane endStackPane, Pane root, Edge edge) {

    // create the visual representaiton of an edge as an edge between nodes that already has a edge
    // between them

    QuadCurve arc = new QuadCurve();
    // set the start point of the arc of the edge to the center of the start node
    arc.startXProperty().bind(startStackPane.layoutXProperty().add(radius));

    arc.startYProperty().bind(startStackPane.layoutYProperty().add(radius));
    setControlPointMultiEdge(arc, startStackPane, endStackPane);
    // if the edge is directed set the end point to the perimeter of the end node
    if (!AppState.undirected) {

      // Bind endXProperty using calculatePointAtDistanceFromEnd
      DoubleBinding endXBinding =
          Bindings.createDoubleBinding(
              () -> {
                ArrayList<Double> coordinates = calculatePointAtDistanceFromEnd(endStackPane, arc);
                return coordinates.get(0);
              },
              endStackPane.layoutXProperty(),
              endStackPane.widthProperty());

      // Bind endYProperty using calculatePointAtDistanceFromEnd
      DoubleBinding endYBinding =
          Bindings.createDoubleBinding(
              () -> {
                ArrayList<Double> coordinates = calculatePointAtDistanceFromEnd(endStackPane, arc);
                return coordinates.get(1);
              },
              endStackPane.layoutYProperty(),
              endStackPane.heightProperty());

      // Add listeners to update the properties when the bindings change

      arc.endXProperty().bind(endXBinding);
      arc.endYProperty().bind(endYBinding);

      makeArcDirected(arc, root);

    }

    // if the edge is undirected set the end point to the center of the end node
    else {
      arc.endXProperty().bind(endStackPane.layoutXProperty().add(radius));

      arc.endYProperty().bind(endStackPane.layoutYProperty().add(radius));
    }

    // set the control point of the arc

    // set the style of the arc
    arc.setStroke(Color.BLUE);
    arc.setStrokeWidth(2);
    arc.setFill(Color.TRANSPARENT);
    // if its weighted create the label
    if (AppState.weighted) {
      makeEdgeWeighted(root, null, arc, false, true, edge);
    }
    // if its directed create the arrow

    root.getChildren().add(arc);
    arc.toBack();
  }

  private void createLine(StackPane startStackPane, StackPane endStackPane, Pane root, Edge edge) {
    Line line = new Line();
    line.setStroke(Color.BLUE);
    line.setStrokeWidth(2);

    line.startXProperty().bind(startStackPane.layoutXProperty().add(radius));
    line.startYProperty().bind(startStackPane.layoutYProperty().add(radius));
    line.endXProperty().bind(endStackPane.layoutXProperty().add(radius));
    line.endYProperty().bind(endStackPane.layoutYProperty().add(radius));

    calculateNodeAngle(line);
    if (AppState.weighted) {
      makeEdgeWeighted(root, line, null, false, false, edge);
    }
    if (!AppState.undirected) {
      makeLineDirected(line, root);
    }

    root.getChildren().add(line);
    line.toBack();
  }

  private static void setControlPointMultiEdge(
      QuadCurve arc, StackPane startStackPane, StackPane endStackPane) {
    // create a list of possible values for the control point
    ArrayList<Integer> list = new ArrayList<Integer>();
    Collections.addAll(list, 40, -40, 80, -80, 120, -120, 140, -140, -180, 180);
    int num;
    int numEdges = edgeCountBetween(startStackPane, endStackPane);

    // if there are more edges between the 2 nodes than values in the list randomly generate a value
    if (numEdges - 2 > 9) {

      if (random.nextBoolean()) {
        // Generate a random number greater than 190
        int minValue = 191; // Minimum value (exclusive)
        int maxValue = 300; // Maximum value

        int randomNumber = random.nextInt(maxValue - minValue) + minValue;
        num = randomNumber;
      } else {
        int minValue = -300; // Minimum value
        int maxValue = -191; // Maximum value (exclusive)

        int randomNumber = random.nextInt(maxValue - minValue) + minValue;
        num = randomNumber;
      }
    }

    // depending on the number of edges between the 2 nodes choose a value from the list to set as a
    // control point
    else {
      num = list.get(numEdges - 2);
    }
    arc.controlXProperty().bind(startStackPane.layoutXProperty().add(radius).add(num));

    // depending on the angle the nodes are at relatively adjust the control point to ensure the
    // edges are clearly seperable
    double nodeAngle = AppState.previousNode.getAngle();
    if ((nodeAngle > 95 && nodeAngle < 166) || nodeAngle > -74 && nodeAngle < -17) {
      arc.controlYProperty().bind(startStackPane.layoutYProperty().add(radius).add(num));
    } else {

      arc.controlYProperty().bind(startStackPane.layoutYProperty().add(radius).subtract(num));
    }
  }

  private static ArrayList<Double> calculatePointAtDistanceFromEnd(
      StackPane endStackPane, QuadCurve arc) {
    double distance = 31;

    // calculate the coordinates of the start point, end point
    double endX = endStackPane.layoutXProperty().add(radius).get();
    double endY = endStackPane.layoutYProperty().add(radius).get();

    // Calculate the angle of the line formed by the end point and the control point
    double angle =
        Math.atan2(endY - arc.controlYProperty().get(), endX - arc.controlXProperty().get());

    // Calculate the new point at a certain distance along the line
    double x = endX - distance * Math.cos(angle);
    double y = endY - distance * Math.sin(angle);
    ArrayList<Double> list = new ArrayList<Double>(2);
    Collections.addAll(list, x, y);

    return list;
  }

  private void calculateNodeAngle(Line line) {
    double x = line.startXProperty().get() - line.endXProperty().get();
    double y = line.startYProperty().get() - line.endYProperty().get();
    angle = Math.toDegrees(Math.atan2(y, x));
    AppState.previousNode.setAngle(angle);

    ChangeListener<Number> angleListener =
        new ChangeListener<Number>() {
          @Override
          public void changed(
              ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            double x = line.startXProperty().get() - line.endXProperty().get();
            double y = line.startYProperty().get() - line.endYProperty().get();
            angle = Math.toDegrees(Math.atan2(y, x));

            AppState.previousNode.setAngle(angle);
          }
        };

    // Add the listener to the endpoint coordinates
    line.startXProperty().addListener(angleListener);
    line.startYProperty().addListener(angleListener);
    line.endXProperty().addListener(angleListener);
    line.endYProperty().addListener(angleListener);
  }

  private void makeArcDirected(QuadCurve arc, Pane root) {
    double size = 12; // Arrow size
    StackPane arrow = createArrow(size);

    arrow.translateXProperty().bind(arc.endXProperty().subtract(size / 2));
    arrow.translateYProperty().bind(arc.endYProperty().subtract(size / 2));
    arrow
        .rotateProperty()
        .bind(
            Bindings.createDoubleBinding(
                () ->
                    Math.toDegrees(
                        Math.atan2(
                            arc.getEndY() - arc.getStartY(), arc.getEndX() - arc.getStartX())),
                arc.startXProperty(),
                arc.startYProperty(),
                arc.endXProperty(),
                arc.endYProperty()));

    root.getChildren().add(arrow);
  }

  private StackPane createArrow(double size) {
    // Arrow size
    StackPane arrow = new StackPane();
    arrow.setStyle(
        "-fx-background-color:#000000 ;-fx-border-width:1px;-fx-border-color:yellow ;-fx-shape:"
            + " \"M0,-4L4,0L0,4Z\""); //
    arrow.setPrefSize(size, size);

    return arrow;
  }

  private void makeLineDirected(Line line, Pane root) {
    double size = 12; // Arrow size
    StackPane arrow = createArrow(size);
    arrow
        .rotateProperty()
        .bind(
            Bindings.createDoubleBinding(
                () ->
                    Math.toDegrees(
                        Math.atan2(
                            line.getEndY() - line.getStartY(), line.getEndX() - line.getStartX())),
                line.startXProperty(),
                line.startYProperty(),
                line.endXProperty(),
                line.endYProperty()));

    DoubleProperty xDifferenceProperty = new SimpleDoubleProperty();

    xDifferenceProperty.bind(
        Bindings.createDoubleBinding(
            () -> line.getEndX() - line.getStartX(), line.startXProperty(), line.endXProperty()));

    DoubleProperty yDifferenceProperty = new SimpleDoubleProperty();
    yDifferenceProperty.bind(
        Bindings.createDoubleBinding(
            () -> line.getEndY() - line.getStartY(), line.startYProperty(), line.endYProperty()));

    DoubleProperty angle = new SimpleDoubleProperty();
    angle.bind(
        Bindings.createDoubleBinding(
            () -> (Math.atan2(yDifferenceProperty.get(), xDifferenceProperty.get())),
            xDifferenceProperty,
            yDifferenceProperty));

    DoubleProperty yValue = new SimpleDoubleProperty();
    yValue.bind(Bindings.createDoubleBinding(() -> 31 * Math.sin((angle.get())), angle));

    DoubleProperty xValue = new SimpleDoubleProperty();
    xValue.bind(Bindings.createDoubleBinding(() -> 31 * Math.cos((angle.get())), angle));

    arrow.layoutXProperty().bind(line.endXProperty().subtract(size / 2).subtract(xValue));
    arrow.layoutYProperty().bind(line.endYProperty().subtract(size / 2).subtract(yValue));

    root.getChildren().add(arrow);
  }

  public void makeEdgeWeighted(
      Pane root, Line line, QuadCurve arc, boolean isSelfLoop, boolean isMultiEdge, Edge edge) {

    Label weightLbl = new Label("0");
    weightLbl.setId("weightLabel");
    weightLbl.setFont(new Font("Arial", 16));

    TextField weightTextField = new TextField();
    weightTextField.setId("weightTextField");

    weightTextField.setVisible(false);

    weightLbl.setOnMouseClicked(
        event -> {
          weightLbl.setVisible(false);
          weightTextField.setText(weightLbl.getText());

          weightTextField.setPrefWidth(40);

          weightTextField.setVisible(true);
          weightTextField.requestFocus();
        });

    weightTextField.setOnKeyPressed(
        event -> {
          if (event.getCode() == KeyCode.ENTER) {
            weightLbl.setText(weightTextField.getText());
            weightTextField.setVisible(false);
            weightLbl.setVisible(true);

            if (AppState.undirected) {
              for (Edge e : SecondaryController.edges) {
                if (e.getSource().equals(edge.getSource())
                    && e.getDestinationNode().equals(edge.getDestinationNode())
                    && !e.equals(edge)) {
                  e.setWeight(Integer.parseInt(weightLbl.getText()));
                }
              }
            }

            edge.setWeight(Integer.parseInt(weightLbl.getText()));
          }
        });

    if (isSelfLoop) {

      weightLbl.layoutXProperty().bind(arc.startXProperty().add(14));
      weightLbl.layoutYProperty().bind(arc.startYProperty().subtract(70));
      weightTextField.layoutXProperty().bind(arc.startXProperty().add(14));
      weightTextField.layoutYProperty().bind(arc.startYProperty().subtract(70));

    } else if (isMultiEdge) {

      double averageArcYProperty = ((arc.startYProperty().get() + arc.endYProperty().get()) / 2);
      double averageArcXProperty = ((arc.startXProperty().get() + arc.endXProperty().get()) / 2);

      if (arc.controlYProperty().get() > (averageArcYProperty)) {
        double halfDifference = (arc.controlYProperty().get() - (averageArcYProperty)) / 2;
        weightLbl.layoutYProperty().bind(arc.controlYProperty().subtract(halfDifference));
        weightTextField.layoutYProperty().bind(arc.controlYProperty().subtract(halfDifference));

        ChangeListener<Number> arcPropertiesListener =
            new ChangeListener<Number>() {
              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double halfDifference =
                    (arc.controlYProperty().get()
                            - ((arc.startYProperty().get() + arc.endYProperty().get()) / 2))
                        / 2;
                weightLbl.layoutYProperty().bind(arc.controlYProperty().subtract(halfDifference));
                weightTextField
                    .layoutYProperty()
                    .bind(arc.controlYProperty().subtract(halfDifference));
              }
            };
        arc.startYProperty().addListener(arcPropertiesListener);
        arc.endYProperty().addListener(arcPropertiesListener);
        arc.controlYProperty().addListener(arcPropertiesListener);
      }

      if (arc.controlYProperty().get() < (averageArcYProperty)) {
        double halfDifference = (((averageArcYProperty) - arc.controlYProperty().get()) / 2);
        weightLbl.layoutYProperty().bind(arc.controlYProperty().add(halfDifference));
        weightTextField.layoutYProperty().bind(arc.controlYProperty().add(halfDifference));
        ChangeListener<Number> arcPropertiesListener =
            new ChangeListener<Number>() {
              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double halfDifference =
                    (((arc.startYProperty().get() + arc.endYProperty().get()) / 2)
                            - arc.controlYProperty().get())
                        / 2;
                weightLbl.layoutYProperty().bind(arc.controlYProperty().add(halfDifference));
                weightTextField.layoutYProperty().bind(arc.controlYProperty().add(halfDifference));
              }
            };

        // Bind the listener to the startYProperty, endYProperty, and controlYProperty of the arc
        arc.startYProperty().addListener(arcPropertiesListener);
        arc.endYProperty().addListener(arcPropertiesListener);
        arc.controlYProperty().addListener(arcPropertiesListener);
      }

      if (arc.controlXProperty().get() > (averageArcXProperty)) {
        double halfDifference = ((arc.controlXProperty().get() - (averageArcXProperty)) / 2);
        weightLbl.layoutXProperty().bind(arc.controlXProperty().subtract(halfDifference));
        weightTextField.layoutXProperty().bind(arc.controlXProperty().subtract(halfDifference));
        ChangeListener<Number> arcPropertiesListener =
            new ChangeListener<Number>() {
              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double halfDifference =
                    (arc.controlXProperty().get()
                            - ((arc.startXProperty().get() + arc.endXProperty().get()) / 2))
                        / 2;
                weightLbl.layoutXProperty().bind(arc.controlXProperty().subtract(halfDifference));
                weightTextField
                    .layoutXProperty()
                    .bind(arc.controlXProperty().subtract(halfDifference));
              }
            };

        // Bind the listener to the startYProperty, endYProperty, and controlYProperty of the arc
        arc.startYProperty().addListener(arcPropertiesListener);
        arc.endYProperty().addListener(arcPropertiesListener);
        arc.controlYProperty().addListener(arcPropertiesListener);
      }

      if (arc.controlXProperty().get() < (averageArcXProperty)) {
        double halfDifference = ((averageArcXProperty - arc.controlXProperty().get()) / 2);

        weightLbl.layoutXProperty().bind(arc.controlXProperty().add(halfDifference));
        weightTextField.layoutXProperty().bind(arc.controlXProperty().add(halfDifference));

        ChangeListener<Number> arcPropertiesListener =
            new ChangeListener<Number>() {
              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double halfDifference =
                    (((arc.startXProperty().get() + arc.endXProperty().get()) / 2)
                            - arc.controlXProperty().get())
                        / 2;
                weightLbl.layoutXProperty().bind(arc.controlXProperty().add(halfDifference));
                weightTextField.layoutXProperty().bind(arc.controlXProperty().add(halfDifference));
              }
            };

        // Bind the listener to the startYProperty, endYProperty, and controlYProperty of the arc
        arc.startYProperty().addListener(arcPropertiesListener);
        arc.endYProperty().addListener(arcPropertiesListener);
        arc.controlYProperty().addListener(arcPropertiesListener);
      }

    } else {

      // Create the label and bind its position to the midpoint
      weightLbl.layoutXProperty().bind(line.startXProperty().add(line.endXProperty()).divide(2));
      weightLbl.layoutYProperty().bind(line.startYProperty().add(line.endYProperty()).divide(2));

      weightTextField
          .layoutXProperty()
          .bind(line.startXProperty().add(line.endXProperty()).divide(2));
      weightTextField
          .layoutYProperty()
          .bind(line.startYProperty().add(line.endYProperty()).divide(2));
    }

    root.getChildren().addAll(weightTextField, weightLbl);
  }

  public boolean isMultiEdge(Edge edge) {
    for (Edge e : SecondaryController.edges) {
      if (e.getSource().equals(edge.getSource())
          && e.getDestinationNode().equals(edge.getDestinationNode())
          && !e.equals(edge)) {
        return true;
      }
    }
    return false;
  }

  public static int edgeCountBetween(StackPane startStackPane, StackPane endStackPane) {
    int i = 0;
    for (Edge edge : SecondaryController.edges) {

      if (edge.getSource().getStackPane().equals(startStackPane)
          && edge.getDestinationNode().getStackPane().equals(endStackPane)) {
        i++;
      }
    }
    return i;
  }

  public StackPane getStackPane() {
    return nodePane;
  }

  public int getNodeNum() {
    return num;
  }

  public int getInDegree() {
    return inDegree;
  }

  public int getOutDegree() {
    return outDegree;
  }

  public void setInDegree(int newInDegree) {
    this.inDegree = newInDegree;
  }

  public void setOutDegree(int newOutDegree) {
    this.outDegree = newOutDegree;
  }

  public double getAngle() {
    return angle;
  }

  public void setAngle(double angle) {
    this.angle = angle;
  }
}
