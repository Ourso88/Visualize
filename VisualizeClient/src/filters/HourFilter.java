package filters;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

public class HourFilter extends AbstractFilter {

	private static final long serialVersionUID = 1L;
	
	public boolean isCorrect(int hour, int minute, int second) {
		if (hour >= 0 && hour < 24 && minute >= 0 && minute < 60 && second >= 0 && second < 60) {
			return true;
		}
		return false;
	}
	
	public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {		
		if (!isValid(str, "0123456789:", 8)) {
			return;
		}

		String text = getText(offset, str);
		
		Integer[] values = getValues(text, ":", 3);
		
		if (offset == 2 || offset == 5 || offset == 8) {
			if (!str.equals(":")) {
				super.insertString(offset, ":" + str, attr);
			} else {
				super.insertString(offset, ":", attr);
			}
		} else {
			if (isCorrect(values[0], values[1], values[2])) {
				super.insertString(offset, str, attr);
			}
		}
	}
}
