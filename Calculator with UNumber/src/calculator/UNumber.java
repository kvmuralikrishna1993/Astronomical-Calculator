package calculator;


/**
 * <p> Title: UNumber Class, a component of the Unlimited Precision Math Package. </p>
 * 
 * <p> Description: A demonstration package to show the notion of a package and to 
 * 		exercise array usage </p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2017 </p>
 * 
 * @author Lynn Robert Carter
 * @author K V MURALI KRISHNA
 * 
 * @version 1.05	Added getter methods to support creation of supporting libraries in
 * 					order to expand UNumber's capabilities. 
 * 
 * @version 1.04	Corrected defect in toString(int), changed the output to display a 
 * 					non-zero (when possible) Most Significant Digit to the left of the
 * 					decimal point in the output, and refined the comments. 
 * 
 *  				Adjusted the normalize routine to produce a standard value of zero
 *  				(characteristic of zero, all mantissa digits are zero, and a sign flag
 *  				is true) and enhance the documentation to match.
 * 
 * @version 1.03	Change key attributes from private to protected to support inheritance,
 *					and changed the normalize method to protected to support inheritance,
 *					enhance toString(size), and  correct round error in mpy and div.  
 *
 * @version 1.02	Addition of code to support formatted output of UNumbers with an 
 * 					uncertainty factor, a "hasUNumber" and makeUNumber methods for reading 
 * 					in UNumbers, quality improvements, and the removal of the div2 method
 * 
 * @version 1.01	The initial version plus an improved add/sub, normalize, div, div2, 
 * 					lessThan, greaterThan, abs, and compareTo
 * 
 */

public class UNumber implements Comparable<UNumber> {
	
	/**
	 * An unlimited precision floating point value in this package is implemented using 
	 * 		<B>decimal</B> arithmetic. Values are stored in scientific notation (i.e. mantissa 
	 * 		x 10 to the characteristic power.  The "mantissa" is a sequence of 0 to 9 digits 
	 * 		with the assumed decimal point to the left of the Most Significant Digit (MSD) in 
	 * 		the mantissa.
	 * 
	 * The "characteristic" is an integer value.  This determines the location of the decimal 
	 * 		point and performs the equivalent of multiply the mantissa by ten raised to the 
	 * 		power of the characteristic
	 * 
	 * After any computation, the package ensures that the result is "normalized".  This means 
	 * that with the exception of a result value of zero, the MSD will be in the range "1" to "9".  
	 * We do this to speed future computations.  Normalization discards leading zeros in the result 
	 * and for each zero from the left side that is removed, the characteristic is reduced by one 
	 * to compensate.
	 */
	
	protected byte d [];		// The mantissa as a sequence of digits, most significant digits (MSD)
								// is in d[0]. The implied decimal point is to the left of the MSD

	protected int dP;			// The characteristic as decimal power of 10 
	protected boolean s;		// The sign, true means positive, false is negative
	
	/**
	 * This default constructor sets up a 20 digit number with a value of zero
	 */
	public UNumber() {
		d = new byte[20];
		for (int i = 0; i < 20; i++) d[i] = 0;
		dP = 0;
		s = true;
	}
	
	/**********
	 * This constructor creates a variable length number based on an integer input value
	 * 
	 * @param v
	 */
	public UNumber(int v){
		// Establish the sign and make the working value positive (or zero)
		if (v>=0) s = true;
		else {s = false; v = -v;}
		
		// Determine the number of significant digits by iterative dividing by 10
		int numDigits = 0;
		int tempV = v;
		while (tempV > 0){
			tempV = tempV/10;
			numDigits++;
		}
		
		// Working from the least significant toward the most, extract the units
		// digit, save it into the array, and then divide by 10 to create the
		// next least significant digit as the units digit
		dP = numDigits;
		d = new byte[numDigits];
		while (numDigits > 0) {
			d[--numDigits] = (byte) (v % 10);	// This extracts the units digit
			v = v / 10;							// This gets the next least significant digit
		}
	}
	
	/**********
	 * This constructor creates a variable length number based on a long integer input value
	 * 
	 * @param v
	 */
	public UNumber(long v){
		// Establish the sign and make the working value positive (or zero)
		if (v>=0) s = true;
		else {s = false; v = -v;}
		
		// Determine the number of significant digits by iterative dividing by 10
		int numDigits = 0;
		long tempV = v;
		while (tempV > 0){
			tempV = tempV/10;
			numDigits++;
		}

		// Working from the least significant toward the most, extract the units
		// digit, save it into the array, and then divide by 10 to create the
		// next least significant digit as the units digit
		dP = numDigits;
		d = new byte[numDigits];
		while (numDigits > 0) {
			d[--numDigits] = (byte) (v % 10);	// This extracts the units digit
			v = v /10;							// This get the next least significant digit
		}
	}
	
	/**********
	 * This constructor creates a number based on a digit sequence in a string, a power of ten, 
	 * and a sign
	 * 
	 * @param str	The string specifies the digit sequence and it's length is the number of 
	 * 		significant digits
	 * 
	 * @param dec	This is the characteristic (the power of 10)
	 * 
	 * @param sign	The sign
	 */
	public UNumber(String str, int dec, boolean sign){
		// Use the length of the string to determine the size of the mantissa
		d = new byte[str.length()];
		
		// Define each element of the mantissa by means of this switch
		for (int i = 0; i < str.length(); i++) 
			switch (str.charAt(i)){
			case '0': d[i] = 0;
			break;
			case '1': d[i] = 1;
			break;
			case '2': d[i] = 2;
			break;
			case '3': d[i] = 3;
			break;
			case '4': d[i] = 4;
			break;
			case '5': d[i] = 5;
			break;
			case '6': d[i] = 6;
			break;
			case '7': d[i] = 7;
			break;
			case '8': d[i] = 8;
			break;
			case '9': d[i] = 9;
			break;
			}
		
		// The characteristic and sign are exactly the same as the parameters
		dP = dec;
		s = sign;
	}
	
	/**********
	 * This constructor creates a number based on a digit sequence in a string, a power of ten, a 
	 * sign, and a size constant.  If the size is larger than the constant sequence, the extra 
	 * positions are filled with zero.  If the size is less than the digit sequence, the excess 
	 * digits are discarded.
	 * 
	 * @param str	The string specifies the digit sequence and it's length is the number of 
	 * 		significant digits
	 * 
	 * @param dec	This is the characteristic (the power of 10)
	 * 
	 * @param sign	The sign
	 */
	public UNumber(String str, int dec, boolean sign, int size){
		// Use the fourth parameter to determine the size of the mantissa
		d = new byte[size];
		
		// Define each element of the mantissa by means of this switch
		for (int i = 0; i < Math.min(str.length(), size); i++) 
			switch (str.charAt(i)){
			case '0': d[i] = 0;
			break;
			case '1': d[i] = 1;
			break;
			case '2': d[i] = 2;
			break;
			case '3': d[i] = 3;
			break;
			case '4': d[i] = 4;
			break;
			case '5': d[i] = 5;
			break;
			case '6': d[i] = 6;
			break;
			case '7': d[i] = 7;
			break;
			case '8': d[i] = 8;
			break;
			case '9': d[i] = 9;
			break;
			}
		for (int i = Math.min(str.length(), size); i < size; i++) d[i] = 0;
		
		// The characteristic and sign are exactly the same as the parameters
		dP = dec;
		s = sign;
	}
	
	/**********
	 * This is a copy constructor
	 * 
	 * @param that
	 */
	public UNumber(UNumber that){
		d = new byte[that.d.length];
		for (int i = 0; i < that.d.length; i++) d[i]=that.d[i];
		dP = that.dP;
		s = that.s;
	}
	
	/**********
	 * This is a copy constructor where the size of the new value is specified
	 * 
	 * @param that
	 */
	public UNumber(UNumber that, int size){
		d = new byte[size];
		for (int i = 0; i < Math.min(that.d.length, size); i++) d[i]=that.d[i];
		for (int i = Math.min(that.d.length, size); i<size; i++)d [i]=0;
		dP = that.dP;
		s = that.s;
	}
	
	/**********
	 * This constructor creates a potentially unnormalized value (there maybe leading zeros) with 
	 * enough space in the mantissa array to hold all of the significant digits of both input 
	 * parameters at the same time.  It initializes the new number to the value of the first 
	 * parameter.  This constructor is used to align the decimal points of two numbers.
	 * 
	 * @param that		This parameter is used to initialize the number
	 * 
	 * @param another	This one helps determine the how many significant digits to use
	 */
	public UNumber(UNumber that, UNumber another){
		// left is the maximum number of digits to the left of the decimal point
		int left = Math.max(Math.max(that.dP, another.dP), 0);
		
		// right is the maximum number of digits to the right of the decimal point
		int right = Math.max(Math.max(that.d.length - that.dP, another.d.length - another.dP), 0);
		
		// The mantissa is allocated to be large enough to hold all of the digits
		d = new byte[left+right];
		
		// Initialize the number based on the value of the left parameter, "that"
		// Index is used to insert the digits into the mantissa array
		int index = 0;
		
		// The first loop fills in leading zeros (if needed) to align the decimal point
		for (int i=0; i<left-that.dP; i++) d[index++] = 0;
		
		// The second loop fills in the mantissa from the first parameter
		for (int i=0; i<that.d.length; i++) d[index++] = that.d[i];
		
		// The third loop fill in any trailing zeros (if needed) to fill the mantissa array
		for (int i=index; i<left+right; i++) d[i] = 0;
		
		// The characteristic and sign from the first parameter is used
		dP = left;
		s = that.s;
	}
	
	/**********
	 * This constructor takes a double and creates a UNumber from it.  The result is rounded to a 
	 * 14 significant digit result.
	 * 
	 * @param v
	 */
	public UNumber(double v){
		 // Allocate enough space to hold 14 significant digits plus one for rounding and one
		 // for loss of significance for normalization.
		System.out.println(v);
		 byte [] tempD = new byte [102];
		 
		 // Set the sign of the result and convert to a positive value for the conversion
		 s = true;
		 if (v < 0) {
			 v = -v;
			 s = false;
		 }
		 
		 // Use log base 10 to determine the characteristic if not too close to zero
		 dP = ((int)Math.log10(v)) + 1;

		 // If the value is too close to zero, return a zero
		 if (dP < -324) {
			 d = new byte[100];
			 for (int i = 0; i < 20; i++) d[i] = 0;
			 dP = 0;
			 s = true;

			 return;
		 }

		 // Use the characteristic to adjust the mantissa to a value in the range from
		 // 0.10000000000000 to 0.99999999999999
		 double man = v/Math.pow(10.0, dP);
		 
		 // One by one, peel off the most-significant digit and place it into the byte array
		 for (int i =0; i < tempD.length; i++){
			 man *= 10;
			 byte digit = (byte)man;
			 tempD[i] = digit;
			 man -= digit;
		 }
		 
		 // If the last believable digit (the 16th) is greater than or equal to 5, round up the
		 // remaining 15 digits.
		 if (tempD[15]>=5){
			 // Round up
			 boolean carry = true;
			 for (int i=14; i>=0; i-- ) {
				 tempD[i] += (carry? 1:0);
				 if (tempD[i]>9){
					 carry = true;
					 tempD[i] -=10;
				 }
				 else carry = false;
			 }
			 // if the carry is true at the end of the loop, we have to deal with a carry off the
			 // left end (this tells up normalization is not required).
			 if (carry) {
				 for (int i=101; i>0; i-- ) tempD[i] = tempD[i-1];
				 tempD[0]=1;
				 this.dP++;
			 }
		 }
		 
		 // Establish the result to have 14 significant places
		 d = new byte [102];
		 
		 // If necessary, normalize the rounded 15 digit number... the value could still be 
		 // 0.0999999
		 if (tempD[0]==0) {
			 // This does the normalizing by eliminating one leading zero digit
			 for (int i = 0; i < d.length; i++) d[i] = tempD[i+1];
			 this.dP--;
		 }
		 else
			 // This is used when the value is already normalized
			 for (int i = 0; i < d.length; i++) d[i] = tempD[i];	
	 }

	
	
	
	public UNumber(double v, int x){
		 // Allocate enough space to hold 14 significant digits plus one for rounding and one
		 // for loss of significance for normalization.
		 byte [] tempD = new byte [x+2];
		 
		 // Set the sign of the result and convert to a positive value for the conversion
		 s = true;
		 if (v < 0) {
			 v = -v;
			 s = false;
		 }
		 
		 // Use log base 10 to determine the characteristic if not too close to zero
		 dP = ((int)Math.log10(v)) + 1;

		 // If the value is too close to zero, return a zero
		 if (dP < -324) {
			 d = new byte[100];
			 for (int i = 0; i < 20; i++) d[i] = 0;
			 dP = 0;
			 s = true;

			 return;
		 }

		 // Use the characteristic to adjust the mantissa to a value in the range from
		 // 0.10000000000000 to 0.99999999999999
		 double man = v/Math.pow(10.0, dP);
		 
		 // One by one, peel off the most-significant digit and place it into the byte array
		 for (int i =0; i < tempD.length; i++){
			 man *= 10;
			 byte digit = (byte)man;
			 tempD[i] = digit;
			 man -= digit;
		 }
		 
		 // If the last believable digit (the 16th) is greater than or equal to 5, round up the
		 // remaining 15 digits.
		 if (tempD[15]>=5){
			 // Round up
			 boolean carry = true;
			 for (int i=14; i>=0; i-- ) {
				 tempD[i] += (carry? 1:0);
				 if (tempD[i]>9){
					 carry = true;
					 tempD[i] -=10;
				 }
				 else carry = false;
			 }
			 // if the carry is true at the end of the loop, we have to deal with a carry off the
			 // left end (this tells up normalization is not required).
			 if (carry) {
				 for (int i=101; i>0; i-- ) tempD[i] = tempD[i-1];
				 tempD[0]=1;
				 this.dP++;
			 }
		 }
		 
		 // Establish the result to have 14 significant places
		 d = new byte [x];
		 
		 // If necessary, normalize the rounded 15 digit number... the value could still be 
		 // 0.0999999
		 if (tempD[0]==0) {
			 // This does the normalizing by eliminating one leading zero digit
			 for (int i = 0; i < d.length; i++) d[i] = tempD[i+1];
			 this.dP--;
		 }
		 else
			 // This is used when the value is already normalized
			 for (int i = 0; i < d.length; i++) d[i] = tempD[i];	
	 }
	/**********
	 * This returns the number of significant digits the UNumber can hold
	 */
	public int length(){
		return d.length;
	}
	
	/**********
	 * This implementation of toString converts the value to a String in scientific notation
	 */
	public String toString(){
		String result;
		if (s) result = "+0.";
		else result = "-0.";
		for (int i = 0; i < d.length; i++) result += d[i];
		result += "E";
		if (dP<0) result += dP;		// If the result is negative, the sign will automatically be 
									// inserted
		else result += "+" + dP;	// For positive values, we must manually insert the "+" sign
		return result;
	}
	
	/**********
	 * This implementation of toString converts the value to a String in scientific notation and
	 * tries to limit the size of the output to a specified limit, no fewer than 8 characters. 
	 * In the case that the output of all of the significant digits in the mantissa is larger than 
	 * the specified limit, lower significant digits are removed and the result is rounded if the
	 * most significant dropped digit is 5 or greater. In the case where the overhead of scientific
	 * notation and the characteristic does not allow even one significant digit and a decimal point,
	 * the size of the output will grow.
	 * 
	 * Even though UNumber values are stored internally with the implied decimal point to the left
	 * of the Most Significant Digit, this toString routine will output the Most Significant Digit to
	 * the left of the decimal point and any additional digits to the right of it. This means the 
	 * displayed characteristic must be reduced by one from what is stored in the value, to 
	 * compensate.
	 * 
	 * Should truncation and the resulting rounding result in a cascade of carries beyond the 
	 * left-most position, a new most significant digit of one is created, the remaining zeros are
	 * shifted to the right, and the exponent is increased by one.
	 * 
	 * The routine does *not* alter the value of the UNumber.
	 * 
	 * @param size		The maximum number of characters in the generated string requested, but it
	 * 					may be adjusted upward as needed and any value less than 8 will be replaced
	 * 					by eight internally at the beginning of this routine.
	 * 
	 */
	public String toString(int size){
		// If fewer than 8 characters is requested, ignore the value and use 8 instead.  The reason 
		if (size <= 8) size = 8;					// for this is the overhead of a leading negative
													// sign, a decimal point, the "E", the size on
													// characteristic, at least one digit for the
													// mantissa and the characteristic, leave only
													// two more significant digits, as in "-1.23E-1"

		// Initialize the result and compute the overhead
		String result = "";							// Initialize the result String to empty
		int overhead = 4;							// The "d.", "E", and the characteristic sign
		if (!this.s) {								// The overhead is one more if the value is
			overhead++;								// negative.  If so, increase the overhead
			result = "-";							// and set up the output with a negative sign.
		}

		// We may be forced to adjust the characteristic, so we make a copy of it for internal use
		int exp = this.dP-1;						// Reduce by one since we put the MSD to the
													// left of the decimal point
		
		// Compute the number of characters in the characteristic using log base 10
		int characteristicSize = 1;					// Assume 1 for a characteristic of zero
		if (this.dP != 0)							// For non-zero characteristics, the log works
			characteristicSize = (int)Math.log10(Math.abs(exp))+1;
		
		// Add the number of the characteristic's digits and the sign to the overhead
		overhead += characteristicSize+1;			// The plus one is for the sign character			
		
		// Computer the number of available digits for the mantissa we can show and fit the size
		int availableDigits = size - overhead;
		
		// If the number of available digits is negative or zero, we will show one
		if (availableDigits <= 0)
			availableDigits = 1;
		
		// In case we need to round, we make a copy of the mantissa
		byte [] d2 = java.util.Arrays.copyOfRange(this.d, 0, availableDigits + 1);

		// If the available space is greater than or a match for what we need, we will use it
		if (availableDigits >= d.length)
			availableDigits = d.length;
		else 
			
			// If not enough room, we will drop less significant digits and round up, if needed
			if (d2[availableDigits-1]>=5){							// Round up if >= 5		
				d2[availableDigits-2]++;							// Carry to the left if needed
				for (int ndx = availableDigits-2; ndx > 0; ndx-- )	// Propagate the carry
					if (d2[ndx] > 9) {					
						d2[ndx-1]++;
						d2[ndx] -= 10;
					}
					else break;
				
				// See if the was a carry off the left hand side.  If so, all digit to the right
				if (d2[0] > 9) {										// are zeros and the new left
					d2[0] = 1;										// most digit is now a 1
					exp++;											// Increase the characteristic							
					if (characteristicSize < (int)Math.log10(Math.abs(exp))+1) {
						// This causes the characteristic to become one digit larger, the number
						availableDigits--;							// of available digits goes down
						if (availableDigits <= 0)					// If it goes to zero, make it one
							availableDigits = 1;						// as we must have at least one
					}												// significant digit showing
				}										
			}											
		
		// Append the most significant digit of the mantissa and the decimal point
		result += d2[0] + ".";	
		
		// Iterative add in the rest of the digits of the mantissa after the decimal point
		for (int ndx = 1; ndx < availableDigits; ndx++)		
			result += d2[ndx];						
		
		// Append the "E"
		result += "E";
			
		// Append the characteristic's plus sign if the value us positive
		if (exp >= 0)
			result += "+";
		
		// Append the characteristic.  If it is negative, Java will append the "-"
		result += "" + exp;
		
		// We are done, so return the right-sized string					
		return result;								
	}
	
	/**********
	 * If the value is not positive and the significant digit is greater than zero, the value must
	 * be negative.
	 * 
	 * @return	true if the value is zero, else false.
	 */
	public boolean isNegative() {
		return (!this.s) && (this.d[0] > 0);
	}
	
	/**********
	 * If the first digit of the UNumber is zero, the value must be zero as these values are 
	 * normalized.
	 * 
	 * @return	true if the value is zero, else false.
	 */
	public boolean isZero() {
		return this.d[0] == 0;
	}
	
	/**********
	 * If the value is positive and the significant digit is greater than zero, the value must
	 * be positive.
	 * 
	 * @return	true if the value is positive and not zero, else false.
	 */
	public boolean isPositive() {
		return (this.s) && (this.d[0] > 0);
	}

	/**********
	 * This implementation of toString converts the value to a String in using a decimal notation
	 */
	public String toDecimalString(){
		// Establish the sign... 
		// We do not put a "+" on a positive value, but do put on a "-" for negative
		String result;
		if (s) result = "";
		else result = "-";
		
		// For values less than one, we will display one zero to the left of the decimal point
		if (dP <= 0) result += "0";
		
		// For values less than 1/10, we need to insert zeros between the decimal point and
		// the most significant digit
		if (dP < 0) {
			result += ".";										// Insert the decimal point
			for (int i = 0; i > dP; i--) result += "0";			// Insert the zeros
			for (int i = 0; i < d.length; i++) result += d[i];	// Insert the significant digits
		}
		else
		{
			// For values greater than 1/10, we insert the decimal point at the appropriate point
			// as we insert in the digits (if dP <= d.length)
			for (int i = 0; i < d.length; i++) {
				if (i == dP) result += ".";			// Insert the decimal point at the right place
				result += d[i];						// Insert the digits
			}
			// If dP > d.length, the decimal point is to the right of all of the significant 
			// digits so we must insert more zeros before we can insert the decimal point
			if (dP > d.length) 
				for (int i = d.length; i < dP; i++) result += "0";	// Insert the zeros
			if (dP >= d.length) result += ".";						// and then the decimal point
		}
		return result;
	}
		
	/**********
	 * This routine is used to display big numbers on the console, 80 characters per line.
	 */
	public void displayBigNumber() {
		String str = "";
		
		// Display the sign
		int count = 2;
		if (! s) 
			str += " -";
		else 
			str += " +";
		
		// Display the decimal point if it is to the left of the mantissa
		if (dP < 1) {
			str = "0.";
			count += 2;
			
			// Display leading zeros before the mantissa when required
			for (int i = dP; i < 0; i++){
				str += "0";
				count++;
				if (count == 80) {
					System.out.println(str);
					count = 0;
					str = "";
				}
			}	
		}
		
		// Display the mantissa
		for (int i = 0; i < d.length; i++){
			str += d[i];
			count++;
			if (count == 80) {
				System.out.println(str);
				count = 0;
				str = "";
			}
			
			// Display the decimal point if it is within the mantissa
			if (i == dP - 1){
				str += ".";
				count++;
				if (count == 80) {
					System.out.println(str);
					count = 0;
					str = "";
				}
			}
		}
		
		// Display the training zeros after the mantissa if required
		for (int i = d.length; i < dP; i++){
			str += "0";
			count++;
			if (count == 80) {
				System.out.println(str);
				count = 0;
				str = "";
			}			
		}
		
		// Display the decimal point if past the right most significant digit
		if (dP > d.length) str += ".";
		
		System.out.println(str);
	}
	
	/**********
	 * This routine displays a UNumber an scientific notation with no more than ten significant
	 * digits, but with no more than are actually there.
	 * 
	 * The output is sent to the console.
	 */
	public void displayShort() {
		if (!s) System.out.print("-");			// Only display a sign if the UNumber is negative
		System.out.print("0.");					// Display the leading zero and decimal point
		for (int i = 0; i < Math.min(d.length, 10); i++) System.out.print(d[i]);
		System.out.print("E");					// Display the "E"
		if (dP > -1) System.out.print("+");		// Ensure a sign is displayed
		System.out.println(dP);					// Display the characteristic
	}
	
	/**********
	 * This internal routine is used to normalize a UNumber
	 * 
	 * @param that	The value to be normalized
	 */
	protected static void normalize(UNumber that){
		// See if we need to normalize at all.  If the first digit is not a zero, we are done.
		if (that.d[0]==0){
			
			// Find where the first non-zero digit is in the mantissa
			int zeroIndex = 1;		
			while (zeroIndex <= that.d.length-1 && that.d[zeroIndex]==0) zeroIndex++;
			
			// if all digits are zero, establish a standard zero of the specified size
			if (that.d.length - zeroIndex <= 0){
				that.dP = 0;
				that.s = true;
				return;
			}
			
			// There is a non-zero mantissa and zero index point to the first non-zero digit. 
			// Create a new mantissa that has the appropriate number of significant digits (at 
			// least one since there is one at the beginning) discarding the leading zeros.
			byte [] newD = new byte [that.d.length - zeroIndex];
			
			// Move the significant digits from the result mantissa array into the new mantissa 
			// array with those digits left aligned (to the zero index) in the array
			for (int i = zeroIndex; i < that.d.length; i++) newD[i - zeroIndex] = that.d[i];
			
			// Establish the normalized result
			that.d = newD;			// Replace the mantissa with the normalized (shortened) version
			that.dP -= zeroIndex;	// Adjust the characteristic accordingly
		}
		return;
	}
	
	/**********
	 * The Addition operation adds the second operand to this object's value  (this = this + that)
	 * The code implements the addition in a very simplistic manner, sacrificing speed and space 
	 * for a very simple algorithm that mirrors how people do addition by hand.
	 * @param that	The second operand that is added to this object's value
	 */
	public void add(UNumber that){
		UNumber temp1;
		UNumber temp2;
		if(this.s == that.s) {
			
			// The signs are the same, so we can add the two unsigned numbers together and then 
			// copy the sign from either
			temp1 = new UNumber(this, that);	// Establish two working values that are decimal  
			temp2 = new UNumber(that, this);	// point aligned
			
			// Work from Least Significant Digit (LSD) toward Most Significant Digit (MSD) and 
			// add digit to digit
			boolean carry = false;
			for (int i = temp1.d.length-1; i>=0; i--){
				// If the previous digit addition had a carry, add it in to this digit
				temp1.d[i] += (byte)(temp2.d[i] + (carry? 1 : 0));
				if (temp1.d[i]>9) {		// If the resulting addition is > 9, there is a carry here
					temp1.d[i] -= 10;	// so reduce the result by 10.  It can't be more than 19 
					carry=true;			// (9 + 9 + 1 = 19) and set the carry for the next digit 
										// addition
				}
				else carry=false;		// If the digit addition is <= 9, there is no carry
			}
			
			// If there is a carry at the end of the loop, we need one more digit of significance
			if (carry){
				d = new byte[temp1.d.length+1];		// Establish a mantissa array that is one digit 
													// larger
				d[0]=1;								// If there was a carry, that new digit must be 
													// a "1"
				for (int i = 0; i<temp1.d.length; i++) d[i+1] = temp1.d[i];	// Copy over rest of 
													// the digits
				temp1.dP++;							// Increase the power of 10 by one
				temp1.d = d;						// Establish this new mantissa array as the 
													// result's mantissa
			}
		}
		
		else {
			// The signs differ, so we subtract the the negative value from the positive value
			// We will put the positive value in temp1 and the negative in temp2
			if (this.s) {						
				temp1 = new UNumber(this, that);
				temp2 = new UNumber(that, this);
			} else {
				temp2 = new UNumber(this, that);
				temp1 = new UNumber(that, this);
			}
			
			// Work from Least Significant Digit (LSD) toward Most Significant Digit (MSD) and 
			// subtract digit from digit.  We start off with no borrowing at the LSD
			boolean borrow = false;
			for (int i = temp1.d.length-1; i>=0; i--){ 
				// If the previous subtraction resulted in a borrow, we must subtract an additional 
				// 1 from this digit
				temp1.d[i] = (byte)(temp1.d[i] - temp2.d[i] - (borrow? 1: 0));
				if (temp1.d[i]<0){		// If the resulting subtraction is < 0, there is a borrow 
					temp1.d[i] += 10;	// here so add 10 to the result.  The result cannot be 
										// less than -10 (0 - 9 - 1 = -10)
					borrow = true;		// and set the borrow for the next digit subtraction
				}
				else borrow = false;	// If the digit subtraction is >= 0, there is no borrow
			}
						
			// if the borrow is true, the negative value was larger and we must invert the values 
			// and set the resulting sign to negative
			if (borrow){
				// For all but the LSD, we must subtract the digit from 9 (10 - digit - 1 for the 
				// next digit to the right borrow)
				for (int i = 0; i <temp1.d.length-1; i++) temp1.d[i] = (byte)(9 - temp1.d[i]);
				
				// For the LSD, we subtract the digit from 10, since there are no more digits that 
				// require a borrow
				temp1.d[temp1.d.length-1] = (byte)(10 - temp1.d[temp1.d.length-1]);
				
				// Resolve any carries working back from the LSD to the MSD
				for (int i = temp1.d.length-1; i > 0; i--)
					if (temp1.d[i]>=10){
						temp1.d[i]-=10;
						temp1.d[i-1]++;
					}
				
				// The resulting number is negative
				temp1.s = false;
			}
			else temp1.s = true;	// Since the borrow was false at the end, the positive value 
									// was larger, so then is the result
		}
		
		// The result of the addition (or subtraction) is in temp1, so we must first normalize it 
		// and then move it into this object's attributes
		normalize(temp1);
		this.d = temp1.d;
		this.dP = temp1.dP;
		this.s = temp1.s;
	}
	
	/**********
	 * The subtract operation negates the second operand and uses the add operator 
	 * (this = this - that)  The code uses a temporary for the second operand so the callers 
	 * second operand is not changed
	 * 
	 * @param that	The second operand that is subtracted from the value of this object's value
	 */
	public void sub(UNumber that){
		UNumber temp = new UNumber(that);
		temp.s = ! temp.s;
		this.add(temp);
	}
	
	/**********
	 * The multiply operation implements multiplication is the traditional, by hand-hand manner, 
	 * where each digit of the multiplicand is multiplied by each digit of the multiplier and 
	 * adding into the proper digit of the product using the index of the multiplier digit as the 
	 * base for the summation. Since we have unlimited number of digits, the intermediate results 
	 * can become arbitrarily large, so the carry must be resolved into the product after each digit 
	 * multiplication.
	 * 
	 * @param that	The multiplier 
	 */
	public void mpy(UNumber that){
		// The temporary product must be at least two digits longer than the result, one digit to 
		// deal with the possible loss of precision when normalizing is required, and one for 
		// rounding.
//		System.out.println(this.d+"dddddd");
		byte [] product = new byte[this.d.length+(that.d.length>2?that.d.length:2)];
		byte [] multiplicand = this.d;
		byte [] multiplier = that.d;
		
		// If either the multiplicand or the multiplier is zero, 
		// the product is zero, so return a default zero
		
//		System.out.println(Arrays.toString(multiplicand)+"om");
//		System.out.println(Arrays.toString(multiplier)+"on");
		if (multiplicand[0] == 0 || multiplier[0] == 0) {
			for (int i = 0; i < this.d.length; i++) this.d[i] = 0;
			dP = 0;
			s = true;	
			return;
		}
		
		// The product is not zero, so compute the product using the by-hand algorithm
		
		// Zero the product
		for (int p = 0; p < product.length; p++)
			product[p] = 0;
		
		// For each digit in the multiplier
		for (int r = multiplier.length - 1; r >= 0; r--) {
			
			// start with the proper digit in the product based on the location in the
			// multiplicand and multiplier
			int p = multiplicand.length + r;
			
			// Computer the product for each digit in the multiplicand and add it into the product
			for (int d = multiplicand.length-1; d >=0; d--) {
				product[p] += multiplicand[d] * multiplier[r];
		
				// Resolve any carries by ensuring each product digit is not larger than 9
				// (Since none of the digits were larger than 9, we can stop as soon as we find
				// one that is not larger than 9 as we resolve the carries working to the left.)
				int c=p;
				while (c > 0 && product[c] > 9) {		// Propagate the carries to the left
					product[c-1] += product[c] / 10;	// Add the carry to the digit to the left
					product[c--] %= 10;					// Use the unit's value for this digit
				}
				
				// use the next product digit to the left;
				p--;
				
			}
		}
		
		// We now have the product, but it may not be normalized.  The first digit of the product 
		// must not be zero since the product is not zero.  If at this point that leading digit is 
		// zero, then we must discard that one zero digit and adjust the size and the exponent 
		// down one.  (There can be at most one zero digit given the multiplicand and the multiplier 
		// were both non-zero and normalized.)
		boolean wasNormalized = false;
		if (product[0] == 0) {
			// The product must be normalized and transferred into this object
			for (int i = 0; i < product.length-1; i++) product[i] = product[i+1];
			// It is not necessary to set the LSD to zero, because it will not be used... two 
			// excess digits were set up in the product... one for normalization and one for 
			// rounding.
			product[product.length-1] = 0;
			
			// Signal that the value was normalized (needed to correct the characteristic)
			wasNormalized = true;
		}
		
		// Since the product is longer than the result array, we need to discard the excess digits 
		// and that leads us to wanting to round the result if the digit to the right of the LSD is 
		// five or larger.
		boolean roundingCarry=false;
		if (product[this.d.length] >= 5) {
			// Since the LSD was a 5 or larger, we must round and propagate the carry to the left.
			int i = d.length-1;
			product[i]++;
			while (i > 0 && product[i] > 9) {
				product[i-1]++;		// Increase the next digit to the left to compensate for > 9
				product[i--]-=10;	// Decrease this digit by ten to balance
			}
			
			// If the carry propagates all the way from the LSD to the MSD and we carry off the end, 
			// all of those intermediate digits must have been "9"s before the rounding and they are 
			// now zeros.  
			if (i == 0 && product[0] > 9) {
				// Set flag so we know we are doing this so we can add one to the characteristic
				roundingCarry=true;
				// Set the MSD to 1, since all the rest of them must be zeros
				product[0] = 1;
			}
		}

		// Compute the characteristic (the sum of the two powers of ten)
		this.dP += that.dP;
		if (wasNormalized) this.dP--;	// Adjust down if there was a leading zero in the product
		if (roundingCarry) this.dP++;	// Adjust up if the rounding resulting in a carry off the 
										// left end
		
		// The product must be transferred into this object
		for (int i = 0; i < this.d.length; i++) this.d[i] = product[i];

		// Compute the sign of the product
		this.s = this.s == that.s;
	}

	

	/**********
	 * The divide operation implements division by means of repeated subtraction, producing as 
	 * many significant digits in the quotient as in the dividend (this object) and the quotient 
	 * replaces the dividend.  This is a faster algorithm than the simpler Div2 below.  This 
	 * algorithm assumes the numbers are normalized.
	 * 
	 * @param that	The divisor 
	 */
	public void div(UNumber that){
		byte [] dividend = new byte[this.d.length+that.d.length+1];
		byte [] quotient = new byte[this.d.length+2];
		byte [] divisor = that.d;
		
		// Check for divide by zero and return close to infinite
		if (that.d[0] == 0) {
			for (int ndx = 0; ndx < this.d.length; ndx++) this.d[ndx] = 9;
			this.dP = 999999;
			this.s = true;
			return;
		}

		// Make working copy of the dividend, which is destroyed during the process
		for (int i = 0; i < this.d.length; i++) dividend[i] = this.d[i];
		for (int i = this.d.length; i < dividend.length; i++) dividend[i] = 0;

		// Initialize the quotient to zero, which will replace the dividend at the end of this 
		// process
		for (int i = 0; i < quotient.length; i++) quotient[i] = 0;

		// Digit by digit, subtract the divisor from the dividend until a borrow occurs, add it 
		// back in, shift the divisor right one digit, do it again, until all of the digits in the 
		// quotient have been filled in.
		for (int qIndex = 0; qIndex < quotient.length; qIndex++){
			boolean okToContinue = true;
			while (okToContinue && quotient[qIndex]<9){
				boolean borrow = false;
				for (int i = divisor.length-1; i>=0; i--){ 
					// If the previous subtraction resulted in a borrow, we must subtract an 
					// additional 1 from this digit
					dividend[i+qIndex] = (byte)(dividend[i+qIndex] - divisor[i] - (borrow? 1: 0));
					if (dividend[i+qIndex]<0){		// If the resulting subtraction is < 0, there 
													// is a borrow here
						dividend[i+qIndex] += 10;	// so add 10 to the result.  The result cannot 
													// be less than -10 (0 - 9 - 1 = -10)
						borrow = true;		// and set the borrow for the next digit subtraction
					}
					else borrow = false;	// If the digit subtraction is >= 0, there is no borrow
				}

				if (borrow){				// If a borrow is true at the end add back the divisor 
					if (qIndex > 0 && dividend[qIndex-1]>0) {
						dividend[qIndex-1]--;
						quotient[qIndex]++;
					}
					else {
						okToContinue = false;	// Stop the loop
						boolean carry = false;
						for (int i = divisor.length-1; i>=0; i--){ 
							// If the previous addition resulted in a carry, we must add an 
							// additional 1 to this digit
							dividend[i+qIndex] = 
									(byte)(dividend[i+qIndex] + divisor[i] + (carry? 1: 0));
							if (dividend[i+qIndex]>9){		// If the resulting subtraction is > 9, 
															// there is a carry here
								dividend[i+qIndex] -= 10;	// so subtract 10 to the result.  The 
															// result cannot be greater than 
															// 19 (9 + 9 + 1 = +19)
								carry = true;		// and set the carry for the next digit addition
							}
							else carry = false;	// If the digit addition is <= 9, there is no carry
						}
					}
				}
				else quotient[qIndex]++;		// If the subtraction was successful, count it
			}
		}
		
		this.dP = this.dP - that.dP + 1;
		this.s = this.s == that.s;
		
		// We now have the quotient, but it may not be normalized.  The first digit of the product 
		// must not be zero since the quotient is not zero.  If at this point that leading digit is 
		// zero, then we must discard that one zero digit and adjust the size and the exponent down 
		// one.  (There can be at most one zero digit given the multiplicand and the multiplier 
		// were both non-zero and normalized.)
		boolean wasNormalized = false;
		if (quotient[0] == 0) {
			// The product must be normalized and transferred into this object
			for (int i = 0; i < quotient.length-1; i++) quotient[i] = quotient[i+1];
			quotient[quotient.length-1] = 0;
			// signal that the value was normalized (needed to correct the characteristic)
			wasNormalized = true;
		}
		
		// Since the quotient is longer than the result array, we need to discard the excess 
		// digits and that leads us to wanting to round the result if the digit to the right of 
		// the LSD is five or larger.
		boolean roundingCarry=false;
		if (quotient[this.d.length] >= 5) {
			// Since the LSD was a 5 or larger, we must round and propagate the carry to the left.
			int i = d.length-1;
			quotient[i]++;
			while (i > 0 && quotient[i] > 9) {
				quotient[i-1]++;		// Increase the next digit to the left to compensate for > 9
				quotient[i--]-=10;		// Decrease this digit by ten to balance
			}
			
			// If the carry propagates all the way from the LSD to the MSD and we carry off the 
			// end, all of those intermediate digits must have been "9"s before the rounding and 
			// they are now zeros.  
			if (i == 0 && quotient[0] > 9) {
				// Set flag so we know we are doing this so we can add one to the characteristic
				roundingCarry=true;
				// Set the MSD to 1, since all the rest of them must be zeros
				quotient[0] = 1;
			}
		}

		// Compute the characteristic (the sum of the two powers of ten)
		if (wasNormalized) this.dP--;	// Adjust down if there was a leading zero in the quotient
		if (roundingCarry) this.dP++;	// Adjust up if the rounding resulting in a carry off the 
										//left end
		
		// The quotient must be transferred into this object
		for (int i = 0; i < this.d.length; i++) this.d[i] = quotient[i];
	}
	
	/**********
	 * This lessThan routine tries to avoid doing a subtraction in order to do things faster.  
	 * Therefore checks of sign bits, then the decimal points, and then the first digits are done 
	 * before falling into a subtract as a last resort.  If one value is negative and the other is 
	 * positive, there is no need to go any further.  If they are of the same size, but the 
	 * characteristics of the normalized values are not the same, there is no need to go any further.  
	 * Given the same size and the same characteristic for normalized numbers, if the first digit can 
	 * resolve the issue, try that.  Only in the case where the signs, characteristics, *and* the 
	 * first digits are the same do we resort to subtraction and a check of the sign bit.
	 * 
	 * @param that	The right operand in the "this lessThan that" relational test
	 * 
	 * @return boolean true if this is indeed less than that
	 */
	public boolean lessThan(UNumber that){
		if (this.s)
			if (that.s) {
				// Both operands are positive
				if (this.dP > that.dP)								// Not Less than if this has a larger
					return false;									// characteristic
				else if (this.dP < that.dP) 						// Less than if this has a smaller
					return true;									// characteristic
				else 
					// Both operands have the same magnitude
					if (this.d[0] < that.d[0]) 						// If the first digit of this is less
						return true;								// than that, it is less than
					else if (this.d[0] > that.d[0]) 				// If the first digit of this is greater
						return false;								// than that, it is not less than
					else {
						// same sign, magnitude, and first significant digit
						UNumber temp = new UNumber(this);			// We will have to use subtraction
						temp.sub(that);								// to see which is less than
						if (temp.s) return false;					// If positive, this is not less than
						return true;
					}				
			}
			else {
				// The left is positive and the right is negative, so this is not less than
				return false;
			}
		else
			if (that.s) {
				// The left is negative and the right is positive, so this is less than
				return true;
			}
			else {
				// Both operands are negative
				if (this.dP > that.dP) 								// Less than if this has a greater
					return true;									// characteristic
				else if (this.dP < that.dP)							// Not less than if this does not have a 
					return false;									// greater characteristic
				else 
					// Both operands have the same magnitude
					if (this.d[0] < that.d[0]) 						// If the first digit of this is less than
						return false;								// that, it is not less than (negative!)
					else if (this.d[0] > that.d[0])					// If the first digit of this is greater 
						return true;								// than that, it is less than
					else {
						// same sign, magnitude, and first significant digit
						UNumber temp = new UNumber(this);			// We will have to use subtraction
						temp.sub(that);								// to see which is greater
						if (temp.s) return true;					// If positive, is it less than
						return false;
					}				
			}
	}

	/**********
	 * This greaterThan routine tries to avoid doing a subtraction in order to do things faster.  
	 * This code follows the same process as the less than code above
	 * 
	 * @param that	The right operand in the "this greater than that" relational test
	 * 
	 * @return boolean true if this is indeed greater than that
	 */
	public boolean greaterThan(UNumber that){
		if (this.s)
			if (that.s) {
				// Both operands are positive
				if (this.dP > that.dP) 								// Greater if this has a larger 
					return true;									// characteristic
				else if (this.dP < that.dP) 						// Not greater if this has a smaller
					return false;									// characteristic
				else 
					// Both operands have the same magnitude
					if (this.d[0] < that.d[0]) 						// If the first digit of this is less
						return false;								// than that, it is not greater
					else if (this.d[0] > that.d[0])					// If the first digit of this is greater
						return true;								// than that, it is greater
					else {
						// same sign, magnitude, and first significant digit
						UNumber temp = new UNumber(this);			// We will have to use subtraction
						temp.sub(that);								// to see which is greater
						if (temp.s) return true;					// If positive, this is greater
						return false;
					}				
			}
			else {
				// The left is positive and the right is negative, so this is greater
				return true;
			}
		else
			if (that.s) {
				// The left is negative and the right is positive, so this is not greater
				return false;
			}
			else {
				// Both operands are negative
				if (this.dP > that.dP) 								// Not greater if this has a greater
					return false;									// characteristic
				else if (this.dP < that.dP) 						// Greater if this does not have a
					return true;									// greater characteristic
				else 
					// Both operands have the same magnitude
					if (this.d[0] < that.d[0]) 						// If the first digit of this is less
						return true;								// than that, it is greater (negative!)
					else if (this.d[0] > that.d[0]) 				// If the first digit of this is greater
						return false;								// than that, it is not greater
					else {
						// same sign, magnitude, and first significant digit
						UNumber temp = new UNumber(this);			// We will have to use subtraction
						temp.sub(that);								// to see which is greater
						if (temp.s) return false;					// If positive, it is not greater
						return true;
					}				
			}
	}
	
	/**********
	 * These getter methods allow supporting libraries to expand the UNumber library
	 */
	public boolean getSign() {
		return s;
	}

	public int getCharacteristic() {
		return dP;
	}

	public byte[] getMantissa() {
		return d;
	}

	/**********
	 * This absolute value routine sets the sign bit true which has the same effect of doing an 
	 * absolute value on this
	 */
	public void abs() {
		s = true;
	}
	/**
	 * This method is used for the absolute.
	 */
	public UNumber abs1(UNumber measuredValue) {
		String s = measuredValue + "";
		int x = -1;
		UNumber xy = new UNumber(x);
		if(s.substring(0,1).equals("-")) {
			measuredValue.mpy(xy);
			return measuredValue;
		}
		return measuredValue;
	}
	/**********
	 * This compareTo routine satisfies the "Comparable" interface
	 */
	public int compareTo(UNumber that) {
		/* The following is the implementation via subtraction.  We believe it is slower than what
		 * we have provided below
			UNumber result = new UNumber(this);
			result.sub(that);
			if (result.s) return 1;
			result = new UNumber(that);
			result.sub(this);
			if (result.s) return -1;
			else return 0;
		*/
		
		// Compare the signs of the operands
		if (this.s)
			if (that.s) {
				// Both operands are positive
				if (this.dP > that.dP) 								// Greater if this has a larger 
					return 1;										// characteristic
				else if (this.dP < that.dP) 						// Not greater if this has a smaller
					return -1;										// characteristic
				else 
					// Both operands have the same magnitude
					if (this.d[0] < that.d[0]) 						// If the first digit of this is less
						return -1;									// than that, it is not greater
					else if (this.d[0] > that.d[0])					// If the first digit of this is greater
						return 1;									// than that, it is greater
					else {
						// same sign, magnitude, and first significant digit
						UNumber temp = new UNumber(this);			// We will have to use subtraction
						temp.sub(that);								// to see which is greater
						if (temp.s) return 1;						// If positive, this is greater
						temp = new UNumber(that);
						temp.sub(this);
						if (temp.s) return -1;						// If positive, that is greater
						return 0;
					}				
			}
			else {
				// The left is positive and the right is negative, so this is greater
				return 1;
			}
		else
			if (that.s) {
				// The left is negative and the right is positive, so this is not greater
				return -1;
			}
			else {
				// Both operands are negative
				if (this.dP > that.dP) 								// Not greater if this has a greater
					return -1;										// characteristic
				else if (this.dP < that.dP) 						// Greater if this does not have a
					return 1;										// greater characteristic
				else 
					// Both operands have the same magnitude
					if (this.d[0] < that.d[0]) 						// If the first digit of this is less
						return 1;									// than that, it is greater (negative!)
					else if (this.d[0] > that.d[0]) 				// If the first digit of this is greater
						return -1;									// than that, it is not greater
					else {
						// same sign, magnitude, and first significant digit
						UNumber temp = new UNumber(this);			// We will have to use subtraction
						temp.sub(that);								// to see which is greater
						if (temp.s) return 1;						// If positive, this is greater
						temp = new UNumber(that);
						temp.sub(this);
						if (temp.s) return -1;						// If positive, that is greater
						return 0;
					}				
			}
	}

	/**********
	 * Convert this UNumber to a Double, truncating the least significant digits
	 * 
	 * @return the Double equivalent of the UNumber
	 */
	public double getDouble() {
		int numDigits = 17;							// Doubles are not accurate beyond 17 digits
		if ( d.length < 17 ) numDigits = d.length;	// If the value has fewer, use just that many
		double result = 0;							// Initialize the result and then add the 
		double divisor = 1;							// the value of each digit into the result
		for (int ndx = 0; ndx < numDigits; ndx++) {	// The first digit is between 0/10 and 9/10
			divisor *= 10.0;						// and each following digit deals with the
			result = result + d[ndx]/divisor;		// subsequent negative powers of ten
		}
		return result * Math.pow(10.0, dP);			// Take the sum and multiply it by the 
	}												// appropriate power of ten using dP
}
