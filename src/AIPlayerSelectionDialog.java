import javax.swing.*;
import java.awt.*;

/**
 * Dialog for selecting the number of AI players at game start.
 * Allows selection of 1-3 AI players.
 *
 * @author G27
 * @version 2.0
 */
public class AIPlayerSelectionDialog extends JDialog {
    
    private int numberOfAIPlayers;
    private boolean confirmed;
    private JComboBox<Integer> AIplayerCountComboBox;
    
    /**
     * Constructs a new PlayerSelectionDialog.
     *
     * @param parent the parent frame
     */
    public AIPlayerSelectionDialog(JFrame parent) {
        // This is a rough guess to try to replicate the style from the image
        super(parent, "AI Player Selection", true);
        numberOfAIPlayers = 1;
        confirmed = false;
        
        initializeComponents();
        
        setSize(450, 200);
        setLocationRelativeTo(parent);
        setResizable(false);
    }
    
    /**
     * Initializes and arranges all dialog components.
     */
    private void initializeComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Label panel
        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel questionIcon = new JLabel("?");
        questionIcon.setFont(new Font("Arial", Font.BOLD, 24));
        questionIcon.setForeground(Color.WHITE);
        questionIcon.setOpaque(true);
        questionIcon.setBackground(new Color(100, 180, 100));
        questionIcon.setPreferredSize(new Dimension(40, 40));
        questionIcon.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel instructionLabel = new JLabel("Select the total number of AI players:");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        labelPanel.add(questionIcon);
        labelPanel.add(Box.createHorizontalStrut(10));
        labelPanel.add(instructionLabel);
        
        // ComboBox panel
        JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        AIplayerCountComboBox = new JComboBox<>(new Integer[]{1, 2, 3});
        AIplayerCountComboBox.setPreferredSize(new Dimension(300, 35));
        AIplayerCountComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        comboPanel.add(AIplayerCountComboBox);
        
        mainPanel.add(labelPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(comboPanel);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton okButton = new JButton("OK");
        okButton.setPreferredSize(new Dimension(100, 30));
        okButton.addActionListener(e -> {
            numberOfAIPlayers = (Integer) AIplayerCountComboBox.getSelectedItem();
            confirmed = true;
            dispose();
        });
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(100, 30));
        cancelButton.addActionListener(e -> {
            confirmed = false;
            dispose();
        });
        
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Gets the selected number of AI players.
     *
     * @return number of AI players (1-3)
     */
    public int getNumberOfAIPlayers() {
        return numberOfAIPlayers;
    }
    
    /**
     * Checks if the user confirmed their selection.
     *
     * @return true if OK was clicked, false if cancelled
     */
    public boolean isConfirmed() {
        return confirmed;
    }
    
    /**
     * Shows the dialog and returns the selected number of AI players.
     *
     * @param parent the parent frame
     * @return number of players, or -1 if cancelled
     */
    public static int showDialog(JFrame parent) {
        AIPlayerSelectionDialog dialog = new AIPlayerSelectionDialog(parent);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            return dialog.getNumberOfAIPlayers();
        }
        return -1;
    }
}