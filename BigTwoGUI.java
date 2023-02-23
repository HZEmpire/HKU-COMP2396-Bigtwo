import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

/**
 * The BigTwoGUI class implements the CardGameUI interface. It is used to build a GUI for 
 * the Big Two card game and handle all user actions.
 * 
 * @author Xu Haozhou
 * @version 1.0
 */
public class BigTwoGUI implements CardGameUI{

	private BigTwo game;// A Big Two card game associates with this GUI.
	private BigTwoClient client; //The client of this card game.
	private int uiplayerindex; //The integer specifying the index of the current ui user.
	private int activePlayer; // An integer specifying the index of the active player.
	private JFrame frame; // The main window of the application.
	private BigTwoPanel bigTwoPanel; // A panel for showing the cards of each player and the cards played on the table.
	private JMenuBar framebar; //The menubar for this game.
	private JButton playButton = new JButton("Play"); // A “Play” button for the active player to play the selected cards.
	private JButton passButton = new JButton("Pass"); //A “Pass” button for the active player to pass his/her turn to the next player.
	private JTextArea msgArea; //A text area for showing the current game status as well as end of game messages.
	private JTextArea chatArea; // A text area for showing chat messages sent by the players.
	private JTextField chatInput; // A text field for players to input chat messages.
	private JPanel downpanel; //The down field.
	private boolean [][] cardselected = new boolean[4][13]; //A list to store if a card is chosen by user.
	private boolean Able; //Used to determine whether the listener is activated.

	private final String [] SUIT_NAME = {"d", "c", "h", "s"}; //Part of file name of the image.
	private final String [] RANK_NAME = { "a", "2", "3", "4", "5", "6", "7", "8", "9", "t", "j", "q", "k"}; //Part of file name of the image.
	private final String [] PLAYER_NAME = {"p0", "p1", "p2", "p3"}; //Part of file name of the image.
	private final String JPG = ".jpg"; //One image type name.
	private final String GIF = ".gif"; //One image type name.
	private final String BACKGROUND = "tablephoto"; //Table background file name.
	private final String PHOTOPATH ="Image/"; //Path of the images.
	private Image backcard = new ImageIcon(PHOTOPATH + "b" + GIF).getImage(); //Table background image.
	private Image background = new ImageIcon(PHOTOPATH + BACKGROUND + JPG).getImage(); //Back cark image.
	private Image [] cardsimage = new Image[52]; //Face of 52 cards.
	private Image [] usersimage = new Image[4]; //Image of 4 players.

	/**
	 * Constructor for creating a BigTwoGUI. The parameter 
	 * game is a reference to a Big Two card game associates with this GUI.
	 * It will will create most components.
	 * 
	 */
	BigTwoGUI(BigTwo game){
		//Below part is set the frame.
		this.game = game;
		frame = new JFrame();
		frame.setLayout(null);
		frame.setSize(970, 790);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Frame set finished.

		//Below part is set for message print.
		msgArea = new JTextArea();
		chatArea = new JTextArea();
		chatInput = new JTextField(19);
		chatInput.addActionListener(new ChatInputListener());

		msgArea.setPreferredSize(new Dimension(299,100000));
		msgArea.setMaximumSize(new Dimension(299,340));
		msgArea.setEditable(false);
		msgArea.setLineWrap(true);
		msgArea.setWrapStyleWord(true);
		JScrollPane msgscroll = new JScrollPane(msgArea);

		chatArea.setPreferredSize(new Dimension(299,100000));
		chatArea.setMaximumSize(new Dimension(299,340));
		chatArea.setEditable(false);
		chatArea.setLineWrap(true);
		chatArea.setWrapStyleWord(true);
		JScrollPane chatscroll = new JScrollPane(chatArea);

		JPanel inputpanel = new JPanel(); //The panel used for store chatinput.
		JLabel mslabel = new JLabel("Msg:");
		inputpanel.add(mslabel);
		inputpanel.add(chatInput);
		inputpanel.setPreferredSize(new Dimension(299 , 15));
		inputpanel.setMaximumSize(new Dimension(299, 15));

		JPanel rightpanel = new JPanel();//The panel put at right to hold all right elements.
		rightpanel.setLayout(new BoxLayout(rightpanel,BoxLayout.Y_AXIS));
		rightpanel.add(msgscroll,BorderLayout.NORTH);
		rightpanel.add(chatscroll,BorderLayout.CENTER);
		rightpanel.add(inputpanel, BorderLayout.SOUTH);
		rightpanel.setLocation(671, 0);
		rightpanel.setSize(299, 700);
		frame.add(rightpanel);
		//Message input part stops here.

		//Below is used for down part.
		downpanel = new JPanel();
		downpanel.setBackground(new Color(22, 75, 67));
		playButton.addActionListener(new PlayButtonListener());
		passButton.addActionListener(new PassButtonListener());
		downpanel.setLocation(0, 700);
		downpanel.setSize(970, 60);
		downpanel.add(playButton);
		downpanel.add(passButton);
		frame.add(downpanel);
		//Down part finished.

		//Below is for menu part.
		framebar = new JMenuBar();
		framebar.setSize(760, 30);
		JMenu control = new JMenu("Game");
		JMenuItem about = new JMenuItem("About");
		JMenuItem restart = new JMenuItem("Connect");
		JMenuItem exit = new JMenuItem("Quit");
		JMenuItem help = new JMenuItem("Help");
		help.setMaximumSize(new Dimension(70, help.getPreferredSize().height));
		restart.addActionListener(new ConnectMenuItemListener());
		about.addActionListener(new AboutMenuItemListener());
		exit.addActionListener(new QuitMenuItemListener());
		help.addActionListener(new HelpMenuItemListener());
		control.add(about);
		control.add(restart);
		control.add(exit);
		framebar.add(control);
		framebar.add(help);
		frame.setJMenuBar(framebar);
		//Menu part finish.

		//Get user images.
		for(int i = 0; i < 4; i++){
			usersimage[i] = new ImageIcon(PHOTOPATH + PLAYER_NAME[i] + JPG).getImage();
		}

		//Get card images.
		for(int i = 0; i < 52; i++){
			cardsimage[i] = new ImageIcon(PHOTOPATH + RANK_NAME[i/4] + SUIT_NAME[i%4] + GIF).getImage();
			cardselected[i%4][i/4] = false;
		}

		//Disable user action.
		disable();

		//Below is set for the bigTwoPanel.
		bigTwoPanel = new BigTwoPanel();
		bigTwoPanel.setLocation(0,0);
		bigTwoPanel.setSize(670, 700);
		frame.add(bigTwoPanel);
		//bigTwoPanel part finished.

		//Set the index of the uiplayer.
		setUiplayer(-1);

		frame.setResizable(false);
		frame.setVisible(true);
	}

	/**
	 * Method for seting the client of the game.
	 *
	 * @param client The client linked to this game.
	 */
	public void setClient(BigTwoClient client){
		this.client = client;
	}

	/**
	 * Set the index of the ui player.
	 *
	 * @param uiplayer An int value representing the index of the ui player.
	 */
	public void setUiplayer(int uiplayer){
		if (uiplayer < 0 || uiplayer >= game.getNumOfPlayers()) {
			this.uiplayerindex = -1;
		} else {
			this.uiplayerindex = uiplayer;
		}
	}

    /**
	 * Sets the index of the active player (i.e., the current player).
	 * 
	 * @param activePlayer an int value representing the index of the active player
	 */
	public void setActivePlayer(int activePlayer){
        if (activePlayer < 0 || activePlayer >= game.getNumOfPlayers()) {
			this.activePlayer = -1;
		} else {
			this.activePlayer = activePlayer;
		}
    }

	/**
	 * Repaints the user interface.
	 *
	 */
	public void repaint(){
		//Repaint the bigtwopanel.
		bigTwoPanel.setcordinate();
		
		//Reset chatinput.
		chatInput.setText("");
		frame.repaint();
    }

	/**
	 * Prints the specified string to the message area of the card game user
	 * interface.
	 * 
	 * @param msg The string to be printed to the message area of the card game user
	 *            interface
	 */
	public void printMsg(String msg){
		msgArea.append(msg);
		msgArea.setCaretPosition(msgArea.getDocument().getLength());
    }

	/**
	 * Print the user message to the msgArea.
	 * 
	 * @param msg The message need to print.
	 */
	public void printChat(String msg){
		chatArea.append(msg);
		chatArea.setCaretPosition(chatArea.getDocument().getLength());
	}

	/**
	 * Clears The message area of the card game user interface.
	 * 
	 */
	public void clearMsgArea(){
		msgArea.setText("");
		chatArea.setText("");
    }

	/**
	 * Resets the card game user interface.
	 * 
	 */
	public void reset(){
		resetCardselect();
		clearMsgArea();
		enable();
	}

	/**
	 * Enables user interactions.
	 * 
	 */
	public void enable(){
		this.Able = true;
		passButton.setEnabled(true);
		playButtonstatus();
	}

	/**
	 * Disables user interactions.
	 * 
	 */
	public void disable(){
		this.Able = false;
		passButton.setEnabled(false);
		playButton.setEnabled(false);
    }

	/**
	 * Prompts active player to select cards and make his/her move.
	 * 
	 */
	public void promptActivePlayer(){
		resetCardselect();
		playButtonstatus();
		if(activePlayer >= 0)
			printMsg(game.getPlayerList().get(activePlayer).getName() + "'s turn: \n");

		if(activePlayer == uiplayerindex)
			enable();
		else{
			disable();
		}//End of game.
		repaint();
    }

	/**
	 * Method for getting the index of current user selected cards.
	 * 
	 * @return The array of the selected cards index.
	 */
	public int[] getSelected(){
		int cardNum = 0;
		int current = 0;
		for (int i = 0; i < 13; i++){
			if (cardselected[activePlayer][i]){
				cardNum++;
			}
		}
		if(cardNum == 0)
			return null;

		int[] cardIdx = new int[cardNum];

		for (int i=0; i < 13; i++){
			if (cardselected[activePlayer][i]){
				cardIdx[current]=i;
				current++;
			}
		}
		return cardIdx;
	}

	/**
	 * Method for setting all cards to be unselected.
	 * 
	 */
	public void resetCardselect() {
		for(int i = 0; i < 52; i++){
			cardselected[i % 4][i / 4] = false;
		}
	}

	/**
	 * Method for setting if the playButton can be enabled.
	 * 
	 */
	public void playButtonstatus() {
		for(int i = 0; i < 52; i++){
			if(cardselected[i % 4][i / 4]){
				playButton.setEnabled(true);
				return;
			}
		}
		playButton.setEnabled(false);
	}

	/**
	 * An inner class that extends the JPanel class and implements the 
	 * MouseListener interface. Overrides the paintComponent() method inherited from the 
	 * JPanel class to draw the card game table. Implements the mouseClicked() method 
	 * from the MouseListener interface to handle mouse click events.
	 * 
	 * @author Xu Haozhou
	 * @version 1.0
	 */
	class BigTwoPanel extends JPanel implements MouseListener{
		/**
		 * Constructor for constructing a BigTwoPanel by setting the coordinate
		 * of the images and needed MouseListener.
		 * 
		 */
		public BigTwoPanel(){
			setcordinate();
			this.addMouseListener(this);
		}

		private int [][] xcord = new int [4][13]; //The x coordinate of the cards.
		private int [][] ycord = new int [4][13]; //The y coordinate of the cards.

		/**
		 * Method for setting the coordinate of cards by given card-selected information.
		 * 
		 */
		public void setcordinate(){
			for (int i = 0; i < 4; i++){
				for(int j = 0; j < 13; j++){
					xcord[i][j] = 157 + j * 20;
					if(!cardselected[i][j]){
						ycord[i][j] = 140 * i + 30;
					}
					else{
						ycord[i][j] = 140 * i + 20;
					}
				}
			}
		}

		/**
		 * Method for draw the Image's of users and there cards, also draw Strings to state
		 * their information.
		 * 
		 */
		@Override
		public void paintComponent(Graphics g){
			g.drawImage(background, 0, 0, this); //Draw background.
			g.drawLine(0, 140, 670, 140);
			g.drawLine(0, 280, 670, 280);
			g.drawLine(0, 420, 670, 420);
			g.drawLine(0, 560, 670, 560);
			if(activePlayer == -1)
			{
				for(int i = 0; i < 4; i++){
					g.setColor(Color.YELLOW);
					g.drawString(game.getPlayerList().get(i).getName(), 30, 140*i + 15);
					if(!(game.getPlayerList().get(i).getName() == null) && !(game.getPlayerList().get(i).getName().equals("")))
						g.drawImage(usersimage[i], 30, 140*i+30,this);
					CardList the_one = game.getPlayerList().get(i).getCardsInHand();
					if(the_one.isEmpty())
						continue;
					for(int j = 0; j < the_one.size(); j++){
						g.drawImage(cardsimage[the_one.getCard(j).getRank()*4 + the_one.getCard(j).getSuit()], xcord[i][j], ycord[i][j], this);
					}
				}
			} // Case when game ends.
			else{
				for(int i = 0; i < 4; i++){
					if(!(game.getPlayerList().get(i).getName() == null) && !(game.getPlayerList().get(i).getName().equals("")))
						g.drawImage(usersimage[i], 30, 140*i+30,this);
					CardList the_one = game.getPlayerList().get(i).getCardsInHand();
					if(i != uiplayerindex){
						if(i != activePlayer)
							g.setColor(Color.YELLOW);
						else
							g.setColor(Color.MAGENTA);
						if(i == activePlayer)
							g.drawString(game.getPlayerList().get(i).getName() + "(Current player)", 30, 140*i + 15);
						else
							g.drawString(game.getPlayerList().get(i).getName(), 30, 140*i + 15);
						for(int j = 0; j < the_one.size(); j++){
							g.drawImage(backcard, xcord[i][j], ycord[i][j], this);
						}
					} //Draw back cards for other user.
					else{
						if(i != activePlayer)
							g.setColor(Color.YELLOW);
						else
							g.setColor(Color.MAGENTA);
						if(i == activePlayer)
							g.drawString(game.getPlayerList().get(i).getName() + " (You)(Current player)", 30, 140*i + 15);
						else
							g.drawString(game.getPlayerList().get(i).getName() + " (You)", 30, 140*i + 15);
						for(int j = 0; j < the_one.size(); j++){
							g.drawImage(cardsimage[the_one.getCard(j).getRank()*4 + the_one.getCard(j).getSuit()], xcord[i][j], ycord[i][j], this);
						}
					} //Draw face cards for current player.
				}
			} //Case game not end.
			g.setColor(Color.YELLOW);
			g.drawString("Current Hands on Table", 30, 560 + 15);
			if (!game.getHandsOnTable().isEmpty()){
				Hand beforehand = game.getHandsOnTable().get(game.getHandsOnTable().size() - 1);
				int beforesize = beforehand.size();
				for(int i = 0; i < beforesize; i++){
					g.drawImage(cardsimage[beforehand.getCard(i).getRank()*4 + beforehand.getCard(i).getSuit()], 30 + 100*i, 590, this);
				}
			}//Draw the hand on table if it is not empty.
		}

		/**
		 * Method for judging which card is selected by the user, used for Mouselistener handling the case when mouse is clicked.
		 * 
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			if(Able != true || activePlayer != uiplayerindex) //It is not enable.
				return;
			if (e.getX() >= 157 && e.getX() <= 470 && e.getY() >= (activePlayer * 140 + 20) && e.getY() <= ( activePlayer * 140 + 127)){
				int numberofcards = game.getPlayerList().get(activePlayer).getNumOfCards();
				for(int i = numberofcards - 1; i >= 0; i--){
					if(e.getX() <= xcord[activePlayer][i] + 73 && e.getX() >= xcord[activePlayer][i] && e.getY() >= ycord[activePlayer][i] && e.getY() <= ycord[activePlayer][i] + 97){
						if(cardselected[activePlayer][i]){
							cardselected[activePlayer][i] = false;
						}
						else{
							cardselected[activePlayer][i] = true;
						}
						this.setcordinate();
						playButtonstatus();
						break;
					}
				}
				frame.repaint();
			}//Judge which card is chosen.
		}

		/**
		 * Used for Mouselistener handling the case, this case will not be used.
		 * 
		 */
		@Override
		public void mouseEntered(MouseEvent e) {}

		/**
		 * Used for Mouselistener handling the case, this case will not be used.
		 * 
		 */
		@Override
		public void mouseExited(MouseEvent e) {}

		/**
		 * Used for Mouselistener handling the case, this case will not be used.
		 * 
		 */
		@Override
		public void mousePressed(MouseEvent e){}

		/**
		 * Used for Mouselistener handling the case, this case will not be used.
		 * 
		 */
		@Override
		public void mouseReleased(MouseEvent e) {}
	}

	/**
	 * An inner class that implements the ActionListener 
	 * interface. Implements the actionPerformed() method from the ActionListener interface 
	 * to handle button-click events for the “Play” button.
	 * 
	 * @author Xu Haozhou
	 * @version 1.0
	 */
	class PlayButtonListener implements ActionListener{

		/**
		 * Used for listener. Method for getting the card user chosen and judge if it is legal(Not empty) to put
		 * into the game object's makeMove() method when "Play" button is clicked. 
		 * 
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			if(Able != true || activePlayer != uiplayerindex) //Case not enable.
				return;
			int [] getCard = getSelected(); //Get the index of selected card.
			resetCardselect();
			client.sendMessage(new CardGameMessage(CardGameMessage.MOVE, -1, getCard));
		}
	}

	/**
	 * An inner class that implements the ActionListener
	 * interface. Implements the actionPerformed() method from the ActionListener interface 
	 * to handle button-click events for the “Pass” button.
	 * 
	 * @author Xu Haozhou
	 * @version 1.0
	 */
	class PassButtonListener implements ActionListener{

		/**
		 * Used for listener. Method for pass and call game object's makeMove method()
		 * when "Pass" button is clicked.
		 * 
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			if(Able != true || activePlayer != uiplayerindex)
				return;
			resetCardselect();
			client.sendMessage(new CardGameMessage(CardGameMessage.MOVE, -1, null));
		}
	}

	/**
	 * An inner class that implements the ActionListener interface. 
	 * Implements the actionPerformed() method from the ActionListener interface 
	 * to handle menu-item-click events for the “Connect” menu item.
	 * 
	 * @author Xu Haozhou
	 * @version 1.0
	 */
	class ConnectMenuItemListener implements ActionListener{
		/**
		 * Used for listener. Method for connecting to the server
		 * when "Connect" menu is clicked.
		 * 
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			if(!client.connectStatus()){
				reset();
				client.connect();
			}
		}
	}

	/**
	 * An inner class that implements the ActionListener interface. 
	 * Implements the actionPerformed() method from the ActionListener interface 
	 * to handle menu-item-click events for the “Quit” menu item.
	 * 
	 * @author Xu Haozhou
	 * @version 1.0
	 */
	class QuitMenuItemListener implements ActionListener{
		/**
		 * Used for listener. Method for ending the program
		 * when "Quit" menu is clicked.
		 * 
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}

	/**
	 * An inner class that implements the ActionListener interface. 
	 * Implements the actionPerformed() method from the ActionListener interface 
	 * to handle menu-item-click events for the “Help” menu item.
	 * 
	 * @author Xu Haozhou
	 * @version 1.0
	 */
	class HelpMenuItemListener implements ActionListener{
		/**
		 * Used for listener. Method for showing the help message
		 * when "Help" menu is clicked.
		 * 
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(null, "BigTwo Card Game\n" +
					"Click card to choose the card.\n" + 
					"Click \"Play\" to play the card.\n" + 
					"Click \"Pass\" to pass your trun.\n" +
					"Click \"Restart\" to start a new game.\n" +
					"Click \"Quit\" to exit the game.\n" +
					"Click \"About\" to get version information.\n" + 
					"Play rule:\n" + 
					"https://www.pagat.com/climbing/bigtwo.html/\n",
					"How two play",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * An inner class that implements the ActionListener interface. 
	 * Implements the actionPerformed() method from the ActionListener interface 
	 * to handle menu-item-click events for the “About” menu item.
	 * 
	 * @author Xu Haozhou
	 * @version 1.0
	 */
	class AboutMenuItemListener implements ActionListener{
		/**
		 * Used for listener. Method for showing the basic information about the program
		 * when "About" menu is clicked.
		 * 
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(null, "BigTwo Game\nAuthor: Xu Haozhou\n" +
					"Version 1.0", "Information", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * An inner class that implements the ActionListener interface. 
	 * Implements the actionPerformed() method from the ActionListener interface 
	 * to handle the input text in chatInput TextField.
	 * 
	 * @author Xu Haozhou
	 * @version 1.0
	 */
	class ChatInputListener implements ActionListener{
		/**
		 * Used for listener. Method for input the user's input text
		 * when "Carriage return" is pressed.
		 * 
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			String ms = chatInput.getText() + "\n";
			client.sendMessage(new CardGameMessage(CardGameMessage.MSG, -1, ms));
			chatInput.setText("");
		}
	}

}