package filters;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

public class TelephoneFilter extends AbstractFilter {

	private static final long serialVersionUID = 1L;
		
	public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
		if (!isValid(str, "0123456789-", 14)) {
			return;
		}
		
		if (offset == 2 || offset == 5 || offset == 8 || offset == 11) {
			if (!str.equals("-")) {
				super.insertString(offset, "-" + str, attr);
			} else {
				super.insertString(offset, "-", attr);
			}
		} else {
			super.insertString(offset, str, attr);
		}
		
	}
}
