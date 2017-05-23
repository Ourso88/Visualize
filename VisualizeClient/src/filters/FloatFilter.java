package filters;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

public class FloatFilter extends AbstractFilter {

	private static final long serialVersionUID = 1L;
	protected Double _max = 0.0; 

	public void setMax(Double max) {
		_max = max;
	}

	public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
		str = str.replace(",", ".");
		if (!isValid(str, "0123456789.-", 0)) {
			return;
		}
		
		Integer length = getLength();
		String text = getText(0, length);
		
		if (!isOnlyOne(".", str, text)) {
			return;
		} else {
			int index = text.indexOf("."); 
			if (index >= 0 && index < length - 2) {
				if (offset > length - 3) {
					return;
				}
			}
		}
		
		if (str.indexOf("-") != -1) {
	       if (str.indexOf("-") != 0 || offset != 0 ) {
	            return;
	       }
	    }
		
		if (!computeMax(_max, text + str)) {
			return;
		}
			
		super.insertString(offset, str, attr);
	}
}
