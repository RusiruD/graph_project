package rusiru.project;

import java.util.ArrayList;
import java.util.Random;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
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

  public Node(int Nodenum, Pane root) {
    num = Nodenum;
    double radius = 25;
    double paneSize = 2 * radius;
    nodePane = new StackPane();
    Random random = new Random();
    dot = new Circle();
    dot.setRadius(radius);
    DoubleProperty widthProperty = new SimpleDoubleProperty();
    widthProperty.bind(root.widthProperty().subtract(300));
    nodePane.setLayoutX((random.nextInt((int) (root.getWidth() - 520)) + 255));
    nodePane.setLayoutY(random.nextInt((int) (root.getHeight() - 110)) + 45);

    Label txt = new Label(Integer.toString(Nodenum));
    txt.setTextFill(Color.WHITE);

    nodePane.getChildren().addAll(dot, txt);

    root.getChildren().add(nodePane);

    nodePane.setPrefSize(paneSize, paneSize);
    nodePane.setMaxSize(paneSize, paneSize);
    nodePane.setMinSize(paneSize, paneSize);

    nodePane.setOnMousePressed(
        e -> {
          sceneX = e.getSceneX();
          sceneY = e.getSceneY();
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
              || event.getSceneX() - xOffset < 240
              || event.getSceneY() - yOffset < 50
              || event.getSceneY() - yOffset > root.getHeight() - 30) {

            return;
          }
          nodePane.setLayoutX(event.getSceneX() - xOffset);
          nodePane.setLayoutY(event.getSceneY() - yOffset);
        });
    SecondaryController.nodes.add(this);
  }

  @FXML
  private void createSelfLoop(
      StackPane currentStackPane,
      Pane root,
      StackPane previousStackPane,
      Node previousNode,
      Node currentNode) {

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
    createSelfLoopArc(AppState.previousStackPane, currentStackPane, root, selfLoopEdge);
  }

  private Edge createEdge(Node sourceNode, Node destinationNode, boolean isUndirected) {
    Edge edge = new Edge(sourceNode, destinationNode, 0);
    sourceNode.setOutDegree(sourceNode.getOutDegree() + 1);
    destinationNode.setInDegree(destinationNode.getInDegree() + 1);

    SecondaryController.edges.add(edge);
    SecondaryController.adjacencyList
        .get(edge.getSource().getNodeNum())
        .add(edge.getDestinationNode().getNodeNum());

    if (isUndirected) {
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

  @FXML
  private void onNodeClicked(MouseEvent event, Pane root) {
    StackPane currentStackPane = (StackPane) event.getSource();

    if (AppState.alreadyClicked) {
      AppState.previousStackPane.getChildren().get(0).setStyle("-fx-fill: Black;");

      // if its a self loop
      if (this.equals(AppState.previousNode) && !(this.hasSelfLoop)) {
        createSelfLoop(
            currentStackPane, root, AppState.previousStackPane, AppState.previousNode, this);
      }

      if (AppState.undirected && !this.equals(AppState.previousNode)) {
        Edge edge = createEdge(AppState.previousNode, this, AppState.undirected);

        if (isMultiEdge(edge)) {
          createMultiEdge(AppState.previousStackPane, currentStackPane, root, edge);
        } else {
          createLine(AppState.previousStackPane, currentStackPane, root, edge);
        }
      } else if (!AppState.undirected && !this.equals(AppState.previousNode)) {
        Edge edge = createEdge(AppState.previousNode, this, AppState.undirected);

        if (isMultiEdge(edge)) {

          createMultiEdge(AppState.previousStackPane, currentStackPane, root, edge);

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

  private void createSelfLoopArc(
      StackPane startStackPane, StackPane endStackPane, Pane root, Edge edge) {

    QuadCurve arc = new QuadCurve();
    arc.startXProperty()
        .bind(
            startStackPane
                .layoutXProperty()
                .add(startStackPane.translateXProperty())
                .add(startStackPane.widthProperty().divide(2)));
    arc.startYProperty()
        .bind(
            startStackPane
                .layoutYProperty()
                .add(startStackPane.translateYProperty())
                .add(startStackPane.heightProperty().divide(2))
                .subtract(25));

    arc.controlXProperty()
        .bind(
            startStackPane
                .layoutXProperty()
                .add(startStackPane.translateXProperty())
                .add(startStackPane.widthProperty().divide(2))
                .add(25));
    arc.controlYProperty()
        .bind(
            startStackPane
                .layoutYProperty()
                .add(startStackPane.translateYProperty())
                .add(startStackPane.heightProperty().divide(2))
                .subtract(100));

    arc.endXProperty()
        .bind(
            startStackPane
                .layoutXProperty()
                .add(startStackPane.translateXProperty())
                .add(startStackPane.widthProperty().divide(2))
                .add(15));
    arc.endYProperty()
        .bind(
            startStackPane
                .layoutYProperty()
                .add(startStackPane.translateYProperty())
                .add(startStackPane.heightProperty().divide(2))
                .subtract(20));

    arc.setStroke(Color.BLUE);
    arc.setStrokeWidth(2);
    arc.setFill(Color.TRANSPARENT);

    if (AppState.weighted) {
      createWeightedEdge(root, null, arc, true, false, edge);
    }
    if (!AppState.undirected) {
      createSelfLoopArrow(arc, root);
    }
    root.getChildren().add(arc);
  }

  private static int generateRandomNumber() {
    ArrayList<Integer> list = new ArrayList<Integer>(21);
    for (int i = -100; i <= 100; i += 10) {
      list.add(i);
    }

    Random random = new Random();
    int range = (90 - (-90)) / 5 + 1; // Total number of possible values
    int randomIndex = random.nextInt(range);
    int randomValue = randomIndex * 5 - 90;
    return randomValue;
  }

  private static QuadCurve setControlPoint(
      QuadCurve arc, StackPane startStackPane, StackPane endStackPane) {
    ArrayList<Integer> list = new ArrayList<Integer>();

    list.add(40);
    list.add(-40);
    list.add(80);
    list.add(-80);
    list.add(120);

    list.add(-120);
    list.add(140);
    list.add(-140);
    list.add(-180);
    list.add(180);
    int num;
    int x = edgeCountBetween(startStackPane, endStackPane);
    Random random = new Random();
    if (x - 2 > 9) {
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
    } else {
      num = list.get(x - 2);
    }

    arc.controlXProperty()
        .bind(
            startStackPane
                .layoutXProperty()
                .add(startStackPane.translateXProperty())
                .add(startStackPane.widthProperty().divide(2))
                .add(num));

    if ((AppState.previousNode.getAngle() > 95 && AppState.previousNode.getAngle() < 166)
        || AppState.previousNode.getAngle() > -74 && AppState.previousNode.getAngle() < -17) {
      arc.controlYProperty()
          .bind(
              startStackPane
                  .layoutYProperty()
                  .add(startStackPane.translateYProperty())
                  .add(startStackPane.heightProperty().divide(2))
                  .add(num));
    } else {

      arc.controlYProperty()
          .bind(
              startStackPane
                  .layoutYProperty()
                  .add(startStackPane.translateYProperty())
                  .add(startStackPane.heightProperty().divide(2))
                  .subtract(num));
    }

    return arc;
  }

  private void createMultiEdge(
      StackPane startStackPane, StackPane endStackPane, Pane root, Edge edge) {

    int randomNum1 = generateRandomNumber();
    int randomNum2 = generateRandomNumber();

    QuadCurve arc = new QuadCurve();
    if (!AppState.undirected) {
      DoubleBinding endXBinding =
          Bindings.createDoubleBinding(
              () -> {
                ArrayList<Double> coordinates =
                    calculatePointAtDistanceFromEnd(
                        31, startStackPane, endStackPane, randomNum1, randomNum2);
                return coordinates.get(0);
              },
              startStackPane.layoutXProperty(),
              startStackPane.translateXProperty(),
              startStackPane.widthProperty(),
              endStackPane.layoutXProperty(),
              endStackPane.translateXProperty(),
              endStackPane.widthProperty());

      // Bind endYProperty using calculatePointAtDistanceFromEnd
      DoubleBinding endYBinding =
          Bindings.createDoubleBinding(
              () -> {
                ArrayList<Double> coordinates =
                    calculatePointAtDistanceFromEnd(
                        31, startStackPane, endStackPane, randomNum1, randomNum2);
                return coordinates.get(1);
              },
              startStackPane.layoutYProperty(),
              startStackPane.translateYProperty(),
              startStackPane.heightProperty(),
              endStackPane.layoutYProperty(),
              endStackPane.translateYProperty(),
              endStackPane.heightProperty());

      // Add listeners to update the properties when the bindings change

      arc.endXProperty().bind(endXBinding);
      arc.endYProperty().bind(endYBinding);
    } else {
      arc.endXProperty()
          .bind(
              endStackPane
                  .layoutXProperty()
                  .add(endStackPane.translateXProperty())
                  .add(endStackPane.widthProperty().divide(2)));
      arc.endYProperty()
          .bind(
              endStackPane
                  .layoutYProperty()
                  .add(endStackPane.translateYProperty())
                  .add(endStackPane.heightProperty().divide(2)));
    }

    arc.startXProperty()
        .bind(
            startStackPane
                .layoutXProperty()
                .add(startStackPane.translateXProperty())
                .add(startStackPane.widthProperty().divide(2)));

    arc.startYProperty()
        .bind(
            startStackPane
                .layoutYProperty()
                .add(startStackPane.translateYProperty())
                .add(startStackPane.heightProperty().divide(2)));

    /*  arc.controlXProperty()
        .bind(
            startStackPane
                .layoutXProperty()
                .add(startStackPane.translateXProperty())
                .add(endStackPane.translateXProperty())
                .add(startStackPane.widthProperty().divide(2))
                .subtract(randomNum));

    arc.controlYProperty()
        .bind(
            startStackPane
                .layoutYProperty()
                .add(startStackPane.translateYProperty())
                .add(endStackPane.translateYProperty())
                .add(startStackPane.heightProperty().divide(2))
                .subtract(randomNum));*/

    QuadCurve arc1 = setControlPoint(arc, startStackPane, endStackPane);

    arc1.setStroke(Color.BLUE);
    arc1.setStrokeWidth(2);
    arc1.setFill(Color.TRANSPARENT);

    if (AppState.weighted) {
      createWeightedEdge(root, null, arc1, false, true, edge);
    }
    if (!AppState.undirected) {
      createSelfLoopArrow(arc1, root);
    }
    root.getChildren().add(arc1);
    arc1.toBack();
  }

  private static ArrayList<Double> calculatePointAtDistanceFromEnd(
      double distance,
      StackPane startStackPane,
      StackPane endStackPane,
      int randomNumber,
      int randomNumber2) {
    double t = 1.0; // set t to 1 to represent the end of the curve
    double x1 =
        startStackPane
            .layoutXProperty()
            .add(startStackPane.translateXProperty())
            .add(startStackPane.widthProperty().divide(2))
            .get();
    double y1 =
        startStackPane
            .layoutYProperty()
            .add(startStackPane.translateYProperty())
            .add(startStackPane.heightProperty().divide(2))
            .get();
    double x2 =
        startStackPane
            .layoutXProperty()
            .add(startStackPane.translateXProperty())
            .add(startStackPane.widthProperty().divide(2))
            .subtract(randomNumber)
            .get();
    double y2 =
        startStackPane
            .layoutYProperty()
            .add(startStackPane.translateYProperty())
            .add(startStackPane.heightProperty().divide(2))
            .subtract(randomNumber2)
            .get();
    double x3 =
        endStackPane
            .layoutXProperty()
            .add(endStackPane.translateXProperty())
            .add(endStackPane.widthProperty().divide(2))
            .subtract(0)
            .get();
    double y3 =
        endStackPane
            .layoutYProperty()
            .add(endStackPane.translateYProperty())
            .add(endStackPane.heightProperty().divide(2))
            .subtract(0)
            .get();
    // Calculate the coordinates of the end point
    double xEnd = (1 - t) * (1 - t) * x1 + 2 * (1 - t) * t * x2 + t * t * x3;
    double yEnd = (1 - t) * (1 - t) * y1 + 2 * (1 - t) * t * y2 + t * t * y3;
    // Calculate the angle of the line formed by the end point and the control point
    double angle = Math.atan2(y3 - y2, x3 - x2);

    // Calculate the new point at a certain distance along the line
    double x = xEnd - distance * Math.cos(angle);
    double y = yEnd - distance * Math.sin(angle);
    ArrayList<Double> list = new ArrayList<Double>(2);
    list.add(x);
    list.add(y);
    return list;
  }

  private void createLine(StackPane startStackPane, StackPane endStackPane, Pane root, Edge edge) {

    Line line = new Line();
    line.setStroke(Color.BLUE);
    line.setStrokeWidth(2);

    line.startXProperty()
        .bind(
            startStackPane
                .layoutXProperty()
                .add(startStackPane.translateXProperty())
                .add(startStackPane.widthProperty().divide(2)));
    line.startYProperty()
        .bind(
            startStackPane
                .layoutYProperty()
                .add(startStackPane.translateYProperty())
                .add(startStackPane.heightProperty().divide(2)));
    line.endXProperty()
        .bind(
            endStackPane
                .layoutXProperty()
                .add(endStackPane.translateXProperty())
                .add(endStackPane.widthProperty().divide(2)));
    line.endYProperty()
        .bind(
            endStackPane
                .layoutYProperty()
                .add(endStackPane.translateYProperty())
                .add(endStackPane.heightProperty().divide(2)));

    if (AppState.weighted) {
      createWeightedEdge(root, line, null, false, false, edge);
    }
    if (!AppState.undirected) {
      createEdgeArrow(line, root);
    }
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
    root.getChildren().add(line);
    line.toBack();
  }

  private void createSelfLoopArrow(QuadCurve arc, Pane root) {
    double size = 12; // Arrow size
    StackPane arrow = new StackPane();
    arrow.setStyle(
        "-fx-background-color:#000000 ;-fx-border-width:1px;-fx-border-color:yellow ;-fx-shape:"
            + " \"M0,-4L4,0L0,4Z\""); //
    arrow.setPrefSize(size, size);
    arrow.setMaxSize(size, size);
    arrow.setMinSize(size, size);

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

  private void createEdgeArrow(Line line, Pane root) {
    double size = 12; // Arrow size
    StackPane arrow = new StackPane();
    arrow.setStyle(
        "-fx-background-color:#000000 ;-fx-border-width:1px;-fx-border-color:yellow ;-fx-shape:"
            + " \"M0,-4L4,0L0,4Z\""); //
    arrow.setPrefSize(size, size);
    arrow.setMaxSize(size, size);
    arrow.setMinSize(size, size);

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

    DoubleProperty y = new SimpleDoubleProperty();
    y.bind(Bindings.createDoubleBinding(() -> 31 * Math.sin((angle.get())), angle));

    DoubleProperty x = new SimpleDoubleProperty();
    x.bind(Bindings.createDoubleBinding(() -> 31 * Math.cos((angle.get())), angle));

    arrow.layoutXProperty().bind(line.endXProperty().subtract(size / 2).subtract(x));

    arrow.layoutYProperty().bind(line.endYProperty().subtract(size / 2).subtract(y));

    root.getChildren().add(arrow);
  }

  public void createWeightedEdge(
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

      // weightLbl.layoutXProperty().bind(arc.startXProperty().add(arc.endXProperty()).divide(2));
      // weightLbl.layoutYProperty().bind(arc.startYProperty().add(arc.endYProperty()).divide(2));

      weightTextField.layoutXProperty().bind(arc.controlXProperty());
      weightTextField.layoutYProperty().bind(arc.controlYProperty());

      if (arc.controlYProperty().get()
          > ((arc.startYProperty().get() + arc.endYProperty().get()) / 2)) {
        double s =
            (arc.controlYProperty().get()
                    - ((arc.startYProperty().get() + arc.endYProperty().get()) / 2))
                / 2;
        weightLbl.layoutYProperty().bind(arc.controlYProperty().subtract(s));

        ChangeListener<Number> arcPropertiesListener =
            new ChangeListener<Number>() {
              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double s =
                    (arc.controlYProperty().get()
                            - ((arc.startYProperty().get() + arc.endYProperty().get()) / 2))
                        / 2;
                weightLbl.layoutYProperty().bind(arc.controlYProperty().subtract(s));
              }
            };
        arc.startYProperty().addListener(arcPropertiesListener);
        arc.endYProperty().addListener(arcPropertiesListener);
        arc.controlYProperty().addListener(arcPropertiesListener);
      }

      if (arc.controlYProperty().get()
          < ((arc.startYProperty().get() + arc.endYProperty().get()) / 2)) {
        double s =
            (((arc.startYProperty().get() + arc.endYProperty().get()) / 2)
                    - arc.controlYProperty().get())
                / 2;
        weightLbl.layoutYProperty().bind(arc.controlYProperty().add(s));
        ChangeListener<Number> arcPropertiesListener =
            new ChangeListener<Number>() {
              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double s =
                    (((arc.startYProperty().get() + arc.endYProperty().get()) / 2)
                            - arc.controlYProperty().get())
                        / 2;
                weightLbl.layoutYProperty().bind(arc.controlYProperty().add(s));
              }
            };

        // Bind the listener to the startYProperty, endYProperty, and controlYProperty of the arc
        arc.startYProperty().addListener(arcPropertiesListener);
        arc.endYProperty().addListener(arcPropertiesListener);
        arc.controlYProperty().addListener(arcPropertiesListener);
      }

      if (arc.controlXProperty().get()
          > ((arc.startXProperty().get() + arc.endXProperty().get()) / 2)) {
        double s =
            (arc.controlXProperty().get()
                    - ((arc.startXProperty().get() + arc.endXProperty().get()) / 2))
                / 2;
        weightLbl.layoutXProperty().bind(arc.controlXProperty().subtract(s));
        ChangeListener<Number> arcPropertiesListener =
            new ChangeListener<Number>() {
              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double s =
                    (arc.controlXProperty().get()
                            - ((arc.startXProperty().get() + arc.endXProperty().get()) / 2))
                        / 2;
                weightLbl.layoutXProperty().bind(arc.controlXProperty().subtract(s));
              }
            };

        // Bind the listener to the startYProperty, endYProperty, and controlYProperty of the arc
        arc.startYProperty().addListener(arcPropertiesListener);
        arc.endYProperty().addListener(arcPropertiesListener);
        arc.controlYProperty().addListener(arcPropertiesListener);
      }
      if (arc.controlXProperty().get()
          < ((arc.startXProperty().get() + arc.endXProperty().get()) / 2)) {
        double s =
            (((arc.startXProperty().get() + arc.endXProperty().get()) / 2)
                    - arc.controlXProperty().get())
                / 2;
        weightLbl.layoutXProperty().bind(arc.controlXProperty().add(s));
        ChangeListener<Number> arcPropertiesListener =
            new ChangeListener<Number>() {
              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double s =
                    (((arc.startXProperty().get() + arc.endXProperty().get()) / 2)
                            - arc.controlXProperty().get())
                        / 2;
                weightLbl.layoutXProperty().bind(arc.controlXProperty().add(s));
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

  public Circle getDot() {
    return dot;
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

  public void removeNodeAndChildren(Pane root) {
    // Remove the current node
    this.getChildren().clear();

    this.getChildren().removeAll();
    root.getChildren().remove(this);

    // Remove all children recursively

  }

  public Circle getCircle() {
    return dot;
  }
}
