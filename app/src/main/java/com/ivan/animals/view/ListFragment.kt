package com.ivan.animals.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ivan.animals.R
import com.ivan.animals.model.Animal
import com.ivan.animals.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment() {

    private lateinit var viewModel: ListViewModel
    private lateinit var animalsAdapter: AnimalAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        animalsAdapter = AnimalAdapter(ArrayList())
        rv_animals.adapter = animalsAdapter

        viewModel.animals.observe(viewLifecycleOwner) {
            it?.let {
                animalsAdapter.onRefreshAnimals(it)
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            pb_loading.visibility = if(it) View.VISIBLE else View.GONE
        }

        viewModel.loadError.observe(viewLifecycleOwner) {
            tv_error_msg.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.refresh()

        srl_refresh.setOnRefreshListener {
            srl_refresh.isRefreshing = false
            pb_loading.visibility = View.VISIBLE
            viewModel.refresh()
        }
    }
}