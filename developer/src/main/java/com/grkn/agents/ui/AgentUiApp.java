package com.grkn.agents.ui;

import com.grkn.agents.core.AgentResult;
import com.grkn.agents.core.DeveloperAgentOrchestrator;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AgentUiApp extends Application {

    @Override
    public void start(Stage stage) {
        Label title = new Label("AI Developer Agent Console");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #1f2937;");

        Label subtitle = new Label("Provide repository and goal, then execute the flow");
        subtitle.setStyle("-fx-font-size: 12px; -fx-text-fill: #6b7280;");

        VBox header = new VBox(6, title, subtitle);

        Label repoLabel = new Label("Repo path:");
        repoLabel.setStyle("-fx-font-weight: bold;");
        TextField repoPathField = new TextField();
        repoPathField.setPromptText("C:\\\\path\\\\to\\\\repository");

        Label goalLabel = new Label("Goal:");
        goalLabel.setStyle("-fx-font-weight: bold;");
        TextArea goalArea = new TextArea();
        goalArea.setPromptText("Describe the engineering task for AI...");
        goalArea.setWrapText(true);
        goalArea.setPrefRowCount(6);

        Button executeButton = new Button("Execute");
        executeButton.setPrefWidth(140);
        executeButton.setStyle("-fx-background-color: #2563eb; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;");

        HBox actionRow = new HBox(executeButton);
        actionRow.setAlignment(Pos.CENTER_RIGHT);

        VBox inputSection = new VBox(8, repoLabel, repoPathField, goalLabel, goalArea, actionRow);

        Label criticsLabel = new Label("Critics");
        criticsLabel.setStyle("-fx-font-weight: bold;");
        TextArea criticsArea = new TextArea();
        criticsArea.setPromptText("Critic feedback will be displayed here...");
        criticsArea.setWrapText(true);
        criticsArea.setEditable(false);
        criticsArea.setPrefRowCount(8);

        Label finalAnswerLabel = new Label("Final Answer");
        finalAnswerLabel.setStyle("-fx-font-weight: bold;");
        TextArea finalAnswerArea = new TextArea();
        finalAnswerArea.setPromptText("Final answer from AI will be displayed here...");
        finalAnswerArea.setWrapText(true);
        finalAnswerArea.setEditable(false);
        finalAnswerArea.setPrefRowCount(10);

        VBox outputSection = new VBox(8, criticsLabel, criticsArea, finalAnswerLabel, finalAnswerArea);

        VBox content = new VBox(16, header, inputSection, outputSection);
        content.setPadding(new Insets(20));
        VBox.setVgrow(goalArea, Priority.ALWAYS);
        VBox.setVgrow(criticsArea, Priority.ALWAYS);
        VBox.setVgrow(finalAnswerArea, Priority.ALWAYS);

        BorderPane root = new BorderPane(content);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #f8fafc, #eef2ff);");

        Scene scene = new Scene(root, 980, 760);
        stage.setTitle("Agentic AI - UI");
        stage.setScene(scene);
        stage.show();


        executeButton.setOnAction(actionEvent -> {
            AnnotationConfigApplicationContext context =
                    new AnnotationConfigApplicationContext(com.grkn.agents.Application.class);

            DeveloperAgentOrchestrator service = context.getBean(DeveloperAgentOrchestrator.class);
            AgentResult result = null;
            try {
                result = service.run(repoPathField.getText(), goalArea.getText());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


            finalAnswerArea.setText("Outcome: " + result.getOutcome() + "\n" + result.getFinalAnswer());
            criticsArea.setText(result.getObservations().get(result.getObservations().size() - 1));
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
