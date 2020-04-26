package com.github.leanite.carterror

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.leanite.carterror.injection.cart
import com.github.leanite.carterror.interaction.GetCartEvent
import com.github.leanite.carterror.interaction.RefreshCartEvent
import com.github.leanite.carterror.viewmodel.CartViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val viewModel: CartViewModel by lazy {
        ViewModelProvider(this, cart().viewModelFactory).get(CartViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupList()
        setupObserver()
        setupButtons()
    }

    private fun setupList() {
        rvCart.apply {
            adapter = CartItemListAdapter(viewModel.cart.items)
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupObserver() {
        viewModel.getCartEvent.observe(this, Observer {
            when(it) {
                is GetCartEvent.Loading -> {}
                is GetCartEvent.Success -> rvCart.adapter?.notifyDataSetChanged()
                is GetCartEvent.Error -> showErrorDialog()
            }
        })

        viewModel.refreshCartEvent.observe(this, Observer {
            when(it) {
                is RefreshCartEvent.Loading -> {}
                is RefreshCartEvent.Success -> rvCart.adapter?.notifyDataSetChanged()
                is RefreshCartEvent.Error -> showErrorDialog(message = it.errorMessage)
            }
        })
    }

    private fun showErrorDialog(
        title: String = "Erro",
        message: String = "Algo de errado não está certo =(") {

        AlertDialog.Builder(this).apply {
            setMessage(message)
            setCancelable(false)
            setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        }.create().apply {
            setTitle(title)
            show()
        }
    }

    private fun setupButtons() {
        btGetCart.setOnClickListener { viewModel.getCart() }
        btRefreshCart.setOnClickListener { viewModel.refreshCart() }
    }
}

/*
package com.github.leanite.androidtest.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.leanite.androidtest.R
import com.github.leanite.androidtest.injection.activeUsers
import com.github.leanite.androidtest.interaction.GetUsersEvent
import com.github.leanite.androidtest.viewmodel.ActiveUsersViewModel
import kotlinx.android.synthetic.main.activity_active_users.*

class ActiveUsersActivity : BaseActivity() {

    private lateinit var activeUsersListAdapter: ActiveUsersListAdapter

    private val viewModel: ActiveUsersViewModel by lazy {
        ViewModelProvider(this, activeUsers().viewModelFactory).get(ActiveUsersViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_active_users)

        setupList()
        setupObserver()
        getUsers()
    }

    private fun setupList() {
        activeUsersListAdapter = ActiveUsersListAdapter(viewModel.activeUsers)
        activeUsersList.apply {
            adapter = activeUsersListAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupObserver() {
        viewModel.getUsersEvent.observe(this, Observer {
            when(it) {
                is GetUsersEvent.Loading -> loading(it.show)
                is GetUsersEvent.Success -> activeUsersListAdapter.notifyDataSetChanged()
                is GetUsersEvent.Error -> showErrorDialog()
            }
        })
    }

    private fun getUsers() = viewModel.getUsers()

    override fun loading(show: Boolean) {
        if (show) {
            progressLoading.visibility = View.VISIBLE
        } else {
            progressLoading.visibility = View.GONE
        }
    }
}

 */
