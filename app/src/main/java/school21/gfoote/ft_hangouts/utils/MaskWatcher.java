package school21.gfoote.ft_hangouts.utils;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.EditText;

public class MaskWatcher implements TextWatcher
{
	private static String TAG = MaskWatcher.class.getSimpleName();
	private boolean isRunning = false;
	private boolean isDeleting = false;
	private EditText phoneNumber;

	public MaskWatcher(EditText phoneNumber)
	{
		this.phoneNumber = phoneNumber;
		phoneNumber.setText("+");
	}

	@Override
	public void beforeTextChanged(CharSequence charSequence, int start, int count, int after)
	{
		isDeleting = count > after;
	}

	@Override
	public void onTextChanged(CharSequence charSequence, int start, int before, int count)
	{ }

	@Override
	public void afterTextChanged(Editable editable)
	{
		if (isRunning || isDeleting)
			return;
		isRunning = true;
		phoneNumberChangeListener(editable);
		isRunning = false;
	}

	private void phoneNumberChangeListener(Editable editable)
	{
		phoneNumber.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(15)});
		if (editable.length() == 2)
			editable.append(' ');
		if (editable.length() == 6 || editable.length() == 10)
			editable.append('-');
	}
}