package studyportals;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.*;

public class TeamGeneratorGui {
    private JButton renderer;
    private JButton fileSelector;
    private JSpinner amountOfTeams;
    private JPanel mainPanel;
    private JTextField selectedFilePath;
    private JSpinner amountOfTries;
    private JTextField outputFileName;
    private JFileChooser fileChooser;
    private JFrame frame;
    private File selectedFile;

    public TeamGeneratorGui(JFrame frame) {
        this.frame = frame;

        SpinnerNumberModel numberModelForTeams = new SpinnerNumberModel(2, 2, null, 1);
        this.amountOfTeams.setModel(numberModelForTeams);

        SpinnerNumberModel numberModelForTries = new SpinnerNumberModel(500, 10, null, 1);
        this.amountOfTries.setModel(numberModelForTries);

        FileFilter filter = new FileNameExtensionFilter("CSV file", "csv");
        this.fileChooser = new JFileChooser();
        this.fileChooser.setFileFilter(filter);

        this.fileSelector.addActionListener(event -> selectFile());
        this.renderer.addActionListener(event -> generateTeams());
    }


    public void selectFile() {
        int returnVal = this.fileChooser.showOpenDialog(this.frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            this.selectedFile = this.fileChooser.getSelectedFile();
            this.selectedFilePath.setText(this.selectedFile.getName());
            this.outputFileName.setText(this.selectedFile.getName().split(".csv")[0] + "_teams.csv");
        }
    }

    public void generateTeams(){
        if(this.selectedFile == null){
            return;
        }
        String outputFileAbsolutePath = this.selectedFile.getParent() + "\\" + this.outputFileName.getText();
        try {
            Information information = TeamGenerator.generate(
                    (Integer) this.amountOfTeams.getValue(),
                    (Integer) this.amountOfTries.getValue(),
                    this.selectedFile,
                    outputFileAbsolutePath
            );

            String informationMessage = generateInformationMessage(information, this.outputFileName.getText());
            JOptionPane.showMessageDialog(this.frame, informationMessage, "Information", JOptionPane.PLAIN_MESSAGE);
        } catch (Error error){
            JOptionPane.showMessageDialog(this.frame, error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public String generateInformationMessage(Information information, String outputFileName){
        List<String> informationMessageLines = new ArrayList<>();
        informationMessageLines.add("Team file '" + outputFileName + "' has been generated.");
        informationMessageLines.add("There are " + information.getMistakes().size() + " people not fairly placed.");

        if(information.getMistakes().size() != 0){
            Map<Integer, List<String>> teamMistakes = generateTeamMistakes(information.getMistakes());
            for (Map.Entry<Integer, List<String>> mistakeMap : teamMistakes.entrySet()){
                informationMessageLines.add("Team " + (mistakeMap.getKey() + 1) + ": " + String.join(", ", mistakeMap.getValue()));
            }
        }

        informationMessageLines.add("Results got after " + information.getUsedTries() + "/" + information.getTotalTries() + " tries.");
        return String.join("\n", informationMessageLines);
    }

    public Map<Integer, List<String>> generateTeamMistakes(List<Mistake> allMistakes){
        Map<Integer, List<String>> teamMistakes = new HashMap<>();
        for(Mistake mistake: allMistakes){
            String criteriaValue = mistake.getCriteria().getValue().isEmpty() ? "(empty)" : mistake.getCriteria().getValue();
            String teamMistake = mistake.getCriteriaOccurrences()
                    + " out of "
                    + mistake.getCriteria().getStringifiedOccurrencesPerTeam()
                    + " allowed people from criteria: "
                    + mistake.getCriteria().getName()
                    + " - "
                    + criteriaValue;

            if(teamMistakes.containsKey(mistake.getTeamNumber())){
                teamMistakes.get(mistake.getTeamNumber()).add(teamMistake);
            } else {
                List<String> mistakes = new ArrayList<>();
                mistakes.add(teamMistake);
                teamMistakes.put(mistake.getTeamNumber(), mistakes);
            }
        }
        return teamMistakes;
    }

    public JPanel getMainPanel() {
        return this.mainPanel;
    }

}
