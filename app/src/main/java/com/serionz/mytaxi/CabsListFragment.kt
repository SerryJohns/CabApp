package com.serionz.mytaxi

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.serionz.mytaxi.adapters.CabsListAdapter
import com.serionz.mytaxi.api.Repository
import com.serionz.mytaxi.data.Bounds
import com.serionz.mytaxi.data.Cab
import com.serionz.mytaxi.viewmodel.CabsListFragmentViewModel
import com.serionz.mytaxi.viewmodel.CustomViewModelFactory

class CabsListFragment : Fragment() {
    private lateinit var mCabsListRecyclerView: RecyclerView
    private lateinit var mCabsListAdapter: CabsListAdapter
    private lateinit var mViewModel: CabsListFragmentViewModel
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private var cabsList: MutableList<Cab> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_cabs_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModelFactory = CustomViewModelFactory(Repository())

        // Initialized shared view model
        mViewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory).get(CabsListFragmentViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        initUI(view)
        initListeners()
        loadCabs()
    }

    private fun initUI(view: View) {
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_to_refresh) as SwipeRefreshLayout

        // Initialize Recycler View
        mCabsListRecyclerView = view.findViewById(R.id.cabs_list_recycler_view) as RecyclerView
        mCabsListAdapter = CabsListAdapter(cabsList)
        mCabsListRecyclerView.layoutManager = LinearLayoutManager(context)
        mCabsListRecyclerView.adapter = mCabsListAdapter
    }

    private fun initListeners() {
        // Swipe to refresh listener
        mSwipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }

        mCabsListAdapter.itemClickListener = object : CabsListAdapter.ItemClickListener {
            override fun onCabItemClick(cab: Cab) {
                // Update the selected cab live data object
                mViewModel.selectCab(cab)

                // Navigate to the map fragment after selecting a cab
                activity?.findViewById<ViewPager>(R.id.viewPager)?.let {
                    it.currentItem = 2
                }
            }
        }

        // Observe for updates on the cabsList object
        mViewModel.cabsList.observe(this, Observer {
            if (it != null) {
                cabsList.clear()
                cabsList.addAll(it)
                mCabsListAdapter.notifyDataSetChanged()
            }
        })
    }

    private fun refreshData() {
        mViewModel.selectCab(null)
        cabsList.clear()
        mCabsListAdapter.notifyDataSetChanged()
        loadCabs(true)
    }

    private fun loadCabs(refresh: Boolean = false) {
        // Load cabs data from the api
        mSwipeRefreshLayout.isRefreshing = true
        LoadCabs(refresh).execute(mViewModel.bounds)
    }

    inner class LoadCabs(private val refresh: Boolean) : AsyncTask<Bounds, Unit, List<Cab>?>() {
        override fun doInBackground(vararg bounds: Bounds): List<Cab>? {
            return mViewModel.fetchCabs(bounds[0], refresh)
        }

        override fun onPostExecute(result: List<Cab>?) {
            super.onPostExecute(result)
            // Populate addresses for the cab objects
            if (result != null)
                LoadAddress(result).execute()
        }
    }

    inner class LoadAddress(private val cabs: List<Cab>) : AsyncTask<Unit, Unit, List<Cab>?>() {
        override fun doInBackground(vararg params: Unit?): List<Cab>? {
            for (cab: Cab in cabs) {
                val address = mViewModel.getGeocodeAddress(cab.coordinate)
                cab.address = address?.formattedAddress
            }
            return cabs
        }

        override fun onPostExecute(result: List<Cab>?) {
            super.onPostExecute(result)
            // Update the cabsList live data object. This fires th observer, which in turn updates the recycler view
            mViewModel.cabsList.value = result
            mSwipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId

        if (id == R.id.action_refresh) {
            refreshData()
        }
        return super.onOptionsItemSelected(item)
    }
}
