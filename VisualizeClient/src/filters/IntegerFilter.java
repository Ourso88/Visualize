package filters;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

public class IntegerFilter extends AbstractFilter {

	private static final long serialVersionUID = 1L;
	protected Integer _max = 0; 
	
	public void setMax(Integer max) {
		_max = max;
	}
	
	public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
		if (!isValid(str, "0123456789.", 0)) {
			return;
		}
		
		if (str.indexOf("-") != -1) {
	         if (str.indexOf("-") != 0 || offset != 0 ) {
	            return;
	       }
	    }
		
		if (!computeMax(new Double(_max), getText(offset, str))) {
			return;
		}
		
		super.insertString(offset, str, attr);
	}
}
