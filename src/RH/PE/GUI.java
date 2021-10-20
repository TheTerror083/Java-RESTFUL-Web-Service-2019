package RH.PE;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class GUI extends JFrame {
    /** 
	* This Class is used to create the engine's UI
	*/
	private JTextField leftPrice, rightPrice, caloriesLeft, caloriesRight,jtfFilter;
	private JButton reset, compare, update, foodMenu, addVal;
	private JPanel panel, leftPanel, comparePanel, searchPanel;
	private JFrame menuFrame;
	private GridBagConstraints location;
	private JComboBox<String> comboxLeft;
	private JComboBox<String> comboxRight;
	private Model exec;
	private static final Logger logger = LogManager.getLogger(GUI.class.getName());
	private JLabel leftLabel, rightLabel;
	private int addedVals = 0;
	
	public GUI() throws IOException {
		super("Food Calories Comparsion");
		setLayout(new FlowLayout());

		//creation of the engine
		logger.info("Starting up the engine...");
		exec = new Model();
		
		this.setTitle("Food Calories Comparsion");
		panel = new JPanel(new GridBagLayout());
		location = new GridBagConstraints();
		location.insets = new Insets(10,10,10,10);

		// Text Field Creation (price and calories):
		// ************************ Left fields:
		//Price field
		logger.info("Creating text fields....");
		leftPrice = new JTextField("0", 5);
		leftPrice.setEditable(false);
		location.gridx = 0;
		location.gridy = 1;
		panel.add(leftPrice, location);
		
		//Calories field
		caloriesLeft = new JTextField("0", 5);
		caloriesLeft.setEditable(false);
		location.gridx = 0;
		location.gridy = 2;
		panel.add(caloriesLeft, location);

		//************************* Right fields:
		//Price field
		caloriesRight = new JTextField("0", 5);
		caloriesRight.setEditable(false);
		location.gridx = 2;
		location.gridy = 2;
		panel.add(caloriesRight, location);
		
		//Calories field
		rightPrice = new JTextField("0", 5);
		rightPrice.setEditable(false);
		location.gridx = 2;
		location.gridy = 1;
		panel.add(rightPrice, location);

		// Button Creation:
		logger.info("Creating buttons...");
		foodMenu = new JButton("Menu");
		location.gridx = 0;
		location.gridy = 3;
		panel.add(foodMenu, location);
		
		update = new JButton("Update");
		location.gridx = 1;
		location.gridy = 3;
		panel.add(update, location);
		
		reset = new JButton("Reset");
		location.gridx = 2;
		location.gridy = 3;
		panel.add(reset, location);

		compare = new JButton("Compare");
		compare.setBackground(Color.orange);
		location.gridx = 1;
		location.gridy = 2;
		panel.add(compare, location);

		//selection box creation
		logger.info("Creating panels...");
		leftPanel = new JPanel(new BorderLayout());
		comparePanel = new JPanel(new BorderLayout());
		
		//search in the menu
		jtfFilter = new JTextField(10);
		jtfFilter.getDocument().addDocumentListener(new DocumentListener(){

            public void insertUpdate(DocumentEvent e) {
                String text = jtfFilter.getText();

                if (text.trim().length() == 0) {
                    exec.getRowSorter().setRowFilter(null);
                } else {
                	exec.getRowSorter().setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
            public void removeUpdate(DocumentEvent e) {
                String text = jtfFilter.getText();

                if (text.trim().length() == 0) {
                	exec.getRowSorter().setRowFilter(null);
                } else {
                	exec.getRowSorter().setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
            public void changedUpdate(DocumentEvent e) {
            	logger.info("Unsupported operation");
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
		
		
		leftLabel = new JLabel("Food:");
		rightLabel = new JLabel("Food:");
		leftLabel.setVisible(true);
		rightLabel.setVisible(true);
		
		// Combo Box Creation
		logger.info("Creating ComboBoxes...");
		comboxLeft = new JComboBox<String>();
		fillCombox(comboxLeft);
		location.gridx = 0;
		location.gridy = 0;
		leftPanel.add(comboxLeft, BorderLayout.EAST);
		leftPanel.add(leftLabel, BorderLayout.WEST);
		panel.add(leftPanel, location);
		
		
		comboxRight = new JComboBox<String>();
		fillCombox(comboxRight);
		location.gridx = 2;
		location.gridy = 0;
		comparePanel.add(comboxRight, BorderLayout.EAST);
		comboxRight.setSelectedIndex(1);
		comparePanel.add(rightLabel, BorderLayout.WEST);
		panel.add(comparePanel, location);
		
		// Adding action listeners
		logger.info("Adding listeners...");
		theHandler handler = new theHandler();
		leftPrice.addActionListener(handler);
		reset.addActionListener(handler);
		compare.addActionListener(handler);
		foodMenu.addActionListener(handler);
		update.addActionListener(handler);

		
		//adding the food menu table
		logger.info("Adding the food table (menu)....");
		menuFrame = new JFrame();
		menuFrame.setLayout(new FlowLayout());
		
		addVal = new JButton("Add");
		addVal.addActionListener(handler);
		menuFrame.add(addVal, BorderLayout.EAST);
		
		
		menuFrame.add(new JScrollPane(exec.getTable()));
		menuFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		menuFrame.setSize(700, 600);
		
		
		//search box for menu table
		searchPanel = new JPanel(new BorderLayout());
		searchPanel.add(new JLabel("Specify a word to match:"), BorderLayout.WEST);
		searchPanel.add(jtfFilter, BorderLayout.CENTER);
		menuFrame.add(searchPanel);

        
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		menuFrame.setLocation(dim.width/2-this.getSize().width/2 - 480, dim.height/2-this.getSize().height/2);
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		add(panel);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(650,230);
		setVisible(true);
	}


	private class theHandler implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String tempCalc = "";
			double secondComp;
			double tempINS;
			if (event.getSource() == foodMenu) {
				menuFrame.setVisible(true);
				
			} else if (event.getSource() == update){
				updateDataBase();
				
			} 
			else if (event.getSource() == addVal){
				
			    /** 
				* Adding a value to the food menu:
				* If one of the dialogs is cancelled, the operation will abort,
				* and the value will not be added
				*/
				DefaultTableModel temp = (DefaultTableModel)exec.getTable().getModel();
				String insertName = JOptionPane.showInputDialog("Enter Food Name");
				if (insertName!=null)
				{
					String insertDescr = JOptionPane.showInputDialog("Enter Description");
					if (insertDescr!=null)
					{
						try {
							String insertPrice = JOptionPane.showInputDialog("Enter Price (in $)");
							Double.parseDouble(insertPrice);
								if (insertPrice!=null)
								{
									String insertCalories = JOptionPane.showInputDialog("Enter Calories");
									Double.parseDouble(insertCalories);
									if (insertCalories!=null)
									{
										comboxLeft.addItem(insertName);
										comboxRight.addItem(insertName);
										exec.getFoodList().add(new Food(insertName, insertDescr, insertPrice, insertCalories));
										exec.addValue(insertName, insertDescr, insertPrice, insertCalories);
										temp.fireTableDataChanged();
										addedVals++;
									}
								}
							}
						catch(java.lang.NumberFormatException e)
						{
							logger.error("Non-number input detected while adding item to the menu");
							JOptionPane.showMessageDialog(null, "Invalid input","Error",JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
			
			else if (event.getSource() == reset) {
				reset();
			} 
			else if (event.getSource() == compare) {
			    /** 
				* This function compares the values of Price and Calories of both selected food items
				* It gets both selected indexes from the Combo Boxes and gets their respective details from the food list.
				* 
				* The function seeks the lower values of Price and Calories between both food items.
				* The lower value's text field will be colored green, and the higher value's field will be colored red.
				* If both values are equal, the text fields' color will be set to white.
				*/
				
				//set the corresponding price values for the food in the text fields
				leftPrice.setText(exec.getFoodList().get((comboxLeft.getSelectedIndex())).getPrice());
				rightPrice.setText(exec.getFoodList().get((comboxRight.getSelectedIndex())).getPrice()); 
				
				
				// get the text from the text field and change it to double
				tempCalc = leftPrice.getText(); 
				secondComp = Double.parseDouble(rightPrice.getText().substring(1));
				tempINS = Double.parseDouble(tempCalc.substring(1)); 
				
				if(tempINS - secondComp > 0)
				{
					rightPrice.setBackground(Color.green);
					leftPrice.setBackground(Color.red);
				}
				else if(tempINS - secondComp < 0)
				{
					rightPrice.setBackground(Color.red);
					leftPrice.setBackground(Color.green);
				}
				else //if both values are equal
				{
					rightPrice.setBackground(Color.white);
					leftPrice.setBackground(Color.white);
				}
				
				caloriesLeft.setText(exec.getFoodList().get((comboxLeft.getSelectedIndex())).getCalories());
				caloriesRight.setText(exec.getFoodList().get((comboxRight.getSelectedIndex())).getCalories());
				
				// change the text to double
				tempCalc = caloriesLeft.getText(); 
				secondComp = Double.parseDouble(caloriesRight.getText());
				tempINS = Double.parseDouble(tempCalc); 
				
				if(tempINS - secondComp > 0)
				{
					caloriesRight.setBackground(Color.green);
					caloriesLeft.setBackground(Color.red);
				}
				else if(tempINS - secondComp < 0)
				{
					caloriesRight.setBackground(Color.red);
					caloriesLeft.setBackground(Color.green);
				}
				else //if both values are equal
				{
					caloriesRight.setBackground(Color.white);
					caloriesLeft.setBackground(Color.white);
				}
			}
		}
	}
	
	public void reset() //reset all fields to their starting state
	{
	    /** 
		* Resets all text fields to 0 and backround color to white
		*/
		caloriesRight.setBackground(Color.white);
		caloriesLeft.setBackground(Color.white);
		caloriesRight.setText("0");
		caloriesLeft.setText("0");
		
		rightPrice.setBackground(Color.white);
		leftPrice.setBackground(Color.white);
		leftPrice.setText("0");
		rightPrice.setText("0");
		
		comboxLeft.setSelectedIndex(0);
		comboxRight.setSelectedIndex(1);
	}
	public void fillCombox(JComboBox<String> ComBox) //fills the combo boxes with data
	{
		for(int i=0; i<exec.getFoodList().size();i++)
		{
			ComBox.addItem(exec.getFoodList().get(i).getName());
		}
	}
	
	public void updateDataBase(){
		//xml parse XML
		logger.info("Resetting to XML values...");
		exec.removeFood(addedVals);
		addedVals=0;
		comboxLeft.removeAllItems();
		comboxRight.removeAllItems();
		fillCombox(comboxLeft);
		fillCombox(comboxRight);
		reset();
		DefaultTableModel dm = (DefaultTableModel)exec.getTable().getModel();
		dm.setRowCount(0);
		exec.createRows(dm);
		dm.fireTableDataChanged();
	}
}
