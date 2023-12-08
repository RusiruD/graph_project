package rusiru.project;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class PrimaryController {
    @FXML Button submitBtn;
    @FXML TextField numNodeTextField;

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
