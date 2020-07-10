/*
 * Created by JFormDesigner on Wed Jul 08 21:30:38 CEST 2020
 */

package updater.frontend.panes;

import updater.DragonflyUpdater;
import updater.frontend.DragonflyPalette;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * @author unknown
 */
public class FinishedPane extends JPanel {
    private final String version;
    
    public FinishedPane(String version) {
        this.version = version;
        initComponents();
    }

    private void finishButtonActionPerformed(ActionEvent e) {
        DragonflyUpdater.resumeClient();
    }

    private void initComponents() {
        setBackground(DragonflyPalette.getBackground());
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        titleLabel = new JLabel();
        subtitleLabel = new JLabel();
        textLabel1 = new JLabel();
        textLabel2 = new JLabel();
        finishButton = new JButton();

        //======== this ========
        setLayout(new GridBagLayout());
        ((GridBagLayout)getLayout()).columnWidths = new int[] {449, 0};
        ((GridBagLayout)getLayout()).rowHeights = new int[] {40, 31, 25, 19, 30, 59, 0};
        ((GridBagLayout)getLayout()).columnWeights = new double[] {0.0, 1.0E-4};
        ((GridBagLayout)getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

        //---- titleLabel ----
        titleLabel.setText("Dragonfly Updater");
        titleLabel.setFont(new Font("Rubik", Font.PLAIN, 22));
        titleLabel.setForeground(DragonflyPalette.getForeground());
        add(titleLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.SOUTH, GridBagConstraints.NONE,
            new Insets(0, 0, 0, 0), 0, 0));

        //---- subtitleLabel ----
        subtitleLabel.setText("Dragonfly " + version + " has been successfully downloaded");
        subtitleLabel.setForeground(DragonflyPalette.getForeground());
        add(subtitleLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.NORTH, GridBagConstraints.NONE,
            new Insets(0, 0, 0, 0), 0, 0));

        //---- textLabel1 ----
        textLabel1.setText("You may now continue playing!");
        textLabel1.setForeground(DragonflyPalette.getForeground());
        add(textLabel1, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
            GridBagConstraints.SOUTH, GridBagConstraints.NONE,
            new Insets(0, 0, 0, 0), 0, 0));

        //---- textLabel2 ----
        textLabel2.setText("Thanks for your patience and have fun with the new features!");
        textLabel2.setForeground(DragonflyPalette.getForeground());
        add(textLabel2, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
            GridBagConstraints.NORTH, GridBagConstraints.NONE,
            new Insets(0, 0, 0, 0), 0, 0));

        //---- finishButton ----
        finishButton.setText("Finish");
        finishButton.setDefaultCapable(true);
        finishButton.requestFocus();
        finishButton.addActionListener(this::finishButtonActionPerformed);
        finishButton.setForeground(DragonflyPalette.getForeground());
        add(finishButton, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets(0, 0, 0, 0), 0, 0));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JLabel textLabel1;
    private JLabel textLabel2;
    private JButton finishButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
