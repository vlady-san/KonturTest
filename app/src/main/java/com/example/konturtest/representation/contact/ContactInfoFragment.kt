package com.example.konturtest.representation.contact

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.konturtest.R
import com.example.konturtest.domain.Contacts
import com.example.konturtest.domain.formPeriod
import com.example.konturtest.representation.MainActivity

private const val ARG_PARAM1 = "contact"

class ContactInfoFragment : Fragment() {
    private lateinit var contact: Contacts

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            contact = it.getSerializable(ARG_PARAM1) as Contacts
        }

        (requireActivity() as MainActivity).supportActionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contact_info, container, false)
    }

    companion object {
        fun createBundle(contactInfo: Contacts): Bundle {
            var arg = Bundle()
            arg.putSerializable(ARG_PARAM1, contactInfo)
            return arg
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = view.findViewById<TextView>(R.id.name)
        val phone = view.findViewById<TextView>(R.id.phone)
        val temperament = view.findViewById<TextView>(R.id.temperament)
        val period = view.findViewById<TextView>(R.id.education_period)
        val biography = view.findViewById<TextView>(R.id.biography)

        name.text = contact.name
        phone.text = contact.phone
        temperament.text = contact.temperament
        period.text = contact.educationPeriod.formPeriod()
        biography.text = contact.biography

    }
}