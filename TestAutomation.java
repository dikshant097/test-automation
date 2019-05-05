import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Iterator;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TestAutomation implements ActionListener, ItemListener {

	private static JButton createTest, editTest, viewPerformance, newSession;
	private static JFrame parentFrame, performanceFrame, editFrame;
	private static JLabel welcomeLabel;
	private static Dimension dim;
	private static JDialog testSelector, performaceDailog, editDailog, sessionDailog;
	private static TestAutomation t;
	private static String path;

	public TestAutomation() {

		parentFrame = new JFrame();
		parentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		parentFrame.setResizable(false);
		parentFrame.setTitle("Sareen's Classes Test Details");
		welcomeLabel = new JLabel("Sareen's Classes");
		welcomeLabel.setForeground(Color.RED);
		welcomeLabel.setFont(new Font("Serif", Font.BOLD, 80));

		createTest = new JButton("Create New Test");
		createTest.setBounds(200, 100, 300, 50);
		parentFrame.add(createTest);

		editTest = new JButton("Edit Previous Test");
		editTest.setBounds(200, 200, 300, 50);
		parentFrame.add(editTest);

		viewPerformance = new JButton("View Performance");
		viewPerformance.setBounds(200, 300, 300, 50);
		parentFrame.add(viewPerformance);

		newSession = new JButton("Start New Session");
		newSession.setBounds(200, 400, 300, 50);
		parentFrame.add(newSession);

		parentFrame.setSize(700, 600);
		dim = Toolkit.getDefaultToolkit().getScreenSize();
		parentFrame.setLocation(dim.width / 2 - parentFrame.getSize().width / 2,
				dim.height / 2 - parentFrame.getSize().height / 2);
		
		welcomeLabel.setSize(700,100);
		welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
		parentFrame.add(welcomeLabel);
		parentFrame.setLayout(null);
		parentFrame.setVisible(true);
	}

	public static void main(String... s) {

		t = new TestAutomation();
		createTest.addActionListener(t);
		newSession.addActionListener(t);
		viewPerformance.addActionListener(t);
		editTest.addActionListener(t);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == createTest) {
			openCreateTestFrame();
		} else if (e.getSource() == newSession) {
			openResetDailog();
		} else if (e.getSource() == viewPerformance) {
			openPerformanceFrame();
		} else if (e.getSource() == editTest) {
			openEditTestFrame();
		}

	}
	
	public static void openResetDailog() {
		JPanel sessionPanel = new JPanel();
		sessionPanel.setOpaque(false);
		sessionPanel.setLayout(null);
		sessionDailog = new JDialog(parentFrame, "Are you sure you want to start new session?");
		JLabel IDLabel = new JLabel("Enter Any Number between 1-10:");
		IDLabel.setBounds(5, 20, 200, 30);
		sessionPanel.add(IDLabel);
		JTextField id = new JTextField();
		id.setBounds(200, 25, 50, 20);
		sessionPanel.add(id);
		JButton ok = new JButton("Proceed");
		ok.setBounds(130, 65, 100, 30);
		JLabel msg = new JLabel("");
		msg.setBounds(10, 100, 280, 20);
		msg.setForeground(Color.RED);
		sessionPanel.add(msg);
		
		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if(id.getText().equals(""))
				{
					msg.setText("Please enter a number");
					return;
				}
				try {
					int num = Integer.parseInt(id.getText());
					if(num >=1 && num <=10)
					{
						new Thread(new Runnable() {

							@Override
							public void run() {
								msg.setText("Please Wait...");
								ok.setEnabled(false);
								DatabaseConnector db = new DatabaseConnector();
								int res = db.startNewSession();
								ok.setEnabled(true);
								if(res == -1)
								{
									msg.setText("Error Encountered.");
								}
								msg.setText("New Session started.");
								return;
								
							}
							
						}).start();
					}
					msg.setText("Please enter a valid number");
				}
				catch(Exception e) {
					msg.setText("Only numeric value is allowed");
				}
			}
			
		});
		
		sessionPanel.add(ok);
		sessionDailog.add(sessionPanel);
		sessionDailog.setSize(370, 180);
		sessionDailog.setLocation(dim.width / 2 - sessionDailog.getSize().width / 2,
				dim.height / 2 - sessionDailog.getSize().height / 2);
		sessionDailog.setVisible(true);

	}
	
	public static void openEditTestFrame() {
		JPanel editPanel = new JPanel();
		editPanel.setOpaque(false);
		editPanel.setLayout(null);
		editDailog = new JDialog(parentFrame, "Select Class");
		JLabel clasLabel = new JLabel("Select Class:");
		clasLabel.setBounds(5, 15, 90, 30);
		editPanel.add(clasLabel);
		JRadioButton class11 = new JRadioButton("11");
		class11.setBounds(95, 5, 50, 50);
		JRadioButton class12 = new JRadioButton("12");
		class12.setBounds(150, 5, 50, 50);
		ButtonGroup group = new ButtonGroup();
		group.add(class11);
		group.add(class12);
		editPanel.add(class11);
		editPanel.add(class12);
		JLabel IDLabel = new JLabel("Enter Test ID:");
		IDLabel.setBounds(5, 60, 110, 30);
		editPanel.add(IDLabel);
		JTextField id = new JTextField();
		id.setBounds(120, 65, 150, 20);
		editPanel.add(id);
		JButton ok = new JButton("Proceed");
		ok.setBounds(100, 105, 100, 30);
		editPanel.add(ok);
		JLabel msg = new JLabel("");
		msg.setBounds(10, 140, 280, 20);
		msg.setForeground(Color.RED);
		editPanel.add(msg);
		
		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!class11.isSelected() && !class12.isSelected()) {
					msg.setText("Please select class");
					return;
				}
				int clas;
				if (class11.isSelected())
					clas = 11;
				else
					clas = 12;
				
				if(id.getText().equals(""))
				{
					msg.setText("Please enter Test ID");
					return;
				}
				
				new Thread(new Runnable() {

					@Override
					public void run() {
						
						ok.setEnabled(false);
						DatabaseConnector db = new DatabaseConnector();
						HashMap<String,String> marks = db.getListToEdit(clas, id.getText());
						ok.setEnabled(true);
						if(marks == null || marks.size() == 0)
						{
							msg.setText("No results found");
							return;
						}
						editDailog.dispose();
						showEditFrame(marks,clas,id.getText().toLowerCase());
					}
				}).start();

			}

		});
		
		editDailog.setSize(320, 200);
		editDailog.setLocation(dim.width / 2 - editDailog.getSize().width / 2,
				dim.height / 2 - editDailog.getSize().height / 2);
		editDailog.add(editPanel);
		editDailog.setVisible(true);
	}
	
	public static void showEditFrame(HashMap<String,String> marks, int clas,String id)
	{
			parentFrame.setVisible(false);
			editFrame = new JFrame("Edit Marks");
			editFrame.setResizable(false);
			editFrame.setSize(500, 630);
			editFrame.setLocation(dim.width / 2 - editFrame.getSize().width / 2,
					dim.height / 2 - editFrame.getSize().height / 2);
			

			JTable tbl = new JTable() {
				 public boolean isCellEditable(int row,int column) {
					 if(column != 0)
				        return true;
					 return false;
				}
			};
			tbl.setSize(500, 500);
			
			DefaultTableModel dtm = new DefaultTableModel(0, 0);

			dtm.setColumnIdentifiers(new Object[] {"Names","Marks"});
			
			tbl.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 15));
			tbl.setRowHeight(30);
			tbl.setModel(dtm);
			
			for (Map.Entry mapElement : marks.entrySet()) {
				String name = (String) mapElement.getKey();
				String obMarks = (String) mapElement.getValue();
				
				dtm.addRow(new Object[] {name.toUpperCase(), obMarks});
			}
			
			
			JScrollPane js = new JScrollPane(tbl,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			js.setPreferredSize(new Dimension(500,500));
			js.setVisible(true);
			
			JButton ok = new JButton("Submit");
			ok.setBounds(200, 520, 100, 30);
			JLabel msg = new JLabel("");
			msg.setBounds(10, 550, 500, 20);
			msg.setForeground(Color.RED);
			editFrame.add(msg);
			ok.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					
					HashMap<String, String> updatedList = new HashMap<>();
					for(int i= 0 ; i<dtm.getRowCount() ; i++) {
						
						String name = (String) tbl.getModel().getValueAt(i, 0);
						String marks = (String) tbl.getModel().getValueAt(i, 1);
						
						updatedList.put(name, marks);
					}
					
					DatabaseConnector db = new DatabaseConnector();
					new Thread(new Runnable() {

						@Override
						public void run() {
							ok.setEnabled(false);
							int res = db.updateMarks(updatedList , clas, id);
							ok.setEnabled(true);
							if(res == -1)
							{
								msg.setText("Error updating marks");
								return;
							}
							
							msg.setText("Updated Successfully");
						}
						
					}).start();
				}
				
			});
			editFrame.add(ok);
			editFrame.getContentPane().add(js, BorderLayout.CENTER);
			
			editFrame.addWindowListener(new WindowListener() {

				@Override
				public void windowOpened(WindowEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void windowClosing(WindowEvent e) {
					parentFrame.setVisible(true);

				}

				@Override
				public void windowClosed(WindowEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void windowIconified(WindowEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void windowDeiconified(WindowEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void windowActivated(WindowEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void windowDeactivated(WindowEvent e) {
					// TODO Auto-generated method stub

				}

			});
			
			editFrame.setVisible(true);
			
	}

	public static void openPerformanceFrame() {
		JPanel performacePanel = new JPanel();
		performacePanel.setOpaque(false);
		performacePanel.setLayout(null);
		performaceDailog = new JDialog(parentFrame, "Select Class");
		JLabel clasLabel = new JLabel("Select Class:");
		clasLabel.setBounds(5, 15, 90, 30);
		performacePanel.add(clasLabel);
		JRadioButton class11 = new JRadioButton("11");
		class11.setBounds(95, 5, 50, 50);
		JRadioButton class12 = new JRadioButton("12");
		class12.setBounds(150, 5, 50, 50);
		ButtonGroup group = new ButtonGroup();
		group.add(class11);
		group.add(class12);
		performacePanel.add(class11);
		performacePanel.add(class12);
		JButton ok = new JButton("Proceed");
		ok.setBounds(100, 70, 100, 30);
		performacePanel.add(ok);
		JLabel msg = new JLabel("");
		msg.setBounds(10, 110, 280, 20);
		msg.setForeground(Color.RED);
		performacePanel.add(msg);
		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!class11.isSelected() && !class12.isSelected()) {
					msg.setText("Please select class");
					return;
				}
				int clas;
				if (class11.isSelected())
					clas = 11;
				else
					clas = 12;
				
				new Thread(new Runnable() {

					@Override
					public void run() {
						DatabaseConnector db = new DatabaseConnector();
						ok.setEnabled(false);
						TreeMap<String, TreeSet<TestBean>> result = db.getTests(clas);
						if (result == null || result.size() == 0) {
							msg.setText("No Results found");
							ok.setEnabled(true);
							return;
						} else {
							performaceDailog.dispose();
							viewPerformanceFrame(clas, result);
						}
					}
				}).start();

			}

		});

		performaceDailog.setSize(320, 170);
		performaceDailog.setLocation(dim.width / 2 - performaceDailog.getSize().width / 2,
				dim.height / 2 - performaceDailog.getSize().height / 2);
		performaceDailog.add(performacePanel);
		performaceDailog.setVisible(true);
	}

	public static void viewPerformanceFrame(int clas, TreeMap<String, TreeSet<TestBean>> result) {
		parentFrame.setVisible(false);
		performanceFrame = new JFrame("Performance Analysis");
		performanceFrame.setResizable(false);
		performanceFrame.setSize(dim.width - 100, dim.height);
		performanceFrame.setLocation(dim.width / 2 - performanceFrame.getSize().width / 2,
				dim.height / 2 - performanceFrame.getSize().height / 2);

		JTable tbl = new JTable() {
			@Override
			public Class<?> getColumnClass(int column) {
				if (convertColumnIndexToModel(column) == 0)
					return Object.class;
				return super.getColumnClass(column);
			}
			 public boolean isCellEditable(int row,int column) {
			        return false;
			}
		};
		tbl.setSize((int) (dim.width *0.9), (int) (dim.height *0.9));
		tbl.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
		
		
		DefaultTableModel dtm = new DefaultTableModel(0, 0);

		Map.Entry element = result.entrySet().iterator().next();
		String key = (String) element.getKey();
		TreeSet<TestBean> test_names = ((TreeSet<TestBean>) element.getValue());
		String testCols[] = new String[test_names.size() + 2];
		int i = 0;
		testCols[i] = "names".toUpperCase();
		i++;
		testCols[i] = "%";
		i++;
		Iterator it =  test_names.iterator(); 
		while (it.hasNext() ) {
			TestBean t = (TestBean) it.next();
			testCols[i] = t.getTestName().toUpperCase() + " (" + t.getMaxMarks() + ")";
			i++;
		}
		dtm.setColumnIdentifiers(testCols);
		
		tbl.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 15));
		tbl.setBounds(0, 0, dim.width - 100, dim.height);
		tbl.setRowHeight(30);
		tbl.setModel(dtm);
		
		
		int rowNum = 0;
		HashMap<String, ArrayList<CellLocation>> typeOfCell = new HashMap<>();
		for (Map.Entry mapElement : result.entrySet()) {

			String row[] = new String[testCols.length];
			i = 0;
			row[i] = ((String) mapElement.getKey()).toUpperCase();
			i += 2;
			float total = 0.0f, ob = 0.0f;
			int per;
			TreeSet<TestBean> tests = (TreeSet<TestBean>) mapElement.getValue();
			
			for (Iterator k = tests.iterator(); k.hasNext(); i++) {
				TestBean t = (TestBean) k.next();
				row[i] = t.getObtainedMarks();
				if (row[i].equalsIgnoreCase("ab")) {
					total += t.getMaxMarks();
					row[i] = row[i].toUpperCase();

					if (typeOfCell.containsKey("ab")) {
						typeOfCell.get("ab").add(new CellLocation(rowNum, i));
					} else {
						ArrayList<CellLocation> arr = new ArrayList<>();
						arr.add(new CellLocation(rowNum, i));
						typeOfCell.put("ab", arr);
					}

				} else if (row[i].equalsIgnoreCase("na")) {
					row[i] = "";

					if (typeOfCell.containsKey("na")) {
						typeOfCell.get("na").add(new CellLocation(rowNum, i));
					} else {
						ArrayList<CellLocation> arr = new ArrayList<>();
						arr.add(new CellLocation(rowNum, i));
						typeOfCell.put("na", arr);
					}
				} else {
					try {

						ob = ob + Float.parseFloat(row[i]);
						total += t.getMaxMarks();

						if (Float.parseFloat(row[i]) < t.getThresholdMarks()) {
							if (typeOfCell.containsKey("th")) {
								typeOfCell.get("th").add(new CellLocation(rowNum, i));
							} else {
								ArrayList<CellLocation> arr = new ArrayList<>();
								arr.add(new CellLocation(rowNum, i));
								typeOfCell.put("th", arr);
							}
						}
					
					}
					catch(Exception e)
					{
						break;
					}
				}

			}
			total = (ob / total);
			per = (int) (total * 100);
			row[1] = String.valueOf(per + "%");
			if (per <= 50) {
				if (typeOfCell.containsKey("50")) {
					typeOfCell.get("50").add(new CellLocation(rowNum, 1));
				} else {
					ArrayList<CellLocation> arr = new ArrayList<>();
					arr.add(new CellLocation(rowNum, 1));
					typeOfCell.put("50", arr);
				}
			} else if (per <= 60) {
				if (typeOfCell.containsKey("60")) {
					typeOfCell.get("60").add(new CellLocation(rowNum, 1));
				} else {
					ArrayList<CellLocation> arr = new ArrayList<>();
					arr.add(new CellLocation(rowNum, 1));
					typeOfCell.put("60", arr);
				}
			}
			dtm.addRow(row);
			rowNum++;
		}
		for(int j=0 ;j < tbl.getColumnModel().getColumnCount() ; j++)
		{
			if(j==0) {
			tbl.getColumnModel().getColumn(j).setPreferredWidth((int) ((dim.width - 100)/6));
			}
			else
			{
				tbl.getColumnModel().getColumn(j).setPreferredWidth((int) ((dim.width - 100)/12));
			}
			
		}
		
		CellRenderer cellRenderer = new CellRenderer(typeOfCell);
		tbl.setDefaultRenderer(Object.class, cellRenderer);
		JScrollPane js = new JScrollPane(tbl,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		js.setPreferredSize(new Dimension((int) (dim.width *0.9), (int) (dim.height *0.9)));
		js.setVisible(true);

		performanceFrame.getContentPane().add(js, BorderLayout.CENTER);
		performanceFrame.pack();
		performanceFrame.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosing(WindowEvent e) {
				parentFrame.setVisible(true);

			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}

		});

		performanceFrame.setVisible(true);
	}

	public static void openCreateTestFrame() {

		JPanel testPanel = new JPanel();
		testPanel.setOpaque(false);
		testPanel.setLayout(null);
		testSelector = new JDialog(parentFrame, "Enter Test Details");
		JLabel l = new JLabel("Enter Threshold marks:");
		l.setBounds(5, 10, 160, 30);
		testPanel.add(l);
		JTextField threshold = new JTextField();
		threshold.setBounds(165, 15, 80, 20);
		testPanel.add(threshold);
		JLabel clasLabel = new JLabel("Select Class:");
		clasLabel.setBounds(5, 45, 90, 30);
		testPanel.add(clasLabel);
		JRadioButton class11 = new JRadioButton("11");
		class11.setBounds(95, 35, 50, 50);
		JRadioButton class12 = new JRadioButton("12");
		class12.setBounds(150, 35, 50, 50);
		ButtonGroup group = new ButtonGroup();
		group.add(class11);
		group.add(class12);
		testPanel.add(class11);
		testPanel.add(class12);
		JLabel IDLabel = new JLabel("Enter Test ID:");
		IDLabel.setBounds(5, 80, 110, 30);
		testPanel.add(IDLabel);
		JTextField id = new JTextField();
		id.setBounds(120, 85, 150, 20);
		testPanel.add(id);
		JLabel attachExcel = new JLabel("Attach Excel Sheet:");
		attachExcel.setBounds(5, 115, 140, 30);
		testPanel.add(attachExcel);
		JButton chooseFile = new JButton("Choose File");
		chooseFile.setBounds(150, 120, 120, 20);
		JLabel filePath = new JLabel("");
		chooseFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("EXCEL FILES", "xls", "xlsx");
				fc.setFileFilter(filter);
				int i = fc.showOpenDialog(testSelector);
				if (i == JFileChooser.APPROVE_OPTION) {
					File f = fc.getSelectedFile();
					path = f.getPath();
					filePath.setText(path);
				}

			}
		});
		testPanel.add(chooseFile);
		filePath.setBounds(5, 150, 550, 20);
		filePath.setForeground(Color.BLUE);
		testPanel.add(filePath);
		JButton ok = new JButton("Proceed");
		ok.setBounds(220, 180, 100, 30);
		JLabel msg = new JLabel("");
		msg.setBounds(20, 210, 550, 20);
		msg.setForeground(Color.RED);
		testPanel.add(msg);
		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (!class11.isSelected() && !class12.isSelected()) {
					msg.setText("Please select class");
					return;
				}

				if (id.getText().equals("")) {
					msg.setText("Please enter test id");
					return;
				}

				if (filePath.getText().equals("")) {
					msg.setText("Please choose excel file.");
					return;
				}
				HashMap<String, TestBean> marks = new HashMap<>();
				try {
					FileInputStream stream = new FileInputStream(path);
					XSSFWorkbook workbook = new XSSFWorkbook(stream);
					XSSFSheet sheet = workbook.getSheetAt(0);
					int rowsCount = sheet.getPhysicalNumberOfRows();
					FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
					Float maxMarks = (float) formulaEvaluator.evaluate(sheet.getRow(0).getCell(1)).getNumberValue();
					for (int r = 2; r < rowsCount; r++) {
						TestBean test = new TestBean();

						if (class11.isSelected())
							test.setClas(11);
						else
							test.setClas(12);

						test.setMaxMarks(maxMarks);
						test.setTestName(id.getText());
						if (threshold.getText().equals(""))
							test.setThresholdMarks((float) (0.5 * maxMarks));
						else
							test.setThresholdMarks((float) (Float.parseFloat(threshold.getText()) * maxMarks * 0.01));

						Row row = sheet.getRow(r);
						String name = "", obMarks = "";
						for (int c = 0; c < 2; c++) {
							Cell cell = row.getCell(c);
							CellValue cellValue = formulaEvaluator.evaluate(cell);
							if (c == 0) {
								try {
									name = "" + cellValue.getStringValue();
								} catch (Exception ex) {
									break;
								}
							} else {
								try {
									if (cellValue.getCellType() == Cell.CELL_TYPE_NUMERIC)
										obMarks = String.valueOf(cellValue.getNumberValue());
									else
										obMarks = cellValue.getStringValue();

								} catch (Exception ex) {
									obMarks = "0.0";
								}
								test.setObtainedMarks(obMarks);
							}
						}
						marks.put(name, test);
					}
				} catch (Exception ee) {
					System.out.println(ee.getMessage());
				}
				ok.setEnabled(false);
				msg.setText("Uploading Marks. Please wait...");
				new Thread(new Runnable() {

					@Override
					public void run() {
						DatabaseConnector db = new DatabaseConnector();
						String res = db.writeMarks(marks);
						msg.setText(res);
						ok.setEnabled(true);
						if(res.equals("Data saved Successfully")) {
							testSelector.dispose();
						}
					}
				}).start();

			}

		});
		testSelector.add(ok);
		testSelector.setSize(550, 270);
		testSelector.setLocation(dim.width / 2 - testSelector.getSize().width / 2,
				dim.height / 2 - testSelector.getSize().height / 2);
		testSelector.add(testPanel);
		testSelector.setVisible(true);

	}

}
