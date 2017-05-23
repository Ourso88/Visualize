package filters;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

public class DateFilter extends AbstractFilter {

	private static final long serialVersionUID = 1L;

	public boolean isCorrect(int day, int month, int year) {
		if (day < 0 || day > 31 || month < 0 || month > 12 || year < 0 || year > 9999) {
			return false;
		}
		
		if (day == 31) {
			if ((month == 0) || (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12)) {
				return true;
			}
		} else if (day == 30) {
			if ((month != 2)) {
				return true;
			}
		} else if (day <= 31) {
			return true;
		}
		return false;
	}
	
	public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {		
		if (!isValid(str, "0123456789/", 10)) {
			return;
		}
		
		String text = getText(offset, str);
		
		Integer[] values = getValues(text, "/", 3);
		
		if (offset == 2 || offset == 5) {
			if (!str.equals("/")) {
				super.insertString(offset, "/" + str, attr);
			} else {
				super.insertString(offset, "/", attr);
			}
		} else {
			if (isCorrect(values[0], values[1], values[2])) {
				super.insertString(offset, str, attr);
			}
		}
	}
}