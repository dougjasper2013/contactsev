package com.trios2024evdj.contactmanager

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.trios2024evdj.contactmanager.databinding.ActivityMainBinding
import com.trios2024evdj.contactmanager.models.ContactList
import com.trios2024evdj.contactmanager.ui.main.MainFragment
import com.trios2024evdj.contactmanager.ui.main.MainViewModel
import com.trios2024evdj.contactmanager.ui.main.MainViewModelFactory


class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this,
            MainViewModelFactory(PreferenceManager.getDefaultSharedPreferences(this))
        )
            .get(MainViewModel::class.java)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }

        binding.fabButton.setOnClickListener {
            showCreateListDialog()
        }

    }

    private fun showCreateListDialog() {
        val dialogTitle = getString(R.string.contact_name)
        val positiveButtonTitle = getString(R.string.create_list)

        val builder = AlertDialog.Builder(this)

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.layoutParams =
            LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.FILL_PARENT,
                LinearLayoutCompat.LayoutParams.FILL_PARENT)

        val listContactEditText = EditText(this)
        listContactEditText.inputType = InputType.TYPE_CLASS_TEXT

        val listEmailEditText = EditText(this)
        listEmailEditText.inputType = InputType.TYPE_CLASS_TEXT

        val listPhoneEditText = EditText(this)
        listPhoneEditText.inputType = InputType.TYPE_CLASS_TEXT

        val listAddressEditText = EditText(this)
        listAddressEditText.inputType = InputType.TYPE_CLASS_TEXT

        layout.addView(listContactEditText)
        layout.addView(listEmailEditText)
        layout.addView(listPhoneEditText)
        layout.addView(listAddressEditText)

        builder.setTitle(dialogTitle)
        builder.setView(layout)

        builder.setPositiveButton(positiveButtonTitle) {
            dialog, _ -> dialog.dismiss()
            val contactList = ContactList(listContactEditText.text.toString(),
                listEmailEditText.text.toString(), listPhoneEditText.text.toString(),
                listAddressEditText.text.toString())
            viewModel.saveList(contactList)
        }

        builder.create().show()
    }

    private fun showMapsActivity(list: ContactList) {
        val mapsActivityIntent = Intent(this, MapsActivity::class.java)

        mapsActivityIntent.putExtra(INTENT_LIST_KEY, list)

        startActivity(mapsActivityIntent)
    }

    companion object {
        const val INTENT_LIST_KEY = "list"
    }

}