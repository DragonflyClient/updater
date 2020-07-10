/*
 * Created by JFormDesigner on Wed Jul 08 16:02:48 CEST 2020
 */

package updater.frontend.panes;

import java.awt.event.*;
import updater.DragonflyUpdater;
import updater.frontend.DragonflyPalette;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import javax.swing.*;

/**
 * @author unknown
 */
public class UpdatingPane extends JPanel {

    private final String version;

    public UpdatingPane(String version) {
        this.version = version;
        initComponents();
    }

    private void cancelButtonActionPerformed(ActionEvent e) {
        DragonflyUpdater.requestCancellation();
    }

    private void initComponents() {
        setBackground(DragonflyPalette.getBackground());

        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        titleLabel = new JLabel();
        subtitleLabel = new JLabel();
        percentLabel = new JLabel();
        progressBar = new JProgressBar();
        infoLabel = new JLabel();
        cancelButton = new JButton();

        //======== this ========
        setLayout(new GridBagLayout());
        ((GridBagLayout)getLayout()).columnWidths = new int[] {449, 0};
        ((GridBagLayout)getLayout()).rowHeights = new int[] {40, 31, 25, 19, 30, 59, 0};
        ((GridBagLayout)getLayout()).columnWeights = new double[] {0.0, 1.0E-4};
        ((GridBagLayout)getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

        //---- titleLabel ----
        titleLabel.setText("Dragonfly Updater");
        titleLabel.setFont(new Font("Rubik", Font.PLAIN, 22).deriveFont(new HashMap<TextAttribute, Double>() {{
            put(TextAttribute.TRACKING, -0.03);
        }}));
        titleLabel.setForeground(DragonflyPalette.getForeground());
        add(titleLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.SOUTH, GridBagConstraints.NONE,
            new Insets(0, 0, 0, 0), 0, 0));

        //---- subtitleLabel ----
        subtitleLabel.setText("Downloading Dragonfly " + version);
        subtitleLabel.setForeground(DragonflyPalette.getForeground());
        add(subtitleLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.NORTH, GridBagConstraints.NONE,
            new Insets(0, 0, 0, 0), 0, 0));

        //---- percentLabel ----
        percentLabel.setText("0%");
        percentLabel.setForeground(DragonflyPalette.getForeground());
        add(percentLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
            GridBagConstraints.SOUTH, GridBagConstraints.NONE,
            new Insets(0, 0, 0, 0), 0, 0));

        add(progressBar, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets(0, 15, 0, 15), 0, 0));

        //---- infoLabel ----
        infoLabel.setText("Downloading Dragonfly-1.8.8.jar");
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        infoLabel.setForeground(DragonflyPalette.getForeground());
        add(infoLabel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets(0, 0, 0, 0), 0, 0));

        //---- cancelButton ----
        cancelButton.setText("Cancel");
        cancelButton.setDefaultCapable(false);
        cancelButton.addActionListener(this::cancelButtonActionPerformed);
        cancelButton.setForeground(DragonflyPalette.getForeground());
        add(cancelButton, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets(0, 0, 0, 0), 0, 0));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JLabel percentLabel;
    JProgressBar progressBar;
    private JLabel infoLabel;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public void setProgress(int progress) {
        this.progressBar.setValue(progress);
        this.percentLabel.setText(progress + "%");
    }

    public void setInfo(String info) {
        this.infoLabel.setText(info);
    }
}
