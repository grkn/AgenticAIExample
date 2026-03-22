package com.grkn.agents.orchestartor.ui;

import com.grkn.agents.Developer;
import com.grkn.agents.ProductOwner;
import com.grkn.agents.architect.SoftwareArchitect;
import com.grkn.agents.developer.core.DeveloperAgentOrchestrator;
import com.grkn.agents.productowner.core.ProductOwnerAgent;
import com.grkn.agents.developer.resource.AgentResult;
import com.grkn.agents.architect.resource.ArchitectureRequest;
import com.grkn.agents.architect.resource.ArchitectureResponse;
import com.grkn.agents.productowner.resource.SubProblem;
import com.grkn.agents.architect.service.ArchitectAgent;
import com.grkn.agents.architect.type.ArchitectureMode;
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

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

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

        AnnotationConfigApplicationContext productOwnerContext =
                new AnnotationConfigApplicationContext(ProductOwner.class);
        AnnotationConfigApplicationContext architectContext =
                new AnnotationConfigApplicationContext(SoftwareArchitect.class);
        AnnotationConfigApplicationContext developerContext =
                new AnnotationConfigApplicationContext(Developer.class);

        executeButton.setOnAction(actionEvent -> {
            try {
                // Clarify problem with product owner


                ProductOwnerAgent productOwnerAgent = productOwnerContext.getBean(ProductOwnerAgent.class);
                String clarifiedProblem = productOwnerAgent.clarifyProblem(goalArea.getText());
                List<SubProblem> subProblems = productOwnerAgent.splitIntoSubProblems(clarifiedProblem);
                // Important divide requirements according to divisionFactorForSubproblems so it is detailed.
                int divisionFactorForProblems = 1;
                int i = 0;

                while (i < divisionFactorForProblems) {

                    List<SubProblem> newSubProblems = new ArrayList<>();
                    ListIterator<SubProblem> subProblemListIterator = subProblems.listIterator();
                    while (subProblemListIterator.hasNext()) {
                        SubProblem next = subProblemListIterator.next();

                        newSubProblems.addAll(productOwnerAgent
                                .splitIntoSubProblems(
                                        "Title: " + next.getTitle() + " Description:" + next.getDescription()));
                        subProblemListIterator.remove();
                    }
                    subProblems.clear();
                    subProblems.addAll(newSubProblems);
                    i++;
                }
                String requirementsDocument = productOwnerAgent.buildRequirementsDocument(clarifiedProblem, subProblems, List.of());

                // Architect will review requirementsDocument and give output of technical details.


                ArchitectAgent architectAgent = architectContext.getBean(ArchitectAgent.class);
                ArchitectureRequest architectureRequest =
                        new ArchitectureRequest(requirementsDocument,
                                "",
                                goalArea.getText(),
                                ArchitectureMode.DEFAULT
                        );
                ArchitectureResponse response = architectAgent.run(architectureRequest);
                while (!response.valid()) {
                    response = architectAgent.run(architectureRequest);
                }

                // Developer will implement details

                DeveloperAgentOrchestrator service = developerContext.getBean(DeveloperAgentOrchestrator.class);
                AgentResult result = null;

                result = service.run(repoPathField.getText(), response.architecture());
                finalAnswerArea.setText("Outcome: " + result.getOutcome() + "\n" + result.getFinalAnswer());
                criticsArea.setText(result.getObservations().get(result.getObservations().size() - 1));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
