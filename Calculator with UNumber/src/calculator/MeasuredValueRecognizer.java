package calculator;

public class MeasuredValueRecognizer {
	/**
	 * <p> Title: FSM-translated ErrorTermRecognizer. </p>
	 * 
	 * <p> Description: A demonstration of the mechanical translation of Finite State Machine 
	 * diagram into an executable Java program using the Measured Value Recognizer. The code 
	 * detailed design is based on a while loop with a select list</p>
	 * 
	 * <p> Copyright: Lynn Robert Carter © 2018 </p>
	 * 
	 
	 * @author K V MURALI KRISHNA
	 * 
	 * @version 0.00		2018-02-04	Initial baseline
	 * @version 0.01    	2019-02-11 	Implemented the FSM diagram in the Measured Value Recognizer. 
	 * 
	 */

	/**********************************************************************************************
	 * 
	 * Result attributes to be used for GUI applications where the returned string
	 * error message and pointer to the character of the error are not adequate for
	 * the required output.
	 * 
	 */

	public static String measuredValueErrorMessage = "";// The alternate error message text
	public static String measuredValueInput = ""; // The input being processed
	public static int measuredValueIndexofError = -1; // The index where the error was located
	private static int state = 0; // The current state value
	private static int nextState = 0; // The next state value
	private static boolean finalState = false; // Is this state a final state
	private static String inputLine = ""; // The input line
	private static char currentChar; // The current character in the line
	private static int currentCharNdx; // The index of the current character
	private static boolean running; // The flag that specifies if it is running

	/**********
	 * This private method display the input line and then on a line under it
	 * displays an up arrow at the point where an error was detected. This method is
	 * designed to be used to display the error message on the console terminal.
	 * 
	 * @param input          The input string
	 * @param currentCharNdx The location where an error was found
	 * @return Two lines, the entire input line followed by a line with an up arrow
	 */
	private static String displayInput(String input, int currentCharNdx) {
		// Display the entire input line
		String result = input + "\n";

		// Display a line with enough spaces so the up arrow point to the point of an
		// error
		for (int ndx = 0; ndx < currentCharNdx; ndx++)
			result += " ";

		// Add the up arrow to the end of the second line
		return result + "\u21EB"; // A Unicode up arrow with a base
	}

	private static void displayDebuggingInfo() {
		if (currentCharNdx >= inputLine.length())
			System.out.println(
					((state < 10) ? "  " : " ") + state + ((finalState) ? "       F   " : "           ") + "None");
		else
			System.out.println(((state < 10) ? "  " : " ") + state + ((finalState) ? "       F   " : "           ")
					+ "  " + currentChar + " " + ((nextState < 10) && (nextState != -1) ? "    " : "   ") + nextState);
	}

	private static void moveToNextCharacter() {
		currentCharNdx++;
		if (currentCharNdx < inputLine.length())
			currentChar = inputLine.charAt(currentCharNdx);
		else {
			currentChar = ' ';
			running = false;
		}
	}

	
	
	/**********
	 * This method is a mechanical transformation of a Finite State Machine diagram
	 * into a Java method.
	 * 
	 * @param input The input string for the Finite State Machine
	 * @return An output string that is empty if every things is okay or it will be
	 *         a string with a help description of the error follow by two lines
	 *         that shows the input line follow by a line with an up arrow at the
	 *         point where the error was found.
	 */
	public static String checkMeasureValue(String input) {
		if (input.length() <= 0)
			return "";
		// The following are the local variable used to perform the Finite State Machine
		// simulation
		state = 0; // This is the FSM state number
		inputLine = input; // Save the reference to the input line as a global
		currentCharNdx = 0; // The index of the current character
		currentChar = input.charAt(0); // The current character from the above indexed position

		// The Finite State Machines continues until the end of the input is reached or
		// at some
		// state the current character does not match any valid transition to a next
		// state

		measuredValueInput = input; // Set up the alternate result copy of the input
		running = true; // Start the loop
		System.out.println("\nCurrent Final Input  Next\nState   State Char  State");

		// The Finite State Machines continues until the end of the input is reached or
		// at some
		// state the current character does not match any valid transition to a next
		// state
		while (running) {
			// The switch statement takes the execution to the code for the current state,
			// where
			// that code sees whether or not the current character is valid to transition to
			// a
			// next state
			switch (state) {
			case 0:
				// State 0 has two valid transitions. Each is addressed by an if statement.

				// This is not a final state
				finalState = false;

				// If the current character is in the range from 1 to 9, it transitions to state
				// 1
				if (currentChar >= '0' && currentChar <= '9') {
					nextState = 1;
					break;
				}
				// If the current character is a decimal point, it transitions to state 3
				else if (currentChar == '.') {
					nextState = 3;
					break;
				}

				// If it is none of those characters, the FSM halts
				else
					running = false;

				// The execution of this state is finished
				break;

			case 1:
				// State 1 has three valid transitions. Each is addressed by an if statement.

				// This is a final state
				finalState = true;

				// In state 1, if the character is 0 through 9, it is accepted and we stay in
				// this
				// state
				if (currentChar >= '0' && currentChar <= '9') {
					nextState = 1;
					break;
				}

				// If the current character is a decimal point, it transitions to state 2
				else if (currentChar == '.') {
					nextState = 2;
					break;
				}
				// If the current character is an E or an e, it transitions to state 5
				else if (currentChar == 'E' || currentChar == 'e') {
					nextState = 5;
					break;
				}
				// If it is none of those characters, the FSM halts
				else
					running = false;

				// The execution of this state is finished
				break;

			case 2:
				// State 2 has two valid transitions. Each is addressed by an if statement.

				// This is a final state
				finalState = true;

				// If the current character is in the range from 1 to 9, it transitions to state
				// 1
				if (currentChar >= '0' && currentChar <= '9') {
					nextState = 2;
					break;
				}
				// If the current character is an 'E' or 'e", it transitions to state 5
				else if (currentChar == 'E' || currentChar == 'e') {
					nextState = 5;
					break;
				}

				// If it is none of those characters, the FSM halts
				else
					running = false;

				// The execution of this state is finished
				break;

			case 3:
				// State 3 has only one valid transition. It is addressed by an if statement.

				// This is not a final state
				finalState = false;

				// If the current character is in the range from 1 to 9, it transitions to state
				// 1
				if (currentChar >= '0' && currentChar <= '9') {
					nextState = 4;
					break;
				}

				// If it is none of those characters, the FSM halts
				else
					running = false;

				// The execution of this state is finished
				break;

			case 4:
				// State 4 has two valid transitions. Each is addressed by an if statement.

				// This is a final state
				finalState = true;

				// If the current character is in the range from 1 to 9, it transitions to state
				// 4
				if (currentChar >= '0' && currentChar <= '9') {
					nextState = 4;
					break;
				}
				// If the current character is an 'E' or 'e", it transitions to state 5
				else if (currentChar == 'E' || currentChar == 'e') {
					nextState = 5;
					break;
				}

				// If it is none of those characters, the FSM halts
				else
					running = false;

				// The execution of this state is finished
				break;

			case 5:
				// State 5 has two valid transitions. Each is addressed by an if statement.

				// This is not a final state
				finalState = false;

				// If the current character is in the range from 1 to 9, it transitions to state
				// 7
				if (currentChar >= '0' && currentChar <= '9') {
					nextState = 7;
					break;
				}
				// If the current character is an '+' or '-", it transitions to state 6
				else if (currentChar == '+' || currentChar == '-') {
					nextState = 6;
					break;
				}

				// If it is none of those characters, the FSM halts
				else
					running = false;

				// The execution of this state is finished
				break;

			case 6:
				// State 6 has one valid transition. Each is addressed by an if statement.

				// This is not a final state
				finalState = false;

				// If the current character is in the range from 1 to 9, it transitions to state
				// 7
				if (currentChar >= '0' && currentChar <= '9') {
					nextState = 7;
					break;
				}

				// If it is none of those characters, the FSM halts
				else
					running = false;

				// The execution of this state is finished
				break;

			case 7:
				// State 7 has one valid transition. Each is addressed by an if statement.

				// This is a final state
				finalState = true;

				// If the current character is in the range from 1 to 9, it transitions to state
				// 7
				if (currentChar >= '0' && currentChar <= '9') {
					nextState = 7;
					break;
				}
				// If it is none of those characters, the FSM halts
				else
					running = false;

				// The execution of this state is finished
				break;
			}

			if (running) {
				displayDebuggingInfo();
				// When the processing of a state has finished, the FSM proceeds to the next
				// character
				// in the input and if there is one, it fetches that character and updates the
				// currentChar. If there is no next character the currentChar is set to a blank.
				moveToNextCharacter();

				// Move to the next state
				state = nextState;

			}
			// Should the FSM get here, the loop starts again

		}

		System.out.println("The loop has ended.");

		measuredValueIndexofError = currentCharNdx; // Copy the index of the current character;

		// When the FSM halts, we must determine if the situation is an error or not.
		// That depends
		// of the current state of the FSM and whether or not the whole string has been
		// consumed.
		// This switch directs the execution to separate code for each of the FSM
		// states.
		switch (state) {
		case 0:
			// State 0 is not a final state, so we can return a very specific error message
			measuredValueIndexofError = currentCharNdx; // Copy the index of the current character;
			measuredValueErrorMessage = "The first character must be a digit or a decimal point.";
			return "The first character must be a digit or a decimal point.";

		case 1:
			// State 1 is a final state, so we must see if the whole string has been
			// consumed.
			if (currentCharNdx < input.length()) {
				// If not all of the string has been consumed, we point to the current character
				// in the input line and specify what that character must be in order to move
				// forward.
				measuredValueErrorMessage = "This character may only be an \"E\", an \"e\", a digit, "
						+ "a \".\", or it must be the end of the input.\n";
				return measuredValueErrorMessage + displayInput(input, currentCharNdx);
			} else {
				measuredValueIndexofError = -1;
				measuredValueErrorMessage = "";
				return measuredValueErrorMessage;
			}

		case 2:
		case 4:
			// States 2 and 4 are the same. They are both final states with only one
			// possible
			// transition forward, if the next character is an E or an e.
			if (currentCharNdx < input.length()) {
				measuredValueErrorMessage = "This character may only be an \"E\", an \"e\", or it must"
						+ " be the end of the input.\n";
				return measuredValueErrorMessage + displayInput(input, currentCharNdx);
			}
			// If there is no more input, the input was recognized.
			else {
				measuredValueIndexofError = -1;
				measuredValueErrorMessage = "";
				return measuredValueErrorMessage;
			}
		case 3:
		case 6:
			// States 3, and 6 are the same. None of them are final states and in order to
			// move forward, the next character must be a digit.
			measuredValueErrorMessage = "This character may only be a digit.\n";
			return measuredValueErrorMessage + displayInput(input, currentCharNdx);

		case 7:
			// States 7 is similar to states 3 and 6, but it is a final state, so it must be
			// processed differently. If the next character is not a digit, the FSM stops
			// with an
			// error. We must see here if there are no more characters. If there are no more
			// characters, we accept the input, otherwise we return an error
			if (currentCharNdx < input.length()) {
				measuredValueErrorMessage = "This character may only be a digit.\n";
				return measuredValueErrorMessage + displayInput(input, currentCharNdx);
			} else {
				measuredValueIndexofError = -1;
				measuredValueErrorMessage = "";
				return measuredValueErrorMessage;
			}

		case 5:
			// State 5 is not a final state. In order to move forward, the next character
			// must be
			// a digit or a plus or a minus character.
			measuredValueErrorMessage = "This character may only be a digit, a plus, or minus " + "character.\n";
			return measuredValueErrorMessage + displayInput(input, currentCharNdx);
		default:
			return "";
		}
	}
}
