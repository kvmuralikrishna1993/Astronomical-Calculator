package calculator;

/**
 * <p> Title: BusinessLogic Class. </p>
 * 
 * <p> Description: The code responsible for performing the calculator business logic functions. 
 * This class performs the business logic of the calculator by using three CalculatorValues and 
 * performs actions with and on them.  The class expects data from the User Interface to arrive
 * as Strings and returns Strings to it.  This class calls the CalculatorValue class to do the 
 * computations and this class knows nothing about the actual representation of CalculatorValues,
 * that is the responsibility of the CalculatorValue class and the classes it calls.  Hiding the 
 * representation of calculator values from the business logic means that this code does not have
 * to change when there is a change in the representation changes. Similarly, the this class does
 * not need to change if aspects of the user interface changes.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2019 </p>
 * 
 * @author Lynn Robert Carter
 * @author K V MURALI KRISHNA
 * 
 * @version 4.00	2017-10-17	The JavaFX-based GUI for the implementation of a calculator.
 * @version 4.01	2019-02-24	I have added the square root operation in the calculator.
 * @version 4.02	2019-02-27	I have imported the Error Term Recognizer
 * @version 4.03	2019-03-01	Implemented the Roundoff method
 * @version 4.04	2019-03-02	Minor Documentation Update
 * @version 4.05	2019-03-05	Implemented the Double to UNumber Calculator
 * @version 4.06	2019-03-07	Implemented the Units in the UNumber Calculator
 * @version 4.07	2019-03-08	I added Hohmann Transfer Equation in the test cases.	
 */
/**
 * Class for business logic.
 */
public class BusinessLogic {

	/**********************************************************************************************
	 * 
	 * Attributes
	 * 
	 **********************************************************************************************/

	// These are the major calculator values
	int x = 0;
	private CalculatorValue operand1 = new CalculatorValue(new UNumber(x));
	private CalculatorValue operand2 = new CalculatorValue(new UNumber(x));
	private CalculatorValue result = new CalculatorValue(new UNumber(x));
	private String operand1ErrorMessage = "";
	private String operand1ErrorMessageET = "";
	private boolean operand1Defined = false;
	private String operand2ErrorMessage = "";
	private String operand2ErrorMessageET = "";
	private boolean operand2Defined = false;
	private String resultErrorMessage = "";

	/**********************************************************************************************
	 * 
	 * Constructors
	 * 
	 **********************************************************************************************/
	
	/**********
	 * This method initializes all of the elements of the business logic aspect of
	 * the calculator. There is no special computational initialization required, so
	 * the initialization of the attributes above are all that are needed.
	 */
	public BusinessLogic() {
	}

	/**********************************************************************************************
	 * 
	 * Getters and Setters
	 * 
	 **********************************************************************************************/

	/**********
	 * This public setter takes an input String, checks to see if there is a
	 * non-empty input string. If so, it uses it to create a new CalculatorValue and
	 * places it into operand1, any associated error message is placed into
	 * operand1ErrorMessage, and sets the defined flag accordingly.
	 * 
	 * @param value
	 * @return True if the set did not generate an error; False if there was invalid
	 *         input
	 */
	/**
	 * Sets the unit operand 1.
	 *
	 * @param      SelectedItem  The selected item
	 */
	public void setUnitOperand1(String SelectedItem) {
//		System.out.println(SelectedItem+" --->setunitoperand 1");
		if(SelectedItem.equals(null)) {
			operand1.setUnit("- No Units Selected -");
		} else {
			operand1.setUnit(SelectedItem);
		}
	}
	/**
	 * Sets the unit operand 2.
	 *
	 * @param      SelectedItem  The selected item
	 */
	public void setUnitOperand2(String SelectedItem) {
//		System.out.println(SelectedItem+" --->setunitoperand 2");
		if(SelectedItem.equals(null)) {
			operand2.setUnit("- No Units Selected -");
		} else {
			operand2.setUnit(SelectedItem);
		}

	}
	/**
	 * Sets the operand 1.
	 *
	 * @param      value  The value
	 *
	 * @return     { description_of_the_return_value }
	 */
	public boolean setOperand1(String value) {
		operand1Defined = false; // Assume the operand will not be defined
		if (value.length() <= 0) { // See if the input is empty. If so no error
			operand1ErrorMessage = ""; // message, but the operand is not defined.
			return true; // Return saying there was no error.
		}
		operand1 = new CalculatorValue(value); // If there was input text, try to convert it
		operand1ErrorMessage = operand1.getErrorMessage(); // into a CalculatorValue and see if it
		if (operand1ErrorMessage.length() > 3) // worked. If there is a non-empty error
			return false; // message, signal there was a problem.
		operand1Defined = true; // Otherwise, set the defined flag and
		return true; // signal that the set worked
	}
	
	/**********
	 * This public setter takes an input String, checks to see if there is a
	 * non-empty input string. If so, it uses it to create a new CalculatorValue and
	 * places it into operand1, any associated error message is placed into
	 * operand1ErrorMessage, and sets the defined flag accordingly.
	 * 
	 * @param value
	 * @return True if the set did not generate an error; False if there was invalid
	 *         input
	 */
	public boolean setOperand1ErrorTerm(String value) {
		System.out.println(value);
		if (value.length() <= 0) { // See if the input is empty. If so no error
			operand1ErrorMessageET = ""; // message, but the operand is not defined.
			return true; // Return saying there was no error.
		}
		if (getOperand1Defined()) {
			operand1.setErrorTerm(value);
//			System.out.println(operand1.errorTerm);
			operand1ErrorMessageET = operand1.getErrorMessageforET();
			System.out.println(value + "-->" + operand1ErrorMessageET);
			if (operand1ErrorMessageET.length() > 3) {
				return false;
			}
			return true;
		}
		return false;
	}

	/**********
	 * This public setter takes an input String, checks to see if there is a
	 * non-empty input string. If so, it uses it to create a new CalculatorValue and
	 * places it into operand2, any associated error message is placed into
	 * operand2ErrorMessage, and sets the defined flag accordingly.
	 * 
	 * The logic of this method is the same as that for operand1 above.
	 * 
	 * @param value
	 * @return True if the set did not generate an error; False if there was invalid
	 *         input
	 */
	public boolean setOperand2(String value) { // The logic of this method is exactly the
		operand2Defined = false; // same as that for operand1, above.
		if (value.length() <= 0) {
			operand2ErrorMessage = "";
			return true;
		}
		operand2 = new CalculatorValue(value);
		operand2ErrorMessage = operand2.getErrorMessage();
		if (operand2ErrorMessage.length() > 3)
			return false;
		operand2Defined = true;
		return true;
	}

	/**********
	 * This public setter takes an input String, checks to see if there is a
	 * non-empty input string. If so, it uses it to create a new CalculatorValue and
	 * places it into operand1, any associated error message is placed into
	 * operand1ErrorMessage, and sets the defined flag accordingly.
	 * 
	 * @param value
	 * @return True if the set did not generate an error; False if there was invalid
	 *         input
	 */
	public boolean setOperand2ErrorTerm(String value) {

		if (value.length() <= 0) { // See if the input is empty. If so no error
			operand2ErrorMessage = ""; // message, but the operand is not defined.
			return true; // Return saying there was no error.
		}
		if (getOperand1Defined()) {
			operand2.setErrorTerm(value);
			operand2ErrorMessageET = operand2.getErrorMessageforET();
			if (operand2ErrorMessageET.length() > 3) {
				return false;
			}
			return true;
		}
		return false;
	}

	/**********
	 * This public setter takes an input String, checks to see if there is a
	 * non-empty input string. If so, it uses it to create a new CalculatorValue and
	 * places it into result and any associated error message is placed into
	 * resultErrorMessage.
	 * 
	 * The logic of this method is similar to that for operand1 above. (There is no
	 * defined flag.)
	 * 
	 * @param value
	 * @return True if the set did not generate an error; False if there was invalid
	 *         input
	 */
	public boolean setResult(String value) { // The logic of this method is similar to
		if (value.length() <= 0) { // that for operand1, above.
			operand2ErrorMessage = "";
			return true;
		}
		result = new CalculatorValue(value);
		resultErrorMessage = operand2.getErrorMessage();
		if (operand2ErrorMessage.length() > 3)
			return false;
		return true;
	}

	/**********
	 * This public setter sets the String explaining the current error in operand1.
	 * 
	 * @return This method returns nothing, but the operand1ErrorMessage has been
	 *         set
	 */
	public void setOperand1ErrorMessage(String m) {
		operand1ErrorMessage = m;
		return;
	}

	/**********
	 * This public getter fetches the String explaining the current error in
	 * operand1, it there is one, otherwise, the method returns an empty String.
	 * 
	 * @return an error message or an empty String if there was no error
	 */
	public String getOperand1ErrorMessage() {
		return operand1ErrorMessage;
	}

	/**********
	 * This public getter fetches the String explaining the current error in
	 * operand1, it there is one, otherwise, the method returns an empty String.
	 * 
	 * @return an error message or an empty String if there was no error
	 */
	public String getOperand1ErrorMessageforET() {
		return operand1ErrorMessageET;
	}

	/**********
	 * This public setter sets the String explaining the current error into
	 * operand2.
	 * 
	 * @return This method returns nothing, but the operand2ErrorMessage has been
	 *         set
	 */
	public void setOperand2ErrorMessage(String m) {
		operand2ErrorMessage = m;
		return;
	}

	/**********
	 * This public getter fetches the String explaining the current error in
	 * operand2, it there is one, otherwise, the method returns an empty String.
	 * 
	 * @return an error message or an empty String if there was no error
	 */
	public String getOperand2ErrorMessage() {
		return operand2ErrorMessage;
	}

	/**********
	 * This public getter fetches the String explaining the current error in
	 * operand1, it there is one, otherwise, the method returns an empty String.
	 * 
	 * @return an error message or an empty String if there was no error
	 */
	public String getOperand2ErrorMessageforET() {
		return operand2ErrorMessageET;
	}

	/**********
	 * This public setter sets the String explaining the current error in the
	 * result.
	 * 
	 * @return This method returns nothing, but the resultErrorMessage has been set
	 */
	public void setResultErrorMessage(String m) {
		resultErrorMessage = m;
		return;
	}

	/**********
	 * This public getter fetches the String explaining the current error in the
	 * result, it there is one, otherwise, the method returns an empty String.
	 * 
	 * @return and error message or an empty String if there was no error
	 */
	public String getResultErrorMessage() {
		return resultErrorMessage;
	}

	/**********
	 * This public getter fetches the defined attribute for operand1. You can't use
	 * the lack of an error message to know that the operand is ready to be used. An
	 * empty operand has no error associated with it, so the class checks to see if
	 * it is defined and has no error before setting this flag true.
	 * 
	 * @return true if the operand is defined and has no error, else false
	 */
	public boolean getOperand1Defined() {
		return operand1Defined;
	}

	/**********
	 * This public getter fetches the defined attribute for operand2. You can't use
	 * the lack of an error message to know that the operand is ready to be used. An
	 * empty operand has no error associated with it, so the class checks to see if
	 * it is defined and has no error before setting this flag true.
	 * 
	 * @return true if the operand is defined and has no error, else false
	 */
	public boolean getOperand2Defined() {
		return operand2Defined;
	}

	/**********************************************************************************************
	 * 
	 * The toString() Method
	 * 
	 **********************************************************************************************/

	/**********
	 * This toString method invokes the toString method of the result type
	 * (CalculatorValue is this case) to convert the value from its hidden internal
	 * representation into a String, which can be manipulated directly by the
	 * BusinessLogic and the UserInterface classes.
	 */
	public String toString() {
		return result.toString();
	}

	/**********
	 * This public toString method is used to display all the values of the
	 * BusinessLogic class in a textual representation for debugging purposes.
	 * 
	 * @return a String representation of the class
	 */
	public String debugToString() {
		String r = "\n******************\n*\n* Business Logic\n*\n******************\n";
		r += "operand1 = " + operand1.toString() + "\n";
		r += "     operand1ErrorMessage = " + operand1ErrorMessage + "\n";
		r += "     operand1Defined = " + operand1Defined + "\n";
		r += "operand2 = " + operand2.toString() + "\n";
		r += "     operand2ErrorMessage = " + operand2ErrorMessage + "\n";
		r += "     operand2Defined = " + operand2Defined + "\n";
		r += "result = " + result.toString() + "\n";
		r += "     resultErrorMessage = " + resultErrorMessage + "\n";
		r += "*******************\n\n";
		return r;
	}

	/**********************************************************************************************
	 * 
	 * Business Logic Operations (e.g. addition)
	 * 
	 **********************************************************************************************/

	/**********
	 * This public method computes the sum of the two operands using the
	 * CalculatorValue class method for addition. The goal of this class is to
	 * support a wide array of different data representations without requiring a
	 * change to this class, user interface class, or the Calculator class.
	 * 
	 * This method assumes the operands are defined and valid. It replaces the left
	 * operand with the result of the computation and it leaves an error message, if
	 * there is one, in a String variable set aside for that purpose.
	 * 
	 * This method does not take advantage or know any detail of the representation!
	 * All of that is hidden from this class by the CalculatorValue class and any
	 * other classes that it may use.
	 * 
	 * @return a String representation of the result
	 */
	public String addition() {
//		System.out.println(Arrays.toString(units));
		result = new CalculatorValue(operand1);
		result.add(operand2);
		resultErrorMessage = result.getErrorMessage();
		return result.toString();
	}

	/**********
	 * This public method computes the subtraction of the two operands using the
	 * CalculatorValue class method for subtractions. The goal of this class is to
	 * support a wide array of different data representations without requiring a
	 * change to this class, user interface class, or the Calculator class.
	 * 
	 * This method assumes the operands are defined and valid. It replaces the left
	 * operand with the result of the computation and it leaves an error message, if
	 * there is one, in a String variable set aside for that purpose.
	 * 
	 * This method does not take advantage or know any detail of the representation!
	 * All of that is hidden from this class by the CalculatorValue class and any
	 * other classes that it may use.
	 * 
	 * @return a String representation of the result
	 */
	public String subtraction() {
		result = new CalculatorValue(operand1);
		result.sub(operand2);
		resultErrorMessage = result.getErrorMessage();
		return result.toString();
	}

	/**********
	 * This public method computes the multiplication of the two operands using the
	 * CalculatorValue class method for multiplication. The goal of this class is to
	 * support a wide array of different data representations without requiring a
	 * change to this class, user interface class, or the Calculator class.
	 * 
	 * This method assumes the operands are defined and valid. It replaces the left
	 * operand with the result of the computation and it leaves an error message, if
	 * there is one, in a String variable set aside for that purpose.
	 * 
	 * This method does not take advantage or know any detail of the representation!
	 * All of that is hidden from this class by the CalculatorValue class and any
	 * other classes that it may use.
	 * 
	 * @return a String representation of the result
	 */
	public String multiplication() {
		result = new CalculatorValue(operand1);
		result.mpy(operand2);
		resultErrorMessage = result.getErrorMessage();
		return result.toString();
	}

	/**********
	 * This public method computes the square root of the two operands using the
	 * CalculatorValue class method for root method. The goal of this class is to
	 * support a wide array of different data representations without requiring a
	 * change to this class, user interface class, or the Calculator class.
	 * 
	 * This method assumes the operands are defined and valid. It replaces the left
	 * operand with the result of the computation and it leaves an error message, if
	 * there is one, in a String variable set aside for that purpose.
	 * 
	 * This method does not take advantage or know any detail of the representation!
	 * All of that is hidden from this class by the CalculatorValue class and any
	 * other classes that it may use.
	 * 
	 * @return a String representation of the result
	 */

	public String root() {
		result = new CalculatorValue(operand1);
		result.root();
		resultErrorMessage = result.getErrorMessage();
		if (resultErrorMessage.equals("__0")) {
			return result.toString();
		} else {
			resultErrorMessage = resultErrorMessage.substring(0, resultErrorMessage.length() - 3);
			return "";
		}
	}

	/**********
	 * This public method computes the division of the two operands using the
	 * CalculatorValue class method for division. The goal of this class is to
	 * support a wide array of different data representations without requiring a
	 * change to this class, user interface class, or the Calculator class.
	 * 
	 * This method assumes the operands are defined and valid. It replaces the left
	 * operand with the result of the computation and it leaves an error message, if
	 * there is one, in a String variable set aside for that purpose.
	 * 
	 * This method does not take advantage or know any detail of the representation!
	 * All of that is hidden from this class by the CalculatorValue class and any
	 * other classes that it may use.
	 * 
	 * @return a String representation of the result
	 */
	public String division() {
		result = new CalculatorValue(operand1);
		result.div(operand2);
		resultErrorMessage = result.getErrorMessage();
		if (resultErrorMessage.equals("__0")) {
			return result.toString();
		} else {
			resultErrorMessage = resultErrorMessage.substring(0, resultErrorMessage.length() - 3);
			return "";
		}

	}

}
