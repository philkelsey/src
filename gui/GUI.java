package gui;

import common.DataSet;
import common.GisDate;
import common.GisDate.DateDomain;
import fileio.FileIO;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JApplet;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.JRadioButton;
import javax.swing.JPopupMenu;
import java.awt.Component;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

@SuppressWarnings("serial")
public class GUI extends JApplet {
	public GUI() {}

	String FileFormat;
	GisDate.DateDomain DateFormat;
	JLabel lblDataFileName;
	JLabel lblInterFileName;
	DataSet dataSet;
	//LocationDataSet locationDataSet;
	JTextPane txtDataSet;
	//JTextPane txtLocationDataSet;
	JTable dsTable;
	JTable ldsTable;
	JLabel lblErrors;
	//private JTextField fromMonth;
	//private JTextField toMonth;
	public void init() {
		setSize(500, 500);
		getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 500, 95);
		getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("Data File:");
		lblNewLabel.setBounds(10, 38, 71, 14);
		panel.add(lblNewLabel);

		JButton btnFileDialog = new JButton("...");
		btnFileDialog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File f = getFileName();
				System.out.println("File Name: " + f.toString());
				if (f != null) {
					OpenFileOptions();
					System.out.println("File Format " + FileFormat);
					System.out.println("Date Format " + DateFormat);

					GUI.this.dataSet = new DataSet(f.getAbsolutePath(),
							DateFormat, DataSet.FileDelimiter
									.valueOf(FileFormat));
					if(GUI.this.dataSet.getDataCount()<1){
						System.out.println("Made it here ");
						GUI.this.dataSet = null;
						lblDataFileName.setText("No File Selected");
					}else{
						lblDataFileName.setText(f.getName());
						dsTable.setModel(TableDs());
						lblErrors.setText("");
					}

				}
			}
		});
		btnFileDialog.setBounds(441, 34, 38, 23);
		panel.add(btnFileDialog);

		lblDataFileName = new JLabel("No File Selected");
		lblDataFileName.setBounds(91, 38, 340, 14);
		panel.add(lblDataFileName);
				JPanel panel_1 = new JPanel();
				panel_1.setBounds(0, 96, 495, 400);
				getContentPane().add(panel_1);
				panel_1.setLayout(null);
				
						dsTable = new JTable(TableDs());
						dsTable.setBounds(-3, -2, 0, 393);
						panel_1.add(dsTable);
								
										JScrollPane scrollPane = new JScrollPane(dsTable);
										scrollPane.setBounds(0, 22, 495, 370);
										panel_1.add(scrollPane);
										scrollPane
												.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		FileIO f = new FileIO();

	}

	private static void addPopup(Component component, final JPopupMenu popup) {
	}

	private TableModel TableDs() {
		TableModel dsDataModel = new AbstractTableModel() {
			public int getColumnCount() {
				return 4;
			}

			public int getRowCount() {
				if (dataSet != null && dataSet.getData() != null)
					return dataSet.getData().size();
				else
					return 0;
			}

			public Object getValueAt(int row, int col) {
				if (dataSet != null && dataSet.getData() != null) {
					switch (col) {
					case 0:
						return dataSet.getData().get(row).getX();
					case 1:
						return dataSet.getData().get(row).getY();
					case 2:
						return dataSet.getData().get(row).getDate();
					default:
						return dataSet.getData().get(row).getMeasurement();
					}
				} else
					return null;
			}
		};
		return dsDataModel;
	} //end TableModel
	private File getFileName() {
		JFileChooser fc = new JFileChooser();
		int val = fc.showOpenDialog(GUI.this);
		if (val == JFileChooser.APPROVE_OPTION) {
			return fc.getSelectedFile();
		} else
			return null;
	} //end getFileName

	private void OpenFileOptions() {
		final JDialog dialog = new JDialog(
				SwingUtilities.windowForComponent(this));
		dialog.setModal(true);
		dialog.getContentPane().setLayout(null);
		dialog.setSize(300, 200);
		JLabel lblNewLabel_2 = new JLabel("Please Select the file format.");
		lblNewLabel_2.setBounds(0, 0, 200, 14);
		dialog.getContentPane().add(lblNewLabel_2);
		JLabel lblNewLabel_1 = new JLabel("Date Format");
		lblNewLabel_1.setBounds(10, 23, 75, 14);
		dialog.getContentPane().add(lblNewLabel_1);

		JComboBox cmbDate = new JComboBox(GisDate.DateDomain.values());
		cmbDate.setBounds(16, 36, 65, 20);
		// cmbDate.setModel(new DefaultComboBoxModel(new String[] {"Y", "YM",
		// "YMD", "YQ"}));
		DateFormat = (DateDomain) cmbDate.getSelectedItem();
		dialog.getContentPane().add(cmbDate);
		JRadioButton rdbComma = new JRadioButton("Comma");
		rdbComma.setBounds(87, 37, 75, 23);
		rdbComma.setSelected(true);
		FileFormat = "comma";
		rdbComma.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				GUI.this.FileFormat = "comma";
			}

		});
		dialog.getContentPane().add(rdbComma);

		JRadioButton rdbTab = new JRadioButton("Tab");
		rdbTab.setBounds(170, 35, 61, 23);
		rdbTab.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				GUI.this.FileFormat = "tab";
			}

		});
		dialog.getContentPane().add(rdbTab);
		ButtonGroup group = new ButtonGroup();
		group.add(rdbTab);
		group.add(rdbComma);

		JLabel lblFileFormat = new JLabel("File Delimiter");
		lblFileFormat.setBounds(95, 23, 83, 14);
		dialog.getContentPane().add(lblFileFormat);
		JButton btnDone = new JButton("Done.");
		btnDone.setBounds(0, 70, 80, 25);
		dialog.getContentPane().add(btnDone);
		btnDone.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				dialog.setVisible(false);
			}

		});

		dialog.setVisible(true);
		DateFormat = (DateDomain) cmbDate.getSelectedItem();

	}
	
}

