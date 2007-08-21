/**
 * QuizUpdaterUI.java
 * (c) 2007 Ann Marie Steichmann
 */
package quizgen.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import quizgen.Choice;
import quizgen.Question;
import quizgen.QuizUpdater;
import quizgen.actions.ExitAction;

/**
 * <code>QuizUpdaterUI</code> GUI for updating quizzes.
 * @author Ann Marie Steichmann
 *
 */
public class QuizUpdaterUI extends JFrame {

	private static final long serialVersionUID = 5887601051150073538L;
	
	private static final char FIRST_CHAR = QuizGeneratorUI.FIRST_CHAR;
	private static final char LAST_CHAR = QuizGeneratorUI.LAST_CHAR;
	
	private JTextField questionIdField;
	private JTextArea questionArea;
	private Map<Character,JTextField> answerFields;
	private Map<Character,JRadioButton> answerRadioButtons;
	
	public QuizUpdaterUI() {
		super( "Quiz Updater v.1" );
		
		initMenu();
		initComponents();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize( 800, 600 );
		pack();
	}
	
	private void initMenu() {
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar( menuBar );
		menuBar.add( createFileMenu() );
	}
	
	private JMenu createFileMenu() {
		
		JMenu fileMenu = new JMenu( "File" );
		JMenuItem exitMenuItem = new JMenuItem();
		exitMenuItem.setAction( new ExitAction() );
		fileMenu.add( exitMenuItem );
		return fileMenu;
	}
	
	private void initComponents() {
		
		getContentPane().setLayout( new BorderLayout() );
		getContentPane().add( createQuestionIdPanel(), BorderLayout.NORTH );
		getContentPane().add( createQuestionValuePanel(), BorderLayout.CENTER );
		getContentPane().add( createAnswerPanel(), BorderLayout.SOUTH );
	}
	
	private JPanel createQuestionIdPanel() {
		
		JPanel questionIdPanel = new JPanel();
		questionIdPanel.add( new JLabel( "Question Id: " ) );
		questionIdField = new JTextField( 5 );
		questionIdPanel.add( questionIdField );
		return questionIdPanel;
	}
	
	private JScrollPane createQuestionValuePanel() {
		
		questionArea = new JTextArea( 10, 50 );
		questionArea.setLineWrap(true);
		questionArea.setWrapStyleWord(true);
		return new JScrollPane( questionArea );
	}
	
	private JPanel createAnswerPanel() {
		
		ButtonGroup answerGroup = new ButtonGroup();
		answerFields = new LinkedHashMap<Character, JTextField>();
		answerRadioButtons = new LinkedHashMap<Character, JRadioButton>();
		JPanel answerPanel = new JPanel( new GridLayout( 6, 1 ) );
		for ( char c = FIRST_CHAR; c <= LAST_CHAR; c++ ) {
			JTextField answerField = new JTextField(40);
			answerFields.put( c, answerField );
			JPanel answerFieldPanel = new JPanel( new BorderLayout() );
			answerFieldPanel.add( new JLabel( c + ".   " ), BorderLayout.WEST );
			answerFieldPanel.add( answerField, BorderLayout.CENTER );
			JRadioButton answerRadio = new JRadioButton();
			answerRadioButtons.put( c, answerRadio );
			answerGroup.add( answerRadio );
			answerFieldPanel.add( answerRadio, BorderLayout.EAST );
			answerPanel.add( answerFieldPanel );
		}
		JPanel otherPanel = new JPanel();
		JButton loadButton = new JButton( "Load" );
		loadButton.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				loadQuestion();
			}
			
		});
		loadButton.setMnemonic( KeyEvent.VK_L );
		JButton addButton = new JButton( "Add" );
		addButton.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				addQuestion();
			}
			
		});
		addButton.setMnemonic( KeyEvent.VK_A );
		JButton deleteButton = new JButton( "Delete" );
		deleteButton.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				deleteQuestion();
			}
			
		});
		deleteButton.setMnemonic( KeyEvent.VK_D );
		JButton updateButton = new JButton( "Update" );
		updateButton.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				updateQuestion();
			}
			
		});
		updateButton.setMnemonic( KeyEvent.VK_U );
		JButton clearButton = new JButton( "Clear" );
		clearButton.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				clearQuestion();				
			}
			
		});
		clearButton.setMnemonic( KeyEvent.VK_C );
		JButton closeButton = new JButton();
		closeButton.setAction( new ExitAction() );
		otherPanel.add( loadButton );
		otherPanel.add( addButton );
		otherPanel.add( deleteButton );
		otherPanel.add( updateButton );
		otherPanel.add( clearButton );
		otherPanel.add( closeButton );
		answerPanel.add( otherPanel );
		return answerPanel;
	}
	
	private void loadQuestion() {
		
		String id = questionIdField.getText();
		if ( id != null && id.trim().length() > 0 ) {
			clearAnswerFields();
			QuizUpdater updater = new QuizUpdater("quizgen");
			try {
				Question question = updater.load( id );
				if ( question != null ) {
					questionArea.setText( question.getText() );
					List<Choice> choices = question.getChoices();
					for ( Choice choice : choices ) {
						char letter = choice.getLetter();
						JTextField answerField = answerFields.get( letter );
						answerField.setText( choice.getText() );
						JRadioButton answerButton = answerRadioButtons.get( letter );
						answerButton.setSelected( choice.isCorrect() );
					}
				}
			} catch (SQLException e) {
				System.out.println("Unable to load question: " + e);
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("Unable to load question: " + e);
				e.printStackTrace();
			} 
			updater.freeResources();
		}
	}
	
	private void addQuestion() {
		
		// TODO: validate
		String id = questionIdField.getText();
		String value = questionArea.getText();
		
		if ( id != null && id.trim().length() > 0 && 
			value != null && value.trim().length() > 0 ) {
		
			List<Choice> choices = new ArrayList<Choice>();
			for( char c = FIRST_CHAR; c <= LAST_CHAR; c++ ) {
				JTextField answerField = answerFields.get( c );
				String text = answerField.getText();
				JRadioButton answerRadio = answerRadioButtons.get( c );
				boolean correct = answerRadio.isSelected();
				if ( text != null && text.trim().length() > 0 ) {
					choices.add( new Choice( c, text, correct ) );
				}
			}
			
			Question question = new Question( id, value, choices );
			QuizUpdater updater = new QuizUpdater("quizgen");
			try {
				updater.add( question );
				System.out.println( "Question was successfully added" );
			} catch (SQLException e) {
				System.out.println("Unable to add question");
				e.printStackTrace();
			}
			updater.freeResources();
		}
	}
	
	private void deleteQuestion() {
		
		// TODO: validate
		String id = questionIdField.getText();
		if ( id != null && id.trim().length() > 0 ) {
			QuizUpdater updater = new QuizUpdater("quizgen");
			try {
				updater.delete( id );
				System.out.println( "Question was successfully deleted" );
			} catch (SQLException e) {
				System.out.println("Unable to delete question");
				e.printStackTrace();
			}
			updater.freeResources();
		}
	}
	
	private void updateQuestion() {
	
		deleteQuestion();
		addQuestion();
	}
	
	private void clearAnswerFields() {
		
		for ( char c = FIRST_CHAR; c <= LAST_CHAR; c++ ) {
			JTextField answerField = answerFields.get( c );
			answerField.setText( "" );
			JRadioButton answerButton = answerRadioButtons.get( c );
			answerButton.setSelected( false );
		}
	}
	
	private void clearQuestion() {
		
		questionIdField.setText( "" );
		questionArea.setText( "" );
		clearAnswerFields();
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		QuizUpdaterUI updaterUI = new QuizUpdaterUI();
		updaterUI.setVisible( true );
	}

}
