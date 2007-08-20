/**
 * QuizGeneratorUI.java
 * (c) 2007 Ann Marie Steichmann
 */
package quizgen.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import quizgen.Choice;
import quizgen.Question;
import quizgen.QuizGenerator;
import quizgen.Score;
import quizgen.actions.ExitAction;
import quizgen.sound.IndicatorSound;

/**
 * <code>QuizGeneratorUI</code> GUI for displaying/scoring quizzes.
 * @author Ann Marie Steichmann
 *
 */
public class QuizGeneratorUI extends JFrame {
		
	private static final long serialVersionUID = 8384847771877135955L;
	
	public static final char FIRST_CHAR = 'a';
	public static final char LAST_CHAR = 'e';
	
	private Iterator<Question> questions;
	private Score score;
	private Question current;
	private char correct;
	
	private JLabel scoreLabel;
	private JTextArea questionArea;
	private Map<Character,JButton> answerButtons;
	private JButton nextButton;
	
	private List<String> wrongAnswers;
	
	public QuizGeneratorUI() {
		super( "Quiz Generator v.1" );
		
		initMenu();
		initComponents();
		initQuestions();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize( 800, 600 );
		pack();
	}
	
	private void initQuestions() {
		
		// TODO: get DB name from user
		QuizGenerator generator = new QuizGenerator("quizgen");
		List<Question> shuffled = generator.listShuffled();
		score = new Score( shuffled.size() );
		wrongAnswers = new ArrayList<String>();
		questions = shuffled.iterator();
		generator.freeResources();
		updateScore();
		updateDisplay();
	}
	
	private void updateScore() {
		scoreLabel.setText( score.toString() );
	}
	
	private void updateDisplay() {
		
		nextButton.setEnabled( false );
		
		questionArea.setText( "" );
		
		for ( char c = FIRST_CHAR; c <= LAST_CHAR; c++ ) {
			answerButtons.get( c ).setText( "" );
		}
		
		if ( questions.hasNext() ) {
			
			current = questions.next();
			
			questionArea.setText( current.getId() + "\n\n" +
					current.getText() );
			
			List<Choice> choices = current.getChoices();
			for ( Choice choice : choices ) {
				char letter = choice.getLetter();
				if ( choice.isCorrect() ) {
					correct = letter;
				}
				JButton answerButton = answerButtons.get( letter );
				if ( answerButton != null ) {
					answerButton.setText( letter + ".   " + choice.getText() );
				} else {
					System.out.println("Unsuppported choice: " + letter);
				}
			}
			
			enableAnswerButtons( true );
			
		} else {
			
			if ( wrongAnswers.size() > 0 ) {
				StringBuffer summary = new StringBuffer( "You got the following questions wrong:\n" );
				for ( String wrongAnswer : wrongAnswers ) {
					summary.append( wrongAnswer + "\n" );
				}
				JOptionPane.showMessageDialog( this, summary.toString() );
			}
		}
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
		getContentPane().add( createStatusPanel(), BorderLayout.NORTH );
		getContentPane().add( createQuestionPanel(), BorderLayout.CENTER );
		getContentPane().add( createAnswerPanel(), BorderLayout.SOUTH );
	}
	
	private JPanel createStatusPanel() {
		
		JPanel statusPanel = new JPanel();
		JLabel scoreHeader = new JLabel( "Score: " );
		scoreHeader.setFont( new Font("arial,helvetica",Font.BOLD,14) );
		statusPanel.add( scoreHeader );
		scoreLabel = new JLabel();
		scoreLabel.setFont( new Font("arial,helvetia",Font.PLAIN,14) );
		statusPanel.add( scoreLabel );
		return statusPanel;
	}
	
	private JScrollPane createQuestionPanel() {
		
		questionArea = new JTextArea( 10, 50 );
		questionArea.setEditable( false );
		questionArea.setLineWrap(true);
		questionArea.setWrapStyleWord(true);
		return new JScrollPane( questionArea );
	}
	
	private JPanel createAnswerPanel() {
		
		JPanel answerPanel = new JPanel( new GridLayout(6, 1) );
		answerButtons = new LinkedHashMap<Character,JButton>();
		for ( char c = FIRST_CHAR; c <= LAST_CHAR; c++ ) {
			JButton answerButton = new JButton();
			answerButtons.put( c, answerButton );
			answerPanel.add( answerButton );
			answerButton.setHorizontalAlignment( JButton.LEFT );
			final char letter = c;
			answerButton.addActionListener( new ActionListener() {
	
				@Override
				public void actionPerformed(ActionEvent arg0) {
					score( letter );
				}
				
			});
		}
		JPanel otherButtonPanel = new JPanel();
		JButton newButton = new JButton( "New Quiz" );
		newButton.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				initQuestions();
			}
			
		});
		nextButton = new JButton( "Next Question" );
		nextButton.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				updateDisplay();
			}
			
		});
		JButton closeButton = new JButton();
		closeButton.setAction( new ExitAction() );
		otherButtonPanel.add( newButton );
		otherButtonPanel.add( nextButton );
		otherButtonPanel.add( closeButton );
		answerPanel.add( otherButtonPanel );
		return answerPanel;
	}
	
	private void score( char letter ) {
		
		IndicatorSound sound = new IndicatorSound();
		
		if ( letter == correct ) {
			score.correct();
			sound.playCorrectSound();
			JOptionPane.showMessageDialog( this, "Correct" );
		} else {
			wrongAnswers.add( current.getId() );
			score.incorrect();
			sound.playIncorrectSound();
			JOptionPane.showMessageDialog( this, "Incorrect.  The correct answer is '" + correct + "'." );
		}
		
		updateScore();
		
		enableAnswerButtons( false );
		
		if ( questions.hasNext() ) {
			nextButton.setEnabled( true );
			nextButton.setSelected( true );
		}
	}
	
	private void enableAnswerButtons( boolean enabled ) {
		
		for ( char c = FIRST_CHAR; c <= LAST_CHAR; c++ ) {
			JButton answerButton = answerButtons.get( c );
			answerButton.setEnabled( enabled );
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		QuizGeneratorUI generatorUI = new QuizGeneratorUI();
		generatorUI.setVisible( true );
	}
}