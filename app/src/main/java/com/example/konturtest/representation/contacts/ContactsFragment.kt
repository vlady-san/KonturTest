package com.example.konturtest.representation.contacts

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.konturtest.R
import com.example.konturtest.domain.Contacts
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.konturtest.representation.contact.ContactInfoFragment
import com.google.android.material.snackbar.Snackbar
import android.util.Log
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener


class ContactsFragment : Fragment(), ContactsAdapterListener {

    private var adapter : ContactsAdapter = ContactsAdapter(this)
    //Переменная для сохранения последнего поискового запроса
    private var searchString = ""
    lateinit var searchView : SearchView
    /*
    Эта переменная используется для повторного запроса
    Например, мы ввели в поисковой запрос имя, а потом перешли на детальный экран
    Пользователи буду ожидать при возвращении на экран со списком всех контактов, чтобы поиск сохарнился
     */
    private var needRepeatRequest = false
    lateinit var progressBar : ProgressBar
    lateinit var mySwipeRefreshLayout : SwipeRefreshLayout
    lateinit var snackbar : Snackbar

    private lateinit var viewModel: ContactsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        viewModel = ViewModelProvider(this)[ContactsViewModel::class.java]
        viewModel.getContacts()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.contacts_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = view.findViewById(R.id.progress_bar)
        mySwipeRefreshLayout = view.findViewById(R.id.swiperefresh)

        snackbar = Snackbar.make(view, "Что то пошло не так", Snackbar.LENGTH_LONG)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        mySwipeRefreshLayout.setOnRefreshListener {
            mySwipeRefreshLayout.isRefreshing = true
            viewModel.loadContacts()
        }

        setObservers()

    }

    private fun setObservers(){
        viewModel.contacts.observe(viewLifecycleOwner, Observer {
            viewModel.updateRequestLastTime(requireContext())
            mySwipeRefreshLayout.isRefreshing = false
            adapter.refreshData(it)

        })

        viewModel.dateIsLoading.observe(viewLifecycleOwner, Observer {
            progressBar.visibility = if(it) View.VISIBLE
            else View.GONE
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            if(it.getContentIfNotHandled() == true)
                snackbar.show()
            mySwipeRefreshLayout.isRefreshing = false
        })
    }

    override fun onContactSelected(contact: Contacts) {
        //если запрос не пуст, нам нужно будет его повторить
        if (searchString.isNotEmpty())
            needRepeatRequest = true
        findNavController().navigate(R.id.action_contactsFragment_to_contactInfoFragment, ContactInfoFragment.createBundle(contact))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.search_menu, menu)
        searchView = menu.findItem(R.id.action_search).actionView as SearchView
        val searchMenuItem = menu.findItem(R.id.action_search) as MenuItem

        //Нужно чтобы мы смоги показать запрос, который нужно повторить
        if (searchString.isNotEmpty()) {
            searchMenuItem.expandActionView()
            searchView.clearFocus()
        }

        if (needRepeatRequest)
            repeatRequest()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                //при формировании меню этот метод вызывается автоматически с нулевой строкой
                //чтобы не перетереть наш сохраненный запрос, я проверяю нужно ли будет сейчас повторять запрос
                if (!needRepeatRequest) {
                    adapter.filter.filter(newText)
                    searchString = newText
                    Log.d("search", newText)
                }
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
        })



        return super.onCreateOptionsMenu(menu, inflater)
    }

    private fun repeatRequest(){
        val viewTreeObserver: ViewTreeObserver = searchView.viewTreeObserver
        val listener = object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (searchString.isNotEmpty()) {
                    needRepeatRequest = false
                    //этот метод работает только после отрисовки searchView, поэтому слежу за его отрисовкой
                    searchView.setQuery(searchString, true)
                }
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        }
        viewTreeObserver.addOnGlobalLayoutListener(listener)
    }

}