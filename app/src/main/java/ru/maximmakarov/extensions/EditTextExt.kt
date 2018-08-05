package ru.maximmakarov.extensions

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText


fun EditText.addTextWatcher(afterTextChanged: (s: Editable?) -> Unit = { _ -> },
                            beforeTextChanged: (s: CharSequence?, start: Int, count: Int, after: Int) -> Unit = { _, _, _, _ -> },
                            onTextChanged: (s: CharSequence?, start: Int, before: Int, count: Int) -> Unit = { _, _, _, _ -> }) =
        object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = afterTextChanged(s)
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = beforeTextChanged(s, start, count, after)
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = onTextChanged(s, start, before, count)
        }.apply { addTextChangedListener(this) }
