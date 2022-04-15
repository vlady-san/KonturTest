package com.example.konturtest.representation.contacts


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.konturtest.PhoneUtils
import com.example.konturtest.R
import com.example.konturtest.domain.Contacts
import java.util.*
import kotlin.collections.ArrayList

interface ContactsAdapterListener {
    fun onContactSelected(contact: Contacts)
}
class ContactsAdapter(
    val listener: ContactsAdapterListener
) : RecyclerView.Adapter<ContactsAdapter.ContactHolder>(), Filterable {
    private val contactList: MutableList<Contacts> = mutableListOf()
    private var contactListFiltered: MutableList<Contacts> = mutableListOf()

    fun refreshData(newContacts : List<Contacts>){
        contactList.clear()
        contactListFiltered.clear()
        contactList.addAll(newContacts)
        contactListFiltered.addAll(newContacts)
        notifyDataSetChanged()
    }

    inner class ContactHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView  = view.findViewById(R.id.name)
        var phone: TextView = view.findViewById(R.id.phone)
        var height: TextView = view.findViewById(R.id.height)

        init {
            view.setOnClickListener {
                    listener.onContactSelected(contactListFiltered[adapterPosition])
                }
            }

        fun bind(contact: Contacts){
            name.text = contact.name
            phone.text = contact.phone
            height.text = contact.height.toString()
        }

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.contact_item, parent, false)
        return ContactHolder(itemView)
    }

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        holder.bind(contactListFiltered[position])
    }

    override fun getItemCount(): Int {
        return contactListFiltered.size
    }

    override fun getFilter(): Filter = object : Filter() {
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            val charString = charSequence.toString()
            contactListFiltered = if (charString.isEmpty()) {
                contactList
            } else {
                val filteredList: MutableList<Contacts> = ArrayList()
                for (row in contactList) {

                    if (row.name.lowercase(Locale.getDefault())
                            .startsWith(charString.lowercase(Locale.getDefault())) ||
                        PhoneUtils.getOnlyNumbersOfString(row.phone).contains(charSequence)
                    ) {
                        filteredList.add(row)
                    }
                }
                filteredList
            }
            val filterResults = FilterResults()
            filterResults.values = contactListFiltered
            return filterResults
        }

        override fun publishResults(p0: CharSequence?, filterResults: FilterResults?) {
            contactListFiltered = (filterResults?.values ?: mutableListOf<Any>()) as MutableList<Contacts>
            notifyDataSetChanged()
        }

    }
}